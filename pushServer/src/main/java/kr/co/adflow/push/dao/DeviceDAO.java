package kr.co.adflow.push.dao;

import kr.co.adflow.push.domain.Device;

/**
 * @author nadir93
 * @date 2014. 4. 28.
 * 
 */
public interface DeviceDAO {
	Device get(String deviceID) throws Exception;

	int post(Device device) throws Exception;

	int put(Device device) throws Exception;

	int delete(String deviceID) throws Exception;
}
