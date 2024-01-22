package com.ssafy.jansorry.member.service;

import com.ssafy.jansorry.follow.repository.FollowRepository;
import com.ssafy.jansorry.member.domain.Gender;
import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.member.dto.MemberResponse;
import com.ssafy.jansorry.member.dto.MemeberEditdto;
import com.ssafy.jansorry.member.dto.SignUpRequest;
import com.ssafy.jansorry.member.dto.SignUpResponse;
import com.ssafy.jansorry.member.repository.GenderRepository;
import com.ssafy.jansorry.member.repository.MemberRepository;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final GenderRepository genderRepository;
	private final FollowRepository followRepository;

	@Transactional
	public SignUpResponse createMember(SignUpRequest request) {
		Member member = memberRepository.findById(request.memberId())
			.orElseThrow(() -> new RuntimeException("NOT_FOUND_MEMBER"));
		Gender gender = genderRepository.findById(request.genderId())
				.orElseThrow(() -> new RuntimeException("NOT_FOUND_GENDER"));

		member.setBirth(request.birth());
		member.setGender(gender);
		member.setNickname(createNickname());

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

	public MemeberEditdto updateMember(Member member, MemeberEditdto request) {
		member.setNickname(request.nickname());

		memberRepository.save(member);

		return MemeberEditdto.builder()
			.nickname(request.nickname())
			.build();
	}

	public void deleteMemeber(Member member) {
		member.setDeleted(true);
		memberRepository.save(member);
	}

	public MemberResponse readMemeber(Member member) {
		return MemberResponse.builder()
			.imageUrl(member.getImageUrl())
			.followingCnt(Long.valueOf(member.getFollows().size()))
			.followerCnt(followRepository.countByToId(member.getId()))
			.build();
	}
}
