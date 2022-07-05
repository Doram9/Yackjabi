package poly.dto;

import java.util.List;

public class VoteInfoDTO {
	private String userid;
	private String username;
	private boolean votetf;
	private List<String> posday;
	private List<String> negday;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public boolean isVotetf() {
		return votetf;
	}
	public void setVotetf(boolean votetf) {
		this.votetf = votetf;
	}
	public List<String> getPosday() {
		return posday;
	}
	public void setPosday(List<String> posday) {
		this.posday = posday;
	}
	public List<String> getNegday() {
		return negday;
	}
	public void setNegday(List<String> negday) {
		this.negday = negday;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}
