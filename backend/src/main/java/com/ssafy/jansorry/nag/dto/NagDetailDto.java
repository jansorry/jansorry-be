package com.ssafy.jansorry.nag.dto;

import lombok.Builder;

@Builder
public record NagDetailDto(
	Long nagId,
	Long categoryId,
	String content,
	Long price) {
}
