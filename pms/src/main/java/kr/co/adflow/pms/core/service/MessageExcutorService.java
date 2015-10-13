/*
 * 
 */
package kr.co.adflow.pms.core.service;

// TODO: Auto-generated Javadoc
/**
 * The Interface MessageSendService.
 */
public interface MessageExcutorService {

	/**
	 * Send message array.
	 * 
	 * @param serverId
	 *            the server id
	 * @param limit
	 *            the limit
	 * @return the int
	 */
	public int sendReservationMessageArray(String serverId, int limit);

}
