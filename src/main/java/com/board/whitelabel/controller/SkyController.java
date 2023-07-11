package com.board.whitelabel.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.board.whitelabel.dto.KakaoPayApprovalDTO;
import com.board.whitelabel.dto.ModifyDTO;
import com.board.whitelabel.dto.SkyAirTicketDTO;
import com.board.whitelabel.dto.SkySearchResultDTO;
import com.board.whitelabel.entity.SkyRerv;
import com.board.whitelabel.service.KakaoPayService;
import com.board.whitelabel.service.SkyRervService;
import com.board.whitelabel.service.SkyService;
import com.fasterxml.jackson.core.JsonProcessingException;

@Controller
@RequestMapping("/whitelabel")
public class SkyController {

	@Autowired
	private SkyService skyService;
	
	@Autowired
	private SkyRervService skyRervService;
	
	@Autowired
	private KakaoPayService kakaoPayService;
	
	@GetMapping("/skySearch2")
	private void skySearch() {
		// TODO Auto-generated method stub

	}
	
	
	@GetMapping("/skyResult2")
	public void resultGet() {
		
	}
	
	@PostMapping("/skyResult")
	private void sky(@RequestParam("departureDate") String date,
					 @RequestParam("cabinClass") String cabinClass, 
					 @RequestParam("adults") String adult,
					 @RequestParam("origin") String origin,
					 @RequestParam("destination") String destination,
					 Model model) throws Exception {
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		Date d = df.parse(date);
		date = df.format(d);
		List<SkySearchResultDTO> list = skyService.getSky(date, cabinClass, adult, origin, destination, model);

			System.out.println("list------->"+ list);
			model.addAttribute("sky", list);
		
		
	}
	
	
	@GetMapping("/skyPayment")
	private void pay() {
		// TODO Auto-generated method stub
	}
	
	@GetMapping("/kakaoPay")
	public String payGet(@ModelAttribute SkyAirTicketDTO dto, HttpSession session) throws JsonProcessingException{
		
		session.setAttribute("getsession", dto);
		String url = kakaoPayService.kakaoPayReady(dto);
		return "redirect:"+url;
	}
	
	@GetMapping("/kakaoPaySuccess")
    public void kakaoPaySuccess(@RequestParam("pg_token") String pg_token, HttpSession session, Model model) {
        
		SkyAirTicketDTO dto = (SkyAirTicketDTO) session.getAttribute("getsession");
		List<SkyAirTicketDTO> list = new ArrayList<>();
		list.add(dto);
		
		KakaoPayApprovalDTO kDto = kakaoPayService.kakaoPayInfo(pg_token);
		int getTotal = kDto.getAmount().getTotal();
		kDto.setTotal_price(getTotal);
		
		skyRervService.getRerv(dto, kDto);
		
        model.addAttribute("payinfo", kDto);
        model.addAttribute("info", list);
        System.out.println(model.toString());
        
    }
	
	@GetMapping("/reservation")
	public void selectPassenger(@RequestParam("name") String name, @RequestParam("passport") String passport, Model model) {
		
		List<SkyRerv> rerv = skyRervService.getPassenger(name, passport, model);
		
		model.addAttribute("reservationList", rerv); 
			
	}
	
	@GetMapping("/detail")
	public String showDetail(@RequestParam("ono") Long ono, Model model) {
		
		System.out.println("ono"+ono);
	    SkyRerv reservation = skyRervService.getReservation(ono);
	    model.addAttribute("reservation", reservation);
	    
	    return "/whitelabel/detail";
	    		
	}
	
	@PostMapping("/rervRemove")
	public String remove(Long ono) {
		
		skyRervService.remove(ono);
		  
		return "redirect:/whitelabel/skySearch2";
	}
	
	@PostMapping("/update")
	public String modify(@ModelAttribute ModifyDTO dto) {
		System.out.println("update호출");
		skyRervService.modify(dto);
		  
		return "redirect:/whitelabel/skySearch2";
	}
	
	@GetMapping("/redirect1")
	public String redirect1(Model model) {
		
		return "redirect:/whitelabel/skySearch2";
	}
	
	@GetMapping("/search")
	public void passSearch() {
		
	}
	
	@GetMapping("/skyResult")
	public void getResu() {
		
	}
}
