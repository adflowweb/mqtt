/*
 * 
 */
package kr.co.adflow.push.service.impl;

import javax.annotation.Resource;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.adflow.push.dao.ServerDao;
import kr.co.adflow.push.domain.ServerInfo;
import kr.co.adflow.push.service.ServerService;

// TODO: Auto-generated Javadoc
/**
 * The Class ServerServiceImpl.
 *
 * @author nadir93
 * @date 2014. 4. 14.
 */
@Service
public class ServerServiceImpl implements ServerService {

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(ServerServiceImpl.class);

	/** The server dao. */
	@Resource
	ServerDao serverDao;

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.service.ServerService#get()
	 */
	@Override
	public ServerInfo get() throws Exception {
		return serverDao.get();
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.service.ServerService#post()
	 */
	@Override
	public void post() throws Exception {
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.service.ServerService#put()
	 */
	@Override
	public void put() throws Exception {
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.service.ServerService#delete()
	 */
	@Override
	public void delete() throws Exception {
	}
}
