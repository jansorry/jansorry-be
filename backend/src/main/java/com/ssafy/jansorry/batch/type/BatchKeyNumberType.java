package com.ssafy.jansorry.batch.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BatchKeyNumberType {
	MALE(1L, "남성"),
	FEMALE(2L, "여성"),
	TEN(10L, "10대"),
	TWENTY(20L, "20대"),
	THIRTY(30L, "30대"),
	ALL(100L, "전체");

	private final Long value;
	private final String specificName;

	public static String getSpecificSortName(Long value) {
		for (BatchKeyNumberType type : BatchKeyNumberType.values()) {
			if (type.getValue().equals(value)) {
				return type.getSpecificName();
			}
		}
		return null;
	}
}
