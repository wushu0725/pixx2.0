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

package com.pig4cloud.pigx.common.security.util;


import cn.hutool.core.util.StrUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.security.service.PigxUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 安全工具类
 *
 * @author L.cm
 */
public class SecurityUtils {
	private static final ThreadLocal<Integer> THREAD_LOCAL_TENANT = new TransmittableThreadLocal<>();

	/**
	 * 获取Authentication
	 */
	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取用户
	 *
	 * @param authentication
	 * @return PigxUser
	 * <p>
	 * 获取当前用户的全部信息
	 * 每次check—token 后根据用户名调用 remoteService 查询用户信息，效率比较低。
	 * 建议长链路调用不要使用，重写configure() 例如  codegen 模块ResourceServerConfigurer
	 * 1.前提使用继承BaseResourceServerConfigurerAdapter
	 * 2. 使用默认 configure（）
	 */
	public static PigxUser getUser(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		if (principal instanceof PigxUser) {
			return (PigxUser) principal;
		}
		return null;
	}

	/**
	 * 获取当前用名
	 *
	 * @return String
	 */
	public static String getUsername() {
		Object principal = getAuthentication().getPrincipal();
		if (principal instanceof String) {
			return principal.toString();
		}
		return null;
	}


	public static String getClientId() {
		Authentication authentication = getAuthentication();
		if (authentication instanceof OAuth2Authentication) {
			OAuth2Authentication auth2Authentication = (OAuth2Authentication) authentication;
			return auth2Authentication.getOAuth2Request().getClientId();
		}
		return null;
	}

	/**
	 * 获取用户
	 */
	public static PigxUser getUser() {
		Authentication authentication = getAuthentication();
		if (authentication == null) {
			return null;
		}
		return getUser(authentication);
	}

	/**
	 * 获取用户角色信息
	 *
	 * @return 角色集合
	 */
	public static List<Integer> getRoles() {
		Authentication authentication = getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		List<Integer> roleIds = new ArrayList<>();
		authorities.stream()
			.filter(granted -> StrUtil.startWith(granted.getAuthority(), SecurityConstants.ROLE))
			.forEach(granted -> {
				String id = StrUtil.removePrefix(granted.getAuthority(), SecurityConstants.ROLE);
				roleIds.add(Integer.parseInt(id));
			});
		return roleIds;
	}

	/**
	 * TTL 设置租户ID
	 *
	 * @param tenantId
	 */
	public static void setTenantId(Integer tenantId) {
		THREAD_LOCAL_TENANT.set(tenantId);
	}

	/**
	 * 获取TTL中的租户ID
	 *
	 * @return
	 */
	public static Integer getTenantId() {
		return THREAD_LOCAL_TENANT.get();
	}
}
