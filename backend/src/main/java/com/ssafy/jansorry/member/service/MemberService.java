package com.ssafy.jansorry.member.service;

import static com.ssafy.jansorry.exception.ErrorCode.*;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.ssafy.jansorry.action.repository.ActionRepository;
import com.ssafy.jansorry.exception.BaseException;
import com.ssafy.jansorry.exception.ErrorCode;
import com.ssafy.jansorry.follow.dto.FollowCountDto;
import com.ssafy.jansorry.follow.service.FollowService;
import com.ssafy.jansorry.member.domain.Gender;
import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.member.domain.OauthId;
import com.ssafy.jansorry.member.domain.type.OauthServerType;
import com.ssafy.jansorry.member.dto.LoginDto;
import com.ssafy.jansorry.member.dto.MemberEditDto;
import com.ssafy.jansorry.member.dto.MemberResponse;
import com.ssafy.jansorry.member.dto.SignUpRequest;
import com.ssafy.jansorry.member.repository.GenderRepository;
import com.ssafy.jansorry.member.repository.MemberRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final GenderRepository genderRepository;
	private final ActionRepository actionRepository;
	private final OauthService oauthService;
	private final TokenService tokenService;
	private final FollowService followService;

	public LoginDto createMember(SignUpRequest request) {
		Gender gender = genderRepository.findById(request.genderId())
			.orElseThrow(() -> new BaseException(NOT_FOUND_GENDER));

		Member member = memberRepository.save(
			Member.builder()
				.oauthId(new OauthId(request.oauthId(), OauthServerType.KAKAO))
				.name(request.kakaoNickname())
				.nickname(createNickname())
				.imageUrl(createImageUrl())
				.birth(request.birth())
				.gender(gender)
				.build()
		);

		return LoginDto.builder()
			.nickname(member.getNickname())
			.kakaoNickname(member.getName())
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
				sb.append((char)('a' + rd.nextInt(26)));
			}

			for (int i = 0; i < 4; i++) {
				sb.append(rd.nextInt(10));
			}
		} while (memberRepository.findByNickname(sb.toString()).isPresent());

		return sb.toString();
	}

	private Long createImageUrl() {
		return ThreadLocalRandom.current().nextLong(10L);
	}

	public MemberEditDto updateMember(Member member, MemberEditDto request) {
		Optional<Member> existingMember = memberRepository.findByNickname(request.nickname());

		if (existingMember.isPresent()) {
			throw new BaseException(ErrorCode.MEMBER_NICKNAME_DUPLICATED);
		}

		member.setNickname(request.nickname());
		memberRepository.save(member);

		return MemberEditDto.builder()
			.nickname(request.nickname())
			.build();
	}

	public void deleteMember(HttpServletResponse response, OauthServerType oauthServerType, Member member) {
		oauthService.logout(response, oauthServerType, member.getOauthId().getOauthServerId()); // 카카오 로그아웃
		tokenService.deleteHeader(response); // header에서 accesstoken, refreshtoken 삭제
		member.setDeleted(true);
		memberRepository.save(member);
	}

	// 스스로의 정보 조회
	public MemberResponse readMemberSelf(Member member) {
		FollowCountDto followCountDto = followService.readFollowCount(member.getId());

		return MemberResponse.builder()
			.nickname(member.getNickname())
			.imageUrl(member.getImageUrl())
			.actionCnt(actionRepository.countAllByMemberIdAndDeletedFalse(member.getId()))
			.followerCnt(followCountDto.followerCount())
			.followingCnt(followCountDto.followingCount())
			.build();
	}

	// 검색 시 다른 멤버의 정보 조회
	public MemberResponse readMemberByNickName(String nickName) {
		Member searchedMember = memberRepository.findByNickname(nickName)
			.orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));
		FollowCountDto followCountDto = followService.readFollowCount(searchedMember.getId());

		return MemberResponse.builder()
			.nickname(nickName)
			.imageUrl(searchedMember.getImageUrl())
			.actionCnt(actionRepository.countAllByMemberIdAndDeletedFalse(searchedMember.getId()))
			.followerCnt(followCountDto.followerCount())
			.followingCnt(followCountDto.followingCount())
			.build();
	}
}
