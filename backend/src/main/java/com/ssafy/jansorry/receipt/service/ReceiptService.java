package com.ssafy.jansorry.receipt.service;

import static com.ssafy.jansorry.exception.ErrorCode.*;
import static com.ssafy.jansorry.favorite.domain.type.FavoriteRedisKeyType.*;
import static com.ssafy.jansorry.receipt.util.ReceiptMapper.*;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.jansorry.exception.BaseException;
import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.receipt.domain.Receipt;
import com.ssafy.jansorry.receipt.dto.ReceiptDto;
import com.ssafy.jansorry.receipt.repository.ReceiptRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReceiptService {
	private final RedisTemplate<String, Object> statisticRedisTemplate;

	private final ReceiptRepository receiptRepository;

	//Dto는 Service <-> Controller
	//Entity는 Repository <-> Service
	public void createReceipt(ReceiptDto receiptDto, Member member) {
		int receiptCount =  receiptRepository.findAllByMemberIdAndDeletedFalseOrderById(member.getId()).size();

		if(receiptCount >= 3){
			throw new BaseException(RECEIPT_OVERFLOW);
		}

		Long sumPrice = receiptDto.sumPrice();


		String nickName = member.getNickname();


		receiptRepository.save(toEntity(receiptDto));
	}

	// private void updateTopReceiptPrice(Long actionId, Long favoriteCount) {
	// 	double score = (double)favoriteCount;
	// 	statisticRedisTemplate.opsForZSet().add(FAVORITE_RANKED_ZSET.getValue(), actionId.toString(), score);
	//
	// 	// ZSet의 크기 확인
	// 	Long countZSetSize = favoriteZSetRedisTemplate.opsForZSet().size(FAVORITE_RANKED_ZSET.getValue());
	//
	// 	// 상위 10개를 초과하는 경우, 초과분 제거
	// 	if (countZSetSize != null && countZSetSize > SIZE_LIMIT) {
	// 		favoriteZSetRedisTemplate.opsForZSet()
	// 			.removeRange(FAVORITE_RANKED_ZSET.getValue(), 0, countZSetSize - (SIZE_LIMIT + 1));
	// 	}
	// }

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
		int receiptCount =  receiptRepository.findAllByMemberIdAndDeletedFalseOrderById(memberId).size();
		return (long)receiptCount;
	}
}
