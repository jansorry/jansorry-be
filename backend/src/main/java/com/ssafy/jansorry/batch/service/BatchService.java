package com.ssafy.jansorry.batch.service;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.jansorry.batch.repository.BatchCustomRepository;
import com.ssafy.jansorry.batch.type.BatchKeyNumberType;
import com.ssafy.jansorry.batch.util.BatchMapper;
import com.ssafy.jansorry.favorite.service.FavoriteService;
import com.ssafy.jansorry.nag.domain.type.GroupType;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BatchService {
	private final RedisTemplate<String, Object> statisticRedisTemplate;
	private final BatchCustomRepository batchCustomRepository;
	private final FavoriteService favoriteService;

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
		for (int i = 2; i < 5; i += 10) { // 성별
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
			key = BatchMapper.getNagStatisticKey(BatchKeyNumberType.values()[6], GroupType.values()[i - 1]);
			statisticRedisTemplate.opsForValue().set(key, nagsIndexes);
		}
	}
}
