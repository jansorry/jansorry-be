package com.ssafy.jansorry.member.service;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.member.domain.authCode.AuthCodeRequestUrlProviderComposite;
import com.ssafy.jansorry.member.domain.client.OauthMemberClientComposite;
import com.ssafy.jansorry.member.domain.type.OauthServerType;
import com.ssafy.jansorry.member.dto.KakaoLogoutResponse;
import com.ssafy.jansorry.member.dto.LoginResponse;
import com.ssafy.jansorry.member.dto.OauthDto;
import com.ssafy.jansorry.member.repository.MemberRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OauthService {
	private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
	private final OauthMemberClientComposite oauthMemberClientComposite;
	private final MemberRepository memberRepository;
	private final TokenService tokenService;
	private final RedisTemplate<String, Object> tokenRedisTemplate;

	public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
		return authCodeRequestUrlProviderComposite.provide(oauthServerType);
	}

	public LoginResponse login(OauthServerType oauthServerType, String authCode) {
		OauthDto dto = oauthMemberClientComposite.fetch(oauthServerType, authCode);

		Member member = memberRepository.findByOauthId(dto.member().getOauthId()).orElseGet(() -> null);

		// redis oauthAccessToken 저장
		HashOperations<String, Object, Object> hashOperations = tokenRedisTemplate.opsForHash();
		hashOperations.put(dto.member().getOauthId().getOauthServerId(), "oauthAccessToken", dto.accessToken());

		if (member == null) { // 가입하지 않은 유저일 경우
			return LoginResponse.builder().oauthId(dto.member().getOauthId().getOauthServerId()).build();
		} else if (member.getDeleted()) {
			throw new RuntimeException("탈퇴한 회원입니다.");
		}

		return LoginResponse.builder()
			.oauthId(member.getOauthId().getOauthServerId())
			.nickname(member.getNickname())
			.accessToken(tokenService.createToken(member))
			.refeshToken(tokenService.createRefreshToken(member))
			.build();
	}

	public KakaoLogoutResponse logout(HttpServletResponse response, OauthServerType oauthServerType, String oauthId) {
		tokenService.deleteHeader(response);
		return oauthMemberClientComposite.logout(oauthServerType, oauthId);
	}
}
