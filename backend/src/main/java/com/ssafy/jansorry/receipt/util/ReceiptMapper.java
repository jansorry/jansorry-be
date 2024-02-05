package com.ssafy.jansorry.receipt.util;

import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.receipt.domain.Receipt;
import com.ssafy.jansorry.receipt.dto.OpenGraphMessage;
import com.ssafy.jansorry.receipt.dto.ReceiptResponse;
import com.ssafy.jansorry.receipt.dto.ReceiptSaveDto;

public class ReceiptMapper {
	public static ReceiptResponse toDto(Receipt receipt){
		return ReceiptResponse.builder()
			.title(receipt.getTitle())
			.description(receipt.getDescription())
			.message(receipt.getMessage())
			.familyUrl(receipt.getFamilyUrl())
			.friendUrl(receipt.getFriendUrl())
			.totalPrice(receipt.getTotalPrice())
			.createdAt(receipt.getCreatedAt())
			.build();
	}

	public static Receipt toEntity(ReceiptSaveDto receiptSaveDto, Member member, OpenGraphMessage openGraphMessage){
		return Receipt.builder()
			.member(member)
			.title(openGraphMessage.title())// 생성
			.description(openGraphMessage.description())// 생성
			.message(openGraphMessage.message())// 생성
			.familyUrl(receiptSaveDto.familyUrl())
			.friendUrl(receiptSaveDto.friendUrl())
			.totalPrice(receiptSaveDto.totalPrice())
			.deleted(false)
			.build();
	}
}
