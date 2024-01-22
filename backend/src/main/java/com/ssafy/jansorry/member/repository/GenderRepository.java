package com.ssafy.jansorry.member.repository;

import com.ssafy.jansorry.member.domain.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Long> {

}
