package com.ssafy.jansorry.receipt.util;

import com.ssafy.jansorry.receipt.domain.Receipt;
import com.ssafy.jansorry.receipt.dto.ReceiptDto;

public class ReceiptMapper {
	public static ReceiptDto toDto(Receipt receipt){
		return ReceiptDto.builder()
			.title(receipt.getTitle())
			.description(receipt.getDescription())
			.message(receipt.getMessage())
			.familyUrl(receipt.getFamilyUrl())
			.friendUrl(receipt.getFriendUrl())
			.build();
	}

	public static Receipt toEntity(ReceiptDto receiptDto){
		return Receipt.builder()
			.title(receiptDto.title())
			.description(receiptDto.description())
			.message(receiptDto.message())
			.familyUrl(receiptDto.familyUrl())
			.friendUrl(receiptDto.friendUrl())
			.build();
	}
}
