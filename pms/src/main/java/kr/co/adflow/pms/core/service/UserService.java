package kr.co.adflow.pms.core.service;

import java.util.List;

import kr.co.adflow.pms.core.request.UserReq;
import kr.co.adflow.pms.core.response.UserInfoRes;
import kr.co.adflow.pms.core.response.UserRes;

public interface UserService {

	public UserRes createUser(UserReq userReq, String requestId)
			throws Exception;

	public UserInfoRes getUserInfo(String userId) throws Exception;

	public void updateUser(UserReq userReq, String requsetUserId)
			throws Exception;

	public void deleteUser(String userId, String requsetUserId) throws Exception;
}
