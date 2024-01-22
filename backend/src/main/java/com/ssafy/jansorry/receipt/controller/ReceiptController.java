package com.ssafy.jansorry.receipt.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.jansorry.receipt.domain.Receipt;
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
	public ResponseEntity<ReceiptDto> saveReciept(	//ResponseEntity: 결과 데이터와 HTTP 상태 코드를 직접 제어할 수 있는 클래스
		@RequestHeader("Authorization") String accessToken,	//@RequestHeader : HTTP 요청 헤더 값을 컨트롤러 메서드의 매개 변수로 받을 수 있게 함
		@RequestBody ReceiptDto receiptDto	//@RequestBody: HTTP 요청 바디 값을 컨트롤러 메서드의 매개 변수로 받을 수 있음
	){
		receiptService.save(receiptDto);
		return ResponseEntity.ok().build();
	}

	// @GetMapping("/receipts/{seq}")

	// @DeleteMapping("/receipts/{seq}")

	// @GetMapping("/receipts/{seq}")
}
