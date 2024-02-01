package com.ssafy.jansorry.batch.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BatchKeyNumberType {
	MALE(0L),
	FEMALE(1L),
	TEN(10L),
	TWENTY(20L),
	THIRTY(30L),
	ALL(100L);

	private final Long value;
}
