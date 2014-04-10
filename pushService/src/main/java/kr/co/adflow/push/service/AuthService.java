package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.AuthResponseData;

public interface AuthService {

	AuthResponseData authencate(String tocken) throws Exception;
}
