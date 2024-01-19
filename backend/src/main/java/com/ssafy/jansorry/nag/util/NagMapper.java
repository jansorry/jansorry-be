package com.ssafy.jansorry.nag.util;

import com.ssafy.jansorry.nag.domain.Nag;
import com.ssafy.jansorry.nag.dto.NagDetailDto;

public class NagMapper {
	public static NagDetailDto toDetailDto(Nag nag) {
		return NagDetailDto.builder()
			.nagId(nag.getId())
			.categoryId(nag.getCategory().getId())
			.content(nag.getContent())
			.price(nag.getPrice())
			.build();
	}
}
