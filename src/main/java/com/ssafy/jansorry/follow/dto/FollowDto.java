package com.ssafy.jansorry.follow.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class FollowDto {// redis에 저장 후 조회 이후 변환이 존재하므로 record가아닌 class를 사용하도록 하였음
	private Set<Long> memberIdSet;
	private LocalDateTime updatedAt;

	// 팔로우 추가
	public void addFollow(Long memberId) {
		if (this.memberIdSet == null) {
			this.memberIdSet = new HashSet<>();
		}
		this.memberIdSet.add(memberId);
		this.updatedAt = LocalDateTime.now();
	}

	// 팔로우 삭제
	public void removeFollow(Long memberId) {
		if (this.memberIdSet != null) {
			this.memberIdSet.remove(memberId);
			this.updatedAt = LocalDateTime.now();
		}
	}
}
