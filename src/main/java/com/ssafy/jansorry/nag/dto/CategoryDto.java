package com.ssafy.jansorry.nag.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record CategoryDto(
	Long categoryId,
	String title,
	List<NagDto> nags
) {

}
