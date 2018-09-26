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

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.act.dto.DataTable;
import com.pig4cloud.pigx.act.dto.TaskDTO;
import com.pig4cloud.pigx.act.service.ActTaskService;
import lombok.AllArgsConstructor;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.TaskInfo;
import org.activiti.engine.task.TaskQuery;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lengleng
 * @date 2018/9/25
 */
@Service
@AllArgsConstructor
public class ActTaskServiceImpl implements ActTaskService {
	private final TaskService actTaskService;
	private final RepositoryService repositoryService;

	/**
	 * 获取代办列表
	 *
	 * @param dt
	 * @return
	 */
	@Override
	public DataTable getTodoTasks(DataTable dt) {
		String TASK_STATUS_TODO = "todo";
		String TASK_STATUS_CLAIM = "claim";

		String userId = "lengleng" + "";
		TaskQuery taskQuery = actTaskService.createTaskQuery().taskCandidateOrAssigned(userId)
			.active().includeProcessVariables().orderByTaskCreateTime().desc();
		String category = (String) dt.getSearchParams().get("category");
		if (StrUtil.isNotBlank(category)) {
			taskQuery.taskCategory(category);
		}
		String title = (String) dt.getSearchParams().get("title");
		if (StrUtil.isNotBlank(title)) {
			taskQuery.processVariableValueLikeIgnoreCase("title", "%" + title + "%");
		}
		Map<String, List<TaskDTO>> collect = taskQuery.listPage((dt.getPageNumber() - 1) * dt.getPageSize(), dt.getPageSize())
			.stream()
			.map(task -> {
				TaskDTO dto = getTaskDTO(task, task.getAssignee() == null ?
					TASK_STATUS_CLAIM : TASK_STATUS_TODO);
				dto.setTime(task.getCreateTime().getTime());
				return dto;
			}).collect(Collectors.groupingBy(TaskDTO::getCategory));
		dt.setRows(Collections.singletonList(collect));
		return dt;
	}


	private TaskDTO getTaskDTO(TaskInfo task, String status) {
		ProcessDefinition processDefinition = getProcessDefinition(task.getProcessDefinitionId());
		String deploymentId = processDefinition.getDeploymentId();
		Deployment deployment = getDeployment(deploymentId);
		return null;
	}

	private ProcessDefinition getProcessDefinition(String processDefinitionId) {
		return repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
	}

	private Deployment getDeployment(String deploymentId) {
		return repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
	}
}
