package com.board.whitelabel.dto;


import lombok.*;

@Builder
@Data
@ToString
public class MemberDTO {
    private String loginId;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String addr;
    private String detailaddr;
    private String path;
    private String recomm;

    private String role;

}