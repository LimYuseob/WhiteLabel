package com.board.whitelabel.service;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.board.whitelabel.dto.MovieSearchDTO;

@Service
public interface GetAPIService {

	
	void getAPI(String search, Model model) throws ParseException,KeyManagementException, NoSuchAlgorithmException, KeyStoreException;
	
	List<MovieSearchDTO> getMovieSearchAPI(String search) throws ParseException;
	
	void getMovieAPI(String search, Model model) throws ParseException;
		
	
}
