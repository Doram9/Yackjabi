package poly.service;

import java.util.HashMap;

import poly.dto.PlanInfoDTO;

public interface IMakePlanService {
	
	
	public String makeRoom(PlanInfoDTO pDTO, String userid, String username) throws Exception;
	
	public void deleteRoom(HashMap<String, Object> pMap) throws Exception;
	
	public PlanInfoDTO getRoomInfo(String roomcode) throws Exception;
	
	
}
