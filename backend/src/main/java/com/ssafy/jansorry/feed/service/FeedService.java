package com.ssafy.jansorry.feed.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.ssafy.jansorry.favorite.service.FavoriteService;
import com.ssafy.jansorry.feed.dto.FeedInfoResponse;
import com.ssafy.jansorry.feed.repository.FeedRepository;
import com.ssafy.jansorry.follow.domain.type.FollowRedisKeyType;
import com.ssafy.jansorry.follow.dto.FollowDto;
import com.ssafy.jansorry.follow.service.FollowService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedService {
	private final FeedRepository feedRepository;
	private final FollowService followService;
	private final FavoriteService favoriteService;

	public Slice<FeedInfoResponse> readLiveFeeds(Long lastActionId, Pageable pageable) {
		return feedRepository.searchFeedsByTime(lastActionId, pageable);
	}

	public Slice<FeedInfoResponse> readGenerationFeeds(Long lastActionId, int age, Pageable pageable) {
		return feedRepository.searchFeedsByAgeRange(lastActionId, age, pageable);
	}

	public Slice<FeedInfoResponse> readFollowingFeeds(Long fromId, Long lastActionId, Pageable pageable) {
		FollowDto followingDto = followService.getFollowDto(FollowRedisKeyType.FOLLOWING.getValue() + fromId);
		return followingDto == null ? null :
			feedRepository.searchFeedsByFollow(followingDto.getMemberIdSet(), lastActionId, pageable);
	}

	public Slice<FeedInfoResponse> readTrendingFeeds(Pageable pageable) {
		Map<Long, Long> map = new HashMap<>();

		List<Long> list = favoriteService.getTopFavoriteIdList();
		for (Long id : list) {
			Long size = favoriteService.getUpdateFavoriteCount(id);
			map.put(id, size);
		}

		return feedRepository.searchFeedsByFavorites(list, map, pageable);
	}
}
