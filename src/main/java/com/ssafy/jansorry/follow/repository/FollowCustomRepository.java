package com.ssafy.jansorry.follow.repository;

import java.util.Set;

public interface FollowCustomRepository {
	void updateFollowsByFromId(Long fromId, Set<Long> updatedMemberIds);
}
