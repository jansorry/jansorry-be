package com.ssafy.jansorry.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponse {

	private Long memberId;
	private String nickname;
	private String accessToken;
	private String refeshToken;
}