package com.ssafy.jansorry.feed.util;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.ssafy.jansorry.action.domain.Action;
import com.ssafy.jansorry.feed.dto.FeedDto;
import com.ssafy.jansorry.feed.dto.FeedInfoResponse;

public class FeedMapper {
	public static FeedInfoResponse fromAction(Action action) {
		return FeedInfoResponse.builder()
			.memberId(action.getMember().getId())
			.actionId(action.getId())
			.nickname(action.getMember().getNickname())
			.nag(action.getNag().getContent())
			.action(action.getContent())
			.categoryId(action.getNag().getCategory().getId())
			.categoryTitle(action.getNag().getCategory().getGroupType().getValue())
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

	public static FeedInfoResponse fromFeedDto(FeedDto feedDto) {
		return FeedInfoResponse.builder()
			.memberId(feedDto.getMember().getId())
			.actionId(feedDto.getId())
			.nickname(feedDto.getMember().getNickname())
			.nag(feedDto.getNag().getContent())
			.action(feedDto.getContent())
			.categoryId(feedDto.getNag().getCategory().getId())
			.categoryTitle(feedDto.getNag().getCategory().getGroupType().getValue())
			.favoriteCount(feedDto.getSize())
			.createdAt(feedDto.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
			.build();
	}

	public static List<FeedInfoResponse> fromFeedDtos(List<FeedDto> feedDtos) {
		if (CollectionUtils.isEmpty(feedDtos)) {
			return Collections.emptyList();
		}
		return feedDtos.stream()
			.map(FeedMapper::fromFeedDto)
			.collect(Collectors.toList());
	}
}
