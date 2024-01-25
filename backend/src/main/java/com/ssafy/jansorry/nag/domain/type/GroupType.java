package com.ssafy.jansorry.nag.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupType {
	LEARNING(0L),
	APPEARANCE(1L),
	LOVE(2L),
	CAREER(3L),
	FAMILY(4L),
	OTHER(5L);

	private final Long value;
}
