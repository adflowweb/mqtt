package kr.co.adflow.push.dao;

import kr.co.adflow.push.domain.ServerInfo;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public interface ServerDAO {
	ServerInfo get() throws Exception;

	void post() throws Exception;

	void put() throws Exception;

	void delete() throws Exception;
}
