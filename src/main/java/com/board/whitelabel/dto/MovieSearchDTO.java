package com.board.whitelabel.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieSearchDTO {

	private String title;
	private String link;
	private String image;
	private String director;
	private String prodYear;
	private String kmUrl;
	private String genre;
	private String type;
	
	
}
