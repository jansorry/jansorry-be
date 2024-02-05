package com.ssafy.jansorry.receipt.dto;

import lombok.Builder;

@Builder
public record OpenGraphMessage(
	String title,
	String description,
	String message
) {
}
