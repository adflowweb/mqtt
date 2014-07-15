package kr.co.adflow.push.bsbank.dao.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.bsbank.sms.SMSSender;
import kr.co.adflow.push.dao.impl.ServerDaoImpl;
import kr.co.adflow.push.domain.ServerInfo;
import kr.co.adflow.push.domain.bsbank.BSBankPushServerInfo;

import org.springframework.stereotype.Repository;

/**
 * @author nadir93
 * @date 2014. 7. 15.
 */
@Repository
public class BSBankServerDaoImpl extends ServerDaoImpl {

	@Resource
	SMSDaoImpl smsDaoImpl;

	@Override
	public ServerInfo get() throws Exception {
		ServerInfo info = super.get();
		BSBankPushServerInfo bsbankInfo = new BSBankPushServerInfo(info);
		// SMS 서버 정보 입력
		SMSSender sender = smsDaoImpl.getSMSSender();
		if (sender != null) {
			bsbankInfo.setSms(sender.isAvailable());
		}
		return bsbankInfo;
	}

}
