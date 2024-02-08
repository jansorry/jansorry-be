package com.ssafy.jansorry.member.dto;

import lombok.Builder;

@Builder
public record MemberResponse(
	Long memberId,
	String nickname,
	Long imageUrl,
	Long actionCnt,
	Long followerCnt,
	Long followingCnt
) {

}
