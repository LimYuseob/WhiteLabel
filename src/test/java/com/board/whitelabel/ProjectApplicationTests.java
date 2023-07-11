package com.board.whitelabel;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.board.whitelabel.repository.BoardRepository;

@SpringBootTest
class ProjectApplicationTests {

	@Autowired
	private BoardRepository boardRepository;
	
    @Test
    void contextLoads() {
    	
    	
    }

}
