package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.ResponseData;

public interface GroupService {

	ResponseData get(String groupID) throws Exception;

	ResponseData post() throws Exception;

	ResponseData put() throws Exception;

	ResponseData delete() throws Exception;

}
