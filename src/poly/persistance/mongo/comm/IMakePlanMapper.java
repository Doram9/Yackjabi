package poly.persistance.mongo.comm;

import java.util.HashMap;

import poly.dto.PlanInfoDTO;

public interface IMakePlanMapper {
	
	
	public int createPlan(PlanInfoDTO rDTO, String userid) throws Exception;
	
	public int getUsermkcnt(String _id)throws Exception;
	
	public void addMkcnt(String mkcnt)throws Exception;
	
	public void deletePlan(HashMap<String, Object> pMap) throws Exception;
	
	public PlanInfoDTO getPlanInfo(String roomcode) throws Exception;
}
