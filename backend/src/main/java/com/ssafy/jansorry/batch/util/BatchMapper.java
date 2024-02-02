package com.ssafy.jansorry.batch.util;

import static com.ssafy.jansorry.batch.type.BatchKeyHeadType.*;

import com.ssafy.jansorry.batch.type.BatchKeyNumberType;
import com.ssafy.jansorry.nag.domain.type.GroupType;

public class BatchMapper {
	public static String getNagStatisticKey(BatchKeyNumberType batchKeyNumberType, GroupType groupType) {
		return KEY.getValue() + batchKeyNumberType.getValue() + CATEGORY.getValue() + groupType.getIdx();
	}
}
