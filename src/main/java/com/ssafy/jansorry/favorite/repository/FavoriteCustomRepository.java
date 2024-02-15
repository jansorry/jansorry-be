package com.ssafy.jansorry.favorite.repository;

import java.util.Set;

public interface FavoriteCustomRepository {
	void updateFavoritesByActionId(Long actionId, Set<Long> updatedMemberIds);
}
