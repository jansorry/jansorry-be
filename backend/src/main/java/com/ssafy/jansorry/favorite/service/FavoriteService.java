package com.ssafy.jansorry.favorite.service;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.jansorry.favorite.dto.FavoriteDto;
import com.ssafy.jansorry.favorite.dto.FavoriteInfoDto;
import com.ssafy.jansorry.favorite.repository.FavoriteRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteService {
	private final RedisTemplate<String, Object> favoriteRedisTemplate;
	private final FavoriteRepository favoriteRepository;

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

		if (isCreate) {
			// 좋아요 추가
			if (favoriteDto == null) {
				favoriteDto = FavoriteDto.builder()
					.memberIdSet(new HashSet<>())
					.updatedAt(LocalDateTime.now())
					.build();
			}
			favoriteDto.addFavorite(memberId);
			updateFavoriteDto(key, favoriteDto); // 좋아요 업데이트
			return;
		}
		// 좋아요 취소
		if (favoriteDto == null) {
			return; // Redis에 키가 없으면 (즉, favoriteDto가 null이면) 바로 리턴
		}
		favoriteDto.removeFavorite(memberId);
		updateFavoriteDto(key, favoriteDto); // 좋아요 업데이트
	}

	// redis 로부터 해당 FavoriteDto 를 반환하는 메서드
	private FavoriteDto getFavoriteDto(String key) {
		return (FavoriteDto)favoriteRedisTemplate.opsForValue().get(key);
	}

	// redis 에 업데이트 하는 메서드
	private void updateFavoriteDto(String key, FavoriteDto updatedFavoriteDto) {
		favoriteRedisTemplate.opsForValue().set(key, updatedFavoriteDto);
	}

	// batch & scheduler: redis to mysql
	public void synchronizeFavorites() {
		// Redis 데이터를 MySQL에 동기화하는 로직 구현

		// todo: 모든 좋아요가 삭제되있다면 batch에 반영 후, redis에서 해당 key,value 완전 제거하기
	}
}
