package kr.co.adflow.pms.adm.service;

import java.util.Map;

import kr.co.adflow.pms.adm.response.MessagesRes;

public interface SvcService {
	
	public MessagesRes getSvcMessageList(Map<String,String> params);

}
