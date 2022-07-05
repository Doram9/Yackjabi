package poly.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.gson.JsonParser;

import poly.dto.UserInfoDTO;
import poly.dto.VoteInfoDTO;
import poly.persistance.mongo.comm.IUserMapper;
import poly.persistance.mongo.impl.UserMapper;
import poly.service.IMongoUserService;
import poly.util.DateUtil;


@Service("MongoUserService")
public class MongoUserService implements IMongoUserService {
	
	@Resource(name="UserMapper")
	IUserMapper userMapper = new UserMapper();
	
	private Logger log = Logger.getLogger(this.getClass());
	
	final String colnm = "User";
	
	@Override
	public UserInfoDTO getUserInfo(String userid) throws Exception {
		
		log.info(this.getClass().getName() + "getUserInfoService Start");
		
		
		UserInfoDTO rDTO = new UserInfoDTO();
		
		rDTO = userMapper.getUserInfo(colnm, userid);
		
		
		
		return rDTO;
	}
	
	
	@Override
	public void insertUser(HashMap<String, Object> kakaoUserInfo) throws Exception {
		
		log.info(this.getClass().getName() + "getUserInfoService Start");
		
		Map<String, Object> pMap = new HashMap<>();
		List<String> roomcode = new ArrayList<>();
		
		pMap.put("_id", (String) kakaoUserInfo.get("id"));
		pMap.put("username", (String) kakaoUserInfo.get("nickname"));
		pMap.put("userbirth", (String) kakaoUserInfo.get("userbirth"));
		pMap.put("regdt", DateUtil.getDateTime());
		pMap.put("mkcnt", 0);
		pMap.put("roomcode", roomcode);
		
		
		userMapper.insertUser(pMap, colnm);
		
	}
	
	@Override
	public void addEventService(HashMap<String ,Object> pMap) throws Exception {
		
		log.info("addEventService Start");
		
		int res = userMapper.addEventUserInfo(pMap);
		
	}
	
	@Override
	public void delEventService(HashMap<String ,Object> pMap) throws Exception {
		
		log.info("delEventService Start");
		
		int res = userMapper.delEventUserInfo(pMap);
		
	}
	
	@Override
	public void inviteRoom(HashMap<String, Object> pMap) throws Exception {
		
		log.info(this.getClass().getName() + "inviteRoom Start");
		
		String userid = (String) pMap.get("userid");
		String roomcode = (String) pMap.get("roomcode");
		String title = (String) pMap.get("title");
		
		VoteInfoDTO pDTO = new VoteInfoDTO();
		pDTO.setNegday(null);
		pDTO.setPosday(null);
		pDTO.setUserid(userid);
		pDTO.setVotetf(false);
		
		userMapper.inviteUser(pDTO, pMap);
		
	}
}
