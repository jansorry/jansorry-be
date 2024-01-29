package com.ssafy.jansorry.action.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.jansorry.action.dto.ActionCreationDto;
import com.ssafy.jansorry.action.dto.ActionDto;
import com.ssafy.jansorry.action.dto.MainPageDto;
import com.ssafy.jansorry.action.service.ActionService;
import com.ssafy.jansorry.member.domain.Member;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ActionController {
	private final ActionService actionService;

	@GetMapping("/actions")
	private ResponseEntity<Slice<ActionDto>> getAllActions(
		@AuthenticationPrincipal Member member,
		@RequestParam(required = false) Long lastActionId,
		Pageable pageable
	) {
		return ResponseEntity.ok(actionService.readAllActions(lastActionId, member.getId(), pageable));
	}

	@PostMapping("/nags/{nagId}/actions")
	private ResponseEntity<Void> addAction(
		@PathVariable Long nagId,
		@AuthenticationPrincipal Member member,
		@RequestBody ActionCreationDto actionCreationDto
	) {
		actionService.createAction(nagId, member, actionCreationDto);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/actions/{actionId}")
	private ResponseEntity<ActionDto> getAction(
		@PathVariable Long actionId,
		@AuthenticationPrincipal Member member
	) {
		return ResponseEntity.ok(actionService.readAction(actionId));
	}

	@DeleteMapping("/actions/{actionId}")
	private ResponseEntity<Void> removeAction(
		@PathVariable Long actionId,
		@AuthenticationPrincipal Member member
	) {
		actionService.deleteAction(actionId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/main")
	private ResponseEntity<MainPageDto> getMainPage(
		@AuthenticationPrincipal Member member
	) {
		return ResponseEntity.ok(actionService.readMainPage(member.getId()));
	}
}
