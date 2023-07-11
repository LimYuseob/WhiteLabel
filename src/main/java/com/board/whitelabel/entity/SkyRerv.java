package com.board.whitelabel.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SkyRerv {

	//예약자 정보 skyAirTicketDTO
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ono;
	
	private String userName;
	private String dob;
	private String gender;
	private String passport;
	private String nationality;
	
	
	//일정 정보 skyAirTicketDTO
	private String airLine;
	private String logo;
	private String depCode;
	private String depDate;
	private String arrCode;
	private String arrDate;
	private String via;//viaCode + viaDate 합쳐서 저장
	private String duration;
	
	
	
	//결제 정보 kakaoPayApprovalDTO
	private String item_name;
	private int total_price;
	private String payment_type;
	private int quantity;
	private String created_at;
	private String approved_at;
	
	public void changeTitle(String userName) {
		this.userName = userName;
	}
	
	public void changePassport(String passport) {
		this.passport = passport;
	}
	
	public void changeGender(String gender) {
		this.gender = gender;
	}
	
}
