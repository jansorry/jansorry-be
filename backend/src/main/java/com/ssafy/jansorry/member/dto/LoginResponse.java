package com.ssafy.jansorry.member.dto;

import lombok.Builder;

@Builder
public record LoginResponse(
	Long memberId,
	String nickname,
	String accessToken,
	String refeshToken
) {
}