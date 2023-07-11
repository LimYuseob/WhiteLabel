package com.board.whitelabel.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.board.whitelabel.dto.MemberAdapter;
import com.board.whitelabel.entity.Member;
import com.board.whitelabel.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpSession;


@RequiredArgsConstructor
@Service
public class LoginService implements UserDetailsService {

   private final MemberRepository memberRepository;

   private final HttpSession httpSession;


   
   @Override
   public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

      Member member = memberRepository.findByLoginId(loginId);

	  httpSession.setAttribute("member", member);


      return new MemberAdapter(member);
   }
   
}