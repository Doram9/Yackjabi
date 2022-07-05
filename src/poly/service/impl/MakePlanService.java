package poly.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.mortbay.log.Log;
import org.springframework.stereotype.Service;

import poly.dto.PlanInfoDTO;
import poly.dto.VoteInfoDTO;
import poly.persistance.mongo.comm.IMakePlanMapper;
import poly.persistance.mongo.impl.MakePlanMapper;
import poly.service.IMakePlanService;
import poly.util.EncryptUtil;




@Service("MakePlanService")
public class MakePlanService implements IMakePlanService {
	
	@Resource(name = "MakePlanMapper")
	IMakePlanMapper makePlanMapper = new MakePlanMapper();
	

	@Override
	public String makeRoom(PlanInfoDTO pDTO, String userid, String username) throws Exception {
		
		int mkcnt = makePlanMapper.getUsermkcnt(userid);
		String usermkcnt = userid + mkcnt;
		
		
		String roomCode = EncryptUtil.encHashSHA256(usermkcnt);
		Log.info("roomcode : " + roomCode);
		
		
		makePlanMapper.addMkcnt(userid);
		
		pDTO.setRoomcode(roomCode);
		pDTO.setFirdate("");
		pDTO.setSecdate("");
		pDTO.setThidate("");
		
		VoteInfoDTO vDTO = new VoteInfoDTO();
		vDTO.setUserid(userid);
		vDTO.setUsername(username);
		vDTO.setVotetf(false);
		vDTO.setPosday(null);
		vDTO.setNegday(null);
		
		List<VoteInfoDTO> rList = new ArrayList<>();
		rList.add(vDTO);
		
		pDTO.setUserlist(rList);
		
		makePlanMapper.createPlan(pDTO, userid);
		
		return roomCode;
	}
	
	@Override
	public void deleteRoom(HashMap<String, Object> pMap) throws Exception {
		
		makePlanMapper.deletePlan(pMap);
		
	}
	
	@Override
	public PlanInfoDTO getRoomInfo(String roomcode) throws Exception {
		
		PlanInfoDTO pDTO = makePlanMapper.getPlanInfo(roomcode);
		
		return pDTO;
	}
	
	
	
}
