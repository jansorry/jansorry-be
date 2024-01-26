package com.ssafy.jansorry.favorite.dto;

import lombok.Builder;

@Builder
public record FavoriteInfoDto(
	Long favoriteCount,
	Boolean checked
) {
}
