package com.ssafy.jansorry.receipt.service;

import static com.ssafy.jansorry.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.jansorry.exception.BaseException;
import com.ssafy.jansorry.nag.domain.Nag;
import com.ssafy.jansorry.nag.repository.NagRepository;
import com.ssafy.jansorry.receipt.dto.OpenGraphMessage;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {
	private final NagRepository nagRepository;

	private final String DESCRIPTION = "님이 청구하신 잔소리 영수증을 확인해보세요.";
	private final String URL_MESSAGE = "https://jansorry.com";

	public OpenGraphMessage generateMessage(String name, Long nagId) {
		return OpenGraphMessage.builder()
			.title(readRandomMessage(nagId))
			.description(name + DESCRIPTION)
			.message(URL_MESSAGE)
			.build();
	}

	private String readRandomMessage(Long nagId) {
		Nag nag = nagRepository.findNagById(nagId).orElseThrow(() -> new BaseException(NAG_NOT_FOUND));
		return nag.getMessages().get((int)(Math.random() * nag.getMessages().size())).getContent();
	}
}
