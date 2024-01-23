package com.ssafy.jansorry.member.service;

import com.ssafy.jansorry.follow.domain.Follow;
import com.ssafy.jansorry.follow.repository.FollowRepository;
import com.ssafy.jansorry.member.domain.Gender;
import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.member.domain.OauthId;
import com.ssafy.jansorry.member.domain.type.OauthServerType;
import com.ssafy.jansorry.member.dto.MemberResponse;
import com.ssafy.jansorry.member.dto.MemberEditDto;
import com.ssafy.jansorry.member.dto.SignUpRequest;
import com.ssafy.jansorry.member.dto.SignUpResponse;
import com.ssafy.jansorry.member.repository.GenderRepository;
import com.ssafy.jansorry.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final GenderRepository genderRepository;
	private final FollowRepository followRepository;
	private final OauthService oauthService;
	private final TokenService tokenService;

	public SignUpResponse createMember(SignUpRequest request) {
		Gender gender = genderRepository.findById(request.genderId())
				.orElseThrow(() -> new RuntimeException("NOT_FOUND_GENDER"));

		Member member = memberRepository.save(
			Member.builder()
				.oauthId(new OauthId(request.oauthId(), OauthServerType.KAKAO))
				.nickname(createNickname())
				.imageUrl(createImageUrl())
				.birth(request.birth())
				.gender(gender)
				.build()
		);

		return SignUpResponse.builder()
			.nickname(member.getNickname())
			.accessToken(tokenService.createToken(member))
			.refreshToken(tokenService.createRefreshToken(member))
			.build();
	}

	private String createNickname() {
		Random rd = new Random();
		StringBuilder sb;

		// 중복된 닉네임이 없을 때까지 반복
		do {
			sb = new StringBuilder();

			for (int i = 0; i < 4; i++) {
				sb.append((char) ('a' + rd.nextInt(26)));
			}

			for (int i = 0; i < 4; i++) {
				sb.append(rd.nextInt(10));
			}
		} while (memberRepository.findByNickname(sb.toString()).isPresent());

		return sb.toString();
	}

	private Long createImageUrl() {
		Random rd = new Random();
		return rd.nextLong(10L);
	}

	public MemberEditDto updateMember(Member member, MemberEditDto request) {
		member.setNickname(request.nickname());

		memberRepository.save(member);

		return MemberEditDto.builder()
			.nickname(request.nickname())
			.build();
	}

	public void deleteMember(OauthServerType oauthServerType, Member member,
		HttpServletResponse response) {
		oauthService.logout(oauthServerType, member.getOauthId().getOauthServerId()); // 카카오 로그아웃
		tokenService.resetHeader(response); // header에서 accesstoken, refreshtoken 삭제
		member.setDeleted(true);
		memberRepository.save(member);
	}

	public MemberResponse readMember(Member member) {
		List<Follow> follows = followRepository.findAllByMember(member);
		member.setFollows(follows);

		return MemberResponse.builder()
			.nickname(member.getNickname())
			.imageUrl(member.getImageUrl())
			.followingCnt(Long.valueOf(member.getFollows().size()))
			.followerCnt(followRepository.countByToId(member.getId()))
			.build();
	}
}
