package com.ssafy.jansorry.receipt.service;

import static com.ssafy.jansorry.exception.ErrorCode.*;
import static com.ssafy.jansorry.receipt.util.ReceiptMapper.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.jansorry.exception.BaseException;
import com.ssafy.jansorry.receipt.domain.Receipt;
import com.ssafy.jansorry.receipt.dto.ReceiptDto;
import com.ssafy.jansorry.receipt.repository.ReceiptRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReceiptService {

	private final ReceiptRepository receiptRepository;

	//Dto는 Service <-> Controller
	//Entity는 Repository <-> Service
	public void createReceipt(ReceiptDto receiptDto) {
		receiptRepository.save(toEntity(receiptDto));
	}

	public ReceiptDto readReceipt(Long memberId, Long seq) {
		List<Receipt> receipts = receiptRepository.findAllByMemberIdAndDeletedFalseOrderById(memberId);

		//빈 리스트 반환 시
		if (receipts.isEmpty()) {
			throw new BaseException(RECEIPT_NOT_FOUND);
		}

		return toDto(receipts.get(seq.intValue()));
	}

	public void deleteReceipt(Long memberId, Long seq) {
		List<Receipt> receipts = receiptRepository.findAllByMemberIdAndDeletedFalseOrderById(memberId);

		//빈 리스트 반환 시
		if (receipts.isEmpty()) {
			throw new BaseException(RECEIPT_NOT_FOUND);
		}

		Receipt receipt = receipts.get(seq.intValue());
		receipt.setDeleted(true);
	}

	public Long readReceiptCount(Long memberId) {
		List<Receipt> receipts = receiptRepository.findAllByMemberIdAndDeletedFalseOrderById(memberId);

		return (long)receipts.size();
	}
}
