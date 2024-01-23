package com.ssafy.jansorry.receipt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.receipt.dto.ReceiptDto;
import com.ssafy.jansorry.receipt.service.ReceiptService;

import lombok.RequiredArgsConstructor;

@RestController	//@RestController: Restful Web Service에서 사용되는 어노테이션
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReceiptController {

	private final ReceiptService receiptService;

	//영수증 저장하는 API
	@PostMapping("/receipts")
	public ResponseEntity<ReceiptDto> saveReceipt(	//ResponseEntity: 결과 데이터와 HTTP 상태 코드를 직접 제어할 수 있는 클래스
		@RequestBody ReceiptDto receiptDto	//@RequestBody: HTTP 요청 바디 값을 컨트롤러 메서드의 매개 변수로 받을 수 있음
	){
		receiptService.save(receiptDto);
		return ResponseEntity.ok().build();
	}

	//영수증 조회하는 API - 3개중 {seq}번째의 영수증을 반환한다.
	@GetMapping("/receipts/{seq}")
	public ResponseEntity<ReceiptDto> getReceipt(
		@AuthenticationPrincipal Member member,	//로그인 세션 정보
		@PathVariable("seq") Long seq
	){
		ReceiptDto receiptDto = receiptService.readReceiptsBySeq(member.getId(),seq);
		return ResponseEntity.ok(receiptDto);
	}

}
