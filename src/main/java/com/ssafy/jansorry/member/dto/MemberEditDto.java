package com.ssafy.jansorry.member.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record MemberEditDto(
	@Size(min = 2, max = 15, message = "잘못된 요청 형식입니다. nickname은 2글자 이상 15글자 이하입니다.")
	@Pattern(regexp = "^[a-zA-Z0-9가-힣\\\\-\\\\_]*$", message = "잘못된 요청 형식입니다. 특수문자(~!@#$%^&*())와 공백을 제외해야 합니다.")
	String nickname
) {

}
