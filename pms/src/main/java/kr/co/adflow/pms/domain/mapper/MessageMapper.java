package kr.co.adflow.pms.domain.mapper;

import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.MsgIdsParams;
import kr.co.adflow.pms.domain.MsgParams;

public interface MessageMapper {

	int insertMessage(Message msg);

	int insertContent(Message msg);

	Message select(String msgId);

	List<Message> selectList(Map<String, Object> params);

	int updateStatus(Message msg);
	
	List<Message> selectReservationList(Map<String, Object> params);
	
	List<Message> getSvcMessageList(MsgParams params);
	
	int getSvcMessageListCnt(MsgParams params);
	
	List<Message> getSvcResevationMessageList(MsgParams params);
	
	int getSvcResevationMessageListCnt(MsgParams params);
	
	List<Message> getMessageResult(MsgIdsParams param);
	
	int cancelMessage(Message msg);
	
	int cancelReservationList(MsgIdsParams params);

}
