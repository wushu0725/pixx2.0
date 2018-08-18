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

package com.pig4cloud.pigx.manager.api.service.impl;


import com.lorne.core.framework.exception.ServiceException;
import com.pig4cloud.pigx.manager.api.service.ApiAdminService;
import com.pig4cloud.pigx.manager.compensate.model.TxModel;
import com.pig4cloud.pigx.manager.compensate.service.CompensateService;
import com.pig4cloud.pigx.manager.manager.service.MicroService;
import com.pig4cloud.pigx.manager.model.ModelName;
import com.pig4cloud.pigx.manager.model.TxState;
import com.pig4cloud.pigx.manager.redis.RedisServerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LCN on 2017/11/12
 *
 * @author LCN
 * @author lengleng
 */
@Service
@AllArgsConstructor
public class ApiAdminServiceImpl implements ApiAdminService {
	private final MicroService eurekaService;
	private final RedisServerService redisServerService;
	private final CompensateService compensateService;

	@Override
	public TxState getState() {
		return eurekaService.getState();
	}

	@Override
	public String loadNotifyJson() {
		return redisServerService.loadNotifyJson();
	}

	@Override
	public List<ModelName> modelList() {
		return compensateService.loadModelList();
	}


	@Override
	public List<String> modelTimes(String model) {
		return compensateService.loadCompensateTimes(model);
	}

	@Override
	public List<TxModel> modelInfos(String path) {
		return compensateService.loadCompensateByModelAndTime(path);
	}

	@Override
	public boolean compensate(String path) throws ServiceException {
		return compensateService.executeCompensate(path);
	}

	@Override
	public boolean delCompensate(String path) {
		return compensateService.delCompensate(path);
	}

	@Override
	public boolean hasCompensate() {
		return compensateService.hasCompensate();
	}
}
