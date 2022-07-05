package poly.dto;

import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;

public class UserInfoDTO {
	
	private String _id; //유저고유번호
	private String username; //유저명
	private String regdt; //가입일
	private String userbirth; //유저생일
	private int mkcnt; //방생성횟수
	private List<String> roomcode; //들어가 있는 방 리스트
	private List<String> event;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRegdt() {
		return regdt;
	}
	public void setRegdt(String regdt) {
		this.regdt = regdt;
	}
	public String getUserbirth() {
		return userbirth;
	}
	public void setUserbirth(String userbirth) {
		this.userbirth = userbirth;
	}
	public int getMkcnt() {
		return mkcnt;
	}
	public void setMkcnt(int mkcnt) {
		this.mkcnt = mkcnt;
	}
	public List<String> getRoomcode() {
		return roomcode;
	}
	public void setRoomcode(List<String> roomcode) {
		this.roomcode = roomcode;
	}
	public List<String> getEvent() {
		return event;
	}
	public void setEvent(List<String> event) {
		this.event = event;
	}
	
	
	
}
