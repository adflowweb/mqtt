package kr.co.adflow.pms.core.service;

public interface MessageSendService {

	public int sendMessageArray(String serverId, int limit);
	
	public int sendReservationMessageArray(String serverId, int limit);

}
