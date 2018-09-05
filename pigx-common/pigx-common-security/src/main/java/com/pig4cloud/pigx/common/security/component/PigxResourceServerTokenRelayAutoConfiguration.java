package com.pig4cloud.pigx.common.security.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.cloud.security.oauth2.client.AccessTokenContextRelay;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.config.annotation.web.configuration.OAuth2ClientConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.*;

/**
 * @author lengleng
 * @date 2018/9/5
 * 注入AccessTokenContextRelay 解决feign 传递token 为空问题
 */
@Configuration
@AutoConfigureAfter(OAuth2AutoConfiguration.class)
@ConditionalOnWebApplication
public class PigxResourceServerTokenRelayAutoConfiguration {

	@Bean
	public AccessTokenContextRelay accessTokenContextRelay(OAuth2ClientContext context) {
		return new AccessTokenContextRelay(context);
	}

	@Configuration
	public static class ResourceServerTokenRelayRegistrationAutoConfiguration implements WebMvcConfigurer {
		@Autowired
		AccessTokenContextRelay accessTokenContextRelay;

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(
				new HandlerInterceptorAdapter() {
					@Override
					public boolean preHandle(HttpServletRequest request,
											 HttpServletResponse response, Object handler) throws Exception {
						accessTokenContextRelay.copyToken();
						return true;
					}
				}

			);
		}

	}

	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Conditional(OAuth2OnClientInResourceServerCondition.class)
	@interface ConditionalOnOAuth2ClientInResourceServer {

	}

	private static class OAuth2OnClientInResourceServerCondition
		extends AllNestedConditions {

		public OAuth2OnClientInResourceServerCondition() {
			super(ConfigurationPhase.REGISTER_BEAN);
		}

		@ConditionalOnBean(ResourceServerConfiguration.class)
		static class Server {
		}

		@ConditionalOnBean(OAuth2ClientConfiguration.class)
		static class Client {
		}

	}
}
