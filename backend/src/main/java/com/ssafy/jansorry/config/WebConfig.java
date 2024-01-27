package com.ssafy.jansorry.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ssafy.jansorry.member.util.OauthServerTypeConverter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("https://jansorry.com")
			// .allowedOrigins("https://localhost:3000")
			// .allowedOriginPatterns("*")
			.allowedMethods(
				HttpMethod.GET.name(),
				HttpMethod.POST.name(),
				HttpMethod.PUT.name(),
				HttpMethod.DELETE.name()
			)
			.allowedHeaders("Content-Type", "Authorization") // Authorization 헤더 허용
			.allowCredentials(true)
			.exposedHeaders("*");
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new OauthServerTypeConverter());
	}
}
