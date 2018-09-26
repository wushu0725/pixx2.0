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
				deploymentList.add(toProcessDefDTO(processDefinition, deployment));
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
	 * 删除流程实例
	 *
	 * @param procInsId
	 * @return
	 */
	@Override
	public Boolean deleteProcIns(String procInsId) {
		runtimeService.deleteProcessInstance(procInsId, "pigx");
		return Boolean.TRUE;
	}

	/**
	 * 抽取流程定义需要返回的内容
	 *
	 * @param processDefinition
	 * @param deployment
	 * @return
	 */
	private ProcessDefDTO toProcessDefDTO(ProcessDefinition processDefinition, Deployment deployment) {

		ProcessDefDTO dto = new ProcessDefDTO();
		dto.category = processDefinition.getCategory();
		dto.processonDefinitionId = processDefinition.getId();
		dto.key = processDefinition.getKey();
		dto.name = deployment.getName();
		dto.revision = processDefinition.getVersion();
		dto.deploymentTime = deployment.getDeploymentTime().getTime();
		dto.xmlName = processDefinition.getResourceName();
		dto.picName = processDefinition.getDiagramResourceName();
		dto.deploymentId = deployment.getId();
		dto.suspend = processDefinition.isSuspended();
		dto.description = processDefinition.getDescription();
		return dto;
	}
}
