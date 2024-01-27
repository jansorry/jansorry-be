package com.ssafy.jansorry.member.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.member.domain.type.OauthServerType;
import com.ssafy.jansorry.member.dto.KakaoLogoutResponse;
import com.ssafy.jansorry.member.dto.LoginDto;
import com.ssafy.jansorry.member.dto.LoginResponse;
import com.ssafy.jansorry.member.service.OauthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

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
		return ResponseEntity.ok().build();
	}

	@GetMapping("/login/{oauthServerType}")
	public ResponseEntity<LoginResponse> login(
		HttpServletResponse response,
		@PathVariable OauthServerType oauthServerType,
		@RequestParam("code") String code
	) {

		LoginDto login = oauthService.login(oauthServerType, code);

		Cookie cookie = new Cookie("refreshToken", login.refreshToken());
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setSecure(true); // HTTPS를 사용하는 경우
		// 기존의 쿠키 설정을 문자열로 변환
		String cookieValue = "refreshToken=" + login.refreshToken() +
			"; HttpOnly; Secure; Path=/; SameSite=None";

		// 응답 헤더에 쿠키 추가
		response.addHeader("Set-Cookie", cookieValue);

		return ResponseEntity.ok(LoginResponse
			.builder()
			.oauthId(login.oauthId())
			.nickname(login.nickname())
			.accessToken(login.accessToken())
			.build());
	}

	@PostMapping("/logout/{oauthServerType}")
	public ResponseEntity<KakaoLogoutResponse> logout(
		@AuthenticationPrincipal Member member,
		HttpServletResponse response,
		@PathVariable OauthServerType oauthServerType
	) {
		return ResponseEntity.ok(
			oauthService.logout(response, oauthServerType, member.getOauthId().getOauthServerId()));
	}
}
