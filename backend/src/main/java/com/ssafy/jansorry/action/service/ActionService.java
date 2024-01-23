package com.ssafy.jansorry.action.service;

import static com.ssafy.jansorry.action.util.ActionMapper.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.jansorry.action.dto.ActionCreationDto;
import com.ssafy.jansorry.action.repository.ActionRepository;
import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.nag.domain.Nag;
import com.ssafy.jansorry.nag.repository.NagRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ActionService {
	private final NagRepository nagRepository;
	private final ActionRepository actionRepository;

	public void createAction(Long nagId, Member member, ActionCreationDto actionCreationDto) {
		Nag nag = nagRepository.findNagById(nagId).orElseThrow(RuntimeException::new);// exception
		actionRepository.save(toEntity(nag, member, actionCreationDto));
	}
}
