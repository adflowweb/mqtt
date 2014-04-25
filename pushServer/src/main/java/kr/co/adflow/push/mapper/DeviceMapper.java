package kr.co.adflow.push.mapper;

import kr.co.adflow.push.domain.Device;

public interface DeviceMapper {

	Device get(String deviceID) throws Exception;

	void post(Device device) throws Exception;

	void put(Device device) throws Exception;

	void delete(String deviceID) throws Exception;

}
