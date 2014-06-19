package kr.co.adflow.push.mapper;

import kr.co.adflow.push.domain.Group;

public interface GroupMapper {

	Group[] get(String userID) throws Exception;

	int post(Group grp) throws Exception;

	int delete(String userID, String group) throws Exception;

}
