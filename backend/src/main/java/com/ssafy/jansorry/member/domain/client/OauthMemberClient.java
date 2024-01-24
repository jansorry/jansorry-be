package com.ssafy.jansorry.member.domain.client;

import com.ssafy.jansorry.member.domain.type.OauthServerType;
import com.ssafy.jansorry.member.dto.KakaoLogoutResponse;
import com.ssafy.jansorry.member.dto.OauthDto;

public interface OauthMemberClient {
	OauthServerType supportServer();

	OauthDto fetch(String code);

	KakaoLogoutResponse logout(String oauthId);
}
