package com.ssafy.jansorry.follow.service;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ssafy.jansorry.follow.dto.FollowCountDto;
import com.ssafy.jansorry.follow.dto.FollowDto;
import com.ssafy.jansorry.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowService {
	private final RedisTemplate<String, Object> followRedisTemplate;
	private final MemberRepository memberRepository;

	private final String FOLLOWING = "following:", FOLLOWER = "follower:";

	/**
	 * @param fromId : 주체
	 * @param toId : 대상
	 * @return : 주체 -> 대상 팔로우 여부 반환
	 */
	public Boolean readFollowCheck(Long fromId, Long toId) {
		String followerKey = FOLLOWER + toId.toString();
		FollowDto followerDto = getFollowDto(followerKey);
		return followerDto.getMemberIdSet().contains(fromId);
	}

	/**
	 * @param memberId
	 * @return : 팔로워 수 & 팔로잉 수
	 * 해당 멤버의 팔로워수와 팔로잉 수를 반환한다.
	 */
	public FollowCountDto readFollowCount(Long memberId) {
		String followerKey = FOLLOWER + memberId.toString();
		String followingKey = FOLLOWING + memberId.toString();

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
		String followingKey = FOLLOWING + fromId.toString();
		String followerKey = FOLLOWER + toId.toString();

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

			updateFollowDto(followingKey, followingDto); // 팔로잉 업데이트
			updateFollowDto(followerKey, followerDto); // 팔로워 업데이트 (양방향 업데이트)
			return;
		}
		// 팔로우 취소
		if (followingDto == null || followerDto == null) {
			return; // Redis에 키가 없으면 (즉, DTO가 null이면) 바로 리턴
		}
		followingDto.removeFollow(toId); // 해당 대상을 삭제
		followerDto.removeFollow(fromId); // 해당 주체를 삭제

		updateFollowDto(followingKey, followingDto); // 팔로잉 업데이트
		updateFollowDto(followerKey, followerDto); // 팔로워 업데이트 (양방향 업데이트)
	}

	// redis 로부터 해당 FollowDto 를 반환하는 메서드
	public FollowDto getFollowDto(String key) {
		return (FollowDto)followRedisTemplate.opsForValue().get(key);
	}

	// redis 에 업데이트 하는 메서드
	private void updateFollowDto(String key, FollowDto updatedFollowDto) {
		followRedisTemplate.opsForValue().set(key, updatedFollowDto);
	}

	// batch & scheduler: redis to mysql
	public void synchronizeFollows() {
		// Redis 데이터를 MySQL에 동기화하는 로직 구현

		// todo: 모든 팔로우 삭제되있다면 batch에 반영 후, redis에서 해당 key,value 완전 제거하기
	}
}
