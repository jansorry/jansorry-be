package com.ssafy.jansorry.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedisDatabaseType {
	TOKEN_DB_IDX,
	FOLLOW_DB_IDX,
	FAVORITE_DB_IDX,
	STATISTIC_DB_IDX;
}
