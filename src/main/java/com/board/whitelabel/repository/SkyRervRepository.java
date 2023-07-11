package com.board.whitelabel.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.whitelabel.entity.SkyRerv;

public interface SkyRervRepository extends JpaRepository<SkyRerv, Long> {

	 List<SkyRerv> findByUserNameAndPassport(String userName, String passport);
	 
	 
}
