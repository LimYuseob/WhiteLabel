package com.board.whitelabel.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.board.whitelabel.dto.SkySearchResultDTO;

@Service
public interface SkyService {

	List<SkySearchResultDTO> getSky(String date,  String cabinClass, String adult, String origin, String destination, Model model) throws Exception;
	
	
	
}
