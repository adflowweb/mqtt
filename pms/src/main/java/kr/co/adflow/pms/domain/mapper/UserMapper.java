package kr.co.adflow.pms.domain.mapper;

import kr.co.adflow.pms.domain.User;

public interface UserMapper {

	int insertUser(User user);

	int updateUserStatus(User user);

	User select(String userId);

}
