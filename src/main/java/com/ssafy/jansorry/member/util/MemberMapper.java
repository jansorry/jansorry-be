package com.ssafy.jansorry.member.util;

import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.member.domain.OauthId;
import com.ssafy.jansorry.member.domain.type.OauthServerType;
import com.ssafy.jansorry.member.dto.KakaoMemberResponse;

public class MemberMapper {
	public static Member toMember(KakaoMemberResponse kakaoMemberResponse) {
		return Member.builder()
			.oauthId(new OauthId(String.valueOf(kakaoMemberResponse.id()), OauthServerType.KAKAO))
			.name(kakaoMemberResponse.kakaoAccount().profile().nickname())
			.build();
	}

}
