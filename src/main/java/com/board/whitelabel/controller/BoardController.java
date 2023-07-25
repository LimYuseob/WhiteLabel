package com.board.whitelabel.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.board.whitelabel.dto.BoardDTO;
import com.board.whitelabel.dto.MemberAdapter;
import com.board.whitelabel.service.LoginService;
import com.board.whitelabel.service.BoardService;

import lombok.RequiredArgsConstructor;

import com.board.whitelabel.dto.PageRequestDTO;
import com.board.whitelabel.entity.Board;
import com.board.whitelabel.entity.Member;
import com.board.whitelabel.repository.BoardRepository;

@Controller
@RequestMapping("/whitelabel")
@RequiredArgsConstructor
public class BoardController {

	private final BoardService service;
	private final LoginService loginService;
	private final BoardRepository boardRepository;


	@GetMapping({"/board"})
	public void list(PageRequestDTO pageRequestDTO, Model model) {

		model.addAttribute("list",service.getList(pageRequestDTO));

	}

	@GetMapping("/register")
	public String register(Model model, HttpServletRequest request) {

		HttpSession session = request.getSession();


		if(session == null){

			return "redirect:/whitelabel/loginPage";
		} else if (session.getAttribute("member") == null) {
			Member member = (Member) session.getAttribute("SNSmember");
			model.addAttribute("member", member);
			return "whitelabel/register";
		}

		Member member = (Member) session.getAttribute("member");

		model.addAttribute("member", member);

		return "whitelabel/register";
	}

	@PostMapping("/registerpo")
	public String postRegister(BoardDTO dto) {


		System.out.println("id==="+dto.getLoginId());
		System.out.println("email===="+dto.getEmail());

		service.register(dto);


		return "redirect:/whitelabel/board";
	}

	@GetMapping("/read")
	public void read(long bno , @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Model model) {

		BoardDTO dto = service.read(bno);
		model.addAttribute("dto", dto);
	}



	@GetMapping("/modify")
	public String read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO,HttpServletRequest request, Long bno, Model model, @AuthenticationPrincipal MemberAdapter memberAdapter ,RedirectAttributes redirectAttributes) {

		HttpSession session = request.getSession();

		if (session.getAttribute("member") == null) {
			// 권한이 없는 경우
			String alertMessage = "권한이 없습니다.";
			model.addAttribute("alertMessage", alertMessage);
			return "whitelabel/historyback";
		}
		Object[] result = (Object[]) boardRepository.getBoardByBno(bno);


		Board board = (Board) result[0];
		Member member = (Member) result[1];
		String dbid = member.getLoginId();
		String check = memberAdapter.getUsername(); // Member 엔티티를 MemberAdapter 객체로 변환

		if (check.equals(dbid)) {
			// 작성자 정보 반환

			BoardDTO boardDTO = service.read(bno);

			model.addAttribute("dto",boardDTO);

			return "/whitelabel/modify";

		} else if(check != dbid) {
			// 작성자 정보 불일치
			return "redirect:/whitelabel/board";
		}
		return null;


	}

	@PostMapping("/modify")
	public String modify(BoardDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
						 RedirectAttributes redirectAttributes, HttpSession session){


		service.modify(dto);

		redirectAttributes.addAttribute("page",requestDTO.getPage());
		redirectAttributes.addAttribute("type",requestDTO.getType());
		redirectAttributes.addAttribute("keyword",requestDTO.getKeyword());

		redirectAttributes.addAttribute("bno",dto.getBno());

		return "redirect:/whitelabel/read";


	}

	@GetMapping("/remove")
	public String remove(Long bno, Model model) {
		System.out.println("remove실행");
		service.remove(bno);

		return "redirect:/whitelabel/board";
	}

}