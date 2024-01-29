package com.ssafy.jansorry.follow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.jansorry.follow.dto.FollowCountDto;
import com.ssafy.jansorry.follow.dto.MemberSearchResponse;
import com.ssafy.jansorry.follow.service.FollowService;
import com.ssafy.jansorry.member.domain.Member;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/follows")
public class FollowController {
	private final FollowService followService;

	@Operation(
		summary = "팔로우 여부 확인")
	@GetMapping("/{toId}/check")
	public ResponseEntity<Boolean> getFollowCheck(
		@AuthenticationPrincipal Member member,
		@PathVariable Long toId) {
		return ResponseEntity.ok(followService.readFollowCheck(member.getId(), toId));
	}

	@Operation(
		summary = "팔로우 추가")
	@PostMapping("/{toId}")
	public ResponseEntity<Void> addFollow(
		@AuthenticationPrincipal Member member,
		@PathVariable Long toId) {
		followService.updateFollow(member.getId(), toId, true);
		return ResponseEntity.ok().build();
	}

	@Operation(
		summary = "팔로우 취소")
	@DeleteMapping("/{toId}")
	public ResponseEntity<Void> removeFollow(
		@AuthenticationPrincipal Member member,
		@PathVariable Long toId) {
		followService.updateFollow(member.getId(), toId, false);
		return ResponseEntity.ok().build();
	}
}
