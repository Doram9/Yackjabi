package poly.persistance.mongo.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;

import poly.dto.PlanInfoDTO;
import poly.dto.UserInfoDTO;
import poly.dto.VoteInfoDTO;
import poly.persistance.mongo.comm.IUserMapper;


@Component("UserMapper")//Component : 스프링에 올라갈때 자동으로 메모리에 파일 올라감
public class UserMapper implements IUserMapper {
	
	@Autowired //MongoTemplate가 메모리에 올라와 있으면 변수에 넣어라
	private MongoTemplate mongodb;
	
	@Autowired
	private RedisTemplate<String, Object> redisDB;
	
	private Logger log = Logger.getLogger(this.getClass());

	
	
	@Override
	public boolean existUser(String colnm, String userid) throws Exception {
		
		log.info(this.getClass().getName() + ".existUser Start!");
		
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(userid));
		
		boolean res = mongodb.exists(query, colnm);
		
		return res;
		
	}

	@Override
	public void insertUser(Map<String, Object> pMap, String colnm) throws Exception {
		log.info(getClass().getName() + ".insertUser Start!");
		
		MongoCollection<Document> rCol = mongodb.getCollection(colnm);
		rCol.insertOne(new Document(pMap));
		
	}

	@Override
	public UserInfoDTO getUserInfo(String colnm, String _id) throws Exception {
		
		log.info(getClass().getName() + ".getUserInfo Start!");
		
		//가져올 컬렉션 선택
		MongoCollection<Document> rCol = mongodb.getCollection(colnm);
		
		//쿼리 만들기
		BasicDBObject query = new BasicDBObject();
		query.put("_id", _id);
		
		UserInfoDTO rDTO = new UserInfoDTO();
		
		Consumer<Document> processBlock = document -> {
			String username = document.getString("username");
			String userbirth = document.getString("userbirth");
			int mkcnt = document.getInteger("mkcnt");
			String regdt = document.getString("regdt");
			List<String> userRoomcodeList = document.getList("roomcode", String.class);
			List<String> event = document.getList("event", String.class);
			
			
			rDTO.set_id(_id);
			rDTO.setUsername(username);
			rDTO.setUserbirth(userbirth);
			rDTO.setMkcnt(mkcnt);
			rDTO.setRegdt(regdt);
			rDTO.setRoomcode(userRoomcodeList);
			rDTO.setEvent(event);
        };
        
        rCol.find(query).forEach(processBlock);

		return rDTO;
	}

	@Override
	public int updateUserInfo(String colnm) throws Exception {
		
		return 0;
	}

	@Override
	public int deleteUserInfo(String colnm) throws Exception {
		
		return 0;
	}
	
	@Override
	public int addEventUserInfo(HashMap<String, Object> pMap) throws Exception {
		
		log.info(getClass().getName() + ".addEventUserInfo Start!");
		
		
		MongoCollection<Document> rCol = mongodb.getCollection("User");
		String userid = (String) pMap.get("userid");
		String start = (String) pMap.get("start");
		String title = (String) pMap.get("title");
		String end = (String) pMap.get("end");
		
		
		String data = "title:\"" + title + "\", start:\"" + start + "\", end:\"" + end + "\"";
		log.info(data);

		
		//쿼리 만들기
		Document findQuery = new Document();
		findQuery.append("_id", userid);
		
		Document updateQuery = new Document();
		
        updateQuery.append("event", data);
        
        UpdateResult updateResults = rCol.updateOne(findQuery, new Document("$push", updateQuery));
        int res = (int) updateResults.getMatchedCount();
		
		 return res;
	}
	
	@Override
	public int delEventUserInfo(HashMap<String, Object> pMap) throws Exception {
		
		log.info(getClass().getName() + ".delEventUserInfo Start!");
		
		
		MongoCollection<Document> rCol = mongodb.getCollection("User");
		String userid = (String) pMap.get("userid");
		String start = (String) pMap.get("start");
		String title = (String) pMap.get("title");
		String end = (String) pMap.get("end");
		
		
		String data = "title:\"" + title + "\", start:\"" + start + "\", end:\"" + end + "\"";
		log.info(data);
		

		
		//쿼리 만들기
		Document findQuery = new Document();
		findQuery.append("_id", userid);
		
		Document updateQuery = new Document();
		
        updateQuery.append("event", data);
        
        UpdateResult updateResults = rCol.updateOne(findQuery, new Document("$pull", updateQuery));
        int res = (int) updateResults.getMatchedCount();
		
		 return res;
	}
	
	@Override
	public int inviteUser(VoteInfoDTO pDTO, HashMap<String, Object> pMap) throws Exception {
		log.info(getClass().getName() + ".inviteUserMapper Start!");
		
		//가져올 컬렉션 선택
		MongoCollection<Document> rCol = mongodb.getCollection("User");
		
		String userid = (String) pMap.get("userid");
		String roomcode = (String) pMap.get("roomcode");
		String title = (String) pMap.get("title");
		
		//쿼리 만들기
		Document query = new Document();
        query.append("_id", userid);
		
		Document projection = new Document();
        projection.append("roomcode", "$roomcode");
        projection.append("username", "$username");
        projection.append("_id", 0);
        
        
        String titleNroomcode = title + "*" + roomcode;
        
        Consumer<Document> processBlock = document -> {
        	boolean roomExist = true;
        	String username = document.getString("username");
        	pDTO.setUsername(username);
        	
            Iterator<String> roomList = document.getList("roomcode", String.class).iterator();
            while(roomList.hasNext()) {
            	String pRoomcode = roomList.next();
            	if(pRoomcode.equals(titleNroomcode)) {
            		roomExist = false;
            		break;
            	}
            }
            if(roomExist) {
            	Document updateQuery = new Document();
        		
                updateQuery.append("roomcode", titleNroomcode);
                
                UpdateResult updateResults = rCol.updateOne(query, new Document("$push", updateQuery));
                
                PlanInfoDTO rDTO = null;
        		if(redisDB.hasKey(roomcode)) {
        			List<PlanInfoDTO> rList = (List) redisDB.opsForList().range(roomcode, 0, -1);
        			rDTO = rList.get(0); //정보를 pDTO에 저장
        			List<VoteInfoDTO> pList = rDTO.getUserlist(); //전체 정보 속 투표 정보들 가져오기
        			pList.add(pDTO);
        			
        			rDTO.setUserlist(pList);
        			redisDB.opsForList().set(roomcode, 0, rDTO); //투표정보 수정
        		}
            }
        };
		
		rCol.find(query).projection(projection).forEach(processBlock);
		
		
		return 0;
	}

}
