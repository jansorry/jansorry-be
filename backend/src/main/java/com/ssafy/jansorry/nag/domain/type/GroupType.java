package com.ssafy.jansorry.nag.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupType {
	LEARNING("학업/진로", 1L),
	APPEARANCE("건강/외모", 2L),
	LOVE("연애/결혼", 3L),
	CAREER("취업/직장", 4L),
	FAMILY("가족/자녀", 5L),
	OTHER("기타", 6L),
	All("전체", 7L);

	private final String value;
	private final Long idx;
}
