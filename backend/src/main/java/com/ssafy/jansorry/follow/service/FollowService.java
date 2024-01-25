package com.ssafy.jansorry.follow.service;

import java.util.Optional;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ssafy.jansorry.follow.domain.Follow;
import com.ssafy.jansorry.follow.dto.MemberSearchResponse;
import com.ssafy.jansorry.follow.repository.FollowRepository;
import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowService {

	private final MemberRepository memberRepository;
	private final FollowRepository followRepository;
	private final RedisTemplate<String, Object> followRedisTemplate;
	private final String FOLLOWREDIS = "followRedis";

	// 회원을 검색한 후, 있으면 바로 팔로우 아니면 null 값 반환
	public MemberSearchResponse createFollow(Member member, String nickname) {
		Optional<Member> existingMember = memberRepository.findByNickname(nickname);

		// 검색한 닉네임에 해당하는 member가 있다면
		if (existingMember.isPresent() && existingMember.get().getId() != member.getId()) {
			followRepository.save( // 팔로우 추가
				Follow.builder()
					.member(member)
					.toId(existingMember.get().getId())
					.build());

			return MemberSearchResponse.builder()
				.memberId(existingMember.get().getId())
				.nickname(existingMember.get().getNickname())
				.imageUrl(existingMember.get().getImageUrl())
				.build();
		}

		return MemberSearchResponse.builder().build(); // 검색한 닉네임에 해당하는 member가 없다면 null반환
	}

	public void createFollow(Member member, Long toId) {
		// redis 팔로우 저장
		HashOperations<String, Object, Object> hashOperations = followRedisTemplate.opsForHash();
		if (hashOperations.get(String.valueOf(toId), FOLLOWREDIS) == null) {
			hashOperations.put(String.valueOf(toId), FOLLOWREDIS, member.getId());
		} else {
			hashOperations.delete(String.valueOf(toId), FOLLOWREDIS, member.getId());
		}
	}

	public void deleteFollow(Member member, Long toId) {

	}

}
