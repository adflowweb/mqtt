package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.ResponseData;

public interface TokenService {

	ResponseData get(String token) throws Exception;

	ResponseData post(String token) throws Exception;

	ResponseData put(String token) throws Exception;

	ResponseData delete(String token) throws Exception;

	ResponseData validate(String token) throws Exception;
}
