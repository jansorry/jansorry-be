package com.ssafy.jansorry.member.util;

import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.member.domain.OauthId;
import com.ssafy.jansorry.member.domain.type.OauthServerType;

public class MemberMapper {
	public static Member toMember(Long id) {
		return Member.builder()
			.oauthId(new OauthId(String.valueOf(id), OauthServerType.KAKAO))
			.build();
	}

}
