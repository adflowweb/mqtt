package kr.co.adflow.pms.domain.mapper;

import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.domain.Ack;
import kr.co.adflow.pms.domain.AckCallback;

public interface AckMapper {

	int insertAck(Ack ack);
	
	List<AckCallback> getCallbackList(Map<String, Object> params);
	
	int updateAckCallback(Map<String, Object> params);

}
