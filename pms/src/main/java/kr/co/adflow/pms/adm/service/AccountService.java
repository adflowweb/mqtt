package kr.co.adflow.pms.adm.service;

import kr.co.adflow.pms.adm.request.AccountReq;
import kr.co.adflow.pms.adm.request.PasswordReq;
import kr.co.adflow.pms.domain.User;

public interface AccountService {
	
	public User retrieveAccount(String appKey);
	
	public int modifyPassword(PasswordReq req,String appKey);
	
	public int modifyAccount(AccountReq req,String appKey);

}
