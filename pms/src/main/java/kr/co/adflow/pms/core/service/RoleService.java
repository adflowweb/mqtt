package kr.co.adflow.pms.core.service;

import kr.co.adflow.pms.core.request.RoleReq;
import kr.co.adflow.pms.core.response.RoleRes;

public interface RoleService {
	public RoleRes createRole(RoleReq roleReq, String requsetUserId)
			throws Exception;
}
