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

package com.pig4cloud.pigx.common.transaction.ribbon;

import com.netflix.loadbalancer.NoOpLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * created by foxdd 2017-12-05
 *
 * @author LCN
 * @since 4.1.0
 */
@Slf4j
public class LcnNoOpLoadBalancerProxy extends NoOpLoadBalancer {

	private LcnLoadBalancerRule lcnLoadBalancerRule = new LcnLoadBalancerRule();

	public LcnNoOpLoadBalancerProxy() {
		super();
	}

	@Override
	public Server chooseServer(Object key) {
		log.debug("enter chooseServer method, key:" + key);

		List<Server> serverList = new ArrayList<Server>();
		return lcnLoadBalancerRule.proxy(serverList, super.chooseServer(key));

	}

}
