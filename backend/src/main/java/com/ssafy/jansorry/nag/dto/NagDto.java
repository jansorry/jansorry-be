package com.ssafy.jansorry.nag.dto;

import lombok.Builder;

@Builder
public record NagDto(
	Long nagId,
	String content,
	Long price) {
}
