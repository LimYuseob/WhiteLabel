package com.board.whitelabel.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.board.whitelabel.entity.Board;


public interface BoardRepository extends JpaRepository<Board, Long> {
	
	@Query("select b from Board b  where b.bno =:bno")
	Board getBoardByBno(@Param("bno") Long bno);


	/*
	 * LEFT JOIN 이용해서 관계 설정된 테이블에서 정보를 모두 가져온 후 가공작업을 합니다.
	 * 이렇게 가공된 정보를 DTO에 담아서 View단으로 넘기면
	 * 일단 List 페이지에서 보여지는 목록 구성은 완성이 되어집니다
	 * 
	 * 여기서 유심히 봐야 할 부분은 어디엔 On을 어디엔 On을 안붙이는지 봐야합니다
	 * 또 하난, countQuery라는 속성이 있습니다
	 * 
	 * 이전 Spring 에서는 위 처럼 Lazy Fetch를 쓸 경우 중심 테이블(여기선 board)
	 * 의 카운트 쿼리를 명세에 넣지 않으면 LazyIntialized 예외가 발생하곤 합니다
	 * 떄문에 count 쿼리를 명시적으로 넣긴 하는데, 일단은 넣지 않고 진행해 볼게요
	 
	//@Query(value = "select b, w, count(r) from Board b LEFT JOIN b.loginId w LEFT JOIN Reply r ON r.board = b group by b",
	//		countQuery = "select count(b) From Board b")
	Page<Object[]> getBoard(Pageable pageable);
	
	
	
	
	//JPQL을 직접 날리는 예제를 수행합니다
	//일반 RDB에서 수행하는 쿼리와 비슷한듯 다릅니다
	//일단 문법을 다 외우려 하지말고, 형태만 기억하세요
	//먼저 연관관계(inner join)이 있는 테이블과의 조인부터 봅니가
	
	//기본문법 형태
	
	 * Select [컬럼명],[컬럼명n...] or table_Alias, table_Alias[n]
	 * from table_name Alias LEFT JOIN joinTable_Alias.컬럼명 조인대상 부모테이블 ALIAS
	 * where alias.columnName =: [컬럼명] ;
	 
	
	//@Query("select b,w from Board b LEFT Join b.loginId w where b.bno =:bno")
	Object getBoardWithLoginId(@Param("bno") Long bno);
	
	//연관관계가 없는 객체 즉 관계설정을 당한 클래스에서 
	//하위 클래스의 내용을 Join을 걸어 추출하려면 명시적으로 ON 
	//키워드를 조인 처리 전 구문에 넣어줘야만 한다
	//@Query("select b, r From Board b LEFT Join Reply r ON r.board=b where b.bno=:bno")
	List<Object[]> getBoard(@Param("bno") Long bno);
	
	
	 * List 페이지에서 필요한 글목록에 해당하는 JPQL 정의합니다.
	 * 참조 테이블인 Board에 참조 글여부에 따라서 하나 이상의 글 또는 댓글이 없는 글을
	 * 가져올 수 있습니다. 그리고 전 주에 했듯이 관계설정이 되어 있지 않은 테이블에서 
	 * Join을 걸때는 반드시 On 구문을 사용해야 합니다. Outer 구문은 옵셔널이라, 
	 * 빼도 상관은 없습니다.
	 * SELECT b.bno, b.title, b.writer_email
	 * FROM board b LEFT OUTER JOIN reple r
     * ON r.board_bno = b.bno
     * WHERE b.bno=1;
	 
	
	//Board의 bno를 기준의 글 상세를 가져오는 메서드 선언
	//@Query("select b , w, count(r) from Board b left join b.loginId w left join Reply r on r.board = b where b.bno =:bno")
	Object getBoardByBno(@Param("bno") Long bno);
	
	
	*/
}

