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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "좋아요 컨트롤러", description = "좋아요 관련 개수, 여부, 추가 및 취소 기능이 포함되어 있음")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FavoriteController {
	private final FavoriteService favoriteService;

	@Operation(
		summary = "좋아요 정보 확인",
		description = "해당 대응의 좋아요 개수와 본인이 좋아요를 눌렀는지 여부를 조회한다.")
	@GetMapping("/actions/{actionId}/favorite")
	private ResponseEntity<FavoriteInfoDto> getFavoriteCount(
		@PathVariable Long actionId,
		@AuthenticationPrincipal Member member
	) {
		return ResponseEntity.ok(favoriteService.readFavoriteInfo(actionId, member.getId()));
	}

	@Operation(
		summary = "좋아요 추가",
		description = "해당 대응에 좋아요를 추가한다.")
	@PostMapping("/actions/{actionId}/favorite")
	private ResponseEntity<Void> addFavorite(
		@PathVariable Long actionId,
		@AuthenticationPrincipal Member member
	) {
		favoriteService.updateFavorite(actionId, member.getId(), true);
		return ResponseEntity.ok().build();
	}

	@Operation(
		summary = "좋아요 취소",
		description = "해당 대응에 추가했던 좋아요를 취소한다.")
	@DeleteMapping("/actions/{actionId}/favorite")
	private ResponseEntity<Void> removeFavorite(
		@PathVariable Long actionId,
		@AuthenticationPrincipal Member member
	) {
		favoriteService.updateFavorite(actionId, member.getId(), false);
		return ResponseEntity.ok().build();
	}
}
