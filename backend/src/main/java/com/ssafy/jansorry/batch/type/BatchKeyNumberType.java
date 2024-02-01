package com.ssafy.jansorry.batch.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BatchKeyNumberType {
	MALE(1L),
	FEMALE(2L),
	TEN(10L),
	TWENTY(20L),
	THIRTY(30L),
	ALL(100L);

	private final Long value;
}
