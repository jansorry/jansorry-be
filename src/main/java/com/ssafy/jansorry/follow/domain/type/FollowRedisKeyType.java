package com.ssafy.jansorry.follow.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FollowRedisKeyType {
	FOLLOW_UPDATES_ZSET("follow:updates"),
	FOLLOWING("following:"),
	FOLLOWER("follower:");

	private final String value;
}
