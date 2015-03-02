package kr.co.adflow.pms.adm.service;

import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.adm.request.MessageReq;

public interface SvcAdmService {
	
	public List<Map<String, String>> sendMessage(String appKey, MessageReq msg);

}
