package com.ssafy.jansorry.follow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.jansorry.follow.service.FollowService;
import com.ssafy.jansorry.member.domain.Member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "팔로우 컨트롤러", description = "팔로우 관련 인원 수, 여부, 추가 및 취소 기능이 포함되어 있음")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/follows")
public class FollowController {
	private final FollowService followService;

	@Operation(
		summary = "팔로우 추가",
		description = "해당 유저에게 팔로우를 추가한다.")
	@PostMapping("/{toId}")
	public ResponseEntity<Void> addFollow(
		@AuthenticationPrincipal Member member,
		@PathVariable Long toId) {
		followService.updateFollow(member.getId(), toId, true);
		return ResponseEntity.ok().build();
	}

	@Operation(
		summary = "팔로우 취소",
		description = "해당 유저에게 추가했던 팔로우를 취소한다.")
	@DeleteMapping("/{toId}")
	public ResponseEntity<Void> removeFollow(
		@AuthenticationPrincipal Member member,
		@PathVariable Long toId) {
		followService.updateFollow(member.getId(), toId, false);
		return ResponseEntity.ok().build();
	}
}
