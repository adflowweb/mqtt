package kr.co.adflow.pms.core.service;

import kr.co.adflow.pms.core.request.GroupReq;
import kr.co.adflow.pms.core.response.GroupListRes;
import kr.co.adflow.pms.core.response.GroupRes;

public interface GroupService {

	public GroupRes createGroup(GroupReq groupReq) throws Exception;

	public GroupListRes getGroup() throws Exception;

	public int updateGroup(int groupCode, int updateGroupCode) throws Exception;

	public int deleteGroup(int groupCode) throws Exception;

}
