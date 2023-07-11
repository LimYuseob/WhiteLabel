package com.board.whitelabel.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
@Table(name = "MEMBER")
public class Member extends BaseEntity implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;
    private String loginId;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String addr;
    private String detailaddr;

    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
        auth.add(new SimpleGrantedAuthority(role));
        return auth;
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


//    @ElementCollection(fetch = FetchType.LAZY)
//    //위에서처럼 Collection 으로 생성시엔 반드시 Builder.default() @ 을 사용해야 합니다.
//    @Builder.Default
//    //사용자 권한 값을 담는 Set 객체 생성
//    private Set<MemberRole> roleset = new HashSet<>();
//
//    //멤버에 권한부여 메서드 정의
//    public void addMemberRole(MemberRole memberRole){
//        roleset.add(memberRole);
//    }

    public Member(String loginId, String password, String name,String email
            , String addr,String detailaddr,String role){
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.addr = addr;
        this.detailaddr = detailaddr;
        this.role = role;

    }
    public Member update(String name){
        this.name = name;
        return this;
    }
}