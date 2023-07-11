package com.board.whitelabel.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkyAirTicketDTO {

	private int adults;
	private String logo;
    private String price;
    private String duration;
    private String depCode;
    private String depDate;
    private String arrCode;
    private String arrDate;
    private String name; //항공사 이름
    private String viaCode;
    private String viaDateTime;
    private String user; //에약자 이름
    private String dob; //생년월일
    private String gender;
    private String passport;
    private String nationality;
  
 
	
}



