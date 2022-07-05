package poly.persistance.mongo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;

import poly.dto.PlanInfoDTO;
import poly.dto.VoteInfoDTO;
import poly.persistance.mongo.comm.IMakePlanMapper;


@Component("MakePlanMapper")
public class MakePlanMapper implements IMakePlanMapper {
	
	@Autowired //MongoTemplate가 메모리에 올라와 있으면 변수에 넣어라
	private MongoTemplate mongodb;
	
	@Autowired
	private RedisTemplate<String, Object> redisDB;
	
	private Logger log = Logger.getLogger(this.getClass());
	
	

	@Override
	public int createPlan(PlanInfoDTO rDTO, String userid) throws Exception {
		
		log.info(this.getClass().getName() + "createPlan start");
		

		redisDB.setKeySerializer(new StringRedisSerializer());
		redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(PlanInfoDTO.class));
		
		String rediskey = rDTO.getRoomcode();
		
		redisDB.opsForList().rightPush(rediskey, rDTO);
		
		redisDB.expire(rediskey, 31, TimeUnit.DAYS);
		log.info("방이 생성되었습니다. Roomcode : " + rDTO.getRoomcode());
		
		//가져올 컬렉션 선택
		MongoCollection<Document> rCol = mongodb.getCollection("User");
		
		
		//쿼리 만들기
		Document findQuery = new Document();
		findQuery.append("_id", userid);
		
		Document updateQuery = new Document();
		String savecode = rDTO.getTitle() + "*" + rediskey;
		
        updateQuery.append("roomcode", savecode);
        
        UpdateResult updateResults = rCol.updateOne(findQuery, new Document("$push", updateQuery));
        int res = (int) updateResults.getMatchedCount();
        
        log.info("res : " + res);
		rDTO = null;

		return res;
	}
	
	@Override
	public int getUsermkcnt(String _id)throws Exception {
		//가져올 컬렉션 선택
		MongoCollection<Document> rCol = mongodb.getCollection("User");
		
		//쿼리 만들기
		Document query = new Document();

        query.append("_id", _id);
        
        Document projection = new Document();
        projection.append("mkcnt", "$mkcnt");
        
        Map<String, Integer> rMap = new HashMap<>();
        
        Consumer<Document> processBlock = document -> {
           int mkcnt = document.getInteger("mkcnt");
           
           rMap.put("mkcnt", mkcnt);
        };
        rCol.find(query).projection(projection).forEach(processBlock);
		
		
		log.info("mkcnt" + rMap.get("mkcnt"));
		return rMap.get("mkcnt");
	}
	
	
	@Override
	public void addMkcnt(String _id)throws Exception {
		
		//가져올 컬렉션 선택
		MongoCollection<Document> rCol = mongodb.getCollection("User");
		
		Document findQuery = new Document();
        findQuery.append("_id", _id);
		
        Document updateQuery = new Document();
        updateQuery.append("mkcnt", 1);
        
        UpdateResult updateResults = rCol.updateOne(findQuery, new Document("$inc", updateQuery));
        int res = (int) updateResults.getMatchedCount();
        log.info("res : " + res);
        
		
	}

	@Override
	public void deletePlan(HashMap<String, Object> pMap) throws Exception {
		
		String userid = (String) pMap.get("userid");
		String title = (String) pMap.get("title");
		String roomcode = (String) pMap.get("roomcode");
		
		//가져올 컬렉션 선택
		MongoCollection<Document> rCol = mongodb.getCollection("User");
		
		//쿼리 만들기
		Document findQuery = new Document();
		findQuery.append("_id", userid);
		
		Document updateQuery = new Document();
		String savecode = title + "*" + roomcode;
		
        updateQuery.append("roomcode", savecode);
        
        UpdateResult updateResults = rCol.updateOne(findQuery, new Document("$pull", updateQuery));
        
        redisDB.setKeySerializer(new StringRedisSerializer());
		redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(PlanInfoDTO.class));
		PlanInfoDTO pDTO = null;
		if(redisDB.hasKey(roomcode)) {
			List<PlanInfoDTO> rList = (List) redisDB.opsForList().range(roomcode, 0, -1);
			pDTO = rList.get(0);
			Iterator<VoteInfoDTO> pList = pDTO.getUserlist().iterator();
			List<VoteInfoDTO> nList = new ArrayList<>();
			while(pList.hasNext()) {
				VoteInfoDTO vDTO = pList.next();
				String delid = vDTO.getUserid(); //나갈 아이디
				log.info(userid);
				if(delid.equals(userid)) { //발견하면
					log.info("삭제 : " + delid);
				} else {
					log.info("유지 : " + delid);
					nList.add(vDTO);
				}
			}
			
			if(nList.size() == 0) { //방에 사람이 없으면
				redisDB.delete(roomcode);
			} else {
				pDTO.setUserlist(nList);
				
				redisDB.opsForList().set(roomcode, 0, pDTO);
			}
			
		}

	}
	
	@Override
	public PlanInfoDTO getPlanInfo(String roomcode) throws Exception {
		
		redisDB.setKeySerializer(new StringRedisSerializer());
		redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(PlanInfoDTO.class));
		PlanInfoDTO pDTO = null;
		if(redisDB.hasKey(roomcode)) {
			List<PlanInfoDTO> rList = (List) redisDB.opsForList().range(roomcode, 0, -1);
			pDTO = rList.get(0);
		}
		return pDTO;
	}

}
