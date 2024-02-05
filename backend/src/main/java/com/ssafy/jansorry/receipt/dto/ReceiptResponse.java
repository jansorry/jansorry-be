package com.ssafy.jansorry.receipt.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ReceiptResponse(
	String title,
	String description,
	String message,
	String familyUrl,
	String friendUrl,
	Long totalPrice,
	LocalDateTime createdAt) {
}
