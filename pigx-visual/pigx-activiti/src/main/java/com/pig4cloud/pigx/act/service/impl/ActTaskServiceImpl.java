/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pigx.act.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.pig4cloud.pigx.act.dto.CommentDto;
import com.pig4cloud.pigx.act.dto.LeaveBillDto;
import com.pig4cloud.pigx.act.dto.ProcessDefDTO;
import com.pig4cloud.pigx.act.dto.TaskDTO;
import com.pig4cloud.pigx.act.entity.LeaveBill;
import com.pig4cloud.pigx.act.mapper.LeaveBillMapper;
import com.pig4cloud.pigx.act.service.ActTaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lengleng
 * @date 2018/9/25
 */
@Slf4j
@Service
@AllArgsConstructor
public class ActTaskServiceImpl implements ActTaskService {
	private final LeaveBillMapper leaveBillMapper;
	private final TaskService taskService;
	private final RuntimeService runtimeService;
	private final RepositoryService repositoryService;

	@Override
	public Page findTaskByName(Map<String, Object> params, String name) {
		TaskQuery taskQuery = taskService.createTaskQuery()
			.taskCandidateOrAssigned(name)
			.orderByTaskCreateTime().asc();

		int page = MapUtil.getInt(params, "page");
		int limit = MapUtil.getInt(params, "limit");

		Page result = new Page(page, limit);
		result.setTotal(taskQuery.count());
		List<TaskDTO> taskDTOList = new ArrayList<>();
		taskQuery.list().forEach(task -> {
			TaskDTO dto = new TaskDTO();
			dto.setTaskId(task.getId());
			dto.setTaskName(task.getName());
			dto.setProcessInstanceId(task.getProcessInstanceId());
			dto.setNodeKey(task.getTaskDefinitionKey());
			dto.setCategory(task.getCategory());
			dto.setTime(task.getCreateTime());
			taskDTOList.add(dto);
		});
		result.setRecords(taskDTOList);
		return result;
	}

	/**
	 * 通过任务ID查询任务信息关联申请单信息
	 *
	 * @param taskId
	 * @return
	 */
	@Override
	public LeaveBillDto findTaskByTaskId(String taskId) {
		//1：使用任务ID，查询任务对象Task
		Task task = taskService.createTaskQuery()
			.taskId(taskId)
			.singleResult();
		//2：使用任务对象Task获取流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		//3：使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()
			.processInstanceId(processInstanceId)
			.singleResult();
		//4：使用流程实例对象获取BUSINESS_KEY
		String buniness_key = pi.getBusinessKey();
		//5：获取BUSINESS_KEY对应的主键ID，使用主键ID，查询请假单对象（LeaveBill.1）
		String id = "";
		if (StrUtil.isNotBlank(buniness_key)) {
			//截取字符串，取buniness_key小数点的第2个值
			id = buniness_key.split("\\.")[1];
		}
		//查询请假单对象
		LeaveBill leaveBill = leaveBillMapper.selectById(id);

		LeaveBillDto leaveBillDto = new LeaveBillDto();
		BeanUtils.copyProperties(leaveBill, leaveBillDto);
		leaveBillDto.setTaskId(taskId);
		leaveBillDto.setTime(task.getCreateTime());
		return leaveBillDto;
	}

