package com.ssafy.jansorry.member.dto;

import com.ssafy.jansorry.member.domain.Member;
import lombok.Builder;

@Builder
public record OauthDto(
	Member member,
	String accessToken
	) {
}
