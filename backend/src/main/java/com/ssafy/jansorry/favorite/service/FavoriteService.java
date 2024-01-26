package com.ssafy.jansorry.favorite.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
	public FavoriteInfoDto readFavoriteCount(Long actionId, Long memberId) {
		FavoriteDto favoriteDto = getFavoriteDto(actionId.toString());
		// redis에 존재하지 않는 데이터라면 0개를 반환 (개수 도출)
		Long favoriteCount = favoriteDto == null ? 0L : (long)favoriteDto.memberIdSet().size();

		if (favoriteCount == 0L) {
			return FavoriteInfoDto.builder()
				.favoriteCount(favoriteCount)
				.checked(Boolean.FALSE)
				.build();
		}

		return FavoriteInfoDto.builder()
			.favoriteCount(favoriteCount)
			.checked(favoriteDto.memberIdSet().contains(memberId))
			.build();
	}

	// redis 좋아요 테이블을 업데이트하는 메서드
	public void updateFavorite(Long actionId, Long memberId, boolean isCreate) {
		String key = actionId.toString();
		FavoriteDto favoriteDto = getFavoriteDto(key);

		// 좋아요 추가 시
		if (isCreate) {
			Set<Long> updatedMemberIdSet =
				favoriteDto != null ? new HashSet<>(favoriteDto.memberIdSet()) : new HashSet<>();
			updatedMemberIdSet.add(memberId);
			updateFavoriteDto(key, updatedMemberIdSet);
			return;
		}

		// 좋아요 취소 시, redis 에 키가 없으면 (즉, favoriteDto가 null이면) 바로 리턴
		if (favoriteDto == null) {
			return;
		}

		// 이 시점에서 favoriteDto는 null이 아니므로 안전하게 Set을 초기화
		Set<Long> updatedMemberIdSet = new HashSet<>(favoriteDto.memberIdSet());
		updatedMemberIdSet.remove(memberId);

		// 모든 좋아요가 삭제되면 key,value 완전 제거
		if (updatedMemberIdSet.isEmpty()) {
			favoriteRedisTemplate.delete(key);
			return;
		}
		// 모든 좋아요가 삭제된게 아니라면 좋아요 업데이트
		updateFavoriteDto(key, updatedMemberIdSet);
	}

	// redis 로부터 해당 FavoriteDto 를 반환하는 메서드
	private FavoriteDto getFavoriteDto(String key) {
		return (FavoriteDto)favoriteRedisTemplate.opsForValue().get(key);
	}

	// redis 에 업데이트 하는 메서드
	private void updateFavoriteDto(String key, Set<Long> memberIdSet) {
		FavoriteDto updatedFavoriteDto = FavoriteDto.builder()
			.memberIdSet(memberIdSet)
			.updatedAt(LocalDateTime.now())
			.build();

		favoriteRedisTemplate.opsForValue().set(key, updatedFavoriteDto);
	}

	// batch & scheduler: redis to mysql
	public void synchronizeFavorites() {
		// Redis 데이터를 MySQL에 동기화하는 로직 구현
	}
}
