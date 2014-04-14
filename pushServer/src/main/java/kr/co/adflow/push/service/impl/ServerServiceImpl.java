package kr.co.adflow.push.service.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.dao.ServerDAO;
import kr.co.adflow.push.domain.ResponseData;
import kr.co.adflow.push.service.ServerService;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Service
public class ServerServiceImpl implements ServerService {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(ServerServiceImpl.class);

	@Resource
	ServerDAO serverDao;

	@Override
	public ResponseData get() throws Exception {
		return serverDao.get();
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
