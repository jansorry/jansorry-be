package com.ssafy.jansorry.member.domain;

import com.ssafy.jansorry.member.domain.type.OauthServerType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthId {
	@Column(nullable = false, name = "oauth_server_id")
	private String oauthServerId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "oauth_server")
	private OauthServerType oauthServerType;
}
