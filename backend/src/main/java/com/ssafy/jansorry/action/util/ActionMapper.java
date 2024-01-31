package com.ssafy.jansorry.action.util;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.ssafy.jansorry.action.domain.Action;
import com.ssafy.jansorry.action.dto.ActionCreationDto;
import com.ssafy.jansorry.action.dto.ActionDto;
import com.ssafy.jansorry.action.dto.NagStatisticDto;
import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.nag.domain.Nag;

public class ActionMapper {
	public static Action toEntity(Nag nag, Member member, ActionCreationDto actionCreationDto) {
		return Action.builder()
			.member(member)
			.nag(nag)
			.content(actionCreationDto.content())
			.build();
	}

	public static ActionDto toDto(Action action) {
		return ActionDto.builder()
			.categoryId(action.getNag().getCategory().getId())
			.actionId(action.getId())
			.content(action.getContent())
			.build();
	}

	public static List<ActionDto> toDtos(List<Action> actions) {
		if (CollectionUtils.isEmpty(actions)) {
			return Collections.emptyList();
		}
		return actions.stream()
			.map(ActionMapper::toDto)
			.collect(Collectors.toList());
	}

	public static NagStatisticDto toStatisticDto(Action action, Long count) {
		return NagStatisticDto.builder()
			.nagId(action.getNag().getId())
			.content(action.getNag().getContent())
			.price(action.getNag().getPrice())
			.count(count)
			.build();
	}
}
