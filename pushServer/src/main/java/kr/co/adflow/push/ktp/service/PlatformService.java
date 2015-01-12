package kr.co.adflow.push.ktp.service;

import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.ktp.Status;
import kr.co.adflow.push.domain.ktp.Subscribe;
import kr.co.adflow.push.domain.ktp.request.DigInfo;
import kr.co.adflow.push.domain.ktp.request.FwInfo;
import kr.co.adflow.push.domain.ktp.request.KeepAliveTime;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public interface PlatformService {

	public void sendPrecheck(String topicName) ;
	
	public void modifyFwInfo(FwInfo fwInfo);
	
	public void modifyDigInfo(DigInfo gigInfo);
	
	public void sendMessage(Message message);
	
	public void modifyKeepAliveTime(KeepAliveTime keepAliveTime);
	
	public void sendUserMessage(Message message);

}
