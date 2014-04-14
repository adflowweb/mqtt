package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.ResponseData;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public interface ServerService {
	ResponseData get() throws Exception;

	ResponseData post() throws Exception;

	ResponseData put() throws Exception;

	ResponseData delete() throws Exception;
}
