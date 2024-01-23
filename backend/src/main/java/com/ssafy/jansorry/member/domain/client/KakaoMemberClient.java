package com.ssafy.jansorry.member.domain.client;

import com.ssafy.jansorry.config.KakaoOauthConfig;
import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.member.domain.type.OauthServerType;
import com.ssafy.jansorry.member.dto.KakaoLogoutResponse;
import com.ssafy.jansorry.member.dto.KakaoMemberResponse;
import com.ssafy.jansorry.member.dto.KakaoToken;
import com.ssafy.jansorry.member.dto.OauthDto;
import com.ssafy.jansorry.member.util.MemberMapper;
import com.ssafy.jansorry.member.util.OauthIdMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class KakaoMemberClient implements OauthMemberClient {
	private final KakaoApiClient kakaoApiClient;
	private final KakaoOauthConfig kakaoOauthConfig;
	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public OauthServerType supportServer() {
		return OauthServerType.KAKAO;
	}

	@Override
	public OauthDto fetch(String authCode) {
		KakaoToken tokenInfo = kakaoApiClient.fetchToken(tokenRequestParams(authCode));
		KakaoMemberResponse kakaoMemberResponse =
			kakaoApiClient.fetchMember("Bearer " + tokenInfo.accessToken());
		Member member = MemberMapper.toMember(kakaoMemberResponse.id());
		return OauthIdMapper.from(member, tokenInfo.accessToken());
	}

	@Override
	public KakaoLogoutResponse logout(String oauthId) {
		String oauthAccessToken = (String) redisTemplate.opsForHash().get(oauthId, "oauthAccessToken");
		return kakaoApiClient.logout("Bearer " + oauthAccessToken);
	}

	private MultiValueMap<String, String> tokenRequestParams(String authCode) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", kakaoOauthConfig.clientId());
		params.add("redirect_uri", kakaoOauthConfig.redirectUri());
		params.add("code", authCode);
		return params;
	}
}
