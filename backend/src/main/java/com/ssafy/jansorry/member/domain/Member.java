package com.ssafy.jansorry.member.domain;

import com.ssafy.jansorry.action.domain.Action;
import com.ssafy.jansorry.common.BaseEntity;
import com.ssafy.jansorry.follow.domain.Follow;
import com.ssafy.jansorry.favorite.domain.Favorite;
import com.ssafy.jansorry.receipt.domain.Receipt;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
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
public class Member extends BaseEntity {
	@NonNull
	private String email;

	@NonNull
	private String nickname;

	@NonNull
	private Long birth;

	private Long imageUrl;

	@NonNull
	private Boolean deleted;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gender_id")
	Gender gender;

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Follow> follows = new ArrayList<>();

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Receipt> receipts = new ArrayList<>();

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Favorite> favorites = new ArrayList<>();

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Action> actions = new ArrayList<>();
}
