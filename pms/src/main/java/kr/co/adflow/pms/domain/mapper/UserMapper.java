package kr.co.adflow.pms.domain.mapper;

import java.util.List;
import java.util.Map;

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
	
	int getMsgCntLimit(String userId);
	
	int updateMsgCntLimit(User user);
	
	int discountMsgCntLimit(User user);

}
