/*
 * 
 */
package kr.co.adflow.push.dao;

import kr.co.adflow.push.domain.HA;

// TODO: Auto-generated Javadoc
/**
 * The Interface HADao.
 *
 * @author nadir93
 * @date 2014. 7. 24.
 */
public interface HADao {

	/**
	 * Gets the.
	 *
	 * @return the ha
	 * @throws Exception the exception
	 */
	HA get() throws Exception;

	/**
	 * Put.
	 *
	 * @param ha the ha
	 * @return the int
	 * @throws Exception the exception
	 */
	int put(HA ha) throws Exception;

	/**
	 * Post.
	 *
	 * @param ha the ha
	 * @return the int
	 * @throws Exception the exception
	 */
	int post(HA ha) throws Exception;
}
