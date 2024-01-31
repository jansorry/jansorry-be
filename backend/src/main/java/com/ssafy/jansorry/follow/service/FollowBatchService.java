package com.ssafy.jansorry.follow.service;

import static com.ssafy.jansorry.follow.domain.type.RedisKeyType.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.jansorry.follow.dto.FollowDto;
import com.ssafy.jansorry.follow.repository.FollowCustomRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowBatchService {
	private final RedisTemplate<String, Object> followRedisTemplate;
	private final RedisTemplate<String, Object> followZSetRedisTemplate;
	private final FollowCustomRepository followCustomRepository;

	// 1. MySQL에 데이터 반영
	public Set<String> synchronizeUpdatedData(LocalDateTime prevBatchTime) {
		// 1-1. zset에 업데이트된 memberId set 가져오기
		Set<String> updatedFromIds = followZSetRedisTemplate.opsForZSet().rangeByScore(
				FOLLOW_UPDATES_ZSET.getValue(),
				prevBatchTime.toEpochSecond(ZoneOffset.UTC),
				Double.POSITIVE_INFINITY
			).stream()
			.map(Object::toString)
			.collect(Collectors.toSet());

		// 1-2. 업데이트된 fromId set을 기반으로 redis로부터 최신 상태의 follow dto 조회 -> 반영
		for (String fromId : updatedFromIds) {
			FollowDto followDto = (FollowDto)followRedisTemplate.opsForValue().get(FOLLOWING.getValue() + fromId);
			if (followDto != null) {
				Set<Long> updatedMemberIds = followDto.getMemberIdSet();

				// MySQL 데이터베이스 업데이트 로직
				followCustomRepository.updateFollowsByFromId(Long.parseLong(fromId), updatedMemberIds);
			}
		}

		return updatedFromIds;
	}

	// 2. redis 에서 empty set row 삭제
	public void deleteEmptySet(Set<String> updatedKeys) {
		updatedKeys.forEach(key -> {
			// Fetch the FollowDto object from Redis
			FollowDto followDto = (FollowDto)followRedisTemplate.opsForValue().get(key);

			if (followDto == null || followDto.getMemberIdSet().isEmpty()) {// delete the key from Redis
				followRedisTemplate.delete(key);
			}
		});
	}

	// 3. 배치 작업 후 ZSet에서 모든 항목을 정리
	public void refreshZSetAfterBatch() {
		followRedisTemplate.delete(FOLLOW_UPDATES_ZSET.getValue());// ZSet 전체를 삭제
	}
}
