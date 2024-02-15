package com.ssafy.jansorry;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "서버 상태 컨트롤러", description = "핑 테스트로 서버 상태를 확인하는 기능이 포함되어 있음")
@RestController
@RequestMapping("/api/v1")
public class HealthCheckController {
	@Operation(summary = "서버 상태 확인(핑 테스트)")
	@GetMapping("/ping")
	public String ping() {
		return "pong";
	}
}
