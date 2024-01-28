package com.ssafy.jansorry.feed.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.ssafy.jansorry.feed.dto.FeedInfoResponse;
import com.ssafy.jansorry.feed.repository.FeedRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedService {
	private final FeedRepository feedRepository;

	public Slice<FeedInfoResponse> readLiveFeeds(Long lastActionId, Pageable pageable) {
		return feedRepository.searchBySlice(lastActionId, pageable);
	}
}
