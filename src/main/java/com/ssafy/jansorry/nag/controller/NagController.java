package com.ssafy.jansorry.nag.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.jansorry.nag.dto.CategoryDto;
import com.ssafy.jansorry.nag.dto.NagDto;
import com.ssafy.jansorry.nag.service.NagService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "잔소리 컨트롤러", description = "특정 잔소리 및 카테고리 별 조회 기능이 포함되어 있음")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/nags")
public class NagController {
	private final NagService nagService;

	@Operation(
		summary = "특정 잔소리 조회",
		description = "nagId를 통해 특정 잔소리의 상세 정보를 조회한다.")
	@GetMapping("/{nagId}")
	private ResponseEntity<NagDto> getNag(@PathVariable Long nagId) {
		return ResponseEntity.ok(nagService.readNag(nagId));
	}

	@Operation(
		summary = "전체 잔소리 조회",
		description = "카테고리를 별 전체 잔소리를 리스트 형식으로 조회한다.")
	@GetMapping
	private ResponseEntity<List<CategoryDto>> getAllNags() {
		return ResponseEntity.ok(nagService.readAllNags());
	}
}
