package com.board.whitelabel.repository;

import com.board.whitelabel.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SNSMemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByEmail(String email);
}
