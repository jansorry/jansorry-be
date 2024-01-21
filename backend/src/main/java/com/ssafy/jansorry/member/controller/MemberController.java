package com.ssafy.jansorry.member.controller;

import com.ssafy.jansorry.member.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemberController {
	private final TokenService tokenService;

	@GetMapping("/reissue")
	public ResponseEntity<Void> reissueAccessToken(
		HttpServletRequest request, HttpServletResponse response) {
		String accessToken = tokenService.reissueAccessToken(request, response);
		response.setHeader("Authorization", accessToken);

		return ResponseEntity.ok().build();
	}
}
