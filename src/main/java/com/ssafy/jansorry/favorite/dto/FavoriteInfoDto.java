package com.ssafy.jansorry.favorite.dto;

import lombok.Builder;

@Builder
public record FavoriteInfoDto(
	Long favoriteCount,// 좋아요 개수
	Boolean checked// 좋아요 여부
) {
}
