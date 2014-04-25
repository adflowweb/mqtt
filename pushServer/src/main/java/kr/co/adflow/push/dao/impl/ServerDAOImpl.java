package kr.co.adflow.push.dao.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.dao.ServerDAO;
import kr.co.adflow.push.domain.ServerInfo;
import kr.co.adflow.push.service.MqttService;

import org.springframework.stereotype.Repository;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Repository
public class ServerDAOImpl implements ServerDAO {

	@Resource
	MqttService mqttService;

	@Override
	public ServerInfo get() throws Exception {
		ServerInfo res = new ServerInfo(mqttService.isConnected(),
				mqttService.getErrorMsg());
		return res;
	}

	@Override
	public void post() throws Exception {
		// return null;
	}

	@Override
	public void put() throws Exception {
		// return null;
	}

	@Override
	public void delete() throws Exception {
		// return null;
	}
}
