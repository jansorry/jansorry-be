package com.ssafy.jansorry.member.domain;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

import com.ssafy.jansorry.action.domain.Action;
import com.ssafy.jansorry.common.BaseEntity;
import com.ssafy.jansorry.favorite.domain.Favorite;
import com.ssafy.jansorry.follow.domain.Follow;
import com.ssafy.jansorry.receipt.domain.Receipt;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@DynamicUpdate
@Table(name = "member", uniqueConstraints = {
	@UniqueConstraint(name = "oauth_id_unique", columnNames = {"oauth_server_id", "oauth_server"})})
public class Member extends BaseEntity {
	@Embedded
	private OauthId oauthId;

	@NonNull
	@Builder.Default
	private String nickname = "";

	@NonNull
	@Builder.Default
	private Long birth = 1900L;

	private Long imageUrl;

	@NonNull
	@Builder.Default
	private Boolean deleted = false;

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
