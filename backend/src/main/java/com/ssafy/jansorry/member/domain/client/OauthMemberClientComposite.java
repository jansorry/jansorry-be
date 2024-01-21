package com.ssafy.jansorry.member.domain.client;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import com.ssafy.jansorry.member.domain.type.OauthServerType;
import com.ssafy.jansorry.member.dto.KakaoLogoutResponse;
import com.ssafy.jansorry.member.dto.OauthDto;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class OauthMemberClientComposite {
	private final Map<OauthServerType, OauthMemberClient> mapping;

	public OauthMemberClientComposite(Set<OauthMemberClient> clients) {
		mapping = clients.stream()
			.collect(toMap(
				OauthMemberClient::supportServer,
				identity()
			));
	}

	public OauthDto fetch(OauthServerType oauthServerType, String authCode) {
		return getClient(oauthServerType).fetch(authCode);
	}

	public KakaoLogoutResponse logout(OauthServerType oauthServerType, Long memberId) {
		return getClient(oauthServerType).logout(memberId);
	}

	private OauthMemberClient getClient(OauthServerType oauthServerType) {
		return Optional.ofNullable(mapping.get(oauthServerType))
			.orElseThrow(() -> new RuntimeException("지원하지 않는 소셜 로그인 타입입니다."));
	}
}
