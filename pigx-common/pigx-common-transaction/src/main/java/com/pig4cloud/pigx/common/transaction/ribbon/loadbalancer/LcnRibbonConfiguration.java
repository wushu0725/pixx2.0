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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LCN
 * @since 4.1.0
 */
@Configuration
public class LcnRibbonConfiguration {

	/**
	 * 为ribbon的loadbalancer做代理，相比于重写IRULE,重写loadbalancer更有利于用户自选LB算法，而且有默认LB算法可用
	 *
	 * @param config
	 * @param serverList
	 * @param serverListFilter
	 * @param rule
	 * @param ping
	 * @param serverListUpdater
	 * @return
	 */
	@Bean
	public ILoadBalancer ribbonLoadBalancer(IClientConfig config,
											ServerList<Server> serverList, ServerListFilter<Server> serverListFilter,
											IRule rule, IPing ping, ServerListUpdater serverListUpdater) {
		return new LcnZoneAwareLoadBalancerProxy(config, rule, ping, serverList,
			serverListFilter, serverListUpdater);
	}


}
