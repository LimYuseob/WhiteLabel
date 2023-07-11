package com.board.whitelabel.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.board.whitelabel.dto.DailyMovieInfoDTO;
import com.board.whitelabel.dto.MovieSearchDTO;
import com.board.whitelabel.dto.WLjmtDTO;

import kr.or.kobis.kobisopenapi.consumer.rest.KobisOpenAPIRestService;
import kr.or.kobis.kobisopenapi.consumer.rest.exception.OpenAPIFault;



@Qualifier("getAPI")
@Service
public class GetAPIServiceImpl implements GetAPIService {

	
	private ClientHttpRequestFactory getClientHttpRequestFactory() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
	    org.apache.http.ssl.TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
	    SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
	    SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

	    CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
	    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
	    requestFactory.setHttpClient(httpClient);
	    return requestFactory;
	}
	
	@Override
	public void getAPI(String search, Model model) throws ParseException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

		WLjmtDTO dto = null;
		
		List<WLjmtDTO> result = new ArrayList<>();
		
		String requestResult = search;

		RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
		
		String url = "https://openapi.gg.go.kr/PlaceThatDoATasteyFoodSt";
		UriComponents uri = UriComponentsBuilder.fromHttpUrl(url)
							.queryParam("key", "f787195a59a444c39c7082b5ccbfe611")
							.queryParam("type", "json")
							.queryParam("pIndex", "1")
							.queryParam("pSize", "50")
							.query("SIGUN_NM=" + requestResult)
							.build(false);

		String jsonStr = restTemplate.getForObject(uri.toUriString(), String.class);

		JSONParser jparser = new JSONParser();

		JSONObject jobj = (JSONObject) jparser.parse(jsonStr);

		JSONArray jarr = (JSONArray) jobj.get("PlaceThatDoATasteyFoodSt");
		
		if(jarr == null) {
			result.add(null);
		}else {
		
		for (int i = 1; i < jarr.size(); i++) {

			JSONObject json = (JSONObject) jarr.get(i);

			JSONArray getRow = (JSONArray) json.get("row");
		
			for (int j = 0; j < getRow.size(); j++) {
				JSONObject rowJson = (JSONObject) getRow.get(j);

				dto = WLjmtDTO.builder().SIGUN_NM((String) rowJson.get("SIGUN_NM"))
						.RESTRT_NM((String) rowJson.get("RESTRT_NM"))
						.REPRSNT_FOOD_NM((String) rowJson.get("REPRSNT_FOOD_NM"))
						.TASTFDPLC_TELNO((String) rowJson.get("TASTFDPLC_TELNO"))
						.REFINE_LOTNO_ADDR((String) rowJson.get("REFINE_LOTNO_ADDR"))
						.REFINE_ROADNM_ADDR((String) rowJson.get("REFINE_ROADNM_ADDR"))
						.REFINE_ZIP_CD((String) rowJson.get("REFINE_ZIP_CD"))
						.REFINE_WGS84_LAT((String) rowJson.get("REFINE_WGS84_LAT"))
						.REFINE_WGS84_LOGT((String) rowJson.get("REFINE_WGS84_LOGT"))
						.build();

				result.add(dto);

			}//inner for
		}//outer for
		}//else
		
		model.addAttribute("getAPI", result);
	}//end method

	@Override
	public List<MovieSearchDTO> getMovieSearchAPI(String search) throws ParseException {
        
		List<MovieSearchDTO> list = new ArrayList<>();
		
		String apiKey = "19C1078TR3C3XM50KXZ9";

        // 영화 검색
        String movieUrl = "http://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp?"
        		+ "collection=kmdb_new2"
        		+ "&detail=Y"
        		+ "&title="+ URLEncoder.encode(search) 
                + "&ServiceKey="+apiKey;
                
        String movieResult = getResult(movieUrl);
        
        JSONParser parser = new JSONParser();
        		
        JSONObject jObj = (JSONObject) parser.parse(movieResult);	
        
        JSONArray jArr = (JSONArray) jObj.get("Data");
        
        for(int i = 0; i<jArr.size(); i++) {
        	
        JSONObject arrResult = (JSONObject) jArr.get(i);         
        
        JSONArray result = (JSONArray) arrResult.get("Result");
        
	        for(int j =0; j<result.size();j++) {
	        	
	        	JSONObject arr = (JSONObject) result.get(j);
	        	
	        	String title = (String) arr.get("title");
	        	
	        	String prodYear = (String) arr.get("prodYear");
	        	
	        	String kmurl = (String) arr.get("kmdbUrl");
	        	
	        	String posters = (String) arr.get("posters");
	        	String firstPoster = posters.split("\\|")[0];
	        	
	        	String genre = (String) arr.get("genre");
	        	
	        	String type = (String) arr.get("type");
	        	
	        	JSONObject directors = (JSONObject) arr.get("directors");
	        	
	        	JSONArray director = (JSONArray) directors.get("director");
	        	
	        	String directorNm = null;
	        	
	        	for(int k=0; k < director.size(); k++) {
	        		
	        		JSONObject directorN = (JSONObject) director.get(i);
	        		
	        		directorNm = (String) directorN.get("directorNm");
	        		
	        		title = title.replace("!HS", "").replace("!HE", "");
	        		
	        		MovieSearchDTO dto = MovieSearchDTO.builder()
    						.director(directorNm)
    						.title(title)
    						.link(kmurl)
    						.prodYear(prodYear)
    						.image(firstPoster)
    						.genre(genre)
    						.type(type)
    						.build();
    	
			    	list.add(dto);
			    						
			    	
	    			
	        	}
	        	
	        }
        
        }
        
		return list;

    }

    // HTTP 요청을 보내고 결과를 받아오는 메서드
    private static String getResult(String url) {
        StringBuilder result = new StringBuilder();
        try {
            URL apiUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)apiUrl.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
	
	
	
	
        





        public void getMovieAPI(String daily, Model model) throws ParseException {
            String key = "0d41567f80635e5218dd386316dc590f";
            KobisOpenAPIRestService service = new KobisOpenAPIRestService(key);
            
            
            String  jsonStr = null;
            String   imgfile = null;
            
            try {
               
//               key            문자열(필수)   발급받은키 값을 입력합니다.
//               targetDt      문자열(필수)   조회하고자 하는 날짜를 yyyymmdd 형식으로 입력합니다.
//               itemPerPage      문자열      결과 ROW 의 개수를 지정합니다.(default : “10”, 최대 : “10“)
//               multiMovieYn   문자열      다양성 영화/상업영화를 구분지어 조회할 수 있습니다.
//                                    “Y” : 다양성 영화 “N” : 상업영화 (default : 전체)
//               repNationCd      문자열      한국/외국 영화별로 조회할 수 있습니다.
//                                    “K: : 한국영화 “F” : 외국영화 (default : 전체)
//               wideAreaCd      문자열      상영지역별로 조회할 수 있으며, 지역코드는 공통코드 조회 서비스에서 “0105000000” 로서 조회된 지역코드입니다. (default : 전체)

               jsonStr = service.getDailyBoxOffice(true, daily, "", "", "", "");
            } catch (OpenAPIFault e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            } catch (Exception e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }

            JSONParser jparser = new JSONParser();

            JSONObject jobj    = (JSONObject)   jparser.parse(jsonStr);
            JSONObject jarrfir    = (JSONObject)   jobj.get("boxOfficeResult");
            JSONArray jarr       = (JSONArray)    jarrfir.get("dailyBoxOfficeList");

            List<DailyMovieInfoDTO> result = new ArrayList<>();
            

            for (int i = 0; i < jarr.size(); i++) {

               JSONObject json = (JSONObject) jarr.get(i);
               
               StringBuilder urlBuilder = new StringBuilder("http://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp?collection=kmdb_new2&ServiceKey=");

               try {
                  urlBuilder.append(URLEncoder.encode("19C1078TR3C3XM50KXZ9","UTF-8"));
                  urlBuilder.append("&" + URLEncoder.encode("title","UTF-8") + "=" + URLEncoder.encode((String) json.get("movieNm"), "UTF-8"));
                                                                           
                  URL url;
                  try {
                     url = new URL(urlBuilder.toString());
                     HttpURLConnection conn;
                     try {
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Content-type", "application/json");
                        BufferedReader rd;
                        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300)
                        { 
                           rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        } else { 
                           rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                        } 
                        StringBuilder    sb       = new StringBuilder();
                        String          line;
                        
                        while ((line = rd.readLine()) != null) { 
                           sb.append(line);
                        } 
                        
                        rd.close();
                        conn.disconnect();
                        
                        String   str   =   sb.toString();
                        
                        int pindex   =   str.indexOf("posters");
                        
                        if (pindex >= 0) {
                           String   str2   =   str.substring(pindex + 10);
                           
                           int    delimiterIndex    =    str2.indexOf("\"");
                           String   poster         =   str2.substring(0, delimiterIndex);
                           
                           if (poster.length() > 0) {
                              
                              int delimiterIndex2   =   poster.toLowerCase().indexOf("jpg");
                              imgfile            =   poster.substring(0,delimiterIndex2 + 3);
                           }
                           else { imgfile = "";}
                           
                           
                        }else { imgfile = "";}      
                     
                     } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                     }
                     
                  } catch (MalformedURLException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                  } 
                  
               } catch (UnsupportedEncodingException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
               } 
               
               

               //======================================================================================
            
//               System.out.println("[[[movie_serch]]] : " + title + ":" + link + ":" + image + ":" + director);
               
               DecimalFormat df = new DecimalFormat("#,###,###,###,###,###");
               
               
               
               DailyMovieInfoDTO dto = DailyMovieInfoDTO.builder()
                           .showRange      ((String) json.get("showRange"))
                           .rank         ((String) json.get("rank"))
                           .movieNm      ((String) json.get("movieNm"))
                           .openDt         ((String) json.get("openDt"))
                           .salesAmt      ((String) df.format(Long.parseLong((String)json.get("salesAmt"))))
                           .salesAcc      ((String) df.format(Long.parseLong((String)json.get("salesAcc"))))
                           .audiCnt      ((String) json.get("audiCnt"))
                           .audiAcc      ((String) json.get("audiAcc"))
//                           .title         (title)      
                           .image         (imgfile)
//                           .link         (link)
//                           .director      (director)
                           .build();


               
               
               result.add(dto);
               model.addAttribute("getmovieAPI", result);
               


            }
         
            
         }
        
       
}
