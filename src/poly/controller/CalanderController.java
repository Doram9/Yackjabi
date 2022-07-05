package poly.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import poly.dto.EventDTO;
import poly.dto.PlanInfoDTO;
import poly.dto.UserInfoDTO;
import poly.persistance.mongo.comm.IUserMapper;
import poly.persistance.mongo.impl.UserMapper;
import poly.service.IKakaoLoginService;
import poly.service.IKakaoLogoutService;
import poly.service.IMakePlanService;
import poly.service.IMongoUserService;
import poly.service.IVotePlanService;
import poly.util.CmmUtil;
import poly.util.DateUtil;
import poly.util.KeyUtil;

@Controller
public class CalanderController {
	
	@Resource(name = "KakaoLoginService")
	private IKakaoLoginService kakaoLoginService;
	
	@Resource(name = "KakaoLogoutService")
	private IKakaoLogoutService kakaoLogoutService;
	
	@Resource(name = "MongoUserService")
	private IMongoUserService mongoUserService;
	
	@Resource(name = "MakePlanService")
	private IMakePlanService makePlanService;
	
	@Resource(name = "VotePlanService")
	private IVotePlanService votePlanService;
	
	@Resource(name="UserMapper")
	IUserMapper userMapper = new UserMapper();
	
	
	private Logger log = Logger.getLogger(this.getClass());
	
	
	//메인페이지
	@RequestMapping(value = "index")
	public String mainPage(HttpServletRequest request, HttpServletResponse response, ModelMap model, HttpSession session) throws Exception {
		log.info(this.getClass().getName() + "index Start!");

		if(CmmUtil.nvl((String) session.getAttribute("userid")).equals("") || CmmUtil.nvl((String) session.getAttribute("userid")).equals("null")) {
			return "/MoCal/loginPage";
		} else {
			String userid = (String)session.getAttribute("userid");
			
			UserInfoDTO rDTO = mongoUserService.getUserInfo(userid);
			String username = rDTO.getUsername();
			String userbirth = rDTO.getUserbirth();
			String regdt = rDTO.getRegdt();
			int mkcnt = rDTO.getMkcnt();
			List<String> roomcode = rDTO.getRoomcode();
			List<String> event = rDTO.getEvent();
			
			session.setAttribute("username", username);
			
			model.addAttribute("username", username);
			model.addAttribute("userbirth", userbirth);
			model.addAttribute("regdt", regdt);
			model.addAttribute("mkcnt", mkcnt);
			model.addAttribute("roomcode", roomcode);
			model.addAttribute("event", event);
			
			return "/index";
		}
	}
	
	
		
	//로그인
	@RequestMapping(value = "login")
	public String logIn(HttpServletRequest request, HttpServletResponse response, ModelMap model, HttpSession session) throws Exception {
		log.info(this.getClass().getName() + "logIn Start!");
		
		String REST_API_KEY = KeyUtil.getKakaoKey();
		String REDIRECT_URI = KeyUtil.getRedirect_uri();
		
		return "redirect:https://kauth.kakao.com/oauth/authorize?client_id="+ REST_API_KEY + "&redirect_uri=" + REDIRECT_URI + "&response_type=code";
	}

			
	
	//로그아웃
	@RequestMapping(value = "logout")
	public String logOut(HttpServletRequest request, HttpServletResponse response, ModelMap model, HttpSession session) throws Exception {
		log.info(this.getClass().getName() + "logOut Start!");
		
		String accessToken = CmmUtil.nvl((String)session.getAttribute("access_Token"));
		
		int logoutResult = kakaoLogoutService.userLogout(accessToken);
		log.info("logoutResult : " + logoutResult);
		
		session.invalidate(); //세션초기화
		
		return "redirect: index.do";
	}
	
	// 카카오 로그인 하기
	@RequestMapping(value = "kakaoLogin")
	public String kakaoLogin(HttpServletRequest request, HttpServletResponse response, ModelMap model, HttpSession session) throws Exception {
		
		log.info(this.getClass().getName() + "kakaoLogin Start!");
		
		String AUTHORIZE_CODE = CmmUtil.nvl(request.getParameter("code"));
		
		//인가코드로 토큰받아오기
		List<String> tokenInfo = kakaoLoginService.getAccessToken(AUTHORIZE_CODE);
		
		String accessToken = tokenInfo.get(0);
		String refreshToken = tokenInfo.get(1);
		
		//access토큰으로 유저정보 HashMap에 담아오기
		HashMap<String, Object> userInfo = kakaoLoginService.getUserInfo(accessToken);
		String userid = userInfo.get("id").toString();
		
		
		if(!userMapper.existUser("User", userid)) { //db에 이미 해당 유저 데이터가 있을경우
			mongoUserService.insertUser(userInfo);
		}
		session.setAttribute("userid", userid);
		session.setAttribute("access_Token", accessToken);
        session.setAttribute("refresh_Token", refreshToken);
        
		return "redirect: index.do";
	}
	
	//약속방 생성
	@RequestMapping(value = "mkPlan")
	public String mkPlan(HttpServletRequest request, HttpServletResponse response, ModelMap model, HttpSession session) throws Exception {
		log.info(this.getClass().getName() + "mkPlan Start!");
		
		if(CmmUtil.nvl((String) session.getAttribute("userid")).equals("") || CmmUtil.nvl((String) session.getAttribute("userid")).equals("null")) {
			return "/MoCal/loginPage";
		}
		
		String userid = session.getAttribute("userid").toString();
		String username = session.getAttribute("username").toString();
		
		String title = CmmUtil.nvl(request.getParameter("title"));
		String yyyymm = CmmUtil.nvl(request.getParameter("month"));
		int voteday = Integer.parseInt(request.getParameter("deadline"));
		String deadline = DateUtil.addDate(voteday);
		
		PlanInfoDTO pDTO = new PlanInfoDTO();
		pDTO.setTitle(title);
		pDTO.setYyyymm(yyyymm);
		pDTO.setDeadline(deadline);
		makePlanService.makeRoom(pDTO, userid, username);
		
		
		return "redirect: index.do";
	}
	
