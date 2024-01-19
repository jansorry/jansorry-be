package com.ssafy.jansorry.nag.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.jansorry.nag.dto.NagDto;
import com.ssafy.jansorry.nag.service.NagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/nags")
public class NagController {
	private final NagService nagService;

	@ResponseBody
	@GetMapping("/{nagId}")
	private ResponseEntity<NagDto> getNagDetail(@PathVariable Long nagId) {
		return ResponseEntity.ok(nagService.findNagDetail(nagId));
	}
}
