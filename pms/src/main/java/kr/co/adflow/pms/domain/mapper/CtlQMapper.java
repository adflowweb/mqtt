package kr.co.adflow.pms.domain.mapper;

import kr.co.adflow.pms.domain.CtlQ;



public interface CtlQMapper {

	int insertQ(CtlQ ctlQ);
	CtlQ fetchQ(CtlQ ctlQ);
	int deleteQ(String msgId);

}
