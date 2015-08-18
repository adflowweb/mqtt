/*
 * 
 */
package kr.co.adflow.pms.core.service;

// TODO: Auto-generated Javadoc
/**
 * The Interface MessageSendService.
 */
public interface MessageSendService {

	/**
	 * Send message array.
	 * 
	 * @param serverId
	 *            the server id
	 * @param limit
	 *            the limit
	 * @return the int
	 */
	// public int sendMessageArray(String serverId, int limit);
	//
	// /**
	// * Send reservation message array.
	// *
	// * @param serverId the server id
	// * @param limit the limit
	// * @return the int
	// */
	public int sendReservationMessageArray(String serverId, int limit);

	// /**
	// * Send callback.
	// *
	// * @param serverId the server id
	// * @param limit the limit
	// */
	// public void sendCallback(String serverId, int limit);

}
