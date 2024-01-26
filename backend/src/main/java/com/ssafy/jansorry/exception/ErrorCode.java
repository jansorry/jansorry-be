package com.ssafy.jansorry.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
	// === GLOBAL BASE ERROR CODE ===
	BAD_REQUEST(400, "잘못된 요청 형식입니다."), UNAUTHORIZED(401, "인증 정보가 누락되었습니다. 헤더에 Authorization을 포함해주세요."), FORBIDDEN(403,
		"이 리소스에 대한 접근 권한이 없습니다."),

	// member
	NOT_FOUND_MEMBER(404, "해당 사용자를 찾을 수 없습니다."), MEMBER_NICKNAME_DUPLICATED(409,
		"이미 사용중인 닉네임입니다."), INVALID_MEMBER_WITHDRAWN(403, "당신이 요청한 사용자는 탈퇴했으므로 접근할 수 없습니다."),

	// gender
	NOT_FOUND_GENDER(404, "해당 성별 타입을 찾을 수 없습니다."),

	// nag
	NAG_NOT_FOUND(404, "해당 잔소리를 찾을 수 없습니다."),

	// action
	ACTION_NOT_FOUND(404, "해당 대응을 찾을 수 없습니다."), ACTION_ALREADY_DELETED(404, "이미 삭제된 대응입니다"),

	// receipt
	RECEIPT_NOT_FOUND(404, "해당 영수증을 찾을 수 없습니다."), RECEIPT_OVERFLOW(400, "영수증 최대 발행 개수는 3개 입니다."),

	// token
	EXPIRED_REFRESH_TOKEN(404, "만료된 refresh token 입니다."),

	// ===========================================================================
	// 4xx: Client Errors
	NOT_FOUND(404, "Not Found"), METHOD_NOT_ALLOWED(405, "Method Not Allowed"), CONFLICT(409, "Conflict"),

	// 5xx: Server Errors
	INTERNAL_SERVER_ERROR(500, "Internal Server Error"), NOT_IMPLEMENTED(501, "Not Implemented"), BAD_GATEWAY(502,
		"Bad Gateway"), SERVICE_UNAVAILABLE(503, "Service Unavailable"), GATEWAY_TIMEOUT(504,
		"Gateway Timeout"), HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported"),

	// validation Errors
	METHOD_ARGUMENT_NOT_VALID(400, "유효성 검사 실패");

	private final Integer errorCode;
	private final String errorMsg;
}
