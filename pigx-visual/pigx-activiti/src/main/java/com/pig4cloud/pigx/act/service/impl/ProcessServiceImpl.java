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
import com.pig4cloud.pigx.act.dto.ProcessDefDTO;
import com.pig4cloud.pigx.act.entity.LeaveBill;
import com.pig4cloud.pigx.act.mapper.LeaveBillMapper;
import com.pig4cloud.pigx.act.service.ProcessService;
import lombok.AllArgsConstructor;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lengleng
 * @date 2018/9/25
 */
@Service
@AllArgsConstructor
public class ProcessServiceImpl implements ProcessService {
	private final RepositoryService repositoryService;
	private final RuntimeService runtimeService;
	private final LeaveBillMapper leaveBillMapper;

	/**
	 * 分页流程列表
	 *
	 * @param params
	 * @return
	 */
	@Override
	public Page<ProcessDefDTO> getProcessByPage(Map<String, Object> params) {
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery().latestVersion();
		String category = MapUtil.getStr(params, "category");
		if (StrUtil.isNotBlank(category)) {
			query.processDefinitionCategory(category);
		}

		int page = MapUtil.getInt(params, "page");
		int limit = MapUtil.getInt(params, "limit");

		Page result = new Page(page, limit);
		result.setTotal(query.count());

		List<ProcessDefDTO> deploymentList = new ArrayList<>();
		query.listPage((page - 1) * limit, limit)
			.stream()
			.forEach(processDefinition -> {
				Deployment deployment = repositoryService.createDeploymentQuery()
					.deploymentId(processDefinition.getDeploymentId()).singleResult();
				deploymentList.add(ProcessDefDTO.toProcessDefDTO(processDefinition, deployment));
			});
		result.setRecords(deploymentList);
		return result;
	}

	/**
	 * 读取xml/image资源
	 *
	 * @param procDefId
	 * @param proInsId
	 * @param resType
	 * @return
	 */
	@Override
	public InputStream resourceRead(String procDefId, String proInsId, String resType) {

		if (StrUtil.isBlank(procDefId)) {
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(proInsId).singleResult();
			procDefId = processInstance.getProcessDefinitionId();
		}
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();

		String resourceName = "";
		if (resType.equals("image")) {
			resourceName = processDefinition.getDiagramResourceName();
		} else if (resType.equals("xml")) {
			resourceName = processDefinition.getResourceName();
		}

		InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
		return resourceAsStream;
	}

	/**
	 * 更新状态
	 *
	 * @param status
	 * @param procDefId
	 * @return
	 */
	@Override
	public Boolean updateStatus(String status, String procDefId) {
		if (status.equals("active")) {
			repositoryService.activateProcessDefinitionById(procDefId, true, null);
		} else if (status.equals("suspend")) {
			repositoryService.suspendProcessDefinitionById(procDefId, true, null);
		}
		return Boolean.TRUE;
	}

	/**
	 * 删除部署的流程，级联删除流程实例
	 *
	 * @param deploymentId
	 * @return
	 */
	@Override
	public Boolean deleteProcIns(String deploymentId) {
		repositoryService.deleteDeployment(deploymentId, true);
		return Boolean.TRUE;
	}

	/**
	 * 启动流程、更新请假单状态
	 *
	 * @param leaveId
	 * @return
	 */
	@Override
	public Boolean saveStartProcess(Integer leaveId) {
		//1：获取请假单ID，使用请假单ID，查询请假单的对象LeaveBill
		LeaveBill leaveBill = leaveBillMapper.selectById(leaveId);
		leaveBill.setState("1");
		//3：使用当前对象获取到流程定义的key（对象的名称就是流程定义的key）
		String key = leaveBill.getClass().getSimpleName();
		/**
		 * 4：从Session中获取当前任务的办理人，使用流程变量设置下一个任务的办理人
		 * inputUser是流程变量的名称，
		 * 获取的办理人是流程变量的值
		 */
		Map<String, Object> variables = new HashMap<>(1);
		variables.put("aaaaaaa", "lengleng");
		/**
		 * 5：
		 * (1)使用流程变量设置字符串（格式：LeaveBill.id的形式），通过设置，让启动的流程（流程实例）关联业务
		 * (2)使用正在执行对象表中的一个字段BUSINESS_KEY（Activiti提供的一个字段），让启动的流程（流程实例）关联业务
		 */
		//格式：LeaveBill.id的形式（使用流程变量）
		String objId = key + "." + leaveBill.getLeaveId();
		variables.put("objId", objId);
		//6：使用流程定义的key，启动流程实例，同时设置流程变量，同时向正在执行的执行对象表中的字段BUSINESS_KEY添加业务数据，同时让流程关联业务
		runtimeService.startProcessInstanceByKey(key, objId, variables);
		leaveBillMapper.updateById(leaveBill);
		return Boolean.TRUE;
	}

}
