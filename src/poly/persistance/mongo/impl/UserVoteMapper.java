package poly.persistance.mongo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import poly.dto.PlanInfoDTO;
import poly.dto.VoteInfoDTO;
import poly.persistance.mongo.comm.IUserVoteMapper;


@Component("UserVoteMapper")
public class UserVoteMapper implements IUserVoteMapper {
	
	@Autowired
	private RedisTemplate<String, Object> redisDB;
	
	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public HashMap<String, ArrayList<String>> userVote(Map<String, Object> pMap) throws Exception {
		
		log.info("uservote Mapper start");
		
		redisDB.setKeySerializer(new StringRedisSerializer());
		redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(PlanInfoDTO.class));
		
		String userid = (String) pMap.get("userid");
		String roomcode = (String) pMap.get("roomcode");
		List<String> posdays = (List) pMap.get("posdays");
		List<String> negdays = (List) pMap.get("negdays");
		
		
		ArrayList<String> rPosdays = new ArrayList<>();
		
		ArrayList<String> rNegdays = new ArrayList<>();
		
		rPosdays.addAll(posdays);
		rNegdays.addAll(negdays);
		
		
		
		PlanInfoDTO pDTO = null;
		if(redisDB.hasKey(roomcode)) {
			List<PlanInfoDTO> rList = (List) redisDB.opsForList().range(roomcode, 0, -1);
			pDTO = rList.get(0); //정보를 pDTO에 저장
			Iterator<VoteInfoDTO> pList = pDTO.getUserlist().iterator(); //전체 정보 속 투표 정보들 가져오기
			List<VoteInfoDTO> nList = new ArrayList<>(); //새로운 투표정보
			while(pList.hasNext()) {
				VoteInfoDTO vDTO = pList.next(); //투표정보리스트들 중 하나
				String chgid = vDTO.getUserid(); //바꿀 아이디
				
				if(chgid.equals(userid)) { //발견하면
					vDTO.setVotetf(true);
					vDTO.setPosday(posdays);
					vDTO.setNegday(negdays);
					nList.add(vDTO);
				} else { //다른 유저일경우
					nList.add(vDTO);
					if(vDTO.isVotetf()) { //투표를 했으면
						rPosdays.addAll(vDTO.getPosday());
						rNegdays.addAll(vDTO.getNegday());
					}
				}
			}
			pDTO.setUserlist(nList);
			redisDB.opsForList().set(roomcode, 0, pDTO); //투표정보 수정
		}
		
		HashMap<String, ArrayList<String>> rMap = new HashMap<>();
		rMap.put("posdays", rPosdays);
		rMap.put("negdays", rNegdays);
		return rMap;
	}
	
	@Override
	public void updateResult(Map<String, Object> pMap) throws Exception {
		
		log.info("updateResult Mapper start");
		
		String roomcode = (String) pMap.get("roomcode");
		String firday = (String) pMap.get("firday");
		String secday = (String) pMap.get("secday");
		String thiday = (String) pMap.get("thiday");
		

		
		redisDB.setKeySerializer(new StringRedisSerializer());
		redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(PlanInfoDTO.class));
		
		PlanInfoDTO pDTO = null;
		if(redisDB.hasKey(roomcode)) {
			List<PlanInfoDTO> rList = (List) redisDB.opsForList().range(roomcode, 0, -1);
			pDTO = rList.get(0); //정보를 pDTO에 저장
			pDTO.setFirdate(firday);
			pDTO.setSecdate(secday);
			pDTO.setThidate(thiday);
			redisDB.opsForList().set(roomcode, 0, pDTO); //투표정보 수정
		}
		
		
	}

}
