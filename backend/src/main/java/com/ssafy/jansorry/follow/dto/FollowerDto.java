package com.ssafy.jansorry.follow.dto;

import lombok.Builder;

@Builder
public record FollowerDto(
	Long fromId,
	Long imageUrl,
	String nickname
) {
}
