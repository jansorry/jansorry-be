package com.ssafy.jansorry.receipt.dto;

import lombok.Builder;

@Builder
public record NagStatisticDto(
	Long nagId,
	String content,
	Long price,
	Long count
) {
}
