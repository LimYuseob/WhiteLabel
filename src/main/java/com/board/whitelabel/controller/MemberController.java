package com.board.whitelabel.controller;

import com.board.whitelabel.dto.MailDTO;
import com.board.whitelabel.dto.MemberDTO;
import com.board.whitelabel.entity.Member;
import com.board.whitelabel.service.CustomOAuth2UserService;
import com.board.whitelabel.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/whitelabel")
public class MemberController {

    private final AuthenticationManager authenticationManager;
    private final MemberService memberService;

    private final CustomOAuth2UserService customOAuth2UserService;


    @GetMapping("/join")
    public String join(@ModelAttribute MemberDTO memberDTO) {
        return "whitelabel/join";
    }

    @PostMapping("/join")
    public String joinProc(@ModelAttribute MemberDTO memberDTO, RedirectAttributes redirectAttributes) {
        log.info("회원가입 요청");
        Member number = memberService.dtoToEntity(memberDTO);

        redirectAttributes.addFlashAttribute("msg", number);
        return "redirect:/whitelabel/loginPage";

    }

    @GetMapping("/checkId")
    @ResponseBody
    public ResponseEntity<?> checkId(@RequestParam String id){
        boolean checkId = memberService.checkId(id);
        return ResponseEntity.ok(checkId);
    }

    @GetMapping("/myPage")
    public String getMyPage(Model model, HttpServletRequest request) {



        HttpSession session = request.getSession();


        if(session == null){

            return "whitelabel/myPage";
        }

        Member member = (Member) session.getAttribute("member");

        model.addAttribute("member", member);


        return "whitelabel/myPage";
    }

    @PostMapping("/myPage")
    public String myPage(MemberDTO memberDTO, Model model) {

        memberService.updateMember(memberDTO);


        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(memberDTO.getLoginId(), memberDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        model.addAttribute("member", memberDTO);

        return "redirect:/whitelabel/logout";

    }

    @GetMapping("/SearchId")
    public String SearchId() {


        return "whitelabel/SearchId";
    }

    @PostMapping("/SearchId")
    public String SearchIdFinal(MemberDTO memberDTO, Model model) {

        int errorMsg = 0;
        Member member = memberService.MemberSearch(memberDTO.getEmail());
        model.addAttribute("member",member);
        if(member == null) {
            errorMsg = 1;
            model.addAttribute("errorMsg",errorMsg);

            return "whitelabel/SearchId";
        }else if(member.getEmail().equals(memberDTO.getEmail())){

            return "whitelabel/SearchIdFinal";
        }
        return null;
    }


    @GetMapping("/SearchPw")
    public String getSearchPw(){

        return "whitelabel/SearchPw";
    }

    @Transactional
    @PostMapping("/SearchPw")
    public String SearchPw(MemberDTO memberDTO, Model model){

        int errMsg = 0;

        Member member = memberService.MemberPwSearch(memberDTO.getLoginId());

        if(member == null){
            errMsg = 1;
            model.addAttribute("errMsg", errMsg);

            return "whitelabel/SearchPw";

        } else if (!member.getEmail().equals(memberDTO.getEmail())) {
            errMsg  = 2;
            model.addAttribute("errMsg", errMsg);

            return "whitelabel/SearchPw";
        }

        MailDTO dto = memberService.createMailAndChangePassword(memberDTO.getEmail());
        memberService.mailSend(dto);

        return "redirect:/whitelabel/loginPage";
    }

    @PostMapping("/sendEmailCheck")
    @ResponseBody
    public ResponseEntity<?> sendEmailCheck(@RequestParam String email){

        boolean emailCheck = memberService.checkEmailSend(email);

        return ResponseEntity.ok(emailCheck);

    }
    @PostMapping("/checkEmailNBFine")
    @ResponseBody
    public ResponseEntity<?> checkEmailNBFine(@RequestParam String emailNB){

        boolean checkEmailNB = memberService.checkEmailNB(emailNB);

        return ResponseEntity.ok(checkEmailNB);

    }

}