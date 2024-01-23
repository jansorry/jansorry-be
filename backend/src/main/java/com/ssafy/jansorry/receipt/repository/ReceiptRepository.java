package com.ssafy.jansorry.receipt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.receipt.domain.Receipt;

import lombok.RequiredArgsConstructor;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt,Long> {
	//JpaRepository: PagingAndSortingRepository, QueryByExampleExecutor 인터페이스를 상속받고 있음
	//PagingAndSqortingRepository: CrudRepository 인터페이스를 상속받고 있음
	List<Receipt> findAllByMemberIdAndDeletedFalseOrderByCreatedAtAsc(Long memberId);
}
