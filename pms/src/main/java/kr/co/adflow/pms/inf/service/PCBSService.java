package kr.co.adflow.pms.inf.service;

import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.inf.request.PasswordReq;
import kr.co.adflow.pms.inf.request.UserReq;
import kr.co.adflow.pms.inf.request.UserUpdateReq;

public interface PCBSService {

	public String addUser(UserReq userReq,String appKey);

	public User retrieveUser(String userId);

	public int updateUser(UserUpdateReq user, String appKey);

	public int deleteUser(String userId, String appKey);

	public int modifyPassword(PasswordReq req, String appKey);

}
