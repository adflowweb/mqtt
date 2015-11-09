package kr.co.adflow.pms.core.service;

import kr.co.adflow.pms.core.request.RoleReq;
import kr.co.adflow.pms.core.response.RoleListRes;
import kr.co.adflow.pms.core.response.RoleRes;

public interface RoleService {

	public RoleListRes getRole() throws Exception;

	public RoleRes createRole(RoleReq roleReq, String requestUserId)
			throws Exception;

	public int updateRole(int groupCode, int apiCode, int updateApiCode)
			throws Exception;

	public int deleteRole(int groupCode, int apiCode) throws Exception;
}
