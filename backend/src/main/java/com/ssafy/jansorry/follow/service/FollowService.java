package com.ssafy.jansorry.follow.service;

import static com.ssafy.jansorry.exception.ErrorCode.*;
import static com.ssafy.jansorry.follow.domain.type.FollowRedisKeyType.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ssafy.jansorry.exception.BaseException;
import com.ssafy.jansorry.follow.dto.FollowCountDto;
import com.ssafy.jansorry.follow.dto.FollowDto;
import com.ssafy.jansorry.follow.dto.FollowerDto;
import com.ssafy.jansorry.follow.dto.FollowingDto;
import com.ssafy.jansorry.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowService {
	private final RedisTemplate<String, Object> followRedisTemplate;
	private final RedisTemplate<String, Object> followZSetRedisTemplate;
	private final MemberRepository memberRepository;

	/**
	 * @param fromId : 주체
	 * @param toId : 대상
	 * @return : 주체 -> 대상 팔로우 여부 반환
	 */
	public Boolean readFollowCheck(Long fromId, Long toId) {
		String followerKey = FOLLOWER.getValue() + toId.toString();
		FollowDto followerDto = getFollowDto(followerKey);
		if (followerDto == null) {
			return false;
		}
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
		updateFollowUpdatesZSet(followingKey, followingDto.getUpdatedAt());// ZSet에 팔로잉 업데이트 정보 추가
		updateFollowUpdatesZSet(followerKey, followingDto.getUpdatedAt());// ZSet에 팔로워 업데이트 정보 추가
	}

	// redis 로부터 팔로워 리스트를 반환하는 메서드
	public List<FollowerDto> readAllFollowers(Long memberId) {
		String followerKey = FOLLOWER.getValue() + memberId.toString();
		return Optional.ofNullable(getFollowDto(followerKey))
			.map(FollowDto::getMemberIdSet)
			.orElse(Collections.emptySet()) // Null 대신 empty set 반환
			.stream()
			.map(fromId -> memberRepository.findById(fromId)
				.map(member -> FollowerDto.builder()
					.fromId(fromId)
					.imageUrl(member.getImageUrl())
					.nickname(member.getNickname())
					.build())
				.orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER))) // 멤버가 없는 경우 예외 처리
			.collect(Collectors.toList());
	}

	// redis 로부터 팔로잉 리스트를 반환하는 메서드
	public List<FollowingDto> readAllFollowings(Long memberId) {
		String followingKey = FOLLOWING.getValue() + memberId.toString();
		return Optional.ofNullable(getFollowDto(followingKey))
			.map(FollowDto::getMemberIdSet)
			.orElse(Collections.emptySet()) // Null 대신 empty set 반환
			.stream()
			.map(toId -> memberRepository.findById(toId)
				.map(member -> FollowingDto.builder()
					.toId(toId)
					.imageUrl(member.getImageUrl())
					.nickname(member.getNickname())
					.build())
				.orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER))) // 멤버가 없는 경우 예외 처리
			.collect(Collectors.toList());
	}

	// redis 로부터 해당 FollowDto 를 반환하는 메서드
	public FollowDto getFollowDto(String key) {
		return (FollowDto)followRedisTemplate.opsForValue().get(key);
	}

	// redis 에 업데이트 하는 메서드
	private void updateFollowDto(String key, FollowDto updatedFollowDto) {
		followRedisTemplate.opsForValue().set(key, updatedFollowDto);
	}

	// ZSet에 팔로우 업데이트 정보를 추가하는 메서드 -> 양방향 모두 저장
	private void updateFollowUpdatesZSet(String key, LocalDateTime updatedAt) {
		double score = updatedAt.toEpochSecond(ZoneOffset.UTC);
		followZSetRedisTemplate.opsForZSet().add(FOLLOW_UPDATES_ZSET.getValue(), key, score);
	}
}
