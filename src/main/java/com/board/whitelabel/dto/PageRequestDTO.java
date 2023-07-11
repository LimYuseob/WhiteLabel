package com.board.whitelabel.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/*
 * 이 DTO는 뷰단에서 사용자의 페이지 요청 시에 데이터를 담아 전달되는 DTO입니다
 */
@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {
	
	//요청되는 페이지 번호
	private int page;
	private int size;
	
	//검색시 맵핑되는 키워드 필드 추가
	private String type;//조건 검색 키워드
	private String keyword;//검색 키워드 필드
	
	public PageRequestDTO() {
		this.page = 1;
		this.size = 10;
		
	}
	//이후 추가 메서드는 페이지 겨로가 처리 DTO를 정의 후에 재정의합니다.
	/*
	 * PageRequest.of() 이용해서 페이지의 index, 목록수(size)
	 * sort를 적용했습니다. 기본적으로 리턴되는 페이지 인덱스는 0번인데, 사용자는
	 * 요청시에 기본이 1페이지가 될겁니다. 이유는 페이징시 인덱스는 1번 부터 시작을 
	 * 하기떄문입니다. 떄문에 해당되는 페이지에서 -1을 해줘야 올바를 페이지 목록이
	 * 리턴될겁니다. 이걸 통해서 Pageable을 리턴시킵니다.
	 */
	//아래의 Sort는 목적에 따라 변경 할 수 있도록 호출시에 파라미터 객체로 넘깁니다
	//ex> dececending, acending etc..
	public Pageable getPageable(Sort sort) {
		return PageRequest.of(page-1, size, sort);
		
	}
	
}
