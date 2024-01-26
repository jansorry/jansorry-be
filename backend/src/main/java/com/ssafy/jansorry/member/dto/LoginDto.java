package com.ssafy.jansorry.member.dto;

import lombok.Builder;

@Builder
public record LoginDto(
	String oauthId,
	String nickname,
	String accessToken,
	String refreshToken
) {
}
