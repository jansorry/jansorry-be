package com.ssafy.jansorry.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.member.domain.type.OauthServerType;
import com.ssafy.jansorry.member.dto.LoginDto;
import com.ssafy.jansorry.member.dto.MemberEditDto;
import com.ssafy.jansorry.member.dto.MemberResponse;
import com.ssafy.jansorry.member.dto.SignUpRequest;
import com.ssafy.jansorry.member.dto.SignUpResponse;
import com.ssafy.jansorry.member.dto.TokenReissueResponse;
import com.ssafy.jansorry.member.dto.TokenResponse;
import com.ssafy.jansorry.member.service.MemberService;
import com.ssafy.jansorry.member.service.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {
	private final MemberService memberService;
	private final TokenService tokenService;

	@Operation(
		summary = "토큰 재발급")
	@PostMapping("/reissue")
	public ResponseEntity<TokenReissueResponse> reissueAccessToken(
		HttpServletRequest request, HttpServletResponse response) {
		TokenResponse tokenResponse = tokenService.reissueAccessToken(request, response);

		return ResponseEntity.ok(
			TokenReissueResponse.builder()
				.accessToken(tokenResponse.accessToken())
				.build()
		);
	}

	@Operation(
		summary = "회원가입")
	@PostMapping("/signup")
	public ResponseEntity<SignUpResponse> addMember(
		HttpServletResponse response,
		@RequestBody SignUpRequest request
	) {
		LoginDto login = memberService.createMember(request);

		Cookie cookie = new Cookie("refreshToken", login.refreshToken());
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		// 기존의 쿠키 설정을 문자열로 변환
		String cookieValue = "refreshToken=" + login.refreshToken() +
			"; HttpOnly; Secure; Path=/; SameSite=None";

		// 응답 헤더에 쿠키 추가
		response.addHeader("Set-Cookie", cookieValue);

		return ResponseEntity.ok(
			SignUpResponse.builder()
				.nickname(login.nickname())
				.accessToken(login.accessToken())
				.build()
		);
	}

	@Operation(
		summary = "닉네임 변경")
	@PutMapping("/rename")
	public ResponseEntity<MemberEditDto> editNickname(
		@AuthenticationPrincipal Member member,
		@Valid @RequestBody MemberEditDto request
	) {
		return ResponseEntity.ok(memberService.updateMember(member, request));
	}

	@Operation(
		summary = "회원탈퇴")
	@DeleteMapping("/withdraw/{oauthServerType}")
	public ResponseEntity<Void> removeMember(
		@AuthenticationPrincipal Member member,
		HttpServletResponse response,
		@PathVariable OauthServerType oauthServerType
	) {
		memberService.deleteMember(response, oauthServerType, member);
		return ResponseEntity.ok().build();
	}

	@Operation(
		summary = "회원정보 확인 (마이페이지)",
		description = "닉네임, 이미지, 대응 개수, 팔로워 팔로잉 개수 반환")
	@GetMapping
	public ResponseEntity<MemberResponse> getMemberBySelf(
		@AuthenticationPrincipal Member member
	) {
		return ResponseEntity.ok(memberService.readMemberSelf(member));
	}

	@Operation(
		summary = "회원 닉네임 검색")
	@GetMapping("/search")
	public ResponseEntity<MemberResponse> getMemberByNickName(@RequestParam(name = "nickName") String nickName) {
		return ResponseEntity.ok(memberService.readMemberByNickName(nickName));
	}
}
