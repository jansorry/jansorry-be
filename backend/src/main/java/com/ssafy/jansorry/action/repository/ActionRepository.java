package com.ssafy.jansorry.action.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ssafy.jansorry.action.domain.Action;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long>, ActionCustomRepository {
	List<Action> findAllByMemberIdAndDeletedFalse(Long memberId);

	Optional<Action> findActionByIdAndDeletedFalse(Long actionId);

	Optional<Action> findActionById(Long actionId);

	@Query("SELECT c.groupType FROM Action a JOIN a.nag n JOIN n.category c WHERE a.member.id = :memberId AND a.deleted = false")
	List<Long> findGroupTypesByMemberId(@Param("memberId") Long memberId);

}
