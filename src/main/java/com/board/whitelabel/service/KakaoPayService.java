package com.board.whitelabel.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.board.whitelabel.dto.KakaoPayApprovalDTO;
import com.board.whitelabel.dto.KakaoPayReadyDTO;
import com.board.whitelabel.dto.SkyAirTicketDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KakaoPayService {

private static final String HOST = "https://kapi.kakao.com";
    
    private KakaoPayReadyDTO kakaoPayReadyDTO;
    private KakaoPayApprovalDTO kakaoPayApprovalDTO;
    
    public String kakaoPayReady(SkyAirTicketDTO dto) throws JsonProcessingException {
 
        RestTemplate restTemplate = new RestTemplate();
        
        //결제 금액 변환
        double exchange = (Double.parseDouble(dto.getPrice()) * 1300);
        String exchangeStr = String.valueOf(exchange);
        String integerPrice = exchangeStr.substring(0, exchangeStr.indexOf('.'));
        
        SkyAirTicketDTO passenger = SkyAirTicketDTO.builder()
        							.depCode(dto.getDepCode())
        							.name(dto.getName())
        							.arrDate(dto.getArrDate())
        							.user(dto.getUser())
        							.passport(dto.getPassport())
        							.viaDateTime(dto.getViaCode() +":"+ dto.getViaDateTime())
        							.nationality(dto.getNationality())
        							.gender(dto.getGender())
        							.dob(dto.getDob())
        							.build();
        
        ObjectMapper objectMapper = new ObjectMapper();
        
        String passengerJson = objectMapper.writeValueAsString(passenger);
        
        							
        							
        
        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + "2b919d7c5c41ed7dad4a4bbaafbe6469");
        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
        
        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", "jisay");
        params.add("item_name", dto.getName() + "(" +dto.getDepCode() + "->"+ dto.getArrCode() +")");
        params.add("quantity", String.valueOf(dto.getAdults()));
        params.add("total_amount", String.valueOf(integerPrice));
        params.add("tax_free_amount", String.valueOf(integerPrice));
        params.add("payload", passengerJson);
        params.add("approval_url", "http://localhost:8080/whitelabel/kakaoPaySuccess");
        params.add("cancel_url", "http://localhost:8080/whitelabel/kakaoPayCancel");
        params.add("fail_url", "http://localhost:8080/whitelabel/kakaoPaySuccessFail");
 
        System.out.println("paramMap===="+params.toString());
        
        System.out.println("price======="+String.valueOf(integerPrice));
        
         HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);
 
        try {
            kakaoPayReadyDTO = restTemplate.postForObject(new URI(HOST + "/v1/payment/ready"), body, KakaoPayReadyDTO.class);
            
            System.out.println("" + kakaoPayReadyDTO);
            
            return kakaoPayReadyDTO.getNext_redirect_pc_url();
 
        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "/pay";
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "/pay";
        }
        
       
    }
    public KakaoPayApprovalDTO kakaoPayInfo(String pg_token) {
    	 
        
        RestTemplate restTemplate = new RestTemplate();
 
        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + "2b919d7c5c41ed7dad4a4bbaafbe6469");
        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
 
        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", kakaoPayReadyDTO.getTid());
        
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", "jisay");
        params.add("pg_token", pg_token);
        //params.add("total_amount", "2100");
        
        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        
        try {
            kakaoPayApprovalDTO = restTemplate.postForObject(new URI(HOST + "/v1/payment/approve"), body, KakaoPayApprovalDTO.class);
          
            return kakaoPayApprovalDTO;
        
        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
    
}
