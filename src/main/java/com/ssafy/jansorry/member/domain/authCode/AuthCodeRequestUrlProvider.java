package com.ssafy.jansorry.member.domain.authCode;

import com.ssafy.jansorry.member.domain.type.OauthServerType;

public interface AuthCodeRequestUrlProvider {
	OauthServerType supportServer();

	String provide();
}
