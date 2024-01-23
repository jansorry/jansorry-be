package com.ssafy.jansorry.member.controller;

import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.member.domain.type.OauthServerType;
import com.ssafy.jansorry.member.dto.KakaoLogoutResponse;
import com.ssafy.jansorry.member.dto.LoginResponse;
import com.ssafy.jansorry.member.service.OauthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/oauth")
public class OauthController {
	private final OauthService oauthService;


	@SneakyThrows
	@GetMapping("/{oauthServerType}")
	public ResponseEntity<Void> redirectAuthCodeRequestUrl(
		@PathVariable OauthServerType oauthServerType,
		HttpServletResponse response
	) {
		String redirectUrl = oauthService.getAuthCodeRequestUrl(oauthServerType);
		response.sendRedirect(redirectUrl);
		return ResponseEntity.ok().build();
	}

	@SneakyThrows
	@GetMapping("/redirected/kakao")
	public ResponseEntity<Void> redirectAuthCodeRequestUrl(
		@RequestParam String code
	) {
		System.out.println(code);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/login/{oauthServerType}")
	public ResponseEntity<LoginResponse> login(
		@PathVariable OauthServerType oauthServerType,
		@RequestParam("code") String code
	) {
		return ResponseEntity.ok(oauthService.login(oauthServerType, code));
	}

	@PostMapping("/logout/{oauthServerType}")
	public ResponseEntity<KakaoLogoutResponse> logout(
		@AuthenticationPrincipal Member member,
		@PathVariable OauthServerType oauthServerType
	) {
		return ResponseEntity.ok(oauthService.logout(oauthServerType, member.getOauthId().getOauthServerId()));
	}
}
