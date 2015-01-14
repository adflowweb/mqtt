/*
 * 
 */
package kr.co.adflow.push.dao.impl;

import kr.co.adflow.push.dao.DeviceDao;
import kr.co.adflow.push.domain.Device;
import kr.co.adflow.push.mapper.DeviceMapper;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

// TODO: Auto-generated Javadoc
/**
 * The Class DeviceDaoImpl.
 *
 * @author nadir93
 * @date 2014. 4. 28.
 */
@Repository
public class DeviceDaoImpl implements DeviceDao {
	
	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(DeviceDaoImpl.class);
	// Autowired를 사용하여 sqlSession을 사용할수 있다.
	/** The sql session. */
	@Autowired
	private SqlSession sqlSession;

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.dao.DeviceDao#get(java.lang.String)
	 */
	@Override
	public Device get(String deviceID) throws Exception {
		return null;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.dao.DeviceDao#post(kr.co.adflow.push.domain.Device)
	 */
	@Override
	public int post(Device device) throws Exception {
		logger.debug("post시작(device=" + device + ")");
		DeviceMapper deviceMapper = sqlSession.getMapper(DeviceMapper.class);
		int result = deviceMapper.post(device);
		logger.debug("post종료(insert=" + device + ")");
		return result;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.dao.DeviceDao#put(kr.co.adflow.push.domain.Device)
	 */
	@Override
	public int put(Device device) throws Exception {
		return 0;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.dao.DeviceDao#delete(java.lang.String)
	 */
	@Override
	public int delete(String deviceID) throws Exception {
		logger.debug("delete시작(deviceID=" + deviceID + ")");
		DeviceMapper deviceMapper = sqlSession.getMapper(DeviceMapper.class);
		int result = deviceMapper.delete(deviceID);
		logger.debug("delete종료(result=" + result + ")");
		return result;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.dao.DeviceDao#getByUser(java.lang.String)
	 */
	@Override
	public Device getByUser(String userID) throws Exception {
		logger.debug("getByUser시작(userID=" + userID + ")");
		DeviceMapper dvcMapper = sqlSession.getMapper(DeviceMapper.class);
		Device device = dvcMapper.getByUser(userID);
		logger.debug("getByUser종료(devices=" + device + ")");
		return device;
	}

}
