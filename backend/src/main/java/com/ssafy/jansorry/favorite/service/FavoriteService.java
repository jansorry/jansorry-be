package com.ssafy.jansorry.favorite.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.jansorry.favorite.repository.FavoriteRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteService {
	private final RedisTemplate<String, Object> favoriteRedisTemplate;
	private final FavoriteRepository favoriteRepository;

	@SuppressWarnings("unchecked")
	public void createFavorite(Long actionId, Long memberId) {
		String key = actionId.toString();
		ValueOperations<String, Object> valueOps = favoriteRedisTemplate.opsForValue();

		Set<Long> favorites = (Set<Long>)valueOps.get(key);
		if (favorites == null) {
			favorites = new HashSet<>();
		}
		favorites.add(memberId);
		valueOps.set(key, favorites);
	}

	@SuppressWarnings("unchecked")
	public void deleteFavorite(Long actionId, Long memberId) {
		String key = actionId.toString();
		ValueOperations<String, Object> valueOps = favoriteRedisTemplate.opsForValue();

		Set<Long> favorites = (Set<Long>)valueOps.get(key);
		if (favorites != null) {
			favorites.remove(memberId);
			valueOps.set(key, favorites);
		}
	}

	// batch & scheduler: redis to mysql
	public void updateFavorite() {

	}
}
