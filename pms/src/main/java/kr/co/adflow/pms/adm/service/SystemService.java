package kr.co.adflow.pms.adm.service;

import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.adm.request.ReservationCancelReq;
import kr.co.adflow.pms.adm.request.UserReq;
import kr.co.adflow.pms.adm.request.UserUpdateReq;
import kr.co.adflow.pms.adm.response.MessagesRes;
import kr.co.adflow.pms.domain.ServerInfo;
import kr.co.adflow.pms.domain.User;

public interface SystemService {
	
	public List<User> listAllUser();
	
	public String createUser(UserReq userReq,String appKey);
	
	public User retrieveUser(String userId);
	
	public int updateUser(UserUpdateReq userReq,String appKey);
	
	public int deleteUser(String userId,String appKey);
	
	public MessagesRes getSysMessageList(Map<String,String> params);
	
	public MessagesRes getSysResevationMessageList(Map<String,String> params);

	public ServerInfo getServerInfo();

	public int cancelReservationList(String appKey, ReservationCancelReq ids);
	
	public List<Map<String,Object>> getMonthSummary(String appKey, String keyMon,String issueId);

}
