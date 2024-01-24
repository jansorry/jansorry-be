package com.ssafy.jansorry.exception;

import static com.ssafy.jansorry.exception.ErrorCode.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
		return ResponseEntity.status(ex.getErrorCode().getErrorCode())
			.body(new ErrorResponse(ex.getErrorCode().getErrorCode(), ex.getErrorCode().getErrorMsg()));
	}

	// 유효성 검사 base 예외 처리에 맞게 custom
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(METHOD_ARGUMENT_NOT_VALID.getErrorCode(), e.getFieldError().getDefaultMessage()));
	}
}
