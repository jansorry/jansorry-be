package com.ssafy.jansorry.favorite.service;

import static com.ssafy.jansorry.favorite.domain.type.RedisKeyType.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.jansorry.favorite.dto.FavoriteDto;
import com.ssafy.jansorry.favorite.dto.FavoriteInfoDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteService {
	private final RedisTemplate<String, Object> favoriteRedisTemplate;
	private final RedisTemplate<String, Object> favoriteZSetRedisTemplate;
	private final Long SIZE_LIMIT = 10L;

	// 해당 대응의 좋아요 개수를 반환하는 메서드
	public FavoriteInfoDto readFavoriteInfo(Long actionId, Long memberId) {
		String key = actionId.toString();
		FavoriteDto favoriteDto = getFavoriteDto(key);

		// redis에 존재하지 않는 데이터라면 개수는 0개
		Long favoriteCount = favoriteDto == null ? 0L : (long)favoriteDto.getMemberIdSet().size();

		if (favoriteCount == 0L) {
			return FavoriteInfoDto.builder()
				.favoriteCount(favoriteCount)// 0개
				.checked(Boolean.FALSE)// 0개일 경우 체크도 False
				.build();
		}

		// 좋아요 개수가 가 0개가 아닐 경우
		return FavoriteInfoDto.builder()
			.favoriteCount(favoriteCount)
			.checked(favoriteDto.getMemberIdSet().contains(memberId))// 체크 여부 판단하여 담아서 리턴
			.build();
	}

	// redis 좋아요 테이블을 업데이트하는 메서드
	public void updateFavorite(Long actionId, Long memberId, boolean isCreate) {
		String key = actionId.toString();
		FavoriteDto favoriteDto = getFavoriteDto(key);

		if (isCreate) {// 좋아요 추가
			if (favoriteDto == null) {
				favoriteDto = FavoriteDto.builder()
					.memberIdSet(new HashSet<>())
					.updatedAt(LocalDateTime.now())
					.build();
			}
			favoriteDto.addFavorite(memberId);
		} else {// 좋아요 취소
			if (favoriteDto == null) {
				return; // redis 에 키가 없으면 (즉, favoriteDto가 null이면) 바로 리턴
			}
			favoriteDto.removeFavorite(memberId);
		}

		updateFavoriteDto(key, favoriteDto);// 좋아요 업데이트
		updateFavoriteCountInZSet(actionId, getUpdateFavoriteCount(actionId));// 좋아요 개수를 스코어로 사용하여 ZSet에 저장
		updateFavoriteUpdatesZSet(actionId, favoriteDto.getUpdatedAt());// ZSet에 업데이트 정보 추가
	}

	// redis 로부터 해당 FavoriteDto 를 반환하는 메서드
	private FavoriteDto getFavoriteDto(String key) {
		return (FavoriteDto)favoriteRedisTemplate.opsForValue().get(key);
	}

	// redis 에 업데이트 하는 메서드
	private void updateFavoriteDto(String key, FavoriteDto updatedFavoriteDto) {
		favoriteRedisTemplate.opsForValue().set(key, updatedFavoriteDto);
	}

	// 업데이트 된 좋아요에 대한 개수를 반환하는 메서드
	private long getUpdateFavoriteCount(Long actionId) {
		String key = actionId.toString();
		FavoriteDto favoriteDto = getFavoriteDto(key);

		// 좋아요 개수 계산
		return (favoriteDto != null) ? favoriteDto.getMemberIdSet().size() : 0L;
	}

	// ZSet에 좋아요 업데이트 정보를 추가하는 메서드
	private void updateFavoriteUpdatesZSet(Long actionId, LocalDateTime updatedAt) {
		double score = updatedAt.toEpochSecond(ZoneOffset.UTC);
		favoriteZSetRedisTemplate.opsForZSet().add(FAVORITE_UPDATES_ZSET.getValue(), actionId.toString(), score);
	}

	// 좋아요 개수에 대한 ZSet을 업데이트하는 메서드
	private void updateFavoriteCountInZSet(Long actionId, Long favoriteCount) {
		double score = (double)favoriteCount;
		favoriteZSetRedisTemplate.opsForZSet().add(FAVORITE_RANKED_ZSET.getValue(), actionId.toString(), score);

		// ZSet의 크기 확인
		Long countZSetSize = favoriteZSetRedisTemplate.opsForZSet().size(FAVORITE_RANKED_ZSET.getValue());

		// 상위 10개를 초과하는 경우, 초과분 제거
		if (countZSetSize != null && countZSetSize > SIZE_LIMIT) {
			favoriteZSetRedisTemplate.opsForZSet()
				.removeRange(FAVORITE_RANKED_ZSET.getValue(), 0, countZSetSize - (SIZE_LIMIT + 1));
		}
	}

	// 상위 10개의 좋아요 순(내림차순)에 대한 actionId 를 반환
	public List<Long> getTopFavoriteIdList() {
		Set<Object> topFavorites = favoriteZSetRedisTemplate.opsForZSet()
			.reverseRange(FAVORITE_RANKED_ZSET.getValue(), 0, SIZE_LIMIT - 1);

		if (topFavorites == null) {
			return Collections.emptyList();
		}

		return topFavorites.stream()
			.filter(obj -> obj instanceof String) // String 타입인지 확인
			.map(obj -> Long.parseLong((String)obj)) // String으로 캐스팅 후 Long으로 파싱
			.collect(Collectors.toList());
	}
}
