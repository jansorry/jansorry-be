package com.ssafy.jansorry.member.util;

import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.member.dto.OauthDto;

public class OauthIdMapper {
	public static OauthDto from(Member member, String at) {
		return OauthDto.builder()
			.member(member)
			.accessToken(at)
			.build();
	}
}
