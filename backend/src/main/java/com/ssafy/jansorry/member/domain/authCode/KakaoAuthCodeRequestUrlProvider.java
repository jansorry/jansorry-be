package com.ssafy.jansorry.member.domain.authCode;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.ssafy.jansorry.config.KakaoOauthConfig;
import com.ssafy.jansorry.member.domain.type.OauthServerType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KakaoAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {
	private final KakaoOauthConfig kakaoOauthConfig;

	@Override
	public OauthServerType supportServer() {
		return OauthServerType.KAKAO;
	}

	@Override
	public String provide() {
		return UriComponentsBuilder
			.fromUriString("https://kauth.kakao.com/oauth/authorize")
			.queryParam("response_type", "code")
			.queryParam("client_id", kakaoOauthConfig.clientId())
			.queryParam("redirect_uri", kakaoOauthConfig.redirectUri())
			.toUriString();
	}
}
