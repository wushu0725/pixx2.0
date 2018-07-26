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


import cn.hutool.core.collection.CollUtil;
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
 * @author lengleng
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
		if (CollUtil.isEmpty(swaggerProperties.getBasePath())) {
			swaggerProperties.getBasePath().add(BASE_PATH);
		}
		//noinspection unchecked
		List<Predicate<String>> basePath = new ArrayList();
		swaggerProperties.getBasePath().forEach(path -> basePath.add(PathSelectors.ant(path)));

		// exclude-path处理
		if (CollUtil.isEmpty(swaggerProperties.getExcludePath())) {
			swaggerProperties.getExcludePath().add(DEFAULT_EXCLUDE_PATH);
		}
		List<Predicate<String>> excludePath = new ArrayList<>();
		swaggerProperties.getExcludePath().forEach(path -> excludePath.add(PathSelectors.ant(path)));

		//noinspection Guava
		return new Docket(DocumentationType.SWAGGER_2)
			.host(swaggerProperties.getHost())
			.apiInfo(apiInfo(swaggerProperties)).select()
			.apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
			.paths(Predicates.and(Predicates.not(Predicates.or(excludePath)), Predicates.or(basePath)))
			.build()
			.securitySchemes(Collections.singletonList(securitySchema()))
			.securityContexts(Collections.singletonList(securityContext()))
			.pathMapping("/");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth())
			.forPaths(PathSelectors.ant(BASE_PATH))
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

	private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
		return new ApiInfoBuilder()
			.title(swaggerProperties.getTitle())
			.description(swaggerProperties.getDescription())
			.license(swaggerProperties.getLicense())
			.licenseUrl(swaggerProperties.getLicenseUrl())
			.termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
			.contact(new Contact(swaggerProperties.getContact().getName(), swaggerProperties.getContact().getUrl(), swaggerProperties.getContact().getEmail()))
			.version(swaggerProperties.getVersion())
			.build();
	}

}
