package com.ssafy.jansorry.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public record LoginResponse(
	Long memberId,
	String nickname,
	String accessToken,
	String refeshToken
) {
}