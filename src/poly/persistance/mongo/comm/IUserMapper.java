package poly.persistance.mongo.comm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import poly.dto.UserInfoDTO;
import poly.dto.VoteInfoDTO;

public interface IUserMapper {
	public boolean existUser(String colnm, String userid)throws Exception;
	
	//create
	public void insertUser(Map<String, Object> pMap, String colnm) throws Exception;
	//read
	public UserInfoDTO getUserInfo(String colnm, String _id) throws Exception;
	//update
	public int updateUserInfo(String colnm) throws Exception;
	//delete
	public int deleteUserInfo(String colnm) throws Exception;
	
	public int addEventUserInfo(HashMap<String, Object> pMap) throws Exception;
	
	public int delEventUserInfo(HashMap<String, Object> pMap) throws Exception;
	
	public int inviteUser(VoteInfoDTO pDTO, HashMap<String, Object> pMap) throws Exception;
}
