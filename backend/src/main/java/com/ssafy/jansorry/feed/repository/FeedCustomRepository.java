package com.ssafy.jansorry.feed.repository;

import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.ssafy.jansorry.feed.dto.FeedInfoResponse;

public interface FeedCustomRepository {
	Slice<FeedInfoResponse> searchFeedsByTime(Long memberId, Long lastActionId, Pageable pageable);

	Slice<FeedInfoResponse> searchFeedsByAgeRange(Long memberId, Long lastActionId, int age, Pageable pageable);

	Slice<FeedInfoResponse> searchFeedsByFollow(Long memberId, Set<Long> memberIdSet, Long lastActionId,
		Pageable pageable);

	Slice<FeedInfoResponse> searchFeedsByFavorites(Long memberId, Map<Long, Long> favoriteCnt,
		Pageable pageable);

}
