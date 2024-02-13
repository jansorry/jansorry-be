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

	@Operation(
		summary = "테스트용 최종 통계 데이터셋 바인딩 (호출 금지)",
		description = "최종 통계 데이터 모으기위해 수동으로 트리거를 준다.")
	@GetMapping("/test/gathering")
	private ResponseEntity<Void> setFinalData() {
		// 잔소리
		// batchService.updateTop5NagsByGender();// 성별 탑 5위 잔소리 = 2 x 7 = 14
		// batchService.updateTop5NagsByAgeRange();// 연령 별 탑 5위 잔소리 = 3 * 7 = 21
		// batchService.updateTop5NagsByAll();// 전체 중 탑 5위 잔소리 = 7
		// // 좋아요
		// batchService.updateTop5ActionByFavoriteCount();// 좋아요 순 탑 5위 actionId = 1
		// // 영수증
		// batchService.updateTop5ReceiptsByPrice();// 가격 순 탑 5위 영수증 = 1

		// 최종 데이터 mysql에 반영
		batchService.bindRedisToMysql();

		return ResponseEntity.ok().build();
	}
}
