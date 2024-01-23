package com.ssafy.jansorry.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
	// === GLOBAL BASE ERROR CODE ===
	BAD_REQUEST(400, "잘못된 요청 형식입니다."),
	UNAUTHORIZED(401, "인증 정보가 누락되었습니다. 헤더에 Authorization을 포함해주세요."),
	FORBIDDEN(403, "이 리소스에 대한 접근 권한이 없습니다."),

	// nag
	NAG_NOT_FOUND(404, "해당 잔소리를 찾을 수 없습니다."),


	// action
	ACTION_NOT_FOUND(404, "해당 대응을 찾을 수 없습니다."),
	ACTION_ALREADY_DELETED(404, "이미 삭제된 대응입니다"),


	// ===========================================================================
	// 4xx: Client Errors
	NOT_FOUND(404, "Not Found"),
	METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
	CONFLICT(409, "Conflict"),

	// 5xx: Server Errors
	INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
	NOT_IMPLEMENTED(501, "Not Implemented"),
	BAD_GATEWAY(502, "Bad Gateway"),
	SERVICE_UNAVAILABLE(503, "Service Unavailable"),
	GATEWAY_TIMEOUT(504, "Gateway Timeout"),
	HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported");

	private final Integer errorCode;
	private final String errorMsg;
}
