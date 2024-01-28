package com.ssafy.jansorry.favorite.service;

import static com.ssafy.jansorry.exception.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.jansorry.exception.BaseException;
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
		favoriteRedisTemplate.watch(key);// redis에서 데이터를 읽기 전에 WATCH 설정
		FavoriteDto favoriteDto = getFavoriteDto(key);
		favoriteRedisTemplate.multi();// WATCH 이후에 데이터가 수정되면 WATCH가 실패하므로 다시 시도

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
		final int MAX_RETRIES = 3; // 최대 재시도 횟수 설정
		int attempts = 0;          // 현재 시도 횟수
		boolean success = false;   // 성공 여부

		String key = actionId.toString();

		while (!success && attempts < MAX_RETRIES) {
			try {
				favoriteRedisTemplate.watch(key); // Redis에서 데이터를 읽기 전에 WATCH 설정

				FavoriteDto favoriteDto = getFavoriteDto(key);

				favoriteRedisTemplate.multi(); // WATCH 이후에 데이터가 수정되면 WATCH가 실패하므로 다시 시도

				if (isCreate) {
					// 좋아요 추가
					if (favoriteDto == null) {
						favoriteDto = FavoriteDto.builder()
							.memberIdSet(new HashSet<>())
							.updatedAt(LocalDateTime.now())
							.build();
					}
					favoriteDto.addFavorite(memberId);
				} else {
					// 좋아요 취소
					if (favoriteDto == null) {
						return; // Redis에 키가 없으면 (즉, favoriteDto가 null이면) 바로 리턴
					}
					favoriteDto.removeFavorite(memberId);
				}

				updateFavoriteDto(key, favoriteDto); // 좋아요 업데이트

				List<Object> results = favoriteRedisTemplate.exec();
				success = results != null; // 트랜잭션이 성공적으로 커밋되었는지 확인

				if (success) {
					return;
				}
				attempts++;
				Thread.sleep(100); // 재시도 전에 짧은 지연
			} catch (Exception e) {
				attempts++;
				if (attempts >= MAX_RETRIES) {
					throw new BaseException(ATTEMPTS_OVERFLOW);// 재시도 횟수 초과
				}
			} finally {
				favoriteRedisTemplate.unwatch(); // 트랜잭션이 성공하거나 모든 재시도가 실패한 경우 watch 해제
			}
		}

		if (!success) {
			throw new BaseException(ATTEMPTS_OVERFLOW);// 재시도 횟수 초과
		}
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
