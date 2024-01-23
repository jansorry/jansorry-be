package com.ssafy.jansorry.action.service;

import static com.ssafy.jansorry.action.util.ActionMapper.*;
import static com.ssafy.jansorry.exception.ErrorCode.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.jansorry.action.domain.Action;
import com.ssafy.jansorry.action.dto.ActionCreationDto;
import com.ssafy.jansorry.action.dto.ActionDto;
import com.ssafy.jansorry.action.repository.ActionRepository;
import com.ssafy.jansorry.action.util.ActionMapper;
import com.ssafy.jansorry.exception.BaseException;
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

	public List<ActionDto> readAllActions(Long memberId) {
		return actionRepository.findAllByMemberIdAndDeletedFalse(memberId)
			.stream()
			.map(ActionMapper::toDto)
			.collect(Collectors.toList());
	}

	public void createAction(Long nagId, Member member, ActionCreationDto actionCreationDto) {
		Nag nag = nagRepository.findNagById(nagId)
			.orElseThrow(() -> new BaseException(NAG_NOT_FOUND));
		actionRepository.save(toEntity(nag, member, actionCreationDto));
	}

	public ActionDto readAction(Long actionId) {
		Action action = actionRepository.findActionByIdAndDeletedFalse(actionId)
			.orElseThrow(() -> new BaseException(ACTION_NOT_FOUND));
		return toDto(action);
	}

	public void deleteAction(Long actionId) {
		Action action = actionRepository.findActionById(actionId)
			.orElseThrow(() -> new BaseException(ACTION_NOT_FOUND));
		if (action.getDeleted()) {
			throw new BaseException(ACTION_ALREADY_DELETED);
		}
		action.setDeleted(true);// dirty checking
	}
}
