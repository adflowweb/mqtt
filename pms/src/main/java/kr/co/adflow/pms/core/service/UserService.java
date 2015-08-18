package kr.co.adflow.pms.core.service;

import kr.co.adflow.pms.adm.request.UserReq;
import kr.co.adflow.pms.adm.request.UserUpdateReq;
import kr.co.adflow.pms.domain.User;

public interface UserService {

	public String createUser(UserReq userReq) throws Exception;

	public User retrieveUser(String userId) throws Exception;

	public int updateUser(UserUpdateReq userReq, String appKey)
			throws Exception;

	public int deleteUser(String userId, String appKey) throws Exception;
}
