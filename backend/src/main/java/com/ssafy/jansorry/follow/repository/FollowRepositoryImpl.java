package com.ssafy.jansorry.follow.repository;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.jansorry.follow.domain.Follow;
import com.ssafy.jansorry.follow.domain.QFollow;
import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.member.repository.MemberRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowCustomRepository {
	private final JPAQueryFactory queryFactory;
	private final EntityManager entityManager;
	private final MemberRepository memberRepository;

	@Override
	public void updateFollowsByFromId(Long fromId, Set<Long> updatedMemberIds) {
		QFollow follow = QFollow.follow;

		// 1. MySQL에서 Redis에 없는 데이터 삭제
		queryFactory.delete(follow)
			.where(follow.member.id.eq(fromId)
				.and(follow.toId.notIn(updatedMemberIds)))
			.execute();

		// 2. Redis에서 MySQL에 없는 데이터 추가
		// 이를 위해 MySQL에서 해당 fromId의 모든 memberId를 가져온다.
		List<Long> existingMemberIds = queryFactory.select(follow.toId)
			.from(follow)
			.where(follow.member.id.eq(fromId))
			.fetch();

		// 3. Member 엔티티 조회
		Member fromMember = memberRepository.findById(fromId).orElse(null);
		if (fromMember == null) {// 해당 회원을 찾을 수 없는 경우 리턴
			return;
		}

		// 4. 새로운 Follow 인스턴스 생성
		List<Follow> followsToAdd = updatedMemberIds.stream()
			.filter(id -> !existingMemberIds.contains(id))
			.map(toId -> Follow.builder()
				.member(fromMember)
				.toId(toId)
				.build())
			.toList();

		// 5. 새로운 Follow 인스턴스 저장
		followsToAdd.forEach(entityManager::persist); // 배치 삽입 최적화
	}
}
