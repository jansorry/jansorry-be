package com.ssafy.jansorry.action.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.ssafy.jansorry.action.domain.Action;
import com.ssafy.jansorry.action.dto.ActionCreationDto;
import com.ssafy.jansorry.action.dto.ActionDto;
import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.nag.domain.Nag;
import com.ssafy.jansorry.receipt.dto.NagStatisticDto;

public class ActionMapper {
	static ArrayList<String> testOauthIds = new ArrayList<>(
		Arrays.asList("3298433859", "3287827161", "3291230371",
			"3299026039", "3301262680", "3324554317"));

	public static Action toEntity(Nag nag, Member member, ActionCreationDto actionCreationDto) {
		String content = "";
		if (testOauthIds.contains(member.getOauthId().getOauthServerId())) {
			content = actionCreationDto.content();
		}

		return Action.builder()
			.member(member)
			.nag(nag)
			.content(content)
			.build();
	}

	public static ActionDto toDto(Action action) {
		return ActionDto.builder()
			.categoryId(action.getNag().getCategory().getId())
			.nagContent(action.getNag().getContent())
			.actionId(action.getId())
			.actionContent(action.getContent())
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
