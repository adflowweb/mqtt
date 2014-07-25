package kr.co.adflow.push.service;

/**
 * @author nadir93
 * @date 2014. 7. 24.
 */
public interface HAService {

	boolean isActive();

	void check() throws Exception;
}
