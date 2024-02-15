package com.ssafy.jansorry.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.jansorry.action.domain.Action;

@Repository
public interface FeedRepository extends JpaRepository<Action, Long>, FeedCustomRepository {
}
