package com.ssafy.jansorry.follow.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.jansorry.follow.domain.Follow;

@Repository
@Primary
public interface FollowRepository extends JpaRepository<Follow, Long>, FollowCustomRepository {
}
