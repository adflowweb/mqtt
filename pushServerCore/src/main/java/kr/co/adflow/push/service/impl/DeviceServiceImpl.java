package kr.co.adflow.push.service.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.dao.DeviceDao;
import kr.co.adflow.push.domain.Device;
import kr.co.adflow.push.service.DeviceService;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author nadir93
 * @date 2014. 4. 28.
 * 
 */
@Service
public class DeviceServiceImpl implements DeviceService {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(UserServiceImpl.class);

	@Resource
	DeviceDao deviceDao;

	@Override
	public Device get(String deviceID) throws Exception {
		return null;
	}

	@Override
	public int post(Device device) throws Exception {
		return 0;
	}

	@Override
	public int put(Device device) throws Exception {
		return 0;
	}

	@Override
	public int delete(String deviceID) throws Exception {
		logger.debug("delete시작(deviceID=" + deviceID + ")");
		int result = deviceDao.delete(deviceID);
		logger.debug("delete종료(result=" + result + ")");
		return result;
	}

}