	//약속방 생성
	@RequestMapping(value = "delPlan")
	public String delPlan(HttpServletRequest request, HttpServletResponse response, ModelMap model, HttpSession session) throws Exception {
		log.info(this.getClass().getName() + "delPlan Start!");
		
		if(CmmUtil.nvl((String) session.getAttribute("userid")).equals("") || CmmUtil.nvl((String) session.getAttribute("userid")).equals("null")) {
			return "/MoCal/loginPage";
		}
		
		String userid = session.getAttribute("userid").toString();
		
		String roomcode = CmmUtil.nvl(request.getParameter("roomcode"));
		String title = CmmUtil.nvl(request.getParameter("title"));
		log.info(roomcode);
		
		HashMap<String, Object> pMap = new HashMap<>();
		pMap.put("userid", userid);
		pMap.put("title", title);
		pMap.put("roomcode", roomcode);
		
		makePlanService.deleteRoom(pMap);
		
		return "redirect: index.do";
	}
	//약속방 들어가기
	@RequestMapping(value = "planPage")
	public String planPage(HttpServletRequest request, HttpServletResponse response, ModelMap model, HttpSession session) throws Exception {
		log.info(this.getClass().getName() + "planPage Start!");
		
		if(CmmUtil.nvl((String) session.getAttribute("userid")).equals("") || CmmUtil.nvl((String) session.getAttribute("userid")).equals("null")) {
			return "/MoCal/loginPage";
		}
		String roomId = CmmUtil.nvl(request.getParameter("id"));
		
		PlanInfoDTO pDTO = makePlanService.getRoomInfo(roomId);
		
		String userid = (String)session.getAttribute("userid");
		UserInfoDTO rDTO = mongoUserService.getUserInfo(userid);
		List<String> roomList = rDTO.getRoomcode();
		
		model.addAttribute("roomlist", roomList);
		model.addAttribute("roomcode", pDTO.getRoomcode());
		model.addAttribute("userlist", pDTO.getUserlist());
		model.addAttribute("title", pDTO.getTitle());
		model.addAttribute("yyyymm", pDTO.getYyyymm());
		model.addAttribute("deadline", pDTO.getDeadline());
		model.addAttribute("firdate", pDTO.getFirdate());
		model.addAttribute("secdate", pDTO.getSecdate());
		model.addAttribute("thidate", pDTO.getThidate());
		
		
		return "/MoCal/planPage";
	}
	
	@RequestMapping(value = "voteDate", method = {RequestMethod.POST})
	@ResponseBody
	public void voteDate(@RequestBody HashMap<String, Object> param, HttpSession session) throws Exception {
		
		log.info(this.getClass().getName() + "voteDate Start!");
		
		param.put("userid", session.getAttribute("userid"));
		
		votePlanService.userVoteService(param);
		
		log.info(param);
		
	}
	
	@RequestMapping(value = "addEvent", method = {RequestMethod.POST})
	@ResponseBody
	public void addEvent(@RequestBody HashMap<String, Object> param, HttpSession session) throws Exception {
		String end = "";
		if((String) param.get("end") == "") {
			end = DateUtil.endDayChg((String) param.get("start"));
		} else {
			end = DateUtil.endDayChg((String) param.get("end"));
		}
		
		HashMap<String, Object> pMap = new HashMap<>();
		pMap.put("userid", session.getAttribute("userid"));
		pMap.put("title", param.get("title"));
		pMap.put("start", param.get("start"));
		pMap.put("end", end);
		
		mongoUserService.addEventService(pMap);
		
	}
	
	@RequestMapping(value = "delEvent", method = {RequestMethod.POST})
	@ResponseBody
	public void delEvent(@RequestBody HashMap<String, Object> param, HttpSession session) throws Exception {
		
		HashMap<String, Object> pMap = new HashMap<>();
		pMap.put("userid", session.getAttribute("userid"));
		pMap.put("title", param.get("title"));
		pMap.put("start", param.get("start"));
		pMap.put("end", param.get("end"));
		
		mongoUserService.delEventService(pMap);
		
	}
	
	
	//약속방 들어가기
	@RequestMapping(value = "invite")
	public String invitePlan(HttpServletRequest request, HttpServletResponse response, ModelMap model, HttpSession session) throws Exception {
		log.info(this.getClass().getName() + "invitePlan Start!");
		if(CmmUtil.nvl((String) session.getAttribute("userid")).equals("") || CmmUtil.nvl((String) session.getAttribute("userid")).equals("null")) {
			return "/MoCal/loginPage";
		}
		String userid = (String) session.getAttribute("userid");
		String roomcode = request.getParameter("roomcode");
		String title = request.getParameter("title");
		log.info(roomcode);
		HashMap<String, Object> invitecode = new HashMap<>();
		invitecode.put("userid", userid);
		invitecode.put("roomcode", roomcode);
		invitecode.put("title", title);
		
		mongoUserService.inviteRoom(invitecode);
		
		return "redirect: index.do";
	}
	
	
}
