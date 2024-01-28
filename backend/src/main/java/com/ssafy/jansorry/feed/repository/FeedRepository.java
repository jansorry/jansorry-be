package com.ssafy.jansorry.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.jansorry.member.domain.Member;

@Repository
public interface FeedRepository extends JpaRepository<Member, Long>, FeedCustomRepository {
}
