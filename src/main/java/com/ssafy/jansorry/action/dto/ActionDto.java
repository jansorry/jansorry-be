package com.ssafy.jansorry.action.dto;

import lombok.Builder;

@Builder
public record ActionDto(
	Long categoryId,
	String nagContent,
	Long actionId,
	String actionContent
) {
}
