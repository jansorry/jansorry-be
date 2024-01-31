package com.ssafy.jansorry.follow.service;

import static com.ssafy.jansorry.follow.domain.type.RedisKeyType.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ssafy.jansorry.follow.dto.FollowCountDto;
import com.ssafy.jansorry.follow.dto.FollowDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowService {
	private final RedisTemplate<String, Object> followRedisTemplate;
	private final RedisTemplate<String, Object> followZSetRedisTemplate;

	/**
	 * @param fromId : 주체
	 * @param toId : 대상
	 * @return : 주체 -> 대상 팔로우 여부 반환
	 */
	public Boolean readFollowCheck(Long fromId, Long toId) {
		String followerKey = FOLLOWER.getValue() + toId.toString();
		FollowDto followerDto = getFollowDto(followerKey);
		return followerDto.getMemberIdSet().contains(fromId);
	}

	/**
	 * @param memberId
	 * @return : 팔로워 수 & 팔로잉 수
	 * 해당 멤버의 팔로워수와 팔로잉 수를 반환한다.
	 */
	public FollowCountDto readFollowCount(Long memberId) {
		String followerKey = FOLLOWER.getValue() + memberId.toString();
		String followingKey = FOLLOWING.getValue() + memberId.toString();

		FollowDto followerDto = getFollowDto(followerKey);
		FollowDto followingDto = getFollowDto(followingKey);

		Long followerCount = followerDto == null ? 0L : getFollowDto(followerKey).getMemberIdSet().size();
		Long followingCount = followingDto == null ? 0L : getFollowDto(followingKey).getMemberIdSet().size();

		return FollowCountDto.builder()
			.followerCount(followerCount)
			.followingCount(followingCount)
			.build();
	}

	/**
	 * @param fromId : 주체
	 * @param toId : 대상
	 * @param isCreate : 추가 여부 (추가 or 삭제)
	 * .
	 * 팔로우를 걸거나 취소하는 메서드로, redis에 양방향으로 관계를 저장한다.
	 *       key                value
	 * following:fromId : Set<memberId>, updatedAt
	 * follower:toId : Set<memberId>, updatedAt
	 */
	public void updateFollow(Long fromId, Long toId, boolean isCreate) {
		String followingKey = FOLLOWING.getValue() + fromId.toString();
		String followerKey = FOLLOWER.getValue() + toId.toString();

		FollowDto followingDto = getFollowDto(followingKey);
		FollowDto followerDto = getFollowDto(followerKey);

		if (isCreate) {// 팔로우 추가
			if (followingDto == null) {
				followingDto = FollowDto.builder()
					.memberIdSet(new HashSet<>())
					.updatedAt(LocalDateTime.now())
					.build();
			}
			if (followerDto == null) {
				followerDto = FollowDto.builder()
					.memberIdSet(new HashSet<>())
					.updatedAt(LocalDateTime.now())
					.build();
			}
			followingDto.addFollow(toId); // fromId의 팔로잉 목록에 toId 추가
			followerDto.addFollow(fromId); // toId의 팔로워 목록에 fromId 추가 (양방향 저장)
		} else {
			// 팔로우 취소
			if (followingDto == null || followerDto == null) {
				return; // Redis에 키가 없으면 (즉, DTO가 null이면) 바로 리턴
			}
			followingDto.removeFollow(toId); // 해당 대상을 삭제
			followerDto.removeFollow(fromId); // 해당 주체를 삭제
		}
		updateFollowDto(followingKey, followingDto); // 팔로잉 업데이트
		updateFollowDto(followerKey, followerDto); // 팔로워 업데이트 (양방향 업데이트)
		updateFollowUpdatesZSet(fromId, followingDto.getUpdatedAt());// ZSet에 업데이트 정보 추가
	}

	// redis 로부터 해당 FollowDto 를 반환하는 메서드
	private FollowDto getFollowDto(String key) {
		return (FollowDto)followRedisTemplate.opsForValue().get(key);
	}

	// redis 에 업데이트 하는 메서드
	private void updateFollowDto(String key, FollowDto updatedFollowDto) {
		followRedisTemplate.opsForValue().set(key, updatedFollowDto);
	}

	// ZSet에 팔로우 업데이트 정보를 추가하는 메서드 -> 단방향만 저장
	private void updateFollowUpdatesZSet(Long fromId, LocalDateTime updatedAt) {
		double score = updatedAt.toEpochSecond(ZoneOffset.UTC);
		followZSetRedisTemplate.opsForZSet().add(FOLLOW_UPDATES_ZSET.getValue(), fromId.toString(), score);
	}
}
