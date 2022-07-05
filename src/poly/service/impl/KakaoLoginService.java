package poly.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import poly.service.IKakaoLoginService;
import poly.util.KeyUtil;


@Service("KakaoLoginService")
public class KakaoLoginService implements IKakaoLoginService {
	
	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public List<String> getAccessToken(String code) throws Exception {
		
		log.info(this.getClass().getName() + "Service Start!");
		
		List<String> tokenInfo = new ArrayList<>();
		
        String reqURL = "https://kauth.kakao.com/oauth/token";
        
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            //    POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            
            //    POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + KeyUtil.getKakaoKey());
            sb.append("&redirect_uri=" + KeyUtil.getRedirect_uri());
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();
            
            //    결과 코드가 200이라면 성공
            // int responseCode = conn.getResponseCode();
 
            //    요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";
            
            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("response body : " + result);
            
            //    Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            
            
            
            
            String access_Token = element.getAsJsonObject().get("access_token").getAsString();
            String refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();
            
            tokenInfo.add(access_Token);
            tokenInfo.add(refresh_Token);
            
            br.close();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        
        return tokenInfo;
	}
	
	@Override
	public HashMap<String, Object> getUserInfo(String access_Token) {
		HashMap<String, Object> userInfo = new HashMap<>();
		String reqURL = "https://kapi.kakao.com/v2/user/me";
        
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            
            //    요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);
            
            
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            
            String line = "";
            String result = ""; //Json형태의 response 저장
            
            while ((line = br.readLine()) != null) {
            	log.info(line);
            	log.info("끝");
                result += line;
            }
            /*
             * {
				"id":2026612933, //유저고유아이디
				"connected_at":"2021-12-15T12:57:58Z", //가입일자
				"properties":{"nickname":"도훈"}, //지정 프러퍼티
				"kakao_account":{"profile_nickname_needs_agreement":false, //계정정보
						"profile":{"nickname":"도훈"},
						"has_birthday":true,
						"birthday_needs_agreement":true
						}
				}
             * 
             * 
             * */
            
            
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            //JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
            
            String id = element.getAsJsonObject().get("id").getAsString();
            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
//            String userbirth = CmmUtil.nvl(properties.getAsJsonObject().get("userbirth").getAsString());
            String userbirth = "";
            userInfo.put("id", id);
            userInfo.put("nickname", nickname);
            userInfo.put("userbirth", userbirth);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
		
		
		return userInfo;
	}

}
