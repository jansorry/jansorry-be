package com.ssafy.jansorry.action.repository;

import static com.ssafy.jansorry.action.domain.QAction.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.jansorry.action.domain.Action;
import com.ssafy.jansorry.action.dto.ActionDto;
import com.ssafy.jansorry.action.util.ActionMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ActionRepositoryImpl implements ActionCustomRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<ActionDto> searchActionsByMember(Long lastActionId, Long memberId, Pageable pageable
	) {
		List<Action> actions = queryFactory
			.selectFrom(action)
			.where(
				ltActionId(lastActionId),
				action.member.id.eq(memberId),
				action.deleted.isFalse()
			)
			.orderBy(action.id.desc())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		List<ActionDto> actionDtos = ActionMapper.toDtos(actions);
		if (CollectionUtils.isEmpty(actionDtos)) {
			return new SliceImpl<>(actionDtos, pageable, false);
		}
		return checkLastPage(pageable, actionDtos);
	}

	// 동적 쿼리를 위한 BooleanExpression
	private BooleanExpression ltActionId(Long actionId) {
		if (actionId == null) { // 요청이 처음일 때 where 절에 null을 주면 page size만큼 반환
			return null;
		}
		return action.id.lt(actionId);
	}

	private Slice<ActionDto> checkLastPage(Pageable pageable,
		List<ActionDto> results) {
		boolean hasNext = false;
		// 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
		if (results.size() > pageable.getPageSize()) {
			hasNext = true;
			results.remove(pageable.getPageSize());
		}

		return new SliceImpl<>(results, pageable, hasNext);
	}
}
