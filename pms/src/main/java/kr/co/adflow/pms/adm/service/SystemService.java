package kr.co.adflow.pms.adm.service;

import java.util.List;

import kr.co.adflow.pms.adm.request.UserReq;
import kr.co.adflow.pms.domain.User;

public interface SystemService {
	
	public List<User> listAllUser();
	
	public String createUser(UserReq userReq,String appKey);
	
	public User retrieveUser(String userId);
	
	public int updateUser(UserReq userReq,String appKey);
	
	public int deleteUser(String userId,String appKey);

}
