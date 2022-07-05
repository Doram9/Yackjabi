package poly.dto;

import java.util.List;

public class PlanInfoDTO {
	
	private String roomcode;
	private List<VoteInfoDTO> userlist;
	private String title;
	private String yyyymm;
	private String deadline;
	private String firdate;
	private String secdate;
	private String thidate;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	public String getRoomcode() {
		return roomcode;
	}
	public void setRoomcode(String roomcode) {
		this.roomcode = roomcode;
	}
	public List<VoteInfoDTO> getUserlist() {
		return userlist;
	}
	public void setUserlist(List<VoteInfoDTO> userlist) {
		this.userlist = userlist;
	}
	public String getYyyymm() {
		return yyyymm;
	}
	public void setYyyymm(String yyyymm) {
		this.yyyymm = yyyymm;
	}
	public String getFirdate() {
		return firdate;
	}
	public void setFirdate(String firdate) {
		this.firdate = firdate;
	}
	public String getSecdate() {
		return secdate;
	}
	public void setSecdate(String secdate) {
		this.secdate = secdate;
	}
	public String getThidate() {
		return thidate;
	}
	public void setThidate(String thidate) {
		this.thidate = thidate;
	}
	
	
}
