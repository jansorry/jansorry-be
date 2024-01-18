package com.ssafy.jansorry.receipt.domain;

import com.ssafy.jansorry.common.BaseEntity;
import com.ssafy.jansorry.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Receipt extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@NonNull
	private String title;
	@NonNull
	private String description;
	@NonNull
	private String message;
	@NonNull
	private String familyUrl;
	@NonNull
	private String friendUrl;
	@NonNull
	private Boolean deleted;
}
