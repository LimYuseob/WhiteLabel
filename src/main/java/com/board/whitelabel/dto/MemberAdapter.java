package com.board.whitelabel.dto;

import com.board.whitelabel.entity.Member;
import lombok.Getter;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class MemberAdapter extends User{

    private Member member;


    public MemberAdapter(Member member) {
        super(member.getLoginId(),member.getPassword(), List.of(new SimpleGrantedAuthority(member.getRole())));
        this.member = member;

    }

}