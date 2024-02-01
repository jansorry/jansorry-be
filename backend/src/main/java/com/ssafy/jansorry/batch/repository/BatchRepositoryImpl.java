package com.ssafy.jansorry.batch.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.jansorry.action.repository.ActionRepository;
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

}
