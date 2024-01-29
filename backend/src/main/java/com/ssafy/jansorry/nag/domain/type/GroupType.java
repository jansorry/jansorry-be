package com.ssafy.jansorry.nag.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupType {
	LEARNING("학업/진로"),
	APPEARANCE("건강/외모"),
	LOVE("연애/결혼"),
	CAREER("취업/직장"),
	FAMILY("가족/자녀"),
	OTHER("기타");

	private final String value;
}
