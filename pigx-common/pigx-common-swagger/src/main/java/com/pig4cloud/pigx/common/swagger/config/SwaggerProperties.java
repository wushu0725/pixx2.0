package com.pig4cloud.pigx.common.swagger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * SwaggerProperties
 *
 * @author: lisb
 * @date: 2018/7/25  14:00
 */
@Data
@ConfigurationProperties("swagger")
public class SwaggerProperties {
	/**
	 * swagger会解析的url规则
	 **/
	private List<String> basePath = new ArrayList<>();
	/**
	 * 在basePath基础上需要排除的url规则
	 **/
	private List<String> excludePath = new ArrayList<>();
}
