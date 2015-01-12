package kr.co.adflow.push.ktp.service;

import kr.co.adflow.push.domain.ktp.Status;
import kr.co.adflow.push.domain.ktp.Subscribe;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public interface PCFService {

	String[] get(String token) throws Exception;

	Status getStatus(String token) throws Exception;

}
