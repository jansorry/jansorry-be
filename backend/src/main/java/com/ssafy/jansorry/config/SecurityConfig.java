package com.ssafy.jansorry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ssafy.jansorry.member.service.TokenService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

	private final TokenService tokenService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.cors(AbstractHttpConfigurer::disable)
			.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.formLogin(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/oauth/kakao", "/api/v1/oauth/redirected/kakao",
					"/api/v1/oauth/login/**", "/api/v1/members/signup", "/api/v1/ping")
				.permitAll()
				.anyRequest()
				.authenticated());

		http.addFilterBefore(new JwtAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
		http.exceptionHandling(e -> e.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

		return http.build();
	}

}
