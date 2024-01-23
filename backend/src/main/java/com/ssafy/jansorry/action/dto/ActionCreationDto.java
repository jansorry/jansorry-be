package com.ssafy.jansorry.action.dto;

import lombok.Builder;

@Builder
public record ActionCreationDto(
	String content
) {
}
