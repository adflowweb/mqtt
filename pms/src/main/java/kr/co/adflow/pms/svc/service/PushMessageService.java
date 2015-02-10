package kr.co.adflow.pms.svc.service;

import java.util.List;

import kr.co.adflow.pms.domain.MessageResult;
import kr.co.adflow.pms.svc.request.MessageIdsReq;
import kr.co.adflow.pms.svc.request.MessageReq;

public interface PushMessageService {

	public String[] sendMessage(String appKey, MessageReq msg);

	public List<MessageResult> getMessageResult(MessageIdsReq msgIds, String appKey);

}
