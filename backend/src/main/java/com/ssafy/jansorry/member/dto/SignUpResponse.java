package com.ssafy.jansorry.member.dto;

import lombok.Builder;

@Builder
public record SignUpResponse(
	Long memberId,
	String nickname
) {
}
