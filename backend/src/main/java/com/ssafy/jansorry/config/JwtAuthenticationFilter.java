package com.ssafy.jansorry.config;

import com.ssafy.jansorry.member.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final TokenService tokenService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		try {
			String token = tokenService.resolveToken((HttpServletRequest) request);

			if (tokenService.validateToken(token)) { // access_token 유효할 때
				Authentication authentication = tokenService.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else { // access_token 유효하지 않을 때 재발급
				String accessToken =
					tokenService.reissueAccessToken((HttpServletRequest) request,
						(HttpServletResponse) response);
				((HttpServletResponse) response).setHeader("Authorization", accessToken);
				Authentication authentication = tokenService.getAuthentication(accessToken);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

//		} catch (CustomException e) {
//			request.setAttribute("errorCode", e.getErrorCode());
//			request.setAttribute("httpStatus", e.getHttpStatus());
//		} catch (SignatureException | MalformedJwtException e) {
//			request.setAttribute("errorCode", INVALID_TOKEN);
//			request.setAttribute("httpStatus", UNAUTHORIZED);
//		} catch (IllegalArgumentException e) {
//			request.setAttribute("errorCode", EMPTY_TOKEN);
//			request.setAttribute("httpStatus", BAD_REQUEST);
//		}
		} catch (Exception e) {
			e.printStackTrace();
		}

		chain.doFilter(request, response);
	}
}
