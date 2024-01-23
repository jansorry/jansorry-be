package com.ssafy.jansorry.nag.service;

import static com.ssafy.jansorry.exception.ErrorCode.*;
import static com.ssafy.jansorry.nag.util.NagMapper.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.jansorry.exception.BaseException;
import com.ssafy.jansorry.nag.domain.Nag;
import com.ssafy.jansorry.nag.dto.NagDto;
import com.ssafy.jansorry.nag.repository.NagRepository;
import com.ssafy.jansorry.nag.util.NagMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NagService {
	private final NagRepository nagRepository;

	public NagDto readNag(Long nagId) {
		Nag nag = nagRepository.findNagById(nagId).orElseThrow(() -> new BaseException(NAG_NOT_FOUND));
		return toDto(nag);
	}

	public List<NagDto> readAllNags() {// dto 전환 추가하기 (가격 뺀 버전으로)
		return nagRepository.findAllByDeletedFalse()
			.stream()
			.map(NagMapper::toDto)
			.collect(Collectors.toList());
	}
}
