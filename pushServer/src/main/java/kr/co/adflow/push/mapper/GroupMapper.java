package kr.co.adflow.push.mapper;

import kr.co.adflow.push.domain.Topic;

public interface GroupMapper {

	Topic[] get(String userID) throws Exception;

	int post(Topic grp) throws Exception;

	int delete(String userID, String group) throws Exception;

}
