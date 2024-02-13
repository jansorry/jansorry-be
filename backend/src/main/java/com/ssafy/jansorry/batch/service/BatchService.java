package com.ssafy.jansorry.batch.service;

import static com.ssafy.jansorry.batch.domain.type.BatchKeyHeadType.*;
import static com.ssafy.jansorry.batch.util.BatchMapper.*;
import static com.ssafy.jansorry.exception.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.jansorry.action.repository.ActionRepository;
import com.ssafy.jansorry.batch.domain.BatchEntity;
import com.ssafy.jansorry.batch.dto.FinalDataDto;
import com.ssafy.jansorry.batch.repository.BatchCustomRepository;
import com.ssafy.jansorry.batch.repository.BatchRepository;
import com.ssafy.jansorry.batch.domain.type.BatchKeyNumberType;
import com.ssafy.jansorry.batch.util.BatchMapper;
import com.ssafy.jansorry.exception.BaseException;
import com.ssafy.jansorry.favorite.service.FavoriteService;
import com.ssafy.jansorry.nag.domain.type.GroupType;
import com.ssafy.jansorry.nag.repository.NagRepository;
import com.ssafy.jansorry.receipt.dto.ReceiptRankDto;
import com.ssafy.jansorry.receipt.service.ReceiptService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BatchService {
	private final RedisTemplate<String, Object> statisticRedisTemplate;
	private final BatchRepository batchRepository;
	private final BatchCustomRepository batchCustomRepository;
	private final FavoriteService favoriteService;
	private final ReceiptService receiptService;
	private final ActionRepository actionRepository;
	private final NagRepository nagRepository;

	public void updateTop5NagsByGender() {
		List<Long> nagsIndexes = null;
		String key = "";
		for (int i = 0; i < 2; i++) { // 성별
			for (int j = 1; j <= 7; j++) { // categoryId
				nagsIndexes = batchCustomRepository.searchTop5NagsByGender((long)j,
					BatchKeyNumberType.values()[i]);
				key = BatchMapper.getNagStatisticKey(BatchKeyNumberType.values()[i], GroupType.values()[j - 1]);
				statisticRedisTemplate.opsForValue().set(key, nagsIndexes);
			}
		}
	}

	public void updateTop5NagsByAgeRange() {
		List<Long> nagsIndexes = null;
		String key = "";
		for (int i = 2; i < 5; i++) { // 나이
			for (int j = 1; j <= 7; j++) { // categoryId
				nagsIndexes = batchCustomRepository.searchTop5NagsByAgeRange((long)j,
					BatchKeyNumberType.values()[i]);
				key = BatchMapper.getNagStatisticKey(BatchKeyNumberType.values()[i], GroupType.values()[j - 1]);
				statisticRedisTemplate.opsForValue().set(key, nagsIndexes);
			}
		}
	}

	public void updateTop5NagsByAll() {
		List<Long> nagsIndexes = null;
		String key = "";
		for (int i = 1; i <= 7; i++) { // categoryId
			nagsIndexes = batchCustomRepository.searchTop5NagsByAll((long)i);
			key = BatchMapper.getNagStatisticKey(BatchKeyNumberType.ALL, GroupType.values()[i - 1]);
			statisticRedisTemplate.opsForValue().set(key, nagsIndexes);
		}
	}

	public void updateTop5ActionByFavoriteCount() {
		List<Long> actionIds = favoriteService.getTopFavoriteIdList();// 좋아요 순 탑 30위 대응
		if (actionIds.size() > 5) {// 크기가 5 이상일 경우
			actionIds = actionIds.subList(0, 5); // 좋아요 순 탑 5위 actionId로 cut
		}
		statisticRedisTemplate.opsForValue().set(TOP_FAVORITE.getValue(), actionIds);
	}

	public void updateTop5ReceiptsByPrice() {
		List<ReceiptRankDto> receiptRankDtos = receiptService.getTopReceiptsList();// 가격 순 탑 5위 영수증 가져오기 (nickname, total price)

		List<String> receipts = receiptRankDtos.stream()
			.map(dto -> dto.getNickname() + ":" + dto.getTotalPrice())
			.collect(Collectors.toList());

		statisticRedisTemplate.opsForValue().set(TOP_RECEIPT.getValue(), receipts);
	}


	public void bindRedisToMysql() {
		Set<String> keys = statisticRedisTemplate.keys("*");
		System.out.println("keys!! = " + keys);
		for(String key : keys) {
			processKey(key);
		}
	}

	private void processKey(String key) {
		System.out.println("key = " + key);
		List<Long> resultSet = (List<Long>)statisticRedisTemplate.opsForValue().get(key);
		if (resultSet == null || resultSet.isEmpty()) {
			return;
		}

		if (key.equals(TOP_FAVORITE.getValue())) {
			System.out.println("ids = " + resultSet);
			processTopFavorite(resultSet);
		} else if (key.equals(TOP_RECEIPT.getValue())) {
			processTopReceipt();
		} else {
			System.out.println("ids = " + resultSet);
			processNags(key, resultSet);
		}
	}

	private void processTopFavorite(List<Long> actionIds) {
		List<String> actions = new ArrayList<>();
		for (Object obj : actionIds) {
			Long actionId;
			if (obj instanceof Integer) {
				actionId = ((Integer) obj).longValue(); // Integer를 Long으로 변환
			} else if (obj instanceof Long) {
				actionId = (Long) obj; // 이미 Long이면 그대로 사용
			} else {
				throw new IllegalArgumentException("Unsupported object type");
			}
			actions.add(actionRepository.findActionById(actionId)
				.orElseThrow(() -> new BaseException(ACTION_NOT_FOUND))
				.getContent());
		}
		batchRepository.save(BatchEntity.builder()
			.finalKey("대응:좋아요순")
			.finalValues(actions)
			.build());
	}

	private void processTopReceipt() {
		List<String> receiptRanks = (List<String>)statisticRedisTemplate.opsForValue().get(TOP_RECEIPT.getValue());
		batchRepository.save(BatchEntity.builder()
			.finalKey("영수증:가격순")
			.finalValues(receiptRanks)
			.build());
	}

	private void processNags(String key, List<Long> nagIds) {
		List<String> nags = new ArrayList<>();
		for (Object obj : nagIds) {
			Long nagId;
			if (obj instanceof Integer) {
				nagId = ((Integer) obj).longValue(); // Integer를 Long으로 변환
			} else if (obj instanceof Long) {
				nagId = (Long) obj; // 이미 Long이면 그대로 사용
			} else {
				throw new IllegalArgumentException("Unsupported object type");
			}
			nags.add(nagRepository.findNagById(nagId)
				.orElseThrow(() -> new BaseException(NAG_NOT_FOUND))
				.getContent());
		}
		batchRepository.save(BatchEntity.builder()
			.finalKey(decodeNagStatisticKey(key))
			.finalValues(nags)
			.build());
	}


	public List<FinalDataDto> readAllFinalData() {
		return batchRepository.findAll().stream()
			.map(batchEntity -> FinalDataDto.builder()
				.key(batchEntity.getFinalKey())
				.values(batchEntity.getFinalValues())
				.build())
			.collect(Collectors.toList());
	}
}
