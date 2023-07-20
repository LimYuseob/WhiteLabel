package com.board.whitelabel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 이 DTO는 JOIN을 걸어서 나온 3개의 Entity객체의 필요 속성의 값을 하나로 합치는 List 목록 페이지에서 사용될 DTO입니다.
 * List 페이지에서 보여질 항목은 글넘버, 글제목, 작성자 이메일, 작성자 이름 그리고 글제목 옆에 붙을 댓글수입니다
 * 
 * 따라서 위에서 사용할 데이터의 필드를 선언하고 get, set을 이용해서 각각의 Entity에서
 * 필요한 정보를 set, get해줍니다
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

	private Long bno;
	private String title;
	private String loginId;
	private String content;
	private String regDate;
	private String modDate;
	private String email;
	private String writer;
}
