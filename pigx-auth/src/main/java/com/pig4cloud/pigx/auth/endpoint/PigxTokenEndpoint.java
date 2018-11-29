/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 *
 */

package com.pig4cloud.pigx.auth.endpoint;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.constant.PaginationConstant;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.core.util.TenantContextHolder;
import com.pig4cloud.pigx.common.security.service.PigxUser;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lengleng
 * @date 2018/6/24
 * 删除token端点
 */
@RestController
@AllArgsConstructor
@RequestMapping("/token")
public class PigxTokenEndpoint {
	private static final String PIGX_OAUTH_ACCESS = SecurityConstants.PIGX_PREFIX + SecurityConstants.OAUTH_PREFIX + "access:";
	private final TokenStore tokenStore;
	private final RedisTemplate redisTemplate;
	private final CacheManager cacheManager;

	/**
	 * 认证页面
	 *
	 * @return ModelAndView
	 */
	@GetMapping("/login")
	public ModelAndView require() {
		return new ModelAndView("ftl/login");
	}

	/**
	 * 退出token
	 *
	 * @param authHeader Authorization
	 */
	@DeleteMapping("/logout")
	public R<Boolean> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
		if (StrUtil.isBlank(authHeader)) {
			return new R<>(false, "退出失败，token 为空");
		}

		String tokenValue = authHeader.replace("Bearer", "").trim();
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
		if (accessToken == null || StrUtil.isBlank(accessToken.getValue())) {
			return new R<>(false, "退出失败，token 无效");
		}

		OAuth2Authentication auth2Authentication = tokenStore.readAuthentication(accessToken);
		cacheManager.getCache("user_details")
			.evict(auth2Authentication.getName());
		tokenStore.removeAccessToken(accessToken);
		return new R<>(Boolean.TRUE);
	}

	/**
	 * 令牌管理调用
	 *
	 * @param token token
	 * @param from  内部调用标志
	 * @return
	 */
	@DeleteMapping("/{token}")
	public R<Boolean> delToken(@PathVariable("token") String token, @RequestHeader(required = false) String from) {
		if (StrUtil.isBlank(from)) {
			return null;
		}
		return new R<>(redisTemplate.delete(PIGX_OAUTH_ACCESS + token));
	}


	/**
	 * 查询token
	 *
	 * @param params 分页参数
	 * @param from   标志
	 * @return
	 */
	@PostMapping("/page")
	public R<Page> tokenList(@RequestBody Map<String, Object> params, @RequestHeader(required = false) String from) {
		if (StrUtil.isBlank(from)) {
			return null;
		}

		List<Map<String, String>> list = new ArrayList<>();
		//根据分页参数获取对应数据
		List<String> pages = findKeysForPage(PIGX_OAUTH_ACCESS + "*", MapUtil.getInt(params, PaginationConstant.CURRENT), MapUtil.getInt(params, PaginationConstant.SIZE));

		for (String page : pages) {
			String accessToken = StrUtil.subAfter(page, PIGX_OAUTH_ACCESS, true);
			OAuth2AccessToken token = tokenStore.readAccessToken(accessToken);
			Map<String, String> map = new HashMap<>(8);


			map.put("token_type", token.getTokenType());
			map.put("token_value", token.getValue());
			map.put("expires_in", token.getExpiresIn() + "");


			OAuth2Authentication oAuth2Auth = tokenStore.readAuthentication(token);
			Authentication authentication = oAuth2Auth.getUserAuthentication();

			map.put("client_id", oAuth2Auth.getOAuth2Request().getClientId());
			map.put("grant_type", oAuth2Auth.getOAuth2Request().getGrantType());

			if (authentication instanceof UsernamePasswordAuthenticationToken) {
				UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
				if (extractToken(map, authenticationToken.getPrincipal())) {
					continue;
				}
			} else if (authentication instanceof PreAuthenticatedAuthenticationToken) {
				//刷新token方式
				PreAuthenticatedAuthenticationToken authenticationToken = (PreAuthenticatedAuthenticationToken) authentication;
				if (extractToken(map, authenticationToken.getPrincipal())) {
					continue;
				}
			}
			list.add(map);
		}

		Page result = new Page(MapUtil.getInt(params, PaginationConstant.CURRENT), MapUtil.getInt(params, PaginationConstant.SIZE));
		result.setRecords(list);
		result.setTotal(Long.valueOf(redisTemplate.keys(PIGX_OAUTH_ACCESS + "*").size()));
		return new R<>(result);
	}

	private boolean extractToken(Map<String, String> map, Object principal) {
		if (principal instanceof PigxUser) {
			PigxUser user = (PigxUser) principal;
			if (!user.getTenantId().equals(TenantContextHolder.getTenantId())) {
				return true;
			}
			map.put("user_id", user.getId() + "");
			map.put("user_name", user.getUsername() + "");
		}
		return false;
	}

	private List<String> findKeysForPage(String patternKey, int pageNum, int pageSize) {
		ScanOptions options = ScanOptions.scanOptions().match(patternKey).build();
		RedisSerializer<String> redisSerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
		Cursor cursor = (Cursor) redisTemplate.executeWithStickyConnection(redisConnection -> new ConvertingCursor<>(redisConnection.scan(options), redisSerializer::deserialize));
		List<String> result = new ArrayList<>();
		int tmpIndex = 0;
		int startIndex = (pageNum - 1) * pageSize;
		int end = pageNum * pageSize;

		assert cursor != null;
		while (cursor.hasNext()) {
			if (tmpIndex >= startIndex && tmpIndex < end) {
				result.add(cursor.next().toString());
				tmpIndex++;
				continue;
			}
			if (tmpIndex >= end) {
				break;
			}
			tmpIndex++;
			cursor.next();
		}
		return result;
	}
}
