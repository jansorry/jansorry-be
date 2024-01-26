package com.ssafy.jansorry.favorite.dto;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.Builder;

@Builder
public record FavoriteDto(
	Set<Long> memberIdSet,
	LocalDateTime updatedAt) {
}
