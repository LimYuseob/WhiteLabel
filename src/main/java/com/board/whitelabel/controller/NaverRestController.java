package com.board.whitelabel.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.board.whitelabel.entity.Member;
import com.board.whitelabel.entity.MemberRole;
import com.board.whitelabel.repository.MemberRepository;

@RestController
@RequestMapping("/whitelabel")
public class NaverRestController {
	
	@Autowired
	MemberRepository memberRepository;

	@PostMapping("/naverAjax")
	public ResponseEntity<String> handleAjaxRequest(@RequestBody Map<String, String> data) throws ParseException {
	    String fragment = data.get("fragment");
	    // fragment 값을 이용하여 처리하는 로직 작성
	    System.out.println("fragment"+fragment);
	    
	 // fragment 값을 URI로 변환하여 쿼리 스트링을 추출
	    String queryString = null;
	    try {
	        URI uri = new URI("http://example.com/?" + fragment);
	        queryString = uri.getQuery();
	    } catch (URISyntaxException e) {
	        // URI 구문 오류 발생 시 처리
	        e.printStackTrace();
	    }

	    // 추출한 쿼리 스트링을 맵으로 변환
	    Map<String, String> paramMap = new HashMap<>();
	    if (queryString != null) {
	        String[] params = queryString.split("&");
	        for (String param : params) {
	            String[] keyValue = param.split("=");
	            if (keyValue.length == 2) {
	                paramMap.put(keyValue[0], keyValue[1]);
	            }
	        }
	    }

	    // 맵에 저장된 값을 사용하여 로직 처리
	    String accessToken = paramMap.get("access_token");
	    
	    String token = accessToken; // 네이버 로그인 접근 토큰;
        String header = "Bearer " + token; // Bearer 다음에 공백 추가

        String apiURL = "https://openapi.naver.com/v1/nid/me";

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        String responseBody = get(apiURL,requestHeaders);

        JSONParser parser = new JSONParser();
        
        JSONObject response = (JSONObject) parser.parse(responseBody);
        
        JSONObject obj = (JSONObject) response.get("response");
        
        String nEmail = (String) obj.get("email");
        String nMobile = (String) obj.get("mobile");
        String nName = (String) obj.get("name");
        
        Properties props = new Properties();
        props.setProperty("name", nName);
        
        String decodeName = props.getProperty("name");
        
        System.out.println(nEmail);
        System.out.println(nMobile);
        System.out.println(decodeName);
        
        Member member = Member.builder()
        		.email(nEmail)
        		.name(decodeName)
        		.loginId(nEmail)
        		.phone(nMobile)
        		.role(MemberRole.ADMIN.getValue())
        		.build();
        
	    memberRepository.save(member);

	    return ResponseEntity.ok("Success");
	  }
	
	
	
	
	
	 private static String get(String apiUrl, Map<String, String> requestHeaders){
	        HttpURLConnection con = connect(apiUrl);
	        try {
	            con.setRequestMethod("GET");
	            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
	                con.setRequestProperty(header.getKey(), header.getValue());
	            }


	            int responseCode = con.getResponseCode();
	            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
	                return readBody(con.getInputStream());
	            } else { // 에러 발생
	                return readBody(con.getErrorStream());
	            }
	        } catch (IOException e) {
	            throw new RuntimeException("API 요청과 응답 실패", e);
	        } finally {
	            con.disconnect();
	        }
	    }


	    private static HttpURLConnection connect(String apiUrl){
	        try {
	            URL url = new URL(apiUrl);
	            return (HttpURLConnection)url.openConnection();
	        } catch (MalformedURLException e) {
	            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
	        } catch (IOException e) {
	            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
	        }
	    }


	    private static String readBody(InputStream body){
	        InputStreamReader streamReader = new InputStreamReader(body);


	        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
	            StringBuilder responseBody = new StringBuilder();


	            String line;
	            while ((line = lineReader.readLine()) != null) {
	                responseBody.append(line);
	            }


	            return responseBody.toString();
	        } catch (IOException e) {
	            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
	        }
	    }
}
