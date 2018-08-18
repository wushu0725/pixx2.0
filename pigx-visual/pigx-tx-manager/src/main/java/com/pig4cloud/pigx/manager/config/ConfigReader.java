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

package com.pig4cloud.pigx.manager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * create by lorne on 2017/11/11
 */
@Component
public class ConfigReader {

	/**
	 * 事务默认数据的位置，有最大时间
	 */
	private final String key_prefix = "tx:manager:default:";
	/**
	 * 负载均衡模块存储信息
	 */
	private final String key_prefix_loadbalance = "tx:manager:loadbalance:";
	/**
	 * 补偿事务永久存储数据
	 */
	private final String key_prefix_compensate = "tx:manager:compensate:";
	@Value("${tm.socket.port}")
	private int socketPort;
	@Value("${tm.socket.maxconnection}")
	private int socketMaxConnection;
	@Value("${tm.transaction.netty.hearttime}")
	private int transactionNettyHeartTime;
	@Value("${tm.transaction.netty.delaytime}")
	private int transactionNettyDelayTime;
	@Value("${tm.redis.savemaxtime}")
	private int redisSaveMaxTime;
	@Value("${tm.compensate.notifyUrl}")
	private String compensateNotifyUrl;
	@Value("${tm.compensate.auto}")
	private boolean isCompensateAuto;
	@Value("${tm.compensate.tryTime}")
	private int compensateTryTime;
	@Value("${tm.compensate.maxWaitTime}")
	private int compensateMaxWaitTime;

	public String getKeyPrefixLoadbalance() {
		return key_prefix_loadbalance;
	}

	public String getCompensateNotifyUrl() {
		return compensateNotifyUrl;
	}

	public String getKeyPrefix() {
		return key_prefix;
	}

	public String getKeyPrefixCompensate() {
		return key_prefix_compensate;
	}

	public int getSocketPort() {
		return socketPort;
	}

	public int getSocketMaxConnection() {
		return socketMaxConnection;
	}

	public int getTransactionNettyHeartTime() {
		return transactionNettyHeartTime;
	}

	public int getRedisSaveMaxTime() {
		return redisSaveMaxTime;
	}

	public int getTransactionNettyDelayTime() {
		return transactionNettyDelayTime;
	}

	public boolean isCompensateAuto() {
		return isCompensateAuto;
	}

	public int getCompensateTryTime() {
		return compensateTryTime;
	}

	public int getCompensateMaxWaitTime() {
		return compensateMaxWaitTime;
	}


}
