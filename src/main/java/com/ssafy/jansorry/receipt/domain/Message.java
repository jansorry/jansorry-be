package com.ssafy.jansorry.receipt.domain;

import com.ssafy.jansorry.common.BaseEntity;
import com.ssafy.jansorry.nag.domain.Nag;

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
public class Message extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nag_id")
	private Nag nag;

	@NonNull
	private String content;

	@NonNull
	@Builder.Default
	private Boolean deleted = false;
}
