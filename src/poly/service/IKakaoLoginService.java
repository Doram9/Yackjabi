package poly.service;

import java.util.HashMap;
import java.util.List;

public interface IKakaoLoginService {
	
	public List<String> getAccessToken(String code) throws Exception;
	
	public HashMap<String, Object> getUserInfo(String access_Token) throws Exception;
}
