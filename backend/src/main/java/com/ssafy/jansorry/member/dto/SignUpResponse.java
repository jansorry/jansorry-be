package com.ssafy.jansorry.member.dto;

import lombok.Builder;

@Builder
public record SignUpResponse(
	String nickname,
	String kakaoNickname,
	String accessToken) {
}
