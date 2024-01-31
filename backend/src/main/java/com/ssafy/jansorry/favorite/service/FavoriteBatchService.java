package com.ssafy.jansorry.favorite.service;

import static com.ssafy.jansorry.favorite.domain.type.RedisKeyType.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.jansorry.favorite.dto.FavoriteDto;
import com.ssafy.jansorry.favorite.repository.FavoriteCustomRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteBatchService {
	private final RedisTemplate<String, Object> favoriteRedisTemplate;
	private final RedisTemplate<String, Object> favoriteZSetRedisTemplate;
	private final FavoriteCustomRepository favoriteCustomRepository;

	// 1. MySQL에 데이터 반영
	public Set<String> synchronizeUpdatedData(LocalDateTime prevBatchTime) {
		// 1-1. zset에 업데이트된 actionId set 가져오기
		Set<String> updatedActionIds = favoriteZSetRedisTemplate.opsForZSet().rangeByScore(
				FAVORITE_UPDATES_ZSET.getValue(),
				prevBatchTime.toEpochSecond(ZoneOffset.UTC),
				Double.POSITIVE_INFINITY
			).stream()
			.map(Object::toString)
			.collect(Collectors.toSet());

		// 1-2. 업데이트된 actionId set을 기반으로 redis로부터 최신 상태의 favorite dto 조회 -> 반영
		for (String actionId : updatedActionIds) {
			FavoriteDto favoriteDto = (FavoriteDto)favoriteRedisTemplate.opsForValue().get(actionId);
			if (favoriteDto != null) {
				Set<Long> updatedMemberIds = favoriteDto.getMemberIdSet();

				// MySQL 데이터베이스 업데이트 로직
				favoriteCustomRepository.updateFavoritesByActionId(Long.parseLong(actionId), updatedMemberIds);
			}
		}

		return updatedActionIds;
	}

	// 2. redis 에서 empty set row 삭제
	public void deleteEmptySet(Set<String> updatedKeys) {
		updatedKeys.forEach(key -> {
			// Fetch the FavoriteDto object from Redis
			FavoriteDto favoriteDto = (FavoriteDto)favoriteRedisTemplate.opsForValue().get(key);

			if (favoriteDto == null || favoriteDto.getMemberIdSet().isEmpty()) {// delete the key from Redis
				favoriteRedisTemplate.delete(key);
			}
		});
	}

	// 3. 배치 작업 후 ZSet에서 모든 항목을 정리
	public void refreshZSetAfterBatch() {
		favoriteRedisTemplate.delete(FAVORITE_UPDATES_ZSET.getValue());// ZSet 전체를 삭제
	}
}
