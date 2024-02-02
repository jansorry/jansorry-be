package com.ssafy.jansorry.receipt.service;

import static com.ssafy.jansorry.batch.type.BatchKeyHeadType.*;
import static com.ssafy.jansorry.exception.ErrorCode.*;
import static com.ssafy.jansorry.receipt.util.ReceiptMapper.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.jansorry.exception.BaseException;
import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.receipt.domain.Receipt;
import com.ssafy.jansorry.receipt.dto.ReceiptDto;
import com.ssafy.jansorry.receipt.dto.ReceiptRankDto;
import com.ssafy.jansorry.receipt.repository.ReceiptRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReceiptService {
	private final ReceiptRepository receiptRepository;
	private final RedisTemplate<String, Object> statisticZSetRedisTemplate;
	private final ObjectMapper objectMapper;
	private final Long SIZE_LIMIT = 5L;

	//Dto는 Service <-> Controller
	//Entity는 Repository <-> Service
	public Long createReceipt(ReceiptDto receiptDto, Member member) {
		int receiptCount = receiptRepository.findAllByMemberAndDeletedFalseOrderById(member).size();

		if (receiptCount >= 3) {
			throw new BaseException(RECEIPT_OVERFLOW);
		}
		// 생성이 가능하다면
		updateTopReceiptPrice(member.getNickname(), receiptDto.totalPrice());// 가격 고점 갱신 (top 5)

		receiptRepository.save(toEntity(receiptDto, member));// 생성
		return receiptCount + 1L;// 기존 개수 + 1 = next seq
	}

	public ReceiptDto readReceipt(Member member, Long seq) {
		List<Receipt> receipts = receiptRepository.findAllByMemberAndDeletedFalseOrderById(member);

		// 빈 리스트 반환 or 없는 영수증 조회 시
		if (receipts.isEmpty() || receipts.size() < seq + 1) {
			throw new BaseException(RECEIPT_NOT_FOUND);
		}

		return toDto(receipts.get(seq.intValue()));
	}

	public void deleteReceipt(Member member, Long seq) {
		List<Receipt> receipts = receiptRepository.findAllByMemberAndDeletedFalseOrderById(member);

		//빈 리스트 반환 시
		if (receipts.isEmpty()) {
			throw new BaseException(RECEIPT_NOT_FOUND);
		}

		Receipt receipt = receipts.get(seq.intValue());
		receipt.setDeleted(true);
	}

	public Long readReceiptCount(Member member) {
		int receiptCount = receiptRepository.findAllByMemberAndDeletedFalseOrderById(member).size();
		return (long)receiptCount;
	}

	// 영수증을 저장하기 전, 생성한 영수증의 총액을 탑 5 에 대조 후 업데이트
	private void updateTopReceiptPrice(String nickname, Long totalPrice) {
		double score = (double)totalPrice;
		statisticZSetRedisTemplate.opsForZSet().add(
			TOP_RECEIPT.getValue(),// key = top_receipt
			ReceiptRankDto.builder()// value = [nickname, totalPrice]
				.nickname(nickname)
				.totalPrice(totalPrice)
				.build(),
			score);// score = totalPrice

		// ZSet의 크기 확인
		Long zSetSize = statisticZSetRedisTemplate.opsForZSet().size(TOP_RECEIPT.getValue());

		// 상위 5개를 초과하는 경우, 초과분 제거
		if (zSetSize != null && zSetSize > SIZE_LIMIT) {
			statisticZSetRedisTemplate.opsForZSet()
				.removeRange(TOP_RECEIPT.getValue(), 0, zSetSize - (SIZE_LIMIT + 1));
		}
	}

	// 상위 5개의 좋아요 순(내림차순)에 대한 닉네임 & 가격을 반환
	public List<ReceiptRankDto> getTopReceiptsList() {
		Set<Object> topReceipts = statisticZSetRedisTemplate.opsForZSet()
			.reverseRange(TOP_RECEIPT.getValue(), 0, SIZE_LIMIT - 1);

		if (topReceipts == null) {
			return Collections.emptyList();
		}

		return topReceipts.stream()
			.filter(obj -> obj instanceof ReceiptRankDto) // String 타입인지 확인
			.map(obj -> (ReceiptRankDto)obj) // ReceiptRankDto 로 캐스팅
			.collect(Collectors.toList());
	}
}
