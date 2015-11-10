package kr.co.adflow.pms.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.exception.GroupRunTimeException;
import kr.co.adflow.pms.core.request.GroupReq;
import kr.co.adflow.pms.core.response.GroupListRes;
import kr.co.adflow.pms.core.response.GroupRes;
import kr.co.adflow.pms.domain.Group;
import kr.co.adflow.pms.domain.mapper.GroupMapper;

@Service
public class GroupServiceImpl implements GroupService {

	@Autowired
	GroupMapper groupMapper;

	@Override
	public GroupRes createGroup(GroupReq groupReq) throws Exception {
		// TODO Auto-generated method stub
		Group group = new Group();
		group.setGroupCode(groupReq.getGroupCode());
		group.setGroupDescription(groupReq.getGroupDescription());
		group.setGroupName(groupReq.getGroupName());

		int insertResult = groupMapper.insertGroup(group);

		if (insertResult < 1) {
			throw new GroupRunTimeException(StaticConfig.ERROR_CODE_579000,
					"그룹생성에 실패하였습니다");
		}
		GroupRes groupRes = new GroupRes();
		groupRes.setGroupCode(groupReq.getGroupCode());
		groupRes.setGroupName(groupReq.getGroupName());

		return groupRes;
	}

	@Override
	public GroupListRes getGroup() throws Exception {
		// TODO Auto-generated method stub
		List<Group> groups = groupMapper.getGroup();
		GroupListRes groupListRes = new GroupListRes();
		groupListRes.setGroup(groups);
		return groupListRes;
	}

	@Override
	public int updateGroup(int groupCode, int updateGroupCode) throws Exception {

		int updateResult = groupMapper.updateGroup(groupCode, updateGroupCode);
		// TODO Auto-generated method stub
		if (updateResult < 1) {
			throw new GroupRunTimeException(StaticConfig.ERROR_CODE_572500,
					"그룹코드 변경에 실패하였습니다");
		}

		return updateResult;
	}

	@Override
	public int deleteGroup(int groupCode) throws Exception {
	
		int deleteResult = groupMapper.deleteGroup(groupCode);
		
		if (deleteResult < 1) {
			throw new GroupRunTimeException(StaticConfig.ERROR_CODE_573500,
					"그룹코드 삭제에 실패하였습니다");
		}

		return deleteResult;

	}

}
