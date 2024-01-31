package com.ssafy.jansorry.favorite.repository;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.jansorry.action.domain.Action;
import com.ssafy.jansorry.action.repository.ActionRepository;
import com.ssafy.jansorry.favorite.domain.Favorite;
import com.ssafy.jansorry.favorite.domain.QFavorite;
import com.ssafy.jansorry.member.repository.MemberRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteCustomRepository {
	private final JPAQueryFactory queryFactory;
	private final EntityManager entityManager;
	private final MemberRepository memberRepository;
	private final ActionRepository actionRepository;

	@Override
	public void updateFavoritesByActionId(Long actionId, Set<Long> updatedMemberIds) {
		QFavorite favorite = QFavorite.favorite;

		// 1. MySQL에서 Redis에 없는 데이터 삭제
		queryFactory.delete(favorite)
			.where(favorite.action.id.eq(actionId)
				.and(favorite.member.id.notIn(updatedMemberIds)))
			.execute();

		// 2. Redis에서 MySQL에 없는 데이터 추가
		// 이를 위해 MySQL에서 해당 actionId의 모든 memberId를 가져온다.
		List<Long> existingMemberIds = queryFactory.select(favorite.member.id)
			.from(favorite)
			.where(favorite.action.id.eq(actionId))
			.fetch();

		// 3. Action 엔티티 조회
		Action action = actionRepository.findById(actionId).orElse(null);
		if (action == null) {// 해당 대응을 찾을 수 없는 경우 리턴
			return;
		}

		// 4. 새로운 Favorite 인스턴스 생성
		List<Favorite> favoritesToAdd = updatedMemberIds.stream()
			.filter(id -> !existingMemberIds.contains(id))
			.map(memberId -> memberRepository.findById(memberId).orElse(null))
			.filter(Objects::nonNull)
			.map(member -> Favorite.builder()
				.member(member)
				.action(action)
				.build())
			.toList();

		// 5. 새로운 Favorite 인스턴스 저장
		favoritesToAdd.forEach(entityManager::persist);// 배치 삽입 최적화
	}
}
