package com.ssafy.jansorry.feed.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.jansorry.feed.dto.FeedInfoResponse;
import com.ssafy.jansorry.feed.service.FeedService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "피드 컨트롤러", description = "실시간, 인기, 나이별 등의 피드 조회 기능이 포함되어 있음")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feed/actions")
public class FeedController {

	private final FeedService feedService;

	// 실시간 피드 조회
	@Operation(
		summary = "실시간 피드 조회",
		description = "최신 순으로 실시간 피드를 조회한다. (무한스크롤)")
	@GetMapping("/live")
	public ResponseEntity<Slice<FeedInfoResponse>> getLiveFeeds(
		@RequestParam(required = false) Long lastActionId,
		Pageable pageable
	) {
		return ResponseEntity.ok(feedService.readLiveFeeds(lastActionId, pageable));
	}

	// 10대, 20대, 30대 피드 조회
	@Operation(
		summary = "연령대 별 피드 조회",
		description = "해당 연령대의 피드를 최신 순으로 조회한다. (무한스크롤)")
	@GetMapping("/generation")
	public ResponseEntity<Slice<FeedInfoResponse>> getTeenageFeeds(
		@RequestParam(required = false) Long lastActionId,
		@RequestParam int age,
		Pageable pageable
	) {
		return ResponseEntity.ok(feedService.readGenerationFeeds(lastActionId, age, pageable));
	}
}
