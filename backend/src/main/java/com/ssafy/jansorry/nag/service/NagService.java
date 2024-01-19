package com.ssafy.jansorry.nag.service;

import static com.ssafy.jansorry.nag.util.NagMapper.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.jansorry.nag.domain.Nag;
import com.ssafy.jansorry.nag.dto.NagDto;
import com.ssafy.jansorry.nag.repository.NagRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NagService {
	private final NagRepository nagRepository;

	public NagDto findNagDetail(Long nagId) {
		Nag nag = nagRepository.findNagById(nagId).orElseThrow(RuntimeException::new);
		return toDto(nag);
	}
}
