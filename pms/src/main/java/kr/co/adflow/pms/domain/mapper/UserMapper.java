package kr.co.adflow.pms.domain.mapper;

import java.util.List;

import kr.co.adflow.pms.domain.User;

public interface UserMapper {

	int insertUser(User user);
	
	User select(String userId);

	int updateUser(User user);
	
	int updateUserStatus(User user);

	int deleteUser(String userId);
	
	User selectAuth(User user);
	
	List<User> listAll();
	
	int logUserHistory(User user);

}
