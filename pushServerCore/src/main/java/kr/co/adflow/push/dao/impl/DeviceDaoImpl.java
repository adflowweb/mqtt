package kr.co.adflow.push.dao.impl;

import kr.co.adflow.push.dao.DeviceDao;
import kr.co.adflow.push.domain.Device;
import kr.co.adflow.push.domain.User;
import kr.co.adflow.push.mapper.DeviceMapper;
import kr.co.adflow.push.mapper.UserMapper;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author nadir93
 * @date 2014. 4. 28.
 * 
 */
@Repository
public class DeviceDaoImpl implements DeviceDao {
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(DeviceDaoImpl.class);
	// Autowired를 사용하여 sqlSession을 사용할수 있다.
	@Autowired
	private SqlSession sqlSession;

	@Override
	public Device get(String deviceID) throws Exception {
		return null;
	}

	@Override
	public int post(Device device) throws Exception {
		logger.debug("post시작(device=" + device + ")");
		DeviceMapper deviceMapper = sqlSession.getMapper(DeviceMapper.class);
		int result = deviceMapper.post(device);
		logger.debug("post종료(updates=" + device + ")");
		return result;
	}

	@Override
	public int put(Device device) throws Exception {
		return 0;
	}

	@Override
	public int delete(String deviceID) throws Exception {
		logger.debug("delete시작(deviceID=" + deviceID + ")");
		DeviceMapper deviceMapper = sqlSession.getMapper(DeviceMapper.class);
		int result = deviceMapper.delete(deviceID);
		logger.debug("delete종료(result=" + result + ")");
		return result;
	}

	@Override
	public Device[] getByUser(String userID) throws Exception {
		logger.debug("getByUser시작(userID=" + userID + ")");
		DeviceMapper dvcMapper = sqlSession.getMapper(DeviceMapper.class);
		Device[] devices = dvcMapper.getAppleDevicesByUser(userID);
		logger.debug("getByUser종료(devices=" + devices + ")");
		return devices;
	}

}
