package poly.persistance.mongo.comm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface IUserVoteMapper {
	
	
	public HashMap<String, ArrayList<String>> userVote(Map<String, Object> pMap) throws Exception;
	
	public void updateResult(Map<String, Object> pMap) throws Exception;
}
