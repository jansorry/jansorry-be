package com.ssafy.jansorry.favorite.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.jansorry.favorite.dto.FavoriteInfoDto;
import com.ssafy.jansorry.favorite.service.FavoriteService;
import com.ssafy.jansorry.member.domain.Member;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FavoriteController {
	private final FavoriteService favoriteService;

	@GetMapping("/actions/{actionId}")
	private ResponseEntity<FavoriteInfoDto> getFavoriteCount(
		@PathVariable Long actionId,
		@AuthenticationPrincipal Member member
	) {
		return ResponseEntity.ok(favoriteService.readFavoriteCount(actionId, member.getId()));
	}

	@PostMapping("/actions/{actionId}/like")
	private ResponseEntity<Void> addFavorite(
		@PathVariable Long actionId,
		@AuthenticationPrincipal Member member
	) {
		favoriteService.updateFavorite(actionId, member.getId(), true);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/actions/{actionId}/like")
	private ResponseEntity<Void> removeFavorite(
		@PathVariable Long actionId,
		@AuthenticationPrincipal Member member
	) {
		favoriteService.updateFavorite(actionId, member.getId(), false);
		return ResponseEntity.ok().build();
	}
}
