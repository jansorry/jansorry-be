package com.ssafy.jansorry.member.domain;

import com.ssafy.jansorry.common.BaseEntity;
import com.ssafy.jansorry.member.domain.type.GenderType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Gender extends BaseEntity {
	@Enumerated(EnumType.STRING)
	@NonNull
	private GenderType genderType;
}
