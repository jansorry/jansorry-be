package com.ssafy.jansorry.favorite.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FavoriteDto {// redis에 저장 후 조회 이후 변환이 존재하므로 record가아닌 class를 사용하도록 하였음
	private Set<Long> memberIdSet;
	private LocalDateTime updatedAt;

	// 좋아요 추가
	public void addFavorite(Long memberId) {
		if (this.memberIdSet == null) {
			this.memberIdSet = new HashSet<>();
		}
		this.memberIdSet.add(memberId);
		this.updatedAt = LocalDateTime.now();
	}

	// 좋아요 삭제
	public void removeFavorite(Long memberId) {
		if (this.memberIdSet != null) {
			this.memberIdSet.remove(memberId);
			this.updatedAt = LocalDateTime.now();
		}
	}
}
