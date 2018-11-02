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

package com.pig4cloud.pigx.gateway.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author lengleng
 * @date 2018/10/31
 * <p>
 * 动态路由操作类
 * see GatewayControllerEndpoint.java
 */
@Slf4j
@Service
public class DynamicRouteHandler implements ApplicationEventPublisherAware {
	private final RouteDefinitionWriter routeDefinitionWriter;
	private final RouteDefinitionLocator routeDefinitionLocator;
	private ApplicationEventPublisher applicationEventPublisher;

	public DynamicRouteHandler(RouteDefinitionWriter routeDefinitionWriter
		, RouteDefinitionLocator routeDefinitionLocator) {
		this.routeDefinitionWriter = routeDefinitionWriter;
		this.routeDefinitionLocator = routeDefinitionLocator;
	}

	/**
	 * 获取全部路由
	 * <p>
	 * RedisRouteDefinitionWriter.java
	 * PropertiesRouteDefinitionLocator.java
	 *
	 * @return
	 */
	public Mono<Map<String, RouteDefinition>> routes() {
		return routeDefinitionLocator.getRouteDefinitions()
			.collectMap(RouteDefinition::getId);
	}

	/**
	 * 添加路由
	 *
	 * @param route 路由定义
	 * @return
	 */
	public Mono<Void> addRoute(Mono<RouteDefinition> route) {
		this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
		return routeDefinitionWriter.save(route);
	}

	public Mono<Void> deleteRoute(String id) {
		this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
		return routeDefinitionWriter.delete(Mono.just(id));
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}
}
