package kr.co.adflow.push.dao;

import kr.co.adflow.push.domain.ResponseData;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public interface TokenDAO {

	ResponseData get(String token) throws Exception;

	ResponseData post(String token) throws Exception;

	ResponseData put(String token) throws Exception;

	ResponseData delete(String token) throws Exception;

	boolean validate(String token) throws Exception;

}
