package com.ssafy.jansorry.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.jansorry.batch.domain.BatchEntity;

@Repository
public interface BatchRepository extends JpaRepository<BatchEntity, Long> {

}
