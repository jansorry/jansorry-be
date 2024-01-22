package com.ssafy.jansorry.follow.repository;

import com.ssafy.jansorry.follow.domain.Follow;
import com.ssafy.jansorry.member.domain.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
	Long countByToId(Long toId);

	List<Follow> findAllByMember(Member member);
}
