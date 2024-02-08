package com.ssafy.jansorry.receipt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.receipt.dto.ReceiptResponse;
import com.ssafy.jansorry.receipt.dto.ReceiptSaveDto;
import com.ssafy.jansorry.receipt.dto.ReceiptStatisticDto;
import com.ssafy.jansorry.receipt.service.ReceiptService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "영수증 컨트롤러", description = "영수증 관련 저장, 조회, 삭제 기능이 포함되어 있음")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/receipts")
public class ReceiptController {
	private final ReceiptService receiptService;

	@Operation(
		summary = "전체 대응에 대한 잔소리 가격 및 통계 확인",
		description = "본인이 작성한 잔소리 카드의 모든 대응에 대한 가격 및 통계를 확인한다.")
	@GetMapping("/statistic")
	private ResponseEntity<ReceiptStatisticDto> getAllActionStatistic(
		@AuthenticationPrincipal Member member
	) {
		return ResponseEntity.ok(receiptService.readAllReceiptStatistic(member.getId()));
	}

	@Operation(
		summary = "영수증 저장",
		description = "영수증이 2개 이하라면, 해당 영수증을 저장한다.")
	@PostMapping
	public ResponseEntity<Long> addReceipt(
		@AuthenticationPrincipal Member member,
		@RequestBody ReceiptSaveDto receiptSaveDto
	) {
		return ResponseEntity.ok(receiptService.createReceipt(receiptSaveDto, member));
	}

	@Operation(
		summary = "영수증 개수 확인",
		description = "현재 보유중인 영수증의 총 개수를 조회한다.")
	@GetMapping
	public ResponseEntity<Long> getReceiptCount(
		@AuthenticationPrincipal Member member
	) {
		return ResponseEntity.ok(receiptService.readReceiptCount(member));
	}

	@Operation(
		summary = "영수증 확인",
		description = "해당 번호의 영수증을 상세 조회한다.")
	@GetMapping("/{seq}")
	public ResponseEntity<ReceiptResponse> getReceipt(
		@AuthenticationPrincipal Member member,
		@PathVariable("seq") Long seq
	) {
		return ResponseEntity.ok(receiptService.readReceipt(member, seq - 1));
	}

	@Operation(
		summary = "영수증 삭제",
		description = "해당 번호의 영수증을 삭제한다.")
	@DeleteMapping("/{seq}")
	public ResponseEntity<Void> removeReceipt(
		@AuthenticationPrincipal Member member,
		@PathVariable("seq") Long seq
	) {
		receiptService.deleteReceipt(member, seq - 1);
		return ResponseEntity.ok().build();
	}
}
