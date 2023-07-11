package com.board.whitelabel.dto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.Data;

/*
 * 이 클래스는 요청된 페이지에 해당하는 글 목록을 가져오는 DTO입니다
 * 
 * 조회된 글은 컬렉션으로 처리되어 리턴하도록 합니다
 */
@Data
public class PageResultDTO<DTO, EN> {

	//글 목록 담은 컬렉션 선언
	private List<DTO> dtoList;
	private int totalPage;//총 페이지 수
	
	//현재 페이지번호(항상 번호 -1임)
	private int page;
	
	//목록 사이즈
	private int size;
	
	//이전, 다음 변수
	private boolean prev, next;
	
	//시작 페이지 번호, 끝 페이지 번호
	private int start, end;
	
	//페이지 번호 목록
	private List<Integer> pageList;

	
	public PageResultDTO(Page<EN> result, Function<EN, DTO> fn){
        dtoList = result.stream().map(fn).collect(Collectors.toList());
        totalPage = result.getTotalPages();

        makePageList(result.getPageable());
    }
	
	//페이징 처리를 하기위한 메서드 정의합니다
	//이 메서드는 페이징을 처리하기 위해서는 Pageable 객체가 필요합니다
	//따라서 파라미터로 Pageable을 받고 객체의 메서드를 통해 paging 처리를 합니다
	private void makePageList(Pageable pageable) {
		//현재 페이지 값을 설정합니다
		this.page = pageable.getPageNumber() + 1;//페이지 시작은 항상 0 이기에
		
		this.size = pageable.getPageSize();
		
		//아래는 실제 DB에 있는 목록을 전체 뽑아서 페이징 index를 생성하는데 사용됩니다.
		//어제 했던 ceil을 이용한 연선식을 적용하는데
		//주의 해야할 것은 공(empty)페이지가 발생된다는 것입니다
		//즉 글의 개수보다 페이지 수가 더 많은 경우를 말합니다
		//떄문에 start page는 항상 고정(1)부터지만
		//끝 페이지(end page)는 가변입니다. 이를 고려해서 연산식을 적용합니다
		/*
		 * 공백페이지(Empty 페이지가 생성되는 경우를 처리 로직)
		 * 만약 여러분의 DB에 101개의 데이터가 존재한다면 아래의 연산식을 적용하면
		 * 즉 (Math.ceil(page/10)) * 10; --> 하면 결과는 20페이지가 됩니다
		 * 하지만 실제 글이 담길 페이지는 11페이지만 필요하기때문에 12~20까지는 
		 * 공페이지(Empty) 페이지로 설정됩니다
		 * 따라서 아래의 endPage 설정의 삼항연산을 적용해서 공페이지가 발생된다면 
		 * 실제 페이지 값(totalPage)를 endPage에 대입하면 11 페이지가 EndPage가 됩니다 
		 */
		
		//먼저 가변의 endPage 변수 선언
		int tempEnd = (int)(Math.ceil(page/10.0)) * 10;
		
		start = tempEnd - 9;//항상 시작은 1페이지 이기에 -9
		
		//이전 페이지를 표현할 지의 여부
		prev = start > 1;
		
		//마지막 페이지 설정합니다
		//아래의 로직은 DB에서 가져온 전체 페이지 수와 위에서 계산한 tempEnd를
		//비교해서 동적으로 끝 페이지를 설정합니다
		end = totalPage > tempEnd ? tempEnd : totalPage;
		
		//다음(next) 항목을 표시할지의 여부에 대한 로직
		next = totalPage > tempEnd;
		
		pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
	}
	
}
