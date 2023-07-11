package com.board.whitelabel.service;

import java.util.List;

import com.board.whitelabel.dto.GtpDTO;
import com.board.whitelabel.dto.GtpDTO2;


public interface GtpApiService {
	public  String callChatGPT(String prompt);
	List<GtpDTO> callblog(GtpDTO2 dto2);
	

	
}
