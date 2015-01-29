package kr.co.adflow.pms.adm.service;

import kr.co.adflow.pms.adm.request.AuthReq;
import kr.co.adflow.pms.adm.response.AuthRes;

public class CommonServiceImpl implements CommonService {

	@Override
	public AuthRes authUser(AuthReq auth) {

		AuthRes res = null;
		// 1. password hashing
		// 2. userId/password 로 조회
		// 3. 정상 사용자의 경우 token 해싱
		// 4 , expired_time 현재로 부터 30분
		// 5. token 테이블 저장

		return res;
	}

}
