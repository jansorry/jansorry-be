package com.ssafy.jansorry.feed.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class FeedInfoResponse {
	private Long memberId;
	private Long actionId;
	private String nickname;
	private Long profileImage;
	private String nag;
	private String action;
	private Long categoryId;
	private String categoryTitle;
	private Long favoriteCount;
	private Boolean isFollow;
	private Boolean isFavorite;
	private String createdAt;
}