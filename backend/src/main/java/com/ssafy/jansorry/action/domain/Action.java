package com.ssafy.jansorry.action.domain;

import java.util.ArrayList;
import java.util.List;

import com.ssafy.jansorry.common.BaseEntity;
import com.ssafy.jansorry.favorite.domain.Favorite;
import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.nag.domain.Nag;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Action extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nag_id")
	private Nag nag;

	@NonNull
	private String content;

	@OneToMany(mappedBy = "action", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Favorite> favorites = new ArrayList<>();
}
