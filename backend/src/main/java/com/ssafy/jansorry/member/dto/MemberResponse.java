package com.ssafy.jansorry.member.dto;

import lombok.Builder;

@Builder
public record MemberResponse(
	Long imageUrl,
	Long followerCnt,
	Long followingCnt
) {

}
