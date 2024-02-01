package com.ssafy.jansorry.receipt.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class ReceiptRankDto {
	private String nickname;
	private Long totalPrice;
}
