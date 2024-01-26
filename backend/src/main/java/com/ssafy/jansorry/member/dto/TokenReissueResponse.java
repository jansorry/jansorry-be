package com.ssafy.jansorry.member.dto;

import lombok.Builder;

@Builder
public record TokenReissueResponse(
	String accessToken
) {
}
