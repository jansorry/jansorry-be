package com.ssafy.jansorry.receipt.service;

import static com.ssafy.jansorry.receipt.util.ReceiptMapper.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public void save(ReceiptDto receiptDto) {
		receiptRepository.save(toEntity(receiptDto));
	}

}
