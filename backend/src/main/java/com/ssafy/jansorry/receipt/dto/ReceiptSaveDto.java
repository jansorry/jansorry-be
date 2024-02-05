package com.ssafy.jansorry.receipt.dto;

import lombok.Builder;

@Builder
public record ReceiptSaveDto(
	String familyUrl,
	String friendUrl,
	Long totalPrice
) {
}
