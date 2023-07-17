package com.board.whitelabel.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;

import com.board.whitelabel.dto.SkySearchResultDTO;
import com.board.whitelabel.repository.SkyRervRepository;

@Service
public class SkyServiceImpl implements SkyService {
	
	@Autowired
	private SkyRervRepository skyRervRepository;
	
	@Autowired
	private SkyRervService skyRervService;

	@Override
	public List<SkySearchResultDTO> getSky(String date,  String cabinClass, String adult, String origin, String destination, Model model) throws Exception {
		
		String resultName = null;//airline 코드 json 변환 dto 저장용
		
		SkySearchResultDTO dto = null;
		
		List<String> viaList = new ArrayList<>();
		
		List<SkySearchResultDTO> list = new ArrayList<>();
		
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://priceline-com-provider.p.rapidapi.com/v2/flight/departures?"
						+ "adults="+adult
						+ "&departure_date="+date
						+ "&sid=iSiX639"
						+ "&results_per_page=10"
						+ "&number_of_itineraries=10"
						+ "&cabin_class="+cabinClass
						+ "&origin_airport_code="+origin
						+ "&destination_airport_code="+destination))
				.header("X-RapidAPI-Key", "3690ee9c27mshc979a1e839677f1p14c4cfjsn275ce096c6b6")
				.header("X-RapidAPI-Host", "priceline-com-provider.p.rapidapi.com")
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
				HttpResponse<InputStream> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofInputStream());
				InputStream inputStream = response.body();
		// check if response is compressed with gzip
		if (response.headers().firstValue("Content-Encoding").orElse("").equalsIgnoreCase("gzip")) {
		    inputStream = new GZIPInputStream(inputStream);
		}

		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) != -1) {
		    result.write(buffer, 0, length);
		}

		String responseBody = result.toString(StandardCharsets.UTF_8);
		
		JSONParser parser = new JSONParser();
		
		JSONObject jObject = (JSONObject) parser.parse(responseBody);
		
		JSONObject content = (JSONObject) jObject.get("getAirFlightDepartures");
		
		JSONObject results = (JSONObject) content.get("results");
		
		System.out.println("results json==="+results);
		
		if(results == null) {
			list.add(dto);
			
			return list;
		}else {
		
		JSONObject result_data = (JSONObject) results.get("result");
		
		JSONObject itinerary_data = (JSONObject) result_data.get("itinerary_data");
		
		System.out.println("itinerary size-==="+itinerary_data.size());
		
		Object viaCode;
		
		Object viaDateTime;
		
		for (int i = 0; i < itinerary_data.size(); i++) {
		    JSONObject itinerary = (JSONObject)itinerary_data.get("itinerary_" + i);
		    //price
		    JSONObject pricedetail = (JSONObject)itinerary.get("price_details");
		    Object display_total_fare_per_ticket = pricedetail.get("display_total_fare_per_ticket");
		    
		    //slice
		    JSONObject slice = (JSONObject)itinerary.get("slice_data");
		    
		    //slice_0
		    JSONObject sl = (JSONObject) slice.get("slice_0");
		    
		    //info
		    JSONObject infomation = (JSONObject) sl.get("info");
		    Object duration = infomation.get("duration");
		    
		    //airLine
		    JSONObject airline = (JSONObject) sl.get("airline");
		    Object airName = airline.get("code");
		    Object logo = airline.get("logo");
		    
		    //departure
		    JSONObject departure = (JSONObject) sl.get("departure");
		    JSONObject airport1 = (JSONObject) departure.get("airport");
		    Object depCode = airport1.get("code");
		    JSONObject dateTime1 = (JSONObject) departure.get("datetime");
		    Object dTime1 = dateTime1.get("date_time");
		    
		    //arrival
		    JSONObject arrival = (JSONObject) sl.get("arrival");
		    JSONObject airport2 = (JSONObject) arrival.get("airport");
		    Object arrCode = airport2.get("code");
		    JSONObject dateTime2 = (JSONObject) arrival.get("datetime");
		    Object dTime2 = dateTime2.get("date_time");
		    
		  //flight
		    JSONObject flight = (JSONObject) sl.get("flight_data");
		    JSONObject flight_1 = (JSONObject) flight.get("flight_1");
		    
		    if(flight_1 == null) {
		    	viaCode="직항";
		    	viaDateTime="직항";
		    } else {
		    JSONObject viadeparture = (JSONObject) flight_1.get("departure");
		    JSONObject viairport = (JSONObject) viadeparture.get("airport");
		    viaCode = viairport.get("code");
		    
		    JSONObject viaTime = (JSONObject) viadeparture.get("datetime"); 
		    viaDateTime = viaTime.get("date_time");
		    
		    
		    }
		    
		    System.out.println("viacode==="+viaCode);
		    System.out.println("viadate==="+viaDateTime);
		    
		    //항공사 코드 변환
		    ClassPathResource resource = new ClassPathResource("iata.json");
			
			String jsonStr = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
			
			String search = (String) airName; // 검색어
	        
	        JSONParser parser1 = new JSONParser();
	        
	        try {
	            JSONArray array = (JSONArray) parser1.parse(jsonStr); // JSON 파일 읽기
	            
	            for (Object obj : array) {
	                JSONObject airline1 = (JSONObject) obj;
	                String name = (String) airline1.get("영문명"); // "영문명" 가져오기
	                String iataCode = (String) airline1.get("IATA_CODE"); // "IATA_CODE" 가져오기
	                
	                if (iataCode.equals(search)) { // 검색어와 일치하는 경우
	                    resultName = name; // 결과 출력
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		    
	      
		    if(logo != null) {
		    dto = SkySearchResultDTO.builder()
    			  .airLineName(resultName)
    			  .logo(String.valueOf(logo))
    			  .price(String.valueOf(display_total_fare_per_ticket))
    			  .duration(String.valueOf(duration))
    			  .depDate(String.valueOf(dTime1))
    			  .depCode(String.valueOf(depCode))
    			  .arrDate(String.valueOf(dTime2))
    			  .arrCode(String.valueOf(arrCode))
    			  .viaCode(String.valueOf(viaCode))
    			  .viaDateTime(String.valueOf(viaDateTime))
    			  .build();
		    	
		    
		    
		    list.add(dto);
		    }
		    
			}
		}
		return list;
	}
	
	

}
