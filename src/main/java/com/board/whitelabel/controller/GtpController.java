package com.board.whitelabel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.board.whitelabel.service.GtpApiService;
import com.board.whitelabel.dto.GtpDTO2;
import com.board.whitelabel.dto.MemberAdapter;
import com.board.whitelabel.entity.Member;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/whitelabel")
@RequiredArgsConstructor
public class GtpController {
		
	@Autowired
	GtpApiService gtpS;
		
	
	@PostMapping("/register2")
	public String qu(GtpDTO2 question , Model model, HttpServletRequest request) {

		HttpSession session = request.getSession();

		model.addAttribute("texts",gtpS.callblog(question));

		if(session == null){

			return "redirect:/whitelabel/loginPage";
		}
		Member member = (Member) session.getAttribute("member");

		model.addAttribute("member", member);


		return "/whitelabel/register2";
	}
	
	
	@GetMapping("/keyword2")
	public void keyword() {
		
	}

}
