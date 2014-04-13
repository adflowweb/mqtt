package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.ResponseData;

public interface MessageService {

	ResponseData get(String messageID) throws Exception;

	ResponseData post(Message msg) throws Exception;

	ResponseData put(Message msg) throws Exception;

	ResponseData delete(Message msg) throws Exception;
}
