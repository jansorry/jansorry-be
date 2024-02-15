package com.ssafy.jansorry.batch.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record FinalDataDto(
	String key,
	List<String> values
) {
}
