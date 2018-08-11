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

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lengleng
 * @date 2018/6/24
 * 删除token端点
 */
@RestController
@AllArgsConstructor
public class RevokeTokenEndpoint {
	private final TokenStore tokenStore;

	/**
	 * 删除token
	 *
	 * @param authHeader Authorization
	 */
	@GetMapping("/oauth/removeToken")
	public R<Boolean> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
		if (StringUtils.hasText(authHeader)) {
			String tokenValue = authHeader.replace("Bearer", "").trim();
			OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
			if (accessToken == null || StrUtil.isBlank(accessToken.getValue())) {
				return new R<>(false, "退出失败，token 为空");
			}
			tokenStore.removeAccessToken(accessToken);
		}

		return new R<>(Boolean.TRUE);
	}
}
