package com.ssafy.jansorry.member.service;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

	@Value("${spring.jwt.token.secret-key")
	private String secretKey;

	@Value("${spring.jwt.token.refresh-secret-key")
	private String refreshSecretKey;

	private final MemberRepository memberRepository;
	private final RedisTemplate<Long, Object> redisTemplate;
	private final long TOKEN_PERIOD = 60 * 60 * 1000L; // 1시간
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
		Long memberId = member.getId();
		Claims claims = Jwts.claims().setSubject(String.valueOf(memberId));
		Date now = new Date();

		String refreshToken =  Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + REFRESH_PERIOD))
			.signWith(SignatureAlgorithm.HS256, refreshSecretKey)
			.compact();

		// redis refreshToken 저장
		HashOperations<Long, Object, Object> hashOperations = redisTemplate.opsForHash();
		hashOperations.put(memberId, REDIS_REFRESH_TOKEN_KEY, refreshToken);
		redisTemplate.expire(memberId, REFRESH_PERIOD, MILLISECONDS);
		return refreshToken;
	}

	public Authentication getAuthentication(String token) {
		Long memberId = getMemberId(token);

		Member member = memberRepository.findById(memberId).orElseThrow(
			() -> new RuntimeException("not found"));

		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority(member.getNickname()));

		return new UsernamePasswordAuthenticationToken(member, "", grantedAuthorities);
	}

	public String resolveToken(HttpServletRequest request) {
		String accessToken = request.getHeader("Authorization");

		if (accessToken == null) {
			throw new RuntimeException("AUTHORIZATION_KEY_DOES_NOT_EXIST");
		}

		return accessToken;
	}

	public boolean validateToken(String token) {
		try {
			Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
			return claims.getExpiration().after(new Date());

		} catch (ExpiredJwtException e) {
			return false;
		}
	}

	public String reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
		try {
			String refreshToken = getRefreshToken(request);
			Long memberId = getMemberIdFromRefreshToken(refreshToken);
			String redisRefreshToken = Objects.requireNonNull(
				redisTemplate.opsForHash().get(memberId, REDIS_REFRESH_TOKEN_KEY)).toString();

			Member member = memberRepository.findById(memberId).orElseThrow(
				() -> new RuntimeException("NOT_FOUND_MEMBER"));

			if (!redisRefreshToken.equals(refreshToken)) {
				resetHeader(response);
				throw new RuntimeException("EXPIRED_REFRESH_TOKEN");
			}

			return createToken(member);

		} catch (NullPointerException e) {
			resetHeader(response);
			throw new RuntimeException("EXPIRED_REFRESH_TOKEN");
		}
	}

	public Long getMemberId(String token) {
		String memberId = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
		return Long.valueOf(memberId);
	}

	public Long getMemberIdFromRefreshToken(String refreshToken) {
		String memberId =
			Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(refreshToken).getBody().getSubject();
		return Long.valueOf(memberId);
	}

	private String getRefreshToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie :  cookies) {
			if (cookie.getName().equals("refreshToken")) {
				return cookie.getValue();
			}
		}
		return null;
	}

	// 만료된 access, refresh token 정보 삭제
	public void resetHeader(HttpServletResponse response) {
		response.setHeader("Authorization", null);
		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
}
