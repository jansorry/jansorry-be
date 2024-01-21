package com.ssafy.jansorry.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.oauth.kakao")
public record KakaoOauthConfig(
	String redirectUri,
	String clientId
) {

}
