package com.ssafy.jansorry.member.repository;

import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.member.domain.OauthId;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByOauthId(OauthId oauthId);
	Optional<Member> findByNickname(String nickname);
}
