/*
 * 
 */
package kr.co.adflow.pms.adm.service;

import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.adm.request.AddressReq;
import kr.co.adflow.pms.adm.request.ReservationCancelReq;
import kr.co.adflow.pms.adm.response.MessagesRes;
import kr.co.adflow.pms.domain.Address;

// TODO: Auto-generated Javadoc
/**
 * The Interface SvcService.
 */
public interface SvcService {

	/**
	 * Gets the svc message list.
	 *
	 * @param params the params
	 * @return the svc message list
	 */
	public MessagesRes getSvcMessageList(Map<String, String> params)  throws Exception;

	/**
	 * Gets the svc resevation message list.
	 *
	 * @param params the params
	 * @return the svc resevation message list
	 */
	public MessagesRes getSvcResevationMessageList(Map<String, String> params);

	/**
	 * Cancel reservation list.
	 *
	 * @param appKey the app key
	 * @param reqIds the req ids
	 * @return the int
	 */
	public int cancelReservationList(String appKey, ReservationCancelReq reqIds);

	/**
	 * Gets the month summary.
	 *
	 * @param appKey the app key
	 * @param keyMon the key mon
	 * @return the month summary
	 */
	public List<Map<String, Object>> getMonthSummary(Map<String, String> params,
			String appKey,
			String keyMon);
	
	/**
	 * insert address.
	 *
	 * @param appKey the app key
	 * @param AddressReq the AddressReq
	 * @return the insert count
	 */
	public int addAdress(String appKey,AddressReq addr);
	
	/**
	 * update address.
	 *
	 * @param appKey the app key
	 * @param AddressReq the AddressReq
	 * @return the update count
	 */
	public int updateAdress(String appKey,AddressReq addr) throws Exception;
	
	/**
	 * get address List.
	 *
	 * @param appKey the app key
	 * @param AddressReq the AddressReq
	 * @return the update count
	 */
	public List<Address> getAddressList(String appKey);
	
	/**
	 * delete address.
	 *
	 * @param appKey the app key
	 * @param AddressReq the AddressReq
	 * @return the update count
	 */
	public int deleteAddress(String appKey, String ufmi);
	
	

}
