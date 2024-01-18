package com.ssafy.jansorry.favorite.domain;

import com.ssafy.jansorry.action.domain.Action;
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
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Favorite extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "action_id")
	Action action;
}
