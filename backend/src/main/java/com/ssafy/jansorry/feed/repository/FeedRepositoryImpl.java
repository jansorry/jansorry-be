package com.ssafy.jansorry.feed.repository;

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
import com.ssafy.jansorry.feed.dto.FeedInfoResponse;
import com.ssafy.jansorry.feed.util.FeedMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedCustomRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<FeedInfoResponse> searchBySlice(Long lastActionId, Pageable pageable) {
		List<Action> actions = queryFactory
			.selectFrom(action)
			.where(
				ltActionId(lastActionId) // action.id < lastActionId
			)
			.orderBy(action.id.desc()) // 최신순으로 보여줌
			.limit(pageable.getPageSize() + 1) // limit보다 한 개 더 들고온다.
			.fetch();

		List<FeedInfoResponse> feedInfoResponses = FeedMapper.fromActions(actions);
		if (CollectionUtils.isEmpty(feedInfoResponses)) {
			return new SliceImpl<>(feedInfoResponses, pageable, false);
		}
		return checkLastPage(pageable, feedInfoResponses);
	}

	// 동적 쿼리를 위한 BooleanExpression
	private BooleanExpression ltActionId(Long actionId) {
		if (actionId == null) { // 요청이 처음일 때 where 절에 null을 주면 page size만큼 반환
			return null;
		}
		return action.id.lt(actionId);
	}

	private Slice<FeedInfoResponse> checkLastPage(Pageable pageable,
		List<FeedInfoResponse> results) {
		boolean hasNext = false;
		// 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
		if (results.size() > pageable.getPageSize()) {
			hasNext = true;
			results.remove(pageable.getPageSize());
		}

		return new SliceImpl<>(results, pageable, hasNext);
	}
}
