package kr.co.adflow.pms.core.service;

public interface PCFService {

	public String getStatus(String token) throws Exception;

	public String[] getTopics(String token) throws Exception;

}