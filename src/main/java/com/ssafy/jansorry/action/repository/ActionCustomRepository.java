package com.ssafy.jansorry.action.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.ssafy.jansorry.action.dto.ActionDto;

public interface ActionCustomRepository {
	Slice<ActionDto> searchActionsByMember(Long lastActionId, Long memberId, Pageable pageable);
}
