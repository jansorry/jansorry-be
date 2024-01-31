package com.ssafy.jansorry.feed.dto;

import lombok.Builder;

@Builder
public record FeedInfoResponse(
	Long memberId,
	Long actionId,
	String nickname,
	String nag,
	String action,
	Long categoryId,
	String categoryTitle,
	Long favoriteSize,
	String createdAt
) {
}