package com.ssafy.jansorry.member.dto;


public record SignUpRequest(
	Long memberId,
	Long birth,
	Long genderId
) {

}
