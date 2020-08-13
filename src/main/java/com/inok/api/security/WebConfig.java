package com.inok.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	private static final String[] EXCLUDE_PATHS = {
			"/h2-console/**",
			"/user/**"
	};
	
	@Autowired
	private JwtInterceptor jwtInterceptor;
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtInterceptor)
					.addPathPatterns("/**")
					.excludePathPatterns(EXCLUDE_PATHS);
	}
}
