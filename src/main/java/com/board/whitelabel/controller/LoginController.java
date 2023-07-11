package com.board.whitelabel.controller;

import com.board.whitelabel.entity.Member;
import com.board.whitelabel.service.LoginService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/whitelabel")
@RequiredArgsConstructor
public class LoginController {


	// Service
	private final LoginService loginService;

	// 아래의 모든 매핑에서는 여러분이 응요할 수 잇도록 요청 패턴과 이에 대응하는
	// Viewer 를 지금까지 했던것과는 틀리게 처리 하였으니, 필요시 응용하세요.
	@GetMapping("/loginPage")
	public String loginForm() {
		// Viewer 매핑

		return "whitelabel/loginPage";
	}

	@PostMapping("/loginPage")
	public String login(Member member) {

		UserDetails members = loginService.loadUserByUsername(member.getLoginId());
		
		if (members.getUsername().equals(member.getLoginId())) {
			return "redirect:/whitelabel/";
		} else if (!members.getUsername().equals(member.getLoginId())) {

			return "redirect:/whitelabel/loginPage";
		}

		return null;

	}

	@GetMapping("/logout")
	public String getLogout(HttpSession session){

		session.invalidate();



		return "whitelabel/loginPage";
	}







}