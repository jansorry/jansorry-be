package com.ssafy.jansorry.batch.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BatchKeyHeadType {
	KEY("key:"),
	CATEGORY(":category:"),
	TOP_FAVORITE("top_favorite"),
	TOP_RECEIPT("top_receipt");

	private final String value;
}
