package com.ssafy.jansorry.nag.dto;

import lombok.Builder;

@Builder
public record NagDto(
	Long nagId,
	Long categoryId,
	String content,
	Long price) {
}
