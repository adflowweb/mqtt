/*
 * 
 */
package kr.co.adflow.pms.domain.mapper;

import java.util.List;

import kr.co.adflow.pms.domain.Address;
import kr.co.adflow.pms.domain.AddressParams;
import kr.co.adflow.pms.domain.CDR;
import kr.co.adflow.pms.domain.CDRParams;
import kr.co.adflow.pms.domain.User;

// TODO: Auto-generated Javadoc
/**
 * The Interface CDRMapper.
 */
public interface AddressMapper {

	
	/**
	 * insert Address.
	 *
	 * @param params the Address
	 * @return the insert count.
	 */
	int insertAddress(Address address);
	
	/**
	 * update Address.
	 *
	 * @param params the Address
	 * @return the update count.
	 */
	int updateAddress(Address address);
	
	
	/**
	 * Select Address.
	 *
	 * @param AddressParams the addressParams
	 * @return the Address
	 */
	Address selectAddress(AddressParams addressParams);
	
	
	/**
	 * delete Address.
	 *
	 * @param AddressParams the addressParams
	 * @return the delete count
	 */
	int deleteAddress(AddressParams addressParams);
	
	/**
	 * Select Address List.
	 *
	 * @param AddressParams the addressParams
	 * @return the Address
	 */
	List<Address> selectAddressList(String userId);
	

}
