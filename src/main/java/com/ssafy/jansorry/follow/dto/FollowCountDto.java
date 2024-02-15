package com.ssafy.jansorry.follow.dto;

import lombok.Builder;

@Builder
public record FollowCountDto(
	Long followerCount,
	Long followingCount
) {
}
