package com.ssafy.jansorry.batch.util;

import static com.ssafy.jansorry.batch.domain.type.BatchKeyHeadType.*;
import static com.ssafy.jansorry.batch.domain.type.BatchKeyNumberType.*;
import static com.ssafy.jansorry.nag.domain.type.GroupType.*;

import com.ssafy.jansorry.batch.domain.type.BatchKeyNumberType;
import com.ssafy.jansorry.nag.domain.type.GroupType;

public class BatchMapper {
	public static String getNagStatisticKey(BatchKeyNumberType batchKeyNumberType, GroupType groupType) {
		return KEY.getValue() + batchKeyNumberType.getValue() + CATEGORY.getValue() + groupType.getIdx();
	}

	public static String decodeNagStatisticKey(String key) {
		String[] component = key.split(":");
		String numberType = getSpecificSortName(Long.parseLong(component[1]));
		String groupType = getSpecificGroupName(Long.parseLong(component[3]));
		return numberType + ":" + groupType;
	}
}
