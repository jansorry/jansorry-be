package com.ssafy.jansorry.member.dto;

public record SignUpRequest(
	String oauthId,
	String kakaoNickname,
	Long birth,
	Long genderId) {

}
