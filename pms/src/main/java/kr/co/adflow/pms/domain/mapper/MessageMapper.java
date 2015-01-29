package kr.co.adflow.pms.domain.mapper;

import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.domain.Message;

public interface MessageMapper {

	int insertMessage(Message msg);

	int insertContent(Message msg);

	Message select(String msgId);

	List<Message> selectList(Map<String, Object> param);

	int updateStatus(Message msg);

}
