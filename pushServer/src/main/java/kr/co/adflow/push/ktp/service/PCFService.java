/*
 * 
 */
package kr.co.adflow.push.ktp.service;

import kr.co.adflow.push.domain.ktp.Status;

// TODO: Auto-generated Javadoc
/**
 * The Interface PCFService.
 *
 * @author nadir93
 * @date 2014. 4. 14.
 */
public interface PCFService {

	/**
	 * Gets the.
	 *
	 * @param token
	 *            the token
	 * @return the string[]
	 * @throws Exception
	 *             the exception
	 */
	String[] get(String token) throws Exception;

	/**
	 * Gets the status.
	 *
	 * @param token
	 *            the token
	 * @return the status
	 * @throws Exception
	 *             the exception
	 */
	Status getStatus(String token) throws Exception;

}
