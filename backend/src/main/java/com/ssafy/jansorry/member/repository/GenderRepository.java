package com.ssafy.jansorry.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.jansorry.member.domain.Gender;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Long> {

}
