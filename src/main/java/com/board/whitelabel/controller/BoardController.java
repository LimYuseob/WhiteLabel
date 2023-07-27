package com.board.whitelabel.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.board.whitelabel.dto.MemberAdapter;
import com.board.whitelabel.entity.Board;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.board.whitelabel.dto.BoardDTO;
import com.board.whitelabel.service.BoardService;

import lombok.RequiredArgsConstructor;

import com.board.whitelabel.dto.PageRequestDTO;
import com.board.whitelabel.entity.Member;
import com.board.whitelabel.repository.BoardRepository;

@Controller
@RequestMapping("/whitelabel")
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;
	private final BoardRepository boardRepository;


	@GetMapping("/board")
	public String list(PageRequestDTO pageRequestDTO, Model model) {

		model.addAttribute("list",boardService.getList(pageRequestDTO));

		return "whitelabel/board";

	}

	@GetMapping("/register")
	public String register(Model model, HttpServletRequest request) {

		HttpSession session = request.getSession();


		if(session == null){

			return "redirect:/whitelabel/loginPage";
		}

		Member member = (Member) session.getAttribute("member");
		String writerId = member.getEmail().substring(0,member.getEmail().indexOf('@'));
		model.addAttribute("writer", writerId);

		return "whitelabel/register";
	}

	@PostMapping("/register")
	public String postRegister(BoardDTO dto) {



		boardService.register(dto);


		return "redirect:/whitelabel/board";
	}

	@GetMapping("/read")
	public String read(long bno , @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Model model) {

		BoardDTO dto = boardService.read(bno);

		model.addAttribute("dto", dto);

		return "whitelabel/read";
	}



	@GetMapping("/modify")
	public String read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO
			, HttpServletRequest request, Model model, BoardDTO boardDTO) {

		String referer = request.getHeader("Referer");

		HttpSession session = request.getSession();

		Member member = (Member) session.getAttribute("member");

		Board checkMember = boardRepository.getBoardByBno(boardDTO.getBno());
		if(member == null){
			return "redirect:"+referer;

		}else if(!member.getMno().equals(checkMember.getMember().getMno())){
			System.out.println("세션정보 : "+member.getMno());
			System.out.println("레포지토리정보 : "+checkMember.getMember().getMno());
			return "redirect:"+referer;

		}

		BoardDTO dto = boardService.read(boardDTO.getBno());

		model.addAttribute("dto", dto);

		return "whitelabel/modify";


	}

	@PostMapping("/modify")
	public String modify(BoardDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
						 RedirectAttributes redirectAttributes){


		boardService.modify(dto);

		redirectAttributes.addAttribute("page",requestDTO.getPage());
		redirectAttributes.addAttribute("type",requestDTO.getType());
		redirectAttributes.addAttribute("keyword",requestDTO.getKeyword());

		redirectAttributes.addAttribute("bno",dto.getBno());

		return "redirect:/whitelabel/read";


	}

	@GetMapping("/remove")
	public String remove(Long bno) {
		System.out.println("remove실행");
		boardService.remove(bno);

		return "redirect:/whitelabel/board";
	}

}