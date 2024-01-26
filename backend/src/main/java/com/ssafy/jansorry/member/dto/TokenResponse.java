package com.ssafy.jansorry.member.dto;

import lombok.Builder;

@Builder
public record TokenResponse(
	String accessToken,
	String refreshToken
) {
}
