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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/follows")
public class FollowController {
	private final FollowService followService;

	@GetMapping("/{toId}/check")
	public ResponseEntity<Boolean> getFollowCheck(
		@AuthenticationPrincipal Member member,
		@PathVariable Long toId) {
		return ResponseEntity.ok(followService.readFollowCheck(member.getId(), toId));
	}

	@GetMapping("/count")
	public ResponseEntity<FollowCountDto> getFollowCount(
		@AuthenticationPrincipal Member member) {
		return ResponseEntity.ok(followService.readFollowCount(member.getId()));
	}

	@PostMapping("/{toId}")
	public ResponseEntity<Void> addFollow(
		@AuthenticationPrincipal Member member,
		@PathVariable Long toId) {
		followService.updateFollow(member.getId(), toId, true);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{toId}")
	public ResponseEntity<Void> removeFollow(
		@AuthenticationPrincipal Member member,
		@PathVariable Long toId) {
		followService.updateFollow(member.getId(), toId, false);
		return ResponseEntity.ok().build();
	}
}
