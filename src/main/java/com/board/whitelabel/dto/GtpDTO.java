package com.board.whitelabel.dto;

import com.board.whitelabel.entity.Member;
import lombok.Builder;
import lombok.Data;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Data
@Builder
public class GtpDTO {

	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;
}
