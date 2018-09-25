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

package com.pig4cloud.pigx.common.security.feign;

import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

/**
 * @author lengleng
 * @date 2018/8/13
 * 扩展OAuth2FeignRequestInterceptor
 */
@Slf4j
public class PigxFeignClientInterceptor extends OAuth2FeignRequestInterceptor {
	private final OAuth2ClientContext oAuth2ClientContext;

	/**
	 * Default constructor which uses the provided OAuth2ClientContext and Bearer tokens
	 * within Authorization header
	 *
	 * @param oAuth2ClientContext provided context
	 * @param resource            type of resource to be accessed
	 */
	public PigxFeignClientInterceptor(OAuth2ClientContext oAuth2ClientContext
		, OAuth2ProtectedResourceDetails resource) {
		super(oAuth2ClientContext, resource);
		this.oAuth2ClientContext = oAuth2ClientContext;
	}


	/**
	 * Create a template with the header of provided name and extracted extract
	 *
	 * @param template
	 */
	@Override
	public void apply(RequestTemplate template) {
		if (template.headers().isEmpty()) {
			return;
		}

		if (oAuth2ClientContext != null
			&& oAuth2ClientContext.getAccessToken() != null) {
			super.apply(template);
		}
	}
}
