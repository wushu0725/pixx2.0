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
package com.pig4cloud.pigx.common.aspect.resolver;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * PaginationArgumentResolver
 * 分页参数解析器
 *
 * @author: lishangbu
 * @date: 2018/10/18
 */
@Configuration
@Slf4j
public class PaginationArgumentResolver implements HandlerMethodArgumentResolver {
	/**
	 * 当前页
	 */
	private static final String CURRENT = "current";

	/**
	 * 每页显示条数
	 */
	private static final String SIZE = "size";

	/**
	 * 入参筛选
	 *
	 * @param parameter 参数集合
	 * @return 格式化后的参数
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(Page.class);
	}

	/**
	 * @param parameter     入参集合
	 * @param mavContainer  model 和 view
	 * @param webRequest    web相关
	 * @param binderFactory 入参解析
	 * @return 包装对象
	 * @throws Exception
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		try {
			return new Page(Long.valueOf(Optional.ofNullable(request.getParameter(CURRENT)).orElse("1")),
				Long.valueOf(Optional.ofNullable(request.getParameter(SIZE)).orElse("10")));
		} catch (NumberFormatException e) {
			return new Page();
		}
	}
}
