package kr.co.adflow.pms.domain.mapper;

import java.util.List;

import kr.co.adflow.pms.domain.Group;

import org.apache.ibatis.annotations.Param;

public interface GroupMapper {

	int insertGroup(Group group);

	List<Group> getGroup();

	int updateGroup(@Param("groupCode") int groupCode,
			@Param("updateGroupCode") int updateGroupCode);

	int deleteGroup(@Param("groupCode") int groupCode);

}
