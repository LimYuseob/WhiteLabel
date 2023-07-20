package com.board.whitelabel.service;

import com.board.whitelabel.dto.OAuthAttributes;
import com.board.whitelabel.entity.Member;
import com.board.whitelabel.repository.SNSMemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final SNSMemberRepository snsMemberRepository;
    private final HttpSession httpSession;


    public CustomOAuth2UserService(SNSMemberRepository snsMemberRepository, HttpSession httpSession) {
        this.snsMemberRepository = snsMemberRepository;
        this.httpSession = httpSession;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName
                , oAuth2User.getAttributes());

        Member member = saveOrUpdate(attributes);
        httpSession.setAttribute("member", member);



        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(member.getRole())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
                );
    }

    private Member saveOrUpdate(OAuthAttributes attributes){
        Member member = snsMemberRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName()))
                .orElse(attributes.toEntity());


        return snsMemberRepository.save(member);
    }
}
