package com.ssafy.jansorry.feed.repository;

import static com.ssafy.jansorry.action.domain.QAction.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.jansorry.action.domain.Action;
import com.ssafy.jansorry.favorite.dto.FavoriteInfoDto;
import com.ssafy.jansorry.favorite.service.FavoriteService;
import com.ssafy.jansorry.feed.dto.FeedDto;
import com.ssafy.jansorry.feed.dto.FeedInfoResponse;
import com.ssafy.jansorry.feed.util.FeedMapper;
import com.ssafy.jansorry.follow.service.FollowService;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedCustomRepository {
	private final JPAQueryFactory queryFactory;
	private final FavoriteService favoriteService;
	private final FollowService followService;

	@Override
	public Slice<FeedInfoResponse> searchFeedsByTime(Long memberId, Long lastActionId, Pageable pageable) {
		List<Action> actions = queryFactory
			.selectFrom(action)
			.where(
				ltActionId(lastActionId), // action.id < lastActionId
				action.deleted.isFalse()
			)
			.orderBy(action.id.desc()) // 최신순으로 보여줌
			.limit(pageable.getPageSize() + 1) // limit보다 한 개 더 들고온다.
			.fetch();

		return getFeedInfoResponses(memberId, pageable, actions);
	}

	@Override
	public Slice<FeedInfoResponse> searchFeedsByAgeRange(Long memberId, Long lastActionId, int age, Pageable pageable) {
		LocalDateTime currentDate = LocalDateTime.now();

		List<Action> actions = queryFactory
			.selectFrom(action)
			.where(
				ltActionId(lastActionId),
				action.member.birth.subtract(currentDate.getYear()).abs().between(age - 1, age + 8),
				action.deleted.isFalse()
			)
			.orderBy(action.id.desc())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		return getFeedInfoResponses(memberId, pageable, actions);
	}

	@Override
	public Slice<FeedInfoResponse> searchFeedsByFollow(Long memberId, Set<Long> memberIdSet, Long lastActionId,
		Pageable pageable) {
		List<Action> actions = queryFactory
			.selectFrom(action)
			.where(
				action.member.id.in(memberIdSet),
				ltActionId(lastActionId),
				action.deleted.isFalse()
			)
			.orderBy(action.id.desc())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		return getFeedInfoResponses(memberId, pageable, actions);
	}

	private Slice<FeedInfoResponse> getFeedInfoResponses(Long memberId, Pageable pageable, List<Action> actions) {
		List<FeedInfoResponse> feedInfoResponses = FeedMapper.fromActions(actions);
		FavoriteInfoDto favoriteInfoDto;
		for (FeedInfoResponse feedInfoResponse : feedInfoResponses) {
			favoriteInfoDto = favoriteService.readFavoriteInfo(feedInfoResponse.getActionId(),
				memberId);
			feedInfoResponse.setFavoriteCount(favoriteInfoDto.favoriteCount());
			feedInfoResponse.setIsFavorite(favoriteInfoDto.checked());

			if (Objects.equals(memberId, feedInfoResponse.getMemberId())) {
				feedInfoResponse.setIsFollow(null);
			} else {
				feedInfoResponse.setIsFollow(
					followService.readFollowCheck(memberId, feedInfoResponse.getMemberId()));
			}
		}

		if (CollectionUtils.isEmpty(feedInfoResponses)) {
			return new SliceImpl<>(feedInfoResponses, pageable, false);
		}
		return checkLastPage(pageable, feedInfoResponses);
	}

	@Override
	public Slice<FeedInfoResponse> searchFeedsByFavorites(Long memberId, Map<Long, Long> map, Pageable pageable) {
		// QueryDSL 쿼리 실행
		List<FeedDto> feeds = queryFactory
			.select(Projections.fields(FeedDto.class,
				action.id.as("id"),
				action.member,
				action.nag,
				action.content,
				action.createdAt
			))
			.from(action)
			.where(
				action.id.in(map.keySet()),
				action.deleted.isFalse()
			)
			.orderBy(action.id.desc())
			.limit(pageable.getPageSize())
			.fetch();

		// 각 Feed에 대한 추가 정보 설정 (예: 즐겨찾기 수)
		FavoriteInfoDto favoriteInfoDto;
		for (FeedDto feed : feeds) {
			favoriteInfoDto = favoriteService.readFavoriteInfo(feed.getId(),
				feed.getMember().getId());
			feed.setIsFavorite(favoriteInfoDto.checked());
			feed.setIsFollow(followService.readFollowCheck(memberId, feed.getMember().getId()));
			feed.setSize(map.get(feed.getId()));
		}

		// size 값을 기준으로 결과 리스트를 내림차순으로 정렬
		feeds.sort((feed1, feed2) -> Long.compare(feed2.getSize(), feed1.getSize()));

		// 마지막 페이지인지 확인
		boolean hasNext = feeds.size() >= pageable.getPageSize();

		List<FeedInfoResponse> feedInfoResponses = FeedMapper.fromFeedDtos(feeds);
		if (CollectionUtils.isEmpty(feedInfoResponses)) {
			return new SliceImpl<>(feedInfoResponses, pageable, false);
		}
		return new SliceImpl<>(feedInfoResponses, pageable, hasNext);
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
