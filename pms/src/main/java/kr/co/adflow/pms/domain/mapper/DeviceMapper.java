/*
 * 
 */
package kr.co.adflow.pms.domain.mapper;

import kr.co.adflow.pms.domain.Device;

// TODO: Auto-generated Javadoc
/**
 * The Interface AckMapper.
 */
public interface DeviceMapper {

	/**
	 * Insert ack.
	 * 
	 * @param ack
	 *            the ack
	 * @return the int
	 */
	int insertDevice(Device device);
	
	int deleteDevice(String deviceId);

}
