package com.ssafy.jansorry.batch.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.jansorry.batch.dto.FinalDataDto;
import com.ssafy.jansorry.batch.service.BatchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "배치 컨트롤러", description = "최종 데이터를 반환하는 역할")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/batch")
public class BatchController {
	private final BatchService batchService;

	@Operation(
		summary = "최종 데이터 반환",
		description = "최종 데이터 시각화 및 테스트 생성을 위한 모든 통계 데이터를 반환한다.")
	@GetMapping
	private ResponseEntity<List<FinalDataDto>> getFinalData() {
		return ResponseEntity.ok(batchService.readAllFinalData());
	}
}
