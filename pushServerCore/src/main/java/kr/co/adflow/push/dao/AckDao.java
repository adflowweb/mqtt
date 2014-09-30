package kr.co.adflow.push.dao;

import kr.co.adflow.push.domain.Acknowledge;

/**
 * @author nadir93
 * 
 */
public interface AckDao {

	Acknowledge[] get(int msgID) throws Exception;

}
