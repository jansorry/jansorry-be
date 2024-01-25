package com.ssafy.jansorry.nag.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupType {
	LEARNING(1L),
	APPEARANCE(2L),
	LOVE(3L),
	CAREER(4L),
	FAMILY(5L),
	OTHER(6L);

	private final Long value;
}
