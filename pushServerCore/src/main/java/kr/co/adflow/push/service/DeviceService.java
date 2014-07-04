package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.Device;

public interface DeviceService {
	Device get(String deviceID) throws Exception;

	int post(Device device) throws Exception;

	int put(Device device) throws Exception;

	int delete(String deviceID) throws Exception;
}
