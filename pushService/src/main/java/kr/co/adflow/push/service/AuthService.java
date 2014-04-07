package kr.co.adflow.push.service;

public interface AuthService {

	boolean authencate(String userID, String clientID) throws Exception;
}
