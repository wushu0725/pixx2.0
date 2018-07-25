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


import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sywd
 * swagger配置
 */
@Configuration
@EnableSwagger2
@EnableAutoConfiguration
public class SwaggerAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SwaggerProperties swaggerProperties() {
		return new SwaggerProperties();
	}

	private static final String DEFAULT_EXCLUDE_PATH = "/error";
	private static final String BASE_PATH = "/**";


	@Bean
	public Docket api(SwaggerProperties swaggerProperties) {
		// base-path处理
		// 当没有配置任何path的时候，解析/**
		if (swaggerProperties.getBasePath().isEmpty()) {
			swaggerProperties.getBasePath().add(BASE_PATH);
		}
		List<Predicate<String>> basePath = new ArrayList();
		for (String path : swaggerProperties.getBasePath()) {
			basePath.add(PathSelectors.ant(path));
		}
		// exclude-path处理
		// 当没有任何配置的时候，解析Spring Boot默认的异常处理路径/error
		if(swaggerProperties.getExcludePath().isEmpty()){
			swaggerProperties.getExcludePath().add(DEFAULT_EXCLUDE_PATH);
		}
		List<Predicate<String>> excludePath = new ArrayList<>();
		for (String path : swaggerProperties.getExcludePath()) {
			excludePath.add(PathSelectors.ant(path));
		}
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
			.apis(RequestHandlerSelectors.any()).paths(Predicates.and(
				Predicates.not(Predicates.or(excludePath)),
				Predicates.or(basePath)))
			.build().securitySchemes(Collections.singletonList(securitySchema()))
			.securityContexts(Collections.singletonList(securityContext())).pathMapping("/");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth())
			.forPaths(PathSelectors.ant("/**"))
			.build();
	}

	private List<SecurityReference> defaultAuth() {

		final AuthorizationScope[] authorizationScopes = new AuthorizationScope[3];
		authorizationScopes[0] = new AuthorizationScope("read", "read all");
		authorizationScopes[1] = new AuthorizationScope("trust", "trust all");
		authorizationScopes[2] = new AuthorizationScope("write", "write all");

		return Collections.singletonList(new SecurityReference("pigX OAuth", authorizationScopes));
	}


	private OAuth securitySchema() {
		ArrayList authorizationScopeList = new ArrayList();
		authorizationScopeList.add(new AuthorizationScope("server", "server all"));
		authorizationScopeList.add(new AuthorizationScope("read", "read all"));
		authorizationScopeList.add(new AuthorizationScope("write", "access all"));

		ArrayList grantTypes = new ArrayList();
		GrantType creGrant = new ResourceOwnerPasswordCredentialsGrant("http://localhost:9999/auth/oauth/token");

		grantTypes.add(creGrant);
		return new OAuth("pigX OAuth", authorizationScopeList, grantTypes);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
			.title("PigX Swagger API ")
			.description("https://gitee.com/log4j/pig/wikis")
			.termsOfServiceUrl("https://gitee.wang/pig/pigx")
			.contact(new Contact("冷冷", "https://gitee.wang/pig/pigx", "wangiegie@gmail.com"))
			.version("2.0")
			.build();
	}

}
