/*
 * 
 */
package kr.co.adflow.push.service;

// TODO: Auto-generated Javadoc
/**
 * The Interface HAService.
 *
 * @author nadir93
 * @date 2014. 7. 24.
 */
public interface HAService {

	/**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
	boolean isActive();

	/**
	 * Check.
	 *
	 * @throws Exception the exception
	 */
	void check() throws Exception;
}
