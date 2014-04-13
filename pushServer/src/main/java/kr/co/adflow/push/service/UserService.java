package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.ResponseData;

public interface UserService {
	ResponseData get(String userID) throws Exception;

	ResponseData post() throws Exception;

	ResponseData put() throws Exception;

	ResponseData delete() throws Exception;
}
