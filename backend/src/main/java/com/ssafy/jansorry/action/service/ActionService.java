package com.ssafy.jansorry.action.service;

import static com.ssafy.jansorry.action.util.ActionMapper.*;
import static com.ssafy.jansorry.exception.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ssafy.jansorry.action.domain.Action;
import com.ssafy.jansorry.action.dto.ActionCreationDto;
import com.ssafy.jansorry.action.dto.ActionDto;
import com.ssafy.jansorry.action.dto.MainPageDto;
import com.ssafy.jansorry.action.repository.ActionRepository;
import com.ssafy.jansorry.exception.BaseException;
import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.nag.domain.Nag;
import com.ssafy.jansorry.nag.domain.type.GroupType;
import com.ssafy.jansorry.nag.repository.NagRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ActionService {
	private final NagRepository nagRepository;
	private final ActionRepository actionRepository;

	public Slice<ActionDto> readAllActions(Long lastActionId, Long memberId, Pageable pageable) {
		return actionRepository.searchActionsByMember(lastActionId, memberId, pageable);
	}

	public void createAction(Long nagId, Member member, ActionCreationDto actionCreationDto) {
		Nag nag = nagRepository.findNagById(nagId)
			.orElseThrow(() -> new BaseException(NAG_NOT_FOUND));
		if (actionRepository.countAllByMemberIdAndDeletedFalse(member.getId()) > 120) {
			throw new BaseException(ACTION_COUNT_FULL);
		}
		actionRepository.save(toEntity(nag, member, actionCreationDto));
	}

	public ActionDto readAction(Long actionId, Long memberId) {
		Action action = actionRepository.findActionByIdAndDeletedFalse(actionId)
			.orElseThrow(() -> new BaseException(ACTION_NOT_FOUND));
		if (!action.getMember().getId().equals(memberId)) {
			throw new BaseException(FORBIDDEN);
		}
		return toDto(action);
	}

	public void deleteAction(Long actionId, Long memberId) {
		Action action = actionRepository.findActionById(actionId)
			.orElseThrow(() -> new BaseException(ACTION_NOT_FOUND));
		if (action.getDeleted()) {
			throw new BaseException(ACTION_ALREADY_DELETED);
		}
		if (!action.getMember().getId().equals(memberId)) {
			throw new BaseException(FORBIDDEN);
		}
		action.setDeleted(true);// dirty checking
	}

	public MainPageDto readMainPage(Long memberId) {
		List<GroupType> groupTypes = actionRepository.findGroupTypesByMemberId(memberId);
		if (CollectionUtils.isEmpty(groupTypes)) {
			return MainPageDto.builder()
				.count(0L)
				.categoryList(new ArrayList<>())
				.build();
		}

		List<Long> groupIndexes = groupTypes.stream()
			.map(GroupType::getIdx)
			.collect(Collectors.toList());

		return MainPageDto.builder()
			.count((long)groupTypes.size())
			.categoryList(groupIndexes)
			.build();
	}
}
