package com.ssafy.jansorry.feed.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.ssafy.jansorry.feed.dto.FeedInfoResponse;

public interface FeedCustomRepository {
	Slice<FeedInfoResponse> searchFeedsByTime(Long lastActionId, Pageable pageable);

	Slice<FeedInfoResponse> searchFeedsByAgeRange(Long lastActionId, int age, Pageable pageable);
}
