package kr.co.adflow.push.mapper;

import kr.co.adflow.push.domain.Role;

public interface RoleMapper {

	Role[] get(String role) throws Exception;

	Role[] getRoles() throws Exception;

}
