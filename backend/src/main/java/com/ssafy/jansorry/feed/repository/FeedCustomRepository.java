package com.ssafy.jansorry.feed.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.ssafy.jansorry.feed.dto.FeedInfoResponse;

public interface FeedCustomRepository {
	Slice<FeedInfoResponse> searchFeedsByTime(Long lastActionId, Pageable pageable);

	Slice<FeedInfoResponse> searchFeedsByAgeRange(Long lastActionId, int age, Pageable pageable);

	Slice<FeedInfoResponse> searchFeedsByFollow(Set<Long> memberIdSet, Long lastActionId, Pageable pageable);

	Slice<FeedInfoResponse> searchFeedsByFavorites(List<Long> keys, Map<Long, Long> favoriteCnt,
		Pageable pageable);

}
