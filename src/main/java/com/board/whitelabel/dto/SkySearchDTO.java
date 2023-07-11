package com.board.whitelabel.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkySearchDTO {

	private String originDate;
	private String departureDate;
	private int adults;
	private String origin;
	private String destination;
	private String cabinClass;
}
