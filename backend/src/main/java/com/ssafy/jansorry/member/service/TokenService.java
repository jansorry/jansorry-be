package com.ssafy.jansorry.member.service;

import static com.ssafy.jansorry.exception.ErrorCode.*;
import static java.util.concurrent.TimeUnit.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.ssafy.jansorry.exception.BaseException;
import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.member.domain.OauthId;
import com.ssafy.jansorry.member.domain.type.OauthServerType;
import com.ssafy.jansorry.member.dto.TokenResponse;
import com.ssafy.jansorry.member.repository.MemberRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenService {

	@Value("${spring.jwt.token.secret-key}")
	private String secretKey;

	@Value("${spring.jwt.token.refresh-secret-key}")
	private String refreshSecretKey;

	private final MemberRepository memberRepository;
	private final RedisTemplate<String, Object> tokenRedisTemplate;
	private final long TOKEN_PERIOD = 30 * 60 * 1000L; // 30분
	private final long REFRESH_PERIOD = 14 * 24 * 60 * 60 * 1000L; // 14일
	private final String REDIS_REFRESH_TOKEN_KEY = "refreshToken";

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
		refreshSecretKey = Base64.getEncoder().encodeToString(refreshSecretKey.getBytes());
	}

	public String createToken(Member member) {
		Claims claims = Jwts.claims().setSubject(String.valueOf(member.getId()));
		Date now = new Date();

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + TOKEN_PERIOD))
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();
	}

	public String createRefreshToken(Member member) {
		Claims claims = Jwts.claims().setSubject(String.valueOf(member.getOauthId().getOauthServerId()));
		Date now = new Date();

		String refreshToken = Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + REFRESH_PERIOD))
			.signWith(SignatureAlgorithm.HS256, refreshSecretKey)
			.compact();

		// redis refreshToken 저장
		HashOperations<String, Object, Object> hashOperations = tokenRedisTemplate.opsForHash();
		hashOperations.put(member.getOauthId().getOauthServerId(), REDIS_REFRESH_TOKEN_KEY, refreshToken);
		tokenRedisTemplate.expire(member.getOauthId().getOauthServerId(), REFRESH_PERIOD, MILLISECONDS);
		return refreshToken;
	}

	public Authentication readAuthentication(String token) {
		Long memberId = readMemberId(token);

		Member member = memberRepository.findById(memberId).orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority(member.getNickname()));

		return new UsernamePasswordAuthenticationToken(member, "", grantedAuthorities);
	}

	public String resolveToken(HttpServletRequest request) {
		String accessToken = request.getHeader("Authorization");
		System.out.println("request uri : " + request.getRequestURI());

		if (accessToken == null || accessToken.trim().length() == 0) {
			throw new BaseException(UNAUTHORIZED);
		}

		return accessToken;
	}

	public boolean validateToken(String token) {
		System.out.println("isValidate? : " + token);
		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
		System.out.println(claims);
		System.out.println(claims.getExpiration().after(new Date()));
		try {
			claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
			return claims.getExpiration().after(new Date());

		} catch (ExpiredJwtException e) {
			System.out.println("expired!!");
			return false;
		}
	}

	public TokenResponse reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = readRefreshToken(request);
		String oauthServerId = readMemberIdFromRefreshToken(refreshToken);
		String redisRefreshToken = Objects.requireNonNull(
			tokenRedisTemplate.opsForHash().get(oauthServerId, REDIS_REFRESH_TOKEN_KEY)).toString();
		System.out.println("refreshToken = " + refreshToken);
		System.out.println("oauthServerId = " + oauthServerId);
		System.out.println("redisRefreshToken = " + redisRefreshToken);

		try {
			refreshToken = readRefreshToken(request);
			oauthServerId = readMemberIdFromRefreshToken(refreshToken);
			redisRefreshToken = Objects.requireNonNull(
				tokenRedisTemplate.opsForHash().get(oauthServerId, REDIS_REFRESH_TOKEN_KEY)).toString();
			System.out.println("refreshToken = " + refreshToken);
			System.out.println("oauthServerId = " + oauthServerId);
			System.out.println("redisRefreshToken = " + redisRefreshToken);
			System.out.println(refreshToken.equals(redisRefreshToken));

			Member member = memberRepository.findByOauthId(new OauthId(oauthServerId, OauthServerType.KAKAO))
				.orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

			if (!redisRefreshToken.equals(refreshToken)) {// 리프레시 토큰 만료 시 헤더에서 삭제
				System.out.println("expired.. different!!");
				deleteHeader(response);
				throw new BaseException(EXPIRED_REFRESH_TOKEN);
			}

			return TokenResponse.builder().accessToken(createToken(member)).refreshToken(refreshToken).build();

		} catch (NullPointerException e) {
			System.out.println("npe... from where??");
			deleteHeader(response);
			throw new BaseException(EXPIRED_REFRESH_TOKEN);
		}
	}

	public Long readMemberId(String token) {
		String memberId = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
		return Long.valueOf(memberId);
	}

	public String readMemberIdFromRefreshToken(String refreshToken) {
		String oauthId = Jwts.parser()
			.setSigningKey(refreshSecretKey)
			.parseClaimsJws(refreshToken)
			.getBody()
			.getSubject();
		return oauthId;
	}

	private String readRefreshToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("refreshToken")) {
				return cookie.getValue().trim();
			}
		}
		return null;
	}

	// access, refresh token 정보 삭제
	public void deleteHeader(HttpServletResponse response) {
		response.setHeader("Authorization", null);
		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
}
