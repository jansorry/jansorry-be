package com.ssafy.jansorry.member.dto;


public record SignUpRequest(
	String oauthId,
	Long birth,
	Long genderId
) {

}
