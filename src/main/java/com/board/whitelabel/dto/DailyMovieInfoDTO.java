package com.board.whitelabel.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DailyMovieInfoDTO {
	
	private	String	oxofficeType;	//	문자열	박스오피스의 종류를 출력합니다.
	private	String	showRange;		//	문자열	박스오피스의 조회일자를 출력합니다.
	private	String	rnum;			//	문자열	순번을 출력합니다.
	private	String	rank;			//	문자열	해당일자의 박스오피스 순위를 출력합니다.
	private	String	rankInten;		//	문자열	전일대비 순위의 증감분을 출력합니다.
	private	String	rankOldAndNew;	//	문자열	랭킹에 신규 진입여부를 출력합니다. "OLD": 기존, "NEW": 신규
	private	String	movieCd;		//	문자열	영화의 대표코드를 출력합니다.
	private	String	movieNm;		//	문자열	영화명(국문)을 출력합니다.
	private	String	openDt;			//	문자열	영화의 개봉일을 출력합니다.
	private	String	salesAmt;		//	문자열	해당일의 매출액을 출력합니다.
	private	String	salesShare;		//	문자열	해당일자 상영작의 매출총액 대비 해당 영화이 매출비율을 출력합니다.
	private	String	salesInten;		//	문자열	전일 대비 매출액 증감분을 출력합니다.
	private	String	salesChange;	//	문자열	전일 대비 매출액 증감비율을 출력합니다.
	private	String	salesAcc;		//	문자열	누적매출액을 출력합니다.
	private	String	audiCnt;		//	문자열	해당일의 관객수를 출력합니다.
	private	String	audiInten;		//	문자열	전일 대비 관객수 증감분을 출력합니다.
	private	String	audiChange;		//	문자열	전일 대비 관객수 증감비율을 출력합니다.
	private	String	audiAcc;		//	문자열	누적관객수를 출력합니다.
	private	String	scrnCnt;		//	문자열	해당일자에 상영한 스크린수를 출력합니다.
	private	String	showCnt;		//	문자열	해당일자에 상영된 횟수를 출력합니다.
	private String title;
	private String image;
	private String link;
	private String director;
	
	
}
