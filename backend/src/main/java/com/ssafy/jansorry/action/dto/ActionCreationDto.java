package com.ssafy.jansorry.action.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ActionCreationDto(
	@Size(max = 100, message = "잘못된 요청 형식입니다. content는 100글자 이하입니다.")
	String content
) {
}
