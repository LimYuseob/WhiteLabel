package com.board.whitelabel.dto;

import com.board.whitelabel.entity.Member;
import lombok.Builder;
import lombok.Data;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Data
@Builder
public class GtpDTO2 {

	String k1;
	String k2;
	String k3;
	String k4;
	String k5;
	String k6;


}
