package com.ssafy.jansorry.action.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.jansorry.action.domain.Action;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {
	List<Action> findAllByMemberIdAndDeletedFalse(Long memberId);

	Optional<Action> findActionByIdAndDeletedFalse(Long actionId);

	Optional<Action> findActionById(Long actionId);
}
