/*
 * 
 */
package kr.co.adflow.push.service.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.dao.DeviceDao;
import kr.co.adflow.push.domain.Device;
import kr.co.adflow.push.service.DeviceService;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class DeviceServiceImpl.
 *
 * @author nadir93
 * @date 2014. 4. 28.
 */
@Service
public class DeviceServiceImpl implements DeviceService {

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(UserServiceImpl.class);

	/** The device dao. */
	@Resource
	DeviceDao deviceDao;

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.service.DeviceService#get(java.lang.String)
	 */
	@Override
	public Device get(String deviceID) throws Exception {
		return null;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.service.DeviceService#post(kr.co.adflow.push.domain.Device)
	 */
	@Override
	public int post(Device device) throws Exception {
		return 0;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.service.DeviceService#put(kr.co.adflow.push.domain.Device)
	 */
	@Override
	public int put(Device device) throws Exception {
		return 0;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.service.DeviceService#delete(java.lang.String)
	 */
	@Override
	public int delete(String deviceID) throws Exception {
		logger.debug("delete시작(deviceID=" + deviceID + ")");
		int result = deviceDao.delete(deviceID);
		logger.debug("delete종료(result=" + result + ")");
		return result;
	}

}
