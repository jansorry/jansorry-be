package com.ssafy.jansorry.receipt.util;

import com.ssafy.jansorry.receipt.dto.OpenGraphMessage;

public class MessageGenerator {
	static final String DESCRIPTION = "님이 청구하신 잔소리 영수증을 확인해보세요.";
	static final String URL_MESSAGE = "https://jansorry.com";

	public static OpenGraphMessage generateMessage(String name) {
		String title = "";

		return OpenGraphMessage.builder()
			.title(title)
			.description(name + DESCRIPTION)
			.message(URL_MESSAGE)
			.build();
	}
}
