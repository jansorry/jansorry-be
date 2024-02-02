package com.ssafy.jansorry.receipt.controller;

import java.util.List;

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
import com.ssafy.jansorry.receipt.dto.ReceiptDto;
import com.ssafy.jansorry.receipt.dto.ReceiptRankDto;
import com.ssafy.jansorry.receipt.service.ReceiptService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "영수증 컨트롤러", description = "영수증 관련 저장, 조회, 삭제 기능이 포함되어 있음")
@RestController    //@RestController: Restful Web Service에서 사용되는 어노테이션
@RequiredArgsConstructor
@RequestMapping("/api/v1/receipts")
public class ReceiptController {

	private final ReceiptService receiptService;

	//영수증 저장하는 API
	@Operation(
		summary = "영수증 저장",
		description = "영수증이 2개 이하라면, 해당 영수증을 저장한다.")
	@PostMapping
	public ResponseEntity<Long> addReceipt(    //ResponseEntity: 결과 데이터와 HTTP 상태 코드를 직접 제어할 수 있는 클래스
		@AuthenticationPrincipal Member member,
		@RequestBody ReceiptDto receiptDto    //@RequestBody: HTTP 요청 바디 값을 컨트롤러 메서드의 매개 변수로 받을 수 있음
	) {

		return ResponseEntity.ok(receiptService.createReceipt(receiptDto, member));
	}

	// 영수증 개수 확인 API - 영수증 개수를 0~3까지 반환
	@Operation(
		summary = "영수증 개수 확인",
		description = "현재 보유중인 영수증의 총 개수를 조회한다.")
	@GetMapping
	public ResponseEntity<Long> getReceiptCount(
		@AuthenticationPrincipal Member member    //로그인 세션 정보
	) {
		return ResponseEntity.ok(receiptService.readReceiptCount(member));
	}

	//영수증 조회하는 API - 3개중 {seq}번째의 영수증을 반환한다.
	@Operation(
		summary = "영수증 확인",
		description = "해당 번호의 영수증을 상세 조회한다.")
	@GetMapping("/{seq}")
	public ResponseEntity<ReceiptDto> getReceipt(
		@AuthenticationPrincipal Member member,    //로그인 세션 정보
		@PathVariable("seq") Long seq
	) {
		ReceiptDto receiptDto = receiptService.readReceipt(member, seq - 1);
		return ResponseEntity.ok(receiptDto);
	}

	// 영수증 삭제하는 API - 3개 중 {seq}번째의 영수증을 반환
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

	@GetMapping("/rank")
	public ResponseEntity<List<ReceiptRankDto>> getRanks() {
		return ResponseEntity.ok(receiptService.getTopReceiptsList());
	}
}
