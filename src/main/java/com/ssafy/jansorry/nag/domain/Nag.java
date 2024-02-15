package com.ssafy.jansorry.nag.domain;

import com.ssafy.jansorry.action.domain.Action;
import com.ssafy.jansorry.common.BaseEntity;
import com.ssafy.jansorry.receipt.domain.Message;

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
public class Nag extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@OneToMany(mappedBy = "nag", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Action> actions = new ArrayList<>();

	@OneToMany(mappedBy = "nag", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Message> messages = new ArrayList<>();

	@NonNull
	private String content;
	@NonNull
	private Long price;
	@NonNull
	private Boolean deleted;
}
