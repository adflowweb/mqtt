package kr.co.adflow.pms.adm.service;

import kr.co.adflow.pms.adm.request.AuthReq;
import kr.co.adflow.pms.adm.response.AuthRes;

public interface CommonService {

	AuthRes authUser(AuthReq auth);

}
