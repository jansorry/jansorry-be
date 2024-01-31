package com.ssafy.jansorry.action.dto;

import lombok.Builder;

// action 으로 부터 파생되었으므로 action-dto 패키지에 배치
@Builder
public record NagStatisticDto(
	Long nagId,
	String content,
	Long price,
	Long count
) {
}
