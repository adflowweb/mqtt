package kr.co.adflow.push.mapper;

import org.apache.ibatis.annotations.Param;

import kr.co.adflow.push.domain.Device;

public interface DeviceMapper {

	Device get(@Param("userID") String userID,
			@Param("deviceID") String deviceID) throws Exception;

	int increaseUnread(@Param("userID") String userID,
			@Param("deviceID") String deviceID) throws Exception;

	int putUnread(@Param("unRead") int unRead, @Param("userID") String userID,
			@Param("deviceID") String deviceID) throws Exception;

	Device getByUser(String userID) throws Exception;

	Device[] getAllAppleDevices() throws Exception;

	int post(Device device) throws Exception;

	int put(Device device) throws Exception;

	int delete(String deviceID) throws Exception;

}
