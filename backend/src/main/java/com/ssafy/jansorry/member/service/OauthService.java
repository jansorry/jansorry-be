package com.ssafy.jansorry.member.service;

import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.member.domain.authCode.AuthCodeRequestUrlProviderComposite;
import com.ssafy.jansorry.member.domain.client.OauthMemberClientComposite;
import com.ssafy.jansorry.member.domain.type.OauthServerType;
import com.ssafy.jansorry.member.dto.KakaoLogoutResponse;
import com.ssafy.jansorry.member.dto.LoginResponse;
import com.ssafy.jansorry.member.dto.OauthDto;
import com.ssafy.jansorry.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {
	private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
	private final OauthMemberClientComposite oauthMemberClientComposite;
	private final MemberRepository memberRepository;
	private final TokenService tokenService;
	private final RedisTemplate<Long, Object> redisTemplate;

	public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
		return authCodeRequestUrlProviderComposite.provide(oauthServerType);
	}

	public LoginResponse login(OauthServerType oauthServerType, String authCode) {
		OauthDto dto = oauthMemberClientComposite.fetch(oauthServerType, authCode);
		Member saved = memberRepository.findByOauthId(dto.member().getOauthId())
			.orElseGet(() -> memberRepository.save(dto.member()));

		if (saved.getDeleted()) {
			throw new RuntimeException("탈퇴한 회원입니다.");
		}
//		 redis oauthAccessToken 저장
		HashOperations<Long, Object, Object> hashOperations = redisTemplate.opsForHash();
		hashOperations.put(saved.getId(), "oauthAccessToken", dto.accessToken());

		if (!saved.getNickname().equals("nickname")) {
			return LoginResponse.builder()
				.memberId(saved.getId())
				.nickname(saved.getNickname())
				.accessToken(tokenService.createToken(saved))
				.refeshToken(tokenService.createRefreshToken(saved))
				.build();
		}

		return LoginResponse.builder()
			.memberId(saved.getId())
			.build();
	}

	public KakaoLogoutResponse logout(OauthServerType oauthServerType, Long memberId) {
		return oauthMemberClientComposite.logout(oauthServerType, memberId);
	}
}
