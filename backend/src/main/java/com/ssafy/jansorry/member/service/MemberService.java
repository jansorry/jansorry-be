package com.ssafy.jansorry.member.service;

import com.ssafy.jansorry.member.domain.Gender;
import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.member.dto.SignUpRequest;
import com.ssafy.jansorry.member.dto.SignUpResponse;
import com.ssafy.jansorry.member.repository.GenderRepository;
import com.ssafy.jansorry.member.repository.MemberRepository;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final GenderRepository genderRepository;

	public SignUpResponse createMember(SignUpRequest request) {
		Member member = memberRepository.findById(request.memberId())
			.orElseThrow(() -> new RuntimeException("NOT_FOUND_MEMBER"));
		Gender gender = genderRepository.findById(request.genderId())
				.orElseThrow(() -> new RuntimeException("NOT_FOUND_GENDER"));

		member.setBirth(request.birth());
		member.setGender(gender);
		member.setNickname(createNickname());
		member = memberRepository.save(member);

		return SignUpResponse.builder()
			.memberId(member.getId())
			.nickname(member.getNickname())
			.build();
	}

	private String createNickname() {
		Random rd = new Random();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 4; i++) {
			sb.append((char)('a' + rd.nextInt(26)));
		}

		for (int i = 0; i < 4; i++) {
			sb.append(rd.nextInt(10));
		}

		return sb.toString();
	}
}
