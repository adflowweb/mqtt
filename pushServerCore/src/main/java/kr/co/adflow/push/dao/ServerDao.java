/*
 * 
 */
package kr.co.adflow.push.dao;

import kr.co.adflow.push.domain.ServerInfo;

// TODO: Auto-generated Javadoc
/**
 * The Interface ServerDao.
 *
 * @author nadir93
 * @date 2014. 4. 14.
 */
public interface ServerDao {

	/**
	 * Gets the.
	 *
	 * @return the server info
	 * @throws Exception
	 *             the exception
	 */
	ServerInfo get() throws Exception;

	/**
	 * Post.
	 *
	 * @throws Exception
	 *             the exception
	 */
	void post() throws Exception;

	/**
	 * Put.
	 *
	 * @throws Exception
	 *             the exception
	 */
	void put() throws Exception;

	/**
	 * Delete.
	 *
	 * @throws Exception
	 *             the exception
	 */
	void delete() throws Exception;
}
