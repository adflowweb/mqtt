package kr.co.adflow.pms.adm.service;

import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.adm.request.MessageReq;
import kr.co.adflow.pms.adm.response.MessagesRes;

public interface SvcService {
	
	public MessagesRes getSvcMessageList(Map<String,String> params);
	
	public MessagesRes getSvcResevationMessageList(Map<String,String> params);

	public List<Map<String, String>> sendMessage(String appKey, MessageReq msg);

}
