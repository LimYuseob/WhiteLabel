package com.board.whitelabel.controller;




import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.board.whitelabel.service.GetAPIService;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class WhiteLabelController {

	@Qualifier("getAPI")
	@Autowired(required = true)
	private GetAPIService getAPIService;

	@GetMapping
	public String index(){

		return "index";
	}

	@GetMapping("listGuest")
	public String getList(){

		return "whitelabel/listGuest";
	}

	@GetMapping("listAPI")
	public String jmt(@RequestParam("search") String search, Model model) throws ParseException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException{

		getAPIService.getAPI(search, model);

		return "whitelabel/listAPI";

	}

	@GetMapping("whitelabel/redirect")
	public String redirect() {
		return "redirect:/listGuest";
	}

	@GetMapping("mapTest")
	public String map() {

		return "whitelabel/mapTest";
	}

	@GetMapping("movieDetail")
	public void getMovie(){


	}

	@PostMapping("movieDetail")
	public String geMovie(Model model) throws ParseException {

		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String getDate = formatter.format(date);
		String date1 = String.valueOf((Long.parseLong(getDate) - 1));

		getAPIService.getMovieAPI(date1, model);

		return "whitelabel/movieDetail";
	}

	@GetMapping("naverSearch")
	public String movieSearch(@RequestParam("search") String search, Model model) throws ParseException{

		model.addAttribute("getMovieAPI", getAPIService.getMovieSearchAPI(search));

		return "whitelabel/naverSearch";

	}

	@PostMapping("detailPage")
	public String movie(@RequestParam("searchDay") String searchDay, Model model) throws ParseException {

		DateFormat df 	= new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df2	= new SimpleDateFormat("yyyyMMdd");

		try {
			Date d = df.parse(searchDay);
			String s_daily = df2.format(d);
			System.out.println("날짜변환=====: " + s_daily);
			getAPIService.getMovieAPI(s_daily, model);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "whitelabel/detailPage";
	}



}
