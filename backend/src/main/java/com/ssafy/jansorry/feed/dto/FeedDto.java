package com.ssafy.jansorry.feed.dto;

import java.time.LocalDateTime;

import com.ssafy.jansorry.member.domain.Member;
import com.ssafy.jansorry.nag.domain.Nag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FeedDto {
	private Long id;
	private Member member;
	private Nag nag;
	private String content;
	private LocalDateTime createdAt;
	private long size;
}
