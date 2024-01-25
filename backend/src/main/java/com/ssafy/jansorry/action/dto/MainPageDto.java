package com.ssafy.jansorry.action.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record MainPageDto(
	Long count,
	List<Long> categoryList
) {
}
