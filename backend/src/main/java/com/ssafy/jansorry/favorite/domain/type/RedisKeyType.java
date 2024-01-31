package com.ssafy.jansorry.favorite.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedisKeyType {
	FAVORITE_UPDATES_ZSET("favorite:updates");

	private final String value;
}
