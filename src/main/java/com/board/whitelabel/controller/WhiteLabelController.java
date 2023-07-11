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

import com.board.whitelabel.entity.Member;

@Controller
@RequestMapping("/whitelabel")
@RequiredArgsConstructor
public class WhiteLabelController {

	@Qualifier("getAPI")
	@Autowired(required = true)
	private GetAPIService getAPIService;
	
	@GetMapping({"/index","/"})
	public String index(){
		
		return "whitelabel/index";
	}
	
	@GetMapping("/listGuest")
	public void list() {

	}
	
	@PostMapping("/listGuest")
	public void getList() {
		
	}

	@GetMapping("/listAPI")
	public void jmt(@RequestParam("search") String search, Model model) throws ParseException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException{
	       
		getAPIService.getAPI(search, model);
			
	}
	
	@GetMapping("/redirect")
	public String redirect() {
		return "redirect:/whitelabel/listGuest";
	}
	
	@GetMapping("/mapTest")
	public void map() {
		
	}
	
	@GetMapping("/movieDetail")
	public void getMovie() {
		
	}
	
	@PostMapping("/movieDetail")
	public void geMovie(Model model) throws ParseException {
		
		Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String getDate = formatter.format(date);
        String date1 = String.valueOf((Long.parseLong(getDate) - 1));
        
		getAPIService.getMovieAPI(date1, model);
	}
	
	@GetMapping("/naverSearch")
	public void movieSearch(@RequestParam("search") String search, Model model) throws ParseException{
		
		model.addAttribute("getMovieAPI", getAPIService.getMovieSearchAPI(search));
		
	}
	
	@PostMapping("/detailPage")
	public void movie(@RequestParam("search") String search, Model model) throws ParseException {
		
		DateFormat df 	= new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df2	= new SimpleDateFormat("yyyyMMdd");
		
		try {
			Date d = df.parse(search);
			String s_daily = df2.format(d);
			System.out.println("날짜변환=====: " + s_daily);
			getAPIService.getMovieAPI(s_daily, model);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	 @PostMapping("/")
	    public String home(Member loginMember, Model model, HttpServletRequest request) {
	        if(loginMember == null) {
	        	System.out.println("loginMember == nul ");
	            return "whitelabel/index";
	        }
	        //세션이 유지되면 다시 홈으로 보낸다.
	        model.addAttribute("member",loginMember);

	        //어노테이션 없는 일반 세션 코드 적용..
	        HttpSession session = request.getSession(false);
	        if(session!= null) {
	        	System.out.println("session!=null");
	            return "whitelabel/index";
	        }

	        return "whitelabel/index";
	    }
	 
	 
	 
}
