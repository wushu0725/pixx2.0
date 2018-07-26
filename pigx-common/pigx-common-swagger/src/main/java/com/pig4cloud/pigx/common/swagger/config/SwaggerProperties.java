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
package com.pig4cloud.pigx.common.swagger.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * SwaggerProperties
 *
 * @author: lengleng
 * @date: 2018/7/25  14:00
 */
@Data
@ConfigurationProperties("swagger")
public class SwaggerProperties {
	/**
	 * swagger会解析的包路径
	 **/
	private String basePackage = "";
	/**
	 * swagger会解析的url规则
	 **/
	private List<String> basePath = new ArrayList<>();
	/**
	 * 在basePath基础上需要排除的url规则
	 **/
	private List<String> excludePath = new ArrayList<>();
	/**
	 * 标题
	 **/
	private String title = "PigX Swagger API";
	/**
	 * 描述
	 **/
	private String description = "https://gitee.com/log4j/pig/wikis";
	/**
	 * 版本
	 **/
	private String version = "2.0";
	/**
	 * 许可证
	 **/
	private String license = "Powered By PigX";
	/**
	 * 许可证URL
	 **/
	private String licenseUrl = "https://gitee.com/log4j/pig/wikis";
	/**
	 * 服务条款URL
	 **/
	private String termsOfServiceUrl = "https://gitee.wang/pig/pigx";

	/**
	 * 忽略的参数类型
	 **/
	private List<Class<?>> ignoredParameterTypes = new ArrayList<>();
	/**
	 * host信息
	 **/
	private String host = "";
	private Contact contact = new Contact();
	@Data
	@NoArgsConstructor
	public static class Contact {

		/**
		 * 联系人
		 **/
		private String name = "冷冷";
		/**
		 * 联系人url
		 **/
		private String url = "https://gitee.wang/pig/pigx";
		/**
		 * 联系人email
		 **/
		private String email = "wangiegie@gmail.com";

	}
}
