package com.ssafy.jansorry.member.util;

import com.ssafy.jansorry.member.domain.type.OauthServerType;
import org.springframework.core.convert.converter.Converter;

public class OauthServerTypeConverter implements Converter<String, OauthServerType> {

	@Override
	public OauthServerType convert(String source) {
		return OauthServerType.fromName(source);
	}
}
