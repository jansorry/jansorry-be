package com.ssafy.jansorry.follow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.jansorry.follow.domain.Follow;
import com.ssafy.jansorry.member.domain.Member;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
	Long countByToId(Long toId);

	List<Follow> findAllByMember(Member member);
}
