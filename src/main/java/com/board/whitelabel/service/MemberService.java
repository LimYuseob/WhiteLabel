package com.board.whitelabel.service;

import com.board.whitelabel.dto.MailDTO;
import com.board.whitelabel.dto.MemberAdapter;
import com.board.whitelabel.dto.MemberDTO;
import com.board.whitelabel.entity.Member;
import com.board.whitelabel.entity.MemberRole;
import com.board.whitelabel.repository.MemberRepository;
import lombok.RequiredArgsConstructor;


import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    private final MailSender mailSender;

    public Member dtoToEntity(MemberDTO memberDTO){


        Member checkMember = memberRepository.findByEmail(memberDTO.getEmail());

        if(checkMember != null){

            updateMember(memberDTO);

            return checkMember;
        }



        Member member = Member.builder()
                .loginId(memberDTO.getLoginId())
                .password(passwordEncoder.encode(memberDTO.getPassword()))
                .name(memberDTO.getName())
                .email(memberDTO.getEmail())
                .phone(memberDTO.getPhone())
                .addr(memberDTO.getAddr())
                .detailaddr(memberDTO.getDetailaddr())
                .role(MemberRole.ADMIN.getValue())
                .build();


        memberRepository.save(member);

        return member;

    }
    
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        Member member = memberRepository.findByLoginId(loginId);


        return new MemberAdapter(member);
    }

    @Transactional
    public Member updateMember(MemberDTO memberDTO){

        Member member = memberRepository.findByEmail(memberDTO.getEmail());

        member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        member.setName(memberDTO.getName());
        member.setEmail(memberDTO.getEmail());
        member.setPhone(memberDTO.getPhone());
        member.setAddr(memberDTO.getAddr());
        member.setDetailaddr(memberDTO.getDetailaddr());
        member.setRole(MemberRole.ADMIN.getValue());

        memberRepository.save(member);


        return member;
    }

    public Member MemberSearch(String email){

        Member member = memberRepository.findByEmail(email);

        return member;
    }

    public Member MemberPwSearch(String loginId){


          Member member = memberRepository.findByLoginId(loginId);

        return member;
    }

    // 메일 내용을 생성하고 임시 비밀번호로 회원 비밀번호를 변경
    public MailDTO createMailAndChangePassword(String email){
        String str = getTempPassword();
        MailDTO dto = new MailDTO();
        dto.setAddress(email);
        dto.setTitle("안녕하세요. Whitelabel 입니다. 임시비밀번호 안내 드립니다.");
        dto.setMessage("안녕하세요. Whitelabel 입니다. 임시비밀번호 안내 드립니다. 회원님의 임시 비밀번호는 "
         + str + " 입니다. 로그인 후에 MyPage에서 비밀번호 변경을 해주세요.");
        updatePassword(str,email);
        return dto;
    }

    //임시 비밀번호로 업데이트
    public void updatePassword(String str, String email){
        String memberPassword = str;
        Member member = memberRepository.findByEmail(email);
        member.setPassword(passwordEncoder.encode(memberPassword));
    }

    //랜덤함수로 임시비밀번호 구문 만들기
    public String getTempPassword(){
        char[] charSet = new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
        ,'G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};

        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++){
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;

    }

    //메일보내기
    public void mailSend(MailDTO mailDTO){
        System.out.println("임시 비밀번호 메일로 발송 완료");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDTO.getAddress());
        message.setSubject(mailDTO.getTitle());
        message.setText(mailDTO.getMessage());
        message.setFrom("dladbtjq@naver.com");
        message.setReplyTo("dladbtjq@naver.com");
        System.out.println("message"+message);
        mailSender.send(message);

    }

}