package com.ssafy.jansorry.follow.dto;

import lombok.Builder;

@Builder
public record FollowingDto(
	Long toId,
	Long imageUrl,
	String nickname
) {
}
