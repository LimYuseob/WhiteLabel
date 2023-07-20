package com.board.whitelabel.service;

import javax.transaction.Transactional;

import com.board.whitelabel.dto.PageRequestDTO;
import com.board.whitelabel.dto.PageResultDTO;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.board.whitelabel.dto.BoardDTO;
import com.board.whitelabel.entity.Board;
import com.board.whitelabel.entity.Member;
import com.board.whitelabel.repository.BoardRepository;
import com.board.whitelabel.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{

	private final BoardRepository boardRepository;
	private final MemberRepository memberRepository;

	@Override
	public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO requestDTO) {
		Pageable pageable = requestDTO.getPageable(Sort.by("bno").descending());
		Page<Object[]> result = boardRepository.findAll(pageable)
				.map(board -> new Object[]{board, board.getMember()});

		Function<Object[], BoardDTO> fn = (entity->entityToDto((Board)entity[0], (Member)entity[1]));

		return new PageResultDTO<>(result, fn);
	}


	@Override
	@Transactional
	public void register(BoardDTO dto) {
		log.info("신규등록 호출");

		System.out.println("dto.email"+dto.getEmail());

		Member member = memberRepository.findByEmail(dto.getEmail());
		if(member == null) {
			// 에러 처리
			System.out.println("에러 null");
		}

		Board board = dtoToEntity(dto);
		board.setMember(member);

		boardRepository.save(board);
	}


	@Override
	public BoardDTO read(Long bno) {

		Object ob = boardRepository.getBoardByBno(bno);
		Object[] arr  = (Object[]) ob;

		return entityToDto((Board)arr[0], (Member)arr[1]);
	}

	@Transactional
	@Override
	public void remove(Long bno) {

		boardRepository.deleteById(bno);


	}

	@Transactional
	@Override
	public void modify(BoardDTO boardDTO) {

		Board board = boardRepository.getOne(boardDTO.getBno());

		if(board != null) {

			board.changeTitle(boardDTO.getTitle());
			board.changeContent(boardDTO.getContent());

			boardRepository.save(board);
		}
	}
	//검색 조건을 추가하여, 검색에 매칭되는 Entity를 구성해서 getListPage()로 보낸다
	//QueryDSL을 이용할 예정이라 리턴타입은 javax.persistant.page 객체로
	//리턴시키기 위해서 BooleanBuilder 객체로 리턴할 예정
	//이렇게 리턴된 BooleanBuilder를 findAll(BooleanBuilder, Pageable)
	//메서드를 통해 Page객체를 얻어내서 list.html 까지 전달 시킨다.
	//QueryDSL의 장점 : Entity 필드를 조회 조건으로 이용할 수 있다



}
