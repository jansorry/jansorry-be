package com.ssafy.jansorry.action.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.jansorry.action.dto.ActionCreationDto;
import com.ssafy.jansorry.action.service.ActionService;
import com.ssafy.jansorry.member.domain.Member;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ActionController {
	private final ActionService actionService;

	@PostMapping("/nags/{nagId}/actions")
	private ResponseEntity<Void> addAction(
		@PathVariable Long nagId,
		@AuthenticationPrincipal Member member,
		@RequestBody ActionCreationDto actionCreationDto
		) {
		actionService.createAction(nagId, member, actionCreationDto);
		return ResponseEntity.ok().build();
	}
}
