package com.ssafy.jansorry.batch.repository;

import java.util.List;

import com.ssafy.jansorry.batch.type.BatchKeyNumberType;

public interface BatchCustomRepository {
	List<Long> searchTop5NagsByGender(Long categoryId, BatchKeyNumberType keyNumberType);

	List<Long> searchTop5NagsByAll(Long categoryId);

	List<Long> searchTop5NagsByAgeRange(Long categoryId, BatchKeyNumberType keyNumberType);

}
