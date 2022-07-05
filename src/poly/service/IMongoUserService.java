package poly.service;

import java.util.HashMap;

import poly.dto.UserInfoDTO;

public interface IMongoUserService {
	
	public UserInfoDTO getUserInfo(String userid) throws Exception;
	
	public void insertUser(HashMap<String, Object> kakaoUserInfo) throws Exception;
	
	public void addEventService(HashMap<String ,Object> pMap) throws Exception;
	
	public void delEventService(HashMap<String ,Object> pMap) throws Exception;
	
	public void inviteRoom(HashMap<String, Object> pMap) throws Exception;
}
