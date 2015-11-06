package kr.co.adflow.pms.core.service;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.exception.RoleRunTimeException;
import kr.co.adflow.pms.core.request.RoleReq;
import kr.co.adflow.pms.core.response.RoleRes;
import kr.co.adflow.pms.domain.Role;
import kr.co.adflow.pms.domain.mapper.RoleMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleMapper roleMapper;

	@Override
	public RoleRes createRole(RoleReq roleReq, String requsetUserId)
			throws Exception {
		// TODO Auto-generated method stub

		Role role = new Role();

		role.setApiCode(roleReq.getApiCode());
		role.setApiMethod(roleReq.getApiMethod());
		role.setApiName(roleReq.getApiName());
		role.setApiUrl(roleReq.getApiUrl());
		role.setGroupCode(roleReq.getGroupCode());
		role.setIssueId(requsetUserId);

		int insertResult = roleMapper.insertRole(role);

		if (insertResult < 1) {
			throw new RoleRunTimeException(StaticConfig.ERROR_CODE_583500,
					"권한API 등록에 실패 하였습니다.");
		}

		RoleRes roleRes = new RoleRes();

		roleRes.setApiCode(roleReq.getApiCode());
		roleRes.setApiMethod(roleReq.getApiMethod());
		roleRes.setApiName(roleReq.getApiName());
		roleRes.setApiUrl(roleReq.getApiUrl());
		roleRes.setGroupCode(roleReq.getGroupCode());

		return roleRes;
	}

}
