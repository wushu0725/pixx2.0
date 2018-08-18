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


import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.lorne.core.framework.utils.encode.MD5Util;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * created by foxdd 2017-12-05
 *
 * @author LCN
 * @since 4.1.0
 */
@Slf4j
public class LcnLoadBalancerRule {

	public Server proxy(List<Server> servers, Server server) {

		TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
		if (txTransactionLocal == null) {
			return server;
		}

		try {
			log.debug("LCNBalanceProxy - > start");

			String groupId = txTransactionLocal.getGroupId();

			//取出组件的appName
			String appName = server.getMetaInfo().getAppName();


			String key = MD5Util.md5((groupId + "_" + appName).getBytes());

			//如果只有一个可调用模块，则用当前的，且需要将数据记录到redis中
			if (servers.size() == 1) {
				putServer(key, txTransactionLocal, server);
				log.debug("LCNBalanceProxy -> only one server available");
				return server;
			}

			Server oldServer = getServer(txTransactionLocal, servers, key);
			if (oldServer != null) {
				log.debug("LCNBalanceProxy - > load old server ");
				return server;
			}

			putServer(key, txTransactionLocal, server);
			log.debug("LCNBalanceProxy - > load new server ");

			return server;
		} finally {
			log.debug("LCNBalanceProxy - > end");
		}
	}


	private void putServer(String key, TxTransactionLocal txTransactionLocal, Server server) {
		String serviceName = server.getMetaInfo().getAppName();
		String address = server.getHostPort();

		String md5 = MD5Util.md5((address + serviceName).getBytes());

		log.debug("putServer->address->" + address + ",md5-->" + md5);

		txTransactionLocal.putLoadBalance(key, md5);
	}


	private Server getServer(TxTransactionLocal txTransactionLocal, List<Server> servers, String key) {
		String val = txTransactionLocal.getLoadBalance(key);
		if (StringUtils.isEmpty(val)) {
			return null;
		}
		for (Server server : servers) {
			String serviceName = server.getMetaInfo().getAppName();
			String address = server.getHostPort();

			String md5 = MD5Util.md5((address + serviceName).getBytes());

			log.debug("getServer->address->" + address + ",md5-->" + md5);

			if (val.equals(md5)) {
				return server;
			}
		}
		return null;
	}

}
