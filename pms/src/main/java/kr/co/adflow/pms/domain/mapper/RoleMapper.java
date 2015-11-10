package kr.co.adflow.pms.domain.mapper;

import java.util.List;

import kr.co.adflow.pms.domain.Role;

import org.apache.ibatis.annotations.Param;

public interface RoleMapper {
	public int insertRole(Role role);

	public List<Role> getRole();

	public int updateRole(@Param("groupCode") int groupCode,
			@Param("apiCode") int apiCode,
			@Param("updateApiCode") int updateApiCode);

	public int deleteRole(@Param("groupCode") int groupCode,
			@Param("apiCode") int apiCode);
}
