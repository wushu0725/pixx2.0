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

package com.pig4cloud.pigx.common.gateway.support;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.pig4cloud.pigx.common.core.constant.CommonConstant;
import com.pig4cloud.pigx.common.gateway.vo.RouteDefinitionVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
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
	private final RouteDefinitionLocator routeDefinitionLocator;
	private final RedisTemplate redisTemplate;
	private ApplicationEventPublisher applicationEventPublisher;

	public DynamicRouteHandler(RedisTemplate redisTemplate
		, RouteDefinitionLocator routeDefinitionLocator) {
		this.redisTemplate = redisTemplate;
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
	 * 更新路由信息
	 *
	 * @param routes 路由信息
	 * @return
	 */
	public Mono<Void> editRoutes(JSONObject routes) {
		Boolean result = redisTemplate.delete(CommonConstant.ROUTE_KEY);
		log.info("清空网关路由 {} ", result);

		routes.forEach((key, value) -> {
			log.info("更新路由 -> {} ->{}", key, value);
			RouteDefinitionVo vo = new RouteDefinitionVo();
			Map<String, Object> map = (Map) value;

			Object id = map.get("id");
			if (id != null) {
				vo.setId(String.valueOf(id));
			}

			Object predicates = map.get("predicates");
			if (predicates != null) {
				JSONArray predicatesArray = (JSONArray) predicates;
				List<PredicateDefinition> predicateDefinitionList =
					predicatesArray.toList(PredicateDefinition.class);
				vo.setPredicates(predicateDefinitionList);
			}

			Object filters = map.get("filters");
			if (filters != null) {
				JSONArray filtersArray = (JSONArray) filters;
				List<FilterDefinition> filterDefinitionList
					= filtersArray.toList(FilterDefinition.class);
				vo.setFilters(filterDefinitionList);
			}

			Object uri = map.get("uri");
			if (uri != null) {
				vo.setUri(URI.create(String.valueOf(uri)));
			}

			Object order = map.get("order");
			if (order != null) {
				vo.setOrder(Integer.parseInt(String.valueOf(order)));
			}

			redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(RouteDefinitionVo.class));
			redisTemplate.opsForHash().put(CommonConstant.ROUTE_KEY, key, vo);
		});
		log.debug("更新网关路由结束 ");
		this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
		return Mono.empty();
	}

	/**
	 * 回滚路由
	 *
	 * @return
	 */
	public Mono<Void> fallback() {
		this.applicationEventPublisher.publishEvent(new DynamicRouteInitEvent(this));
		return Mono.empty();
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}
}
