package com.ssafy.jansorry.favorite.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedisKeyType {
	FAVORITE_UPDATES_ZSET("favorite:updates"),
	FAVORITE_RANKED_ZSET("favorite:top10");

	private final String value;
}
