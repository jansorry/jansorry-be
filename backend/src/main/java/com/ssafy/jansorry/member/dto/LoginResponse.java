package com.ssafy.jansorry.member.dto;

import lombok.Builder;

@Builder
public record LoginResponse(
	String oauthId,
	String nickname,
	String accessToken
) {
}