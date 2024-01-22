package com.ssafy.jansorry.config;

import com.ssafy.jansorry.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
		throws IOException {

		ErrorCode errorCode = (ErrorCode) request.getAttribute("errorCode");
		HttpStatus httpStatus = (HttpStatus) request.getAttribute("httpStatus");
		setResponse(response, errorCode, httpStatus);
	}

	private void setResponse(HttpServletResponse response, ErrorCode errorCode, HttpStatus httpStatus)
		throws IOException {

		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(httpStatus.value());

		JSONObject responseJson = new JSONObject();
		responseJson.put("errorCode", errorCode.toString());
		responseJson.put("errorMessage", errorCode.getErrorMsg());
		response.getWriter().print(responseJson);
	}
}
