package poly.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


import poly.service.IKakaoLogoutService;

@Service("KakaoLogoutService")
public class KakaoLogoutService implements IKakaoLogoutService {
	
	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public int userLogout(String access_Token) throws Exception {


		log.info(this.getClass().getName() + "Service Start!");
		
		
        String reqURL = "https://kapi.kakao.com/v1/user/logout";
        
        int responseCode = 0;
        
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            
            //    요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            
            //          결과 코드가 200이라면 성공
            responseCode = conn.getResponseCode();
            
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
		
		return responseCode;
	}

}