	/**
	 * 提交任务
	 *
	 * @param leaveBillDto
	 * @return
	 */
	@Override
	public Boolean submitTask(LeaveBillDto leaveBillDto) {
		//获取任务ID
		String taskId = leaveBillDto.getTaskId();
		//批注信息
		String message = leaveBillDto.getComment();
		//获取请假单ID
		Integer id = leaveBillDto.getLeaveId();
		//1：在完成之前，添加一个批注信息，向act_hi_comment表中添加数据，用于记录对当前申请人的一些审核信息
		Task task = taskService.createTaskQuery()
			.taskId(taskId)
			.singleResult();
		//获取流程实例ID
		String processInstanceId = task.getProcessInstanceId();

		// 设置批注用户
		Authentication.setAuthenticatedUserId("lengleng");
		taskService.addComment(taskId, processInstanceId, message);
		/**
		 * 2：如果连线的名称是“默认提交”，那么就不需要设置，如果不是，就需要设置流程变量
		 * 在完成任务之前，设置流程变量，按照连线的名称，去完成任务
		 流程变量的名称：outcome
		 流程变量的值：连线的名称
		 */
		Map<String, Object> variables = new HashMap<String, Object>();
		String outcome = "";
		if (outcome != null && !outcome.equals("默认提交")) {
			variables.put("outcome", outcome);
		}

		//3：使用任务ID，完成当前人的个人任务，同时流程变量
		taskService.complete(taskId, variables);
		//4：当任务完成之后，需要指定下一个任务的办理人（使用类）-----已经开发完成

		/**
		 * 5：在完成任务之后，判断流程是否结束
		 如果流程结束了，更新请假单表的状态从1变成2（审核中-->审核完成）
		 */
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()
			.processInstanceId(processInstanceId)
			.singleResult();
		//流程结束了
		if (pi == null) {
			LeaveBill bill = new LeaveBill();
			bill.setLeaveId(id);
			bill.setState("2");
			leaveBillMapper.updateById(bill);
		}
		return null;
	}

	/**
	 * 根据 taskId 查询 流程定义 （活动任务坐标）
	 *
	 * @param taskId
	 * @return
	 */
	@Override
	public ProcessDefDTO findProcessDefinitionByTaskId(String taskId) {
		//使用任务ID，查询任务对象
		Task task = taskService.createTaskQuery()
			.taskId(taskId)
			.singleResult();
		//获取流程定义ID
		String processDefinitionId = task.getProcessDefinitionId();
		//查询流程定义的对象act_re_procdef
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
			.processDefinitionId(processDefinitionId)
			.singleResult();

		//获取流程定义的实体对象（对应.bpmn文件中的数据）
		ProcessDefinitionEntity processDefinitionEntity =
			(ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);

		//使用流程实例ID，查询正在执行的执行对象表，获取当前活动对应的流程实例对象
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()
			.processInstanceId(task.getProcessInstanceId())
			.singleResult();

		//获取当前活动对象
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(pi.getActivityId());

		Deployment deployment = repositoryService.createDeploymentQuery()
			.deploymentId(processDefinition.getDeploymentId()).singleResult();
		ProcessDefDTO processDefDTO = ProcessDefDTO.toProcessDefDTO(processDefinition, deployment);
		processDefDTO.setXAxis(activityImpl.getX());
		processDefDTO.setYAxis(activityImpl.getY());
		processDefDTO.setWidth(activityImpl.getWidth());
		processDefDTO.setHeight(activityImpl.getWidth());
		return processDefDTO;
	}

	@Override
	public List<CommentDto> findCommentByTaskId(String taskId) {
		//使用当前的任务ID，查询当前流程对应的历史任务ID
		//使用当前任务ID，获取当前任务对象
		Task task = taskService.createTaskQuery()
			.taskId(taskId)
			.singleResult();
		//获取流程实例ID
		List<CommentDto> commentDtoList = new ArrayList<>();
		List<Comment> commentList = taskService.getProcessInstanceComments(task.getProcessInstanceId());

		commentList.forEach(comment -> {
			CommentDto commentDto = new CommentDto();
			commentDto.setId(comment.getId());
			commentDto.setTime(comment.getTime());
			commentDto.setType(comment.getType());
			commentDto.setTaskId(comment.getTaskId());
			commentDto.setUserId(comment.getUserId());
			commentDto.setFullMessage(comment.getFullMessage());
			commentDto.setProcessInstanceId(comment.getProcessInstanceId());
			commentDtoList.add(commentDto);
		});
		return commentDtoList;
	}

}
