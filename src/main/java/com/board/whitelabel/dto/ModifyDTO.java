package com.board.whitelabel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyDTO {

	private Long ono;
	private String userName;
	private String gender;
	private String passport;
	
}
