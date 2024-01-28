package com.ssafy.jansorry.feed.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.jansorry.feed.dto.FeedInfoResponse;
import com.ssafy.jansorry.feed.service.FeedService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feed/actions")
public class FeedController {

	private final FeedService feedService;

	// 실시간 피드 조회
	@GetMapping("/live")
	public ResponseEntity<Slice<FeedInfoResponse>> getLiveFeeds(
		@RequestParam(required = false) Long lastActionId,
		@PageableDefault(size = 3) Pageable pageable
	) {
		return ResponseEntity.ok(feedService.readLiveFeeds(lastActionId, pageable));
	}
}
