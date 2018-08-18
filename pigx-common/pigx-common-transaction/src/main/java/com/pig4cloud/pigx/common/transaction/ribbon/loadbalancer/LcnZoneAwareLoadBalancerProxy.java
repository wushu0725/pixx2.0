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

package com.pig4cloud.pigx.common.transaction.ribbon.loadbalancer;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * created by foxdd 2017-12-05
 *
 * @author LCN
 * @since 4.1.0
 */
@Slf4j
public class LcnZoneAwareLoadBalancerProxy extends ZoneAwareLoadBalancer<Server> {

	LcnLoadBalancerRule lcnLoadBalancerRule = new LcnLoadBalancerRule();

	public LcnZoneAwareLoadBalancerProxy(IClientConfig clientConfig, IRule rule,
										 IPing ping, ServerList<Server> serverList, ServerListFilter<Server> filter,
										 ServerListUpdater serverListUpdater) {
		super(clientConfig, rule, ping, serverList, filter, serverListUpdater);
	}

	@Override
	public Server chooseServer(Object key) {
		log.info("enter chooseServer method, key:" + key);
		//利用Dynamic负载类中的方法，可以在运行时更新服务实例清单
		List<Server> serverList;
		//获取处理之后的serverlist
		serverList = super.getServerListImpl().getUpdatedListOfServers();
		//获取过滤之后的serverlist
		serverList = super.getFilter().getFilteredListOfServers(serverList);
		if (null == serverList || serverList.isEmpty()) {
			return super.chooseServer(key);
		}
		return lcnLoadBalancerRule.proxy(serverList, super.chooseServer(key));

	}

}
