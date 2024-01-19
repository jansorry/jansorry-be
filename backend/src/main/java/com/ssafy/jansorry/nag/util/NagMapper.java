package com.ssafy.jansorry.nag.util;

import com.ssafy.jansorry.nag.domain.Nag;
import com.ssafy.jansorry.nag.dto.NagDto;

public class NagMapper {
	public static NagDto toDto(Nag nag) {
		return NagDto.builder()
			.nagId(nag.getId())
			.categoryId(nag.getCategory().getId())
			.content(nag.getContent())
			.price(nag.getPrice())
			.build();
	}
}
