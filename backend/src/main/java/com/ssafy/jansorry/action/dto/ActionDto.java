package com.ssafy.jansorry.action.dto;

import lombok.Builder;

@Builder
public record ActionDto(
	Long categoryId,
	Long actionId,
	String content
) {
}
