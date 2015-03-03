package kr.co.adflow.pms.adm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.adflow.pms.adm.request.AccountReq;
import kr.co.adflow.pms.adm.request.AuthReq;
import kr.co.adflow.pms.adm.request.PasswordReq;
import kr.co.adflow.pms.core.util.CheckUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.domain.mapper.UserMapper;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private InterceptMapper interceptMapper;
	
	@Autowired
	private CheckUtil checkUtil;

	@Override
	public User retrieveAccount(String appKey) {
		User resultUser = null;
		String userId = interceptMapper.selectCashedUserId(appKey);
		resultUser = userMapper.select(userId);
		
		resultUser.setDefaultExpiry(checkUtil.getMessageExpiry(resultUser.getDefaultExpiry()));
		resultUser.setDefaultQos(checkUtil.getMessageQos(resultUser.getDefaultQos()));
		resultUser.setMsgSizeLimit(checkUtil.getMessageSizeLimit(resultUser.getMsgSizeLimit()));
		
		return resultUser;
	}

	@Override
	public int modifyPassword(PasswordReq req, String appKey) {
		
		if(!req.getNewPassword().trim().equals(req.getRePassword().trim())) {
			throw new RuntimeException("패스워드 변경 실패1");
		}
		
		//TODO OLD <> NEW 확인 필요?
		
		String userId = interceptMapper.selectCashedUserId(appKey);
		
		User paramUser = new User();
		paramUser.setUserId(userId);
		paramUser.setPassword(this.getPassword(userId, req.getOldPassword()));
		
		User user = userMapper.selectAuth(paramUser);
		if (user == null) {
			throw new RuntimeException("패스워드 변경 실패2");
		}
		
		paramUser.setAction("modifyPassword");
		paramUser.setPassword(this.getPassword(userId, req.getNewPassword()));
		paramUser.setIssueId(userId);
		
		int cnt = userMapper.logUserHistory(paramUser);
		
		if (cnt > 0) {
			cnt = userMapper.updatePassword(paramUser);
		} else {
			throw new RuntimeException("패스워드 변경 실패3");
		}
		
		return cnt;
	}
	
	private String getPassword(String userId,String password) {

		return KeyGenerator.createPw(userId,password);
	}

	@Override
	public int modifyAccount(AccountReq req, String appKey) {

		String userId = interceptMapper.selectCashedUserId(appKey);
		
		User paramUser = userMapper.select(userId);
		
		paramUser.setUserName(req.getUserName());
		if (req.getIpFilters().trim().length() != 0) {
			paramUser.setIpFilters(req.getIpFilters());
		}
		paramUser.setIssueId(userId);
		paramUser.setAction("modifyAccount");
		
	int cnt = userMapper.logUserHistory(paramUser);
		
		if (cnt > 0) {
			cnt = userMapper.updateUser(paramUser);
		} else {
			throw new RuntimeException("modifyAccount fail");
		}
		
		
		return cnt;
	}
}
