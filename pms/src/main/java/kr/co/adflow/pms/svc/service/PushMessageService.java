package kr.co.adflow.pms.svc.service;

import kr.co.adflow.pms.svc.request.MessageReq;

public interface PushMessageService {

	String[] sendMessage(String appKey, MessageReq msg);

}
