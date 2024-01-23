package com.ssafy.jansorry.member.dto;

import lombok.Builder;

@Builder
public record MemberResponse(
	String nickname,
	Long imageUrl,
	Long followerCnt,
	Long followingCnt
) {

}
