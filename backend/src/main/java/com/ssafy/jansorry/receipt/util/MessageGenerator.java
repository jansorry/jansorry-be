package com.ssafy.jansorry.receipt.util;

import com.ssafy.jansorry.receipt.dto.OpenGraphMessage;

public class MessageGenerator {
	static final String DESCRIPTION = "님이 청구하신 잔소리 영수증을 확인해보세요.";

	public static OpenGraphMessage generateMessage(String name) {
		String title = "";
		String message = "";

		return OpenGraphMessage.builder()
			.title(title)
			.message(name + DESCRIPTION)
			.description(message)
			.build();
	}
}
