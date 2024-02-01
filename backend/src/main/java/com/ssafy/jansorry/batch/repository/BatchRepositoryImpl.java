package com.ssafy.jansorry.batch.repository;

import static com.ssafy.jansorry.action.domain.QAction.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.jansorry.action.repository.ActionRepository;
import com.ssafy.jansorry.batch.type.BatchKeyNumberType;
import com.ssafy.jansorry.member.domain.type.GenderType;
import com.ssafy.jansorry.member.repository.MemberRepository;
import com.ssafy.jansorry.receipt.repository.ReceiptRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BatchRepositoryImpl implements BatchCustomRepository {
	private final JPAQueryFactory queryFactory;
	private final EntityManager entityManager;
	private final MemberRepository memberRepository;
	private final ActionRepository actionRepository;
	private final ReceiptRepository repository;

	@Override
	public List<Long> searchTop5NagsByGender(Long categoryId, BatchKeyNumberType keyNumberType) {
		return queryFactory
			.select(action.nag.id)
			.from(action)
			.where(
				categoryEq(categoryId),
				action.member.gender.genderType.eq(GenderType.values()[keyNumberType.ordinal()])
			)
			.groupBy(action.nag.id)
			.orderBy(action.nag.id.count().desc())
			.limit(5)
			.fetch();
	}

	@Override
	public List<Long> searchTop5NagsByAll(Long categoryId) {
		return queryFactory
			.select(action.nag.id)
			.from(action)
			.where(
				categoryEq(categoryId)
			)
			.groupBy(action.nag.id)
			.orderBy(action.nag.id.count().desc())
			.limit(5)
			.fetch();
	}

	@Override
	public List<Long> searchTop5NagsByAgeRange(Long categoryId, BatchKeyNumberType keyNumberType) {
		LocalDateTime currentDate = LocalDateTime.now();
		return queryFactory
			.select(action.nag.id)
			.from(action)
			.where(
				categoryEq(categoryId),
				action.member.birth.subtract(currentDate.getYear())
					.abs()
					.between(keyNumberType.getValue() - 1, keyNumberType.getValue() + 8)
			)
			.groupBy(action.nag.id)
			.orderBy(action.nag.id.count().desc())
			.limit(5)
			.fetch();
	}

	private BooleanExpression categoryEq(Long categoryId) {
		return categoryId < 7L ? action.nag.category.id.eq(categoryId) : null;
	}
}
