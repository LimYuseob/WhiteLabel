package com.board.whitelabel.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.board.whitelabel.dto.GtpDTO;
import com.board.whitelabel.dto.GtpDTO2;



@Primary
@Service
@ComponentScan
public class GtpApiServiceImpl implements GtpApiService{
   
   GtpDTO gtpDTO = null; 
   @Override
   public String callChatGPT(String prompt) {
      
       try {
               URL url = new URL("https://api.openai.com/v1/completions");
               HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
               httpURLConnection.setRequestMethod("POST");
               httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
               httpURLConnection.setRequestProperty("Authorization", "Bearer " + "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");
               httpURLConnection.setDoOutput(true);

               String input = "{\"model\": \"text-davinci-003\", \"prompt\": \"" + prompt + "\",\"max_tokens\" : 1024}";

               try (OutputStream os = httpURLConnection.getOutputStream()) {
                   byte[] inputBytes = input.getBytes("utf-8");
                   os.write(inputBytes, 0, inputBytes.length);
               }

               int resCode = httpURLConnection.getResponseCode();
               System.out.println("resCode:" + resCode);

               try (BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"))) {
                   StringBuilder res = new StringBuilder();
                   String resLine = null;

                   while ((resLine = br.readLine()) != null) {
                       res.append(resLine.trim());
                   }

                   return (res.toString());
               }
           } catch (Exception e) {
               e.printStackTrace();
           }

           return "";
   }

   @Override
   public List<GtpDTO> callblog(GtpDTO2 dto2) {
      List<GtpDTO> list= new ArrayList<>();
      String text2="";
      System.out.println("디티오겟"+dto2);
      String a="아래 단서로 음식 리뷰 써줘 내가 다녀온 식당 이름은"+dto2.getK1()+"이고"+"음식 이름은"+dto2.getK2()+"음식의 종류는"+dto2.getK3()+"이였는데 가격은 "+dto2.getK4()+"가게의 분위기는 "+dto2.getK5()+"했고 최종적으로 가계를 다녀온 나의 느낌은 "+dto2.getK6()+"글 분위기는 친절한 느낌으로 길게 음식 리뷰를 작성해줘";
      //String a="음식리뷰 자세하게 길게 써줘 글 분위기는 불쾌한느낌이 들도록 음식이름은 짬뽕 이고 종류는 중식 음식의 맛을 별로였고 모양도 이쁘지 않았어 식당의 서비스는 불친절했고 가격은 비쌌어 분위기는 어두워서 창고같은 느낌이였어 최종적으로 내 느낌은 다시는 가고 싶지않아 추천하지 않아 위의 정보를 갖고 길고 자세하게 리뷰 글을 써줘";
      String res = callChatGPT(a);
        System.out.println("JJHHN(((((((((((((((((((((((((((((("+res);
        JSONParser jsonParser = new JSONParser();

        try {
           Object obj = jsonParser.parse(res);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray  jarr      = (JSONArray)  jsonObject.get("choices");
            JSONObject json = (JSONObject) jarr.get(0);
           System.out.println("@@@@"+jarr);
           
            System.out.println(json);
            
            
    
            GtpDTO dto = GtpDTO.builder()
                  .text   ((String) json.get("text"))
                  .build();
             text2=dto.getText();
             System.out.println("000000"+text2);
 
           list.add(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
      return  list;
   }







   

   }
   
