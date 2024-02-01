package com.ssafy.jansorry.batch.service;

import static com.ssafy.jansorry.batch.type.BatchKeyHeadType.*;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.jansorry.batch.type.BatchKeyNumberType;
import com.ssafy.jansorry.nag.domain.type.GroupType;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BatchService {
	private final RedisTemplate<String, Object> statisticRedisTemplate;

	

	private String getNagStatisticKey(BatchKeyNumberType batchKeyNumberType, GroupType groupType) {
		return KEY.getValue() + batchKeyNumberType.getValue() + CATEGORY.getValue() + groupType.getValue();
	}
}
