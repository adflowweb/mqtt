package kr.co.adflow.push.dao.impl;

import kr.co.adflow.push.dao.DeviceDAO;
import kr.co.adflow.push.domain.Device;
import kr.co.adflow.push.mapper.DeviceMapper;

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
public class DeviceDAOImpl implements DeviceDAO {
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(DeviceDAOImpl.class);
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

}
