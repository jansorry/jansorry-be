package com.ssafy.jansorry.receipt.dto;

import lombok.Builder;

@Builder
public record ReceiptDto (
	String title,
	String description,
	String message,
	String familyUrl,
	String friendUrl,
	Long sumPrice) {
}
