package com.ssafy.jansorry.member.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderType {
	MALE(0L),
	FEMALE(1L),
	UNDISCLOSED(2L),
	UNKNOWN(3L);

	private final Long value;
}
