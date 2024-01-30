package com.ssafy.jansorry.feed.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.ssafy.jansorry.feed.dto.FeedInfoResponse;
import com.ssafy.jansorry.feed.repository.FeedRepository;
import com.ssafy.jansorry.follow.dto.FollowDto;
import com.ssafy.jansorry.follow.service.FollowService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedService {
	private final FeedRepository feedRepository;
	private final FollowService followService;

	public Slice<FeedInfoResponse> readLiveFeeds(Long lastActionId, Pageable pageable) {
		return feedRepository.searchFeedsByTime(lastActionId, pageable);
	}

	public Slice<FeedInfoResponse> readGenerationFeeds(Long lastActionId, int age, Pageable pageable) {
		return feedRepository.searchFeedsByAgeRange(lastActionId, age, pageable);
	}

	public Slice<FeedInfoResponse> readFollowingFeeds(Long fromId, Long lastActionId, Pageable pageable) {
		FollowDto followingDto = followService.getFollowDto("following:" + fromId);
		return followingDto == null ? null :
			feedRepository.searchFeedsByFollow(followingDto.getMemberIdSet(), lastActionId, pageable);
	}
}
