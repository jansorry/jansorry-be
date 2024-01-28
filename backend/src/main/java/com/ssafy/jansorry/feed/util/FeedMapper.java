package com.ssafy.jansorry.feed.util;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.ssafy.jansorry.action.domain.Action;
import com.ssafy.jansorry.feed.dto.FeedInfoResponse;

public class FeedMapper {
	public static FeedInfoResponse fromAction(Action action) {
		return FeedInfoResponse.builder()
			.memberId(action.getMember().getId())
			.nickname(action.getMember().getNickname())
			.nag(action.getNag().getContent())
			.action(action.getContent())
			.category(action.getNag().getCategory().getGroupType().name())
			.createdAt(action.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
			.build();
	}

	public static List<FeedInfoResponse> fromActions(List<Action> actions) {
		if (CollectionUtils.isEmpty(actions)) {
			return Collections.emptyList();
		}
		return actions.stream()
			.map(FeedMapper::fromAction)
			.collect(Collectors.toList());
	}
}
