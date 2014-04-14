package kr.co.adflow.push.dao.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.dao.ServerDAO;
import kr.co.adflow.push.domain.ResponseData;
import kr.co.adflow.push.domain.ServerInfoResponseData;
import kr.co.adflow.push.service.MqttService;

import org.springframework.stereotype.Component;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Component
public class ServerDAOImpl implements ServerDAO {

	@Resource
	MqttService mqttService;

	@Override
	public ResponseData get() throws Exception {
		ServerInfoResponseData res = new ServerInfoResponseData(
				mqttService.isConnected(), mqttService.getErrorMsg());
		return res;
	}

	@Override
	public ResponseData post() throws Exception {
		return null;
	}

	@Override
	public ResponseData put() throws Exception {
		return null;
	}

	@Override
	public ResponseData delete() throws Exception {
		return null;
	}
}
