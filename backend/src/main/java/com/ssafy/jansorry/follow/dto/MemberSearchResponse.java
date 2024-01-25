package com.ssafy.jansorry.follow.dto;

import lombok.Builder;

@Builder
public record MemberSearchResponse(
	Long memberId,
	String nickname,
	Long imageUrl
) {
}
