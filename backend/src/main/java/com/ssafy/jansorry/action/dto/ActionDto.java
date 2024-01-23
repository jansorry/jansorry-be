package com.ssafy.jansorry.action.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ActionDto(
	Long nagId,
	Long categoryId,
	String content,
	LocalDateTime createdAt,
	Long price
) {
}
