package com.ssafy.jansorry.action.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.jansorry.action.domain.Action;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

}
