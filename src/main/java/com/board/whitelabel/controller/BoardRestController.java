package com.board.whitelabel.controller;

import com.board.whitelabel.dto.BoardDTO;
import com.board.whitelabel.dto.PageRequestDTO;
import com.board.whitelabel.entity.Board;
import com.board.whitelabel.entity.Member;
import com.board.whitelabel.repository.BoardRepository;
import com.board.whitelabel.service.BoardService;
import com.board.whitelabel.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/whitelabel")
@RequiredArgsConstructor
public class BoardRestController {

    private final BoardService service;
    private final LoginService loginService;
    private final BoardRepository boardRepository;
    @GetMapping("/modify")
    public String getModify(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long bno, Model model
            , HttpServletRequest request) {

        HttpSession session = request.getSession();
        Object[] result = (Object[]) boardRepository.getBoardByBno(bno);
        Board board = (Board) result[0];
        Member memberRS = (Member) result[1];
        String dbid = memberRS.getEmail();

        if (session.getAttribute("member") == null) {
            return "<script type='text/javascript'>"
                    + "alert('권한이 없습니다.');"
                    + "location.href = document.referrer;"
                    +"</script>";
        }

        Member member = (Member) session.getAttribute("member");

        System.out.println("member=="+member);

        model.addAttribute("member", member);

        String check = member.getEmail();

        if (check.equals(dbid)) {
            // 작성자 정보 반환

            BoardDTO boardDTO = service.read(bno);

            model.addAttribute("dto",boardDTO);

            return "<script>window.location.href='/modify'</script>";

        }
        // 작성자 정보 불일치
        return "redirect:/whitelabel/board";


    }

}