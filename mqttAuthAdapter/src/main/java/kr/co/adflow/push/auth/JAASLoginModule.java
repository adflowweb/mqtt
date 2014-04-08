package kr.co.adflow.push.auth;

import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.net.ssl.SSLSession;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextInputCallback;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import javax.security.auth.x500.X500Principal;

import com.ibm.mq.mqxr.AuthCallback;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/* @start_prolog@
 * -----------------------------------------------------------------
 * IBM Websphere MQ MQTT Sample JAAS LoginModume
 * Version: @(#) MQMBID sn=p750-001-130308 su=_h5hlwIgCEeKYL-Plq9baPw pn=com.ibm.mq.mqxr.listener/listenerSamplesSource/samples/JAASLoginModule.java  
 *
 *   <copyright 
 *   notice="lm-source-program" 
 *   pids="5724-H72," 
 *   years="2009,2012" 
 *   crc="1623246244" > 
 *   Licensed Materials - Property of IBM  
 *    
 *   5724-H72, 
 *    
 *   (C) Copyright IBM Corp. 2009, 2012 All Rights Reserved.  
 *    
 *   US Government Users Restricted Rights - Use, duplication or  
 *   disclosure restricted by GSA ADP Schedule Contract with  
 *   IBM Corp.  
 *   </copyright> 
 * -----------------------------------------------------------------
 */
/**
 * Requires .../SDK/MQServer/MQXR.jar on the classpath to build.
 */
public class JAASLoginModule implements LoginModule {

	private static Logger logger = Logger.getLogger("JAASLoginModule");
	private Level logLevel = Level.SEVERE;

	private Subject subject;
	private CallbackHandler callbackHandler;
	private Map<String, ?> sharedState;
	private Map<String, ?> options;

	JAASPrincipal principal;
	boolean loggedIn = false;

	public JAASLoginModule() {
		logger.info("JAASLoginModule초기화되었습니다." + this);
		logger.info("thread=" + Thread.currentThread());
	}

	private void setMqttClientLog() {
		// logger.debug("setMqttClientLog시작()");
		Handler defaultHandler = new ConsoleHandler();
		LogManager logManager = LogManager.getLogManager();
		Logger rootLogger = Logger.getLogger("kr.co.adflow.push.auth");
		defaultHandler.setFormatter(new SimpleFormatter());
		defaultHandler.setLevel(logLevel);
		rootLogger.setLevel(logLevel);
		rootLogger.addHandler(defaultHandler);
		logManager.addLogger(rootLogger);
		// logger.debug("setMqttClientLog종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.security.auth.spi.LoginModule#initialize(javax.security.auth.Subject
	 * , javax.security.auth.callback.CallbackHandler, java.util.Map,
	 * java.util.Map)
	 */
	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map<String, ?> sharedState, Map<String, ?> options) {
		logger.info("initialize시작(subject=" + subject + "|callbackHandler="
				+ callbackHandler + "|sharedState=" + sharedState + "|options="
				+ options + ")");
		this.subject = subject;
		this.callbackHandler = callbackHandler;
		this.sharedState = sharedState;
		this.options = options;
		logger.info("thread=" + Thread.currentThread());
		logger.info("initialize종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#login()
	 */
	public boolean login() throws LoginException {
		logger.info("login시작()");
		logger.info("thread=" + Thread.currentThread());
		NameCallback nameCallback = new NameCallback("NameCallback");
		PasswordCallback passwordCallback = new PasswordCallback(
				"PasswordCallback", false);
		TextInputCallback validPromptsCallback = new TextInputCallback(
				"ValidPrompts");
		TextInputCallback clientIdentifierCallback = new TextInputCallback(
				"ClientIdentifier");
		TextInputCallback networkAddressCallback = new TextInputCallback(
				"NetworkAddress");
		TextInputCallback channelNameCallback = new TextInputCallback(
				"ChannelName");
		AuthCallback xrCallback = new AuthCallback();

		try {
			callbackHandler.handle(new javax.security.auth.callback.Callback[] {
					nameCallback, passwordCallback, validPromptsCallback,
					clientIdentifierCallback, networkAddressCallback,
					channelNameCallback, xrCallback });
			String username = nameCallback.getName();
			logger.info("username=" + username);
			char[] password = passwordCallback.getPassword();
			logger.info("password="
					+ (password != null ? new String(password) : password));
			String validPrompts = validPromptsCallback.getText();
			String clientId = clientIdentifierCallback.getText();
			String networkAddress = networkAddressCallback.getText();
			String channelName = channelNameCallback.getText();
			SSLSession sslSession = xrCallback.getSSLSession();
			// Note that if multiple nameCallBack or clientIdentifierCallback
			// callback's
			// are used callbackHandler.handle will throw
			// UnsupportedCallbackException.

			if (sslSession != null) {
				// Assuming the clients certificate was created with a
				// distinguished name of the form:
				// "CN=ClientId:userName, OU=MQ, O=IBM, C=UK"
				X500Principal peerPrincipal = (X500Principal) sslSession
						.getPeerPrincipal();
				String[] dnFields = peerPrincipal.getName().split(",");
				String[] cn = dnFields[0].substring(3, dnFields[0].length())
						.split(":");
				// ClientIdentifier used by the connecting client.
				clientIdentifierCallback.setText(cn[0]);
				// User name to be used by the connecting client.
				nameCallback.setName(cn[1]);
				// The channel definitions, MCAUSER( ) and USECLTID(NO) are
				// applied using the new clientIdentifier and userNname.
				principal = new JAASPrincipal(cn[1]);
			} else {
				principal = new JAASPrincipal(username);
			}

			// Accept everything.
			if (true) {
				// todo 푸시서버에 인증요청을 보내고 결과를 리턴한다.
				logger.info("푸시서버에 인증요청을 보내고 결과를 리턴한다.");
				ClientConfig clientConfig = new DefaultClientConfig();
				Client client = Client.create();
				WebResource webResource = client.resource(
						"http://192.168.0.60:8080/users/").queryParam(
						"clientID", new String(password));

				ClientResponse response = webResource
						.accept("application/json").get(ClientResponse.class);

				if (response.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ response.getStatus());
				}

				String output = response.getEntity(String.class);

				System.out.println("Output from Server .... \n");
				System.out.println(output);
				logger.info("rest호출종료");
				loggedIn = true;
			} else
				throw new FailedLoginException("Login failed");

			principal = new JAASPrincipal(username);

		} catch (Exception exception) {
			logger.log(Level.SEVERE, exception.getMessage(), exception);
			throw new LoginException(exception.toString());
		}

		logger.info("login종료(loggedIn=" + loggedIn + ")");
		return loggedIn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#abort()
	 */
	public boolean abort() throws LoginException {
		logger.info("abort시작()");
		logger.info("thread=" + Thread.currentThread());
		logout();
		logger.info("abort종료(true)");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#commit()
	 */
	public boolean commit() throws LoginException {
		logger.info("commit시작()");
		logger.info("thread=" + Thread.currentThread());
		if (loggedIn) {
			if (!subject.getPrincipals().contains(principal))
				subject.getPrincipals().add(principal);
		}
		logger.info("commit종료(true)");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#logout()
	 */
	public boolean logout() throws LoginException {
		logger.info("logout시작()");
		logger.info("thread=" + Thread.currentThread());
		subject.getPrincipals().remove(principal);
		principal = null;
		loggedIn = false;
		logger.info("logout종료()");
		return true;
	}
}
