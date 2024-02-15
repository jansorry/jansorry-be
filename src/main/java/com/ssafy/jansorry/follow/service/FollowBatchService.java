package com.ssafy.jansorry.follow.service;

import static com.ssafy.jansorry.follow.domain.type.FollowRedisKeyType.*;

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
		Set<String> updatedKeys = followZSetRedisTemplate.opsForZSet().rangeByScore(
				FOLLOW_UPDATES_ZSET.getValue(),
				prevBatchTime.toEpochSecond(ZoneOffset.UTC),
				Double.POSITIVE_INFINITY
			).stream()
			.map(Object::toString)
			.collect(Collectors.toSet());

		// 1-2. 업데이트된 fromId set을 기반으로 redis로부터 최신 상태의 follow dto 조회 -> 반영
		for (String key : updatedKeys) {
			if (key.contains(FOLLOWER.getValue())) {// 팔로워 관련 zset value일 경우 스킵
				continue;
			}
			// 팔로잉 관련 zset value일 경우
			FollowDto followingDto = (FollowDto)followRedisTemplate.opsForValue().get(key);// from id (following:~)
			if (followingDto != null) {// 지워지지 않았다면
				Set<Long> updatedMemberIds = followingDto.getMemberIdSet();

				// MySQL로 데이터 업데이트
				followCustomRepository.updateFollowsByFromId(Long.parseLong(key.replace(FOLLOWING.getValue(), "")), updatedMemberIds);// 접두사 제거 후 진행
			}
		}

		return updatedKeys;
	}

	// 2. redis 에서 empty set row 삭제
	public void deleteEmptySet(Set<String> updatedKeys) {// 팔로잉 및 팔로워 접두사 포함으로 이루어진 set
		updatedKeys.forEach(key -> {
			// Fetch the FollowDto object from Redis
			FollowDto followDto = (FollowDto)followRedisTemplate.opsForValue().get(key);// following + follower 전체 업데이트 관련 키셋
			if (followDto == null || followDto.getMemberIdSet().isEmpty()) {// null이거나 비어있다면
				followRedisTemplate.delete(key);// 삭제
			}
		});
	}

	// 3. 배치 작업 후 ZSet에서 모든 항목을 정리
	public void refreshZSetAfterBatch() {
		followRedisTemplate.delete(FOLLOW_UPDATES_ZSET.getValue());// ZSet 반영 완료 후 비우기 작업
	}
}
