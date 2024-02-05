package com.ssafy.jansorry.receipt.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record ReceiptStatisticDto(
	Long totalCount,
	Long totalPrice,
	List<NagStatisticDto> nagStatisticDtos
) {
}
