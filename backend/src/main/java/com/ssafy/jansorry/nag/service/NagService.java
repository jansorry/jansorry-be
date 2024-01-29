package com.ssafy.jansorry.nag.service;

import static com.ssafy.jansorry.exception.ErrorCode.*;
import static com.ssafy.jansorry.nag.util.NagMapper.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.jansorry.exception.BaseException;
import com.ssafy.jansorry.nag.domain.Nag;
import com.ssafy.jansorry.nag.domain.type.GroupType;
import com.ssafy.jansorry.nag.dto.CategoryDto;
import com.ssafy.jansorry.nag.dto.NagDto;
import com.ssafy.jansorry.nag.repository.NagRepository;
import com.ssafy.jansorry.nag.util.NagMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NagService {
	private final NagRepository nagRepository;

	public NagDto readNag(Long nagId) {
		Nag nag = nagRepository.findNagById(nagId).orElseThrow(() -> new BaseException(NAG_NOT_FOUND));
		return toDto(nag);
	}

	public List<CategoryDto> readAllNags() {
		List<CategoryDto> categoryDtos = new ArrayList<>();
		List<NagDto> nagDtos = new ArrayList<>();

		Long categoryId = 1L;
		List<Nag> nags = nagRepository.findAllByDeletedFalse();

		for (Nag nag : nags) {
			if (!Objects.equals(categoryId, nag.getCategory().getId())) {
				categoryDtos.add(CategoryDto.builder()
					.categoryId(categoryId)
					.title(GroupType.values()[categoryId.intValue() - 1].getValue())
					.nags(nagDtos)
					.build());
				nagDtos = new ArrayList<>();
				categoryId = nag.getCategory().getId();
			} else {
				nagDtos.add(NagMapper.toDto(nag));
			}
		}

		categoryDtos.add(CategoryDto.builder()
			.categoryId(categoryId)
			.title(GroupType.values()[categoryId.intValue() - 1].getValue())
			.nags(nagDtos)
			.build());

		return categoryDtos;
	}
}
