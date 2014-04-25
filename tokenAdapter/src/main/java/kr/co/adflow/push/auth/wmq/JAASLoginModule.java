package kr.co.adflow.push.auth.wmq;

import java.util.Map;

import javax.net.ssl.SSLSession;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.TextInputCallback;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import kr.co.adflow.jersey.RestClient;
import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.mq.mqxr.AuthCallback;

/**
 * Requires .../SDK/MQServer/MQXR.jar on the classpath to build.
 */

/**
 * IBM JAAS 인증모듈
 * 
 * @author nadir93
 */
public class JAASLoginModule implements LoginModule {

	private static final Logger logger = LoggerFactory
			.getLogger(JAASLoginModule.class);

	private Subject subject;
	private CallbackHandler callbackHandler;
	private Map<String, ?> sharedState;
	private Map<String, ?> options;
	private static RestClient client = new RestClient();

	JAASPrincipal principal;
	boolean loggedIn = false;

	public JAASLoginModule() {
		logger.debug("JAASLoginModule초기화되었습니다." + this);
		logger.debug("thread=" + Thread.currentThread());
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
		logger.debug("initialize시작(subject=" + subject + "|callbackHandler="
				+ callbackHandler + "|sharedState=" + sharedState + "|options="
				+ options + ")");
		this.subject = subject;
		this.callbackHandler = callbackHandler;
		this.sharedState = sharedState;
		this.options = options;
		logger.debug("thread=" + Thread.currentThread());
		logger.debug("initialize종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#login()
	 */
	public boolean login() throws LoginException {
		logger.debug("login시작()");
		logger.debug("thread=" + Thread.currentThread());

		TextInputCallback validPromptsCallback = new TextInputCallback(
				"ValidPrompts");
		TextInputCallback clientIdentifierCallback = new TextInputCallback(
				"ClientIdentifier");
		AuthCallback xrCallback = new AuthCallback();

		// NameCallback nameCallback = new NameCallback("NameCallback");
		// PasswordCallback passwordCallback = new PasswordCallback(
		// "PasswordCallback", false);

		//
		// TextInputCallback networkAddressCallback = new TextInputCallback(
		// "NetworkAddress");
		// TextInputCallback channelNameCallback = new TextInputCallback(
		// "ChannelName");

		try {
			callbackHandler.handle(new Callback[] { validPromptsCallback,
					clientIdentifierCallback, xrCallback
			// ,nameCallback,
			// passwordCallback, ,
			// networkAddressCallback,
			// channelNameCallback
					});
			String validPrompts = validPromptsCallback.getText();
			logger.debug("validPrompts=" + validPrompts);
			String clientId = clientIdentifierCallback.getText();
			logger.debug("clientId=" + clientId);
			SSLSession sslSession = xrCallback.getSSLSession();
			logger.debug("sslSession=" + sslSession);

			// String username = nameCallback.getName();
			// logger.debug("username=" + username);
			// char[] password = passwordCallback.getPassword();
			// logger.debug("password="
			// + (password != null ? new String(password) : password));
			// String networkAddress = networkAddressCallback.getText();
			// logger.debug("networkAddress=" + networkAddress);
			// String channelName = channelNameCallback.getText();
			// logger.debug("channelName=" + channelName);
			// if (sslSession != null) {
			// // Assuming the clients certificate was created with a
			// // distinguished name of the form:
			// // "CN=ClientId:userName, OU=MQ, O=IBM, C=UK"
			// X500Principal peerPrincipal = (X500Principal) sslSession
			// .getPeerPrincipal();
			// String[] dnFields = peerPrincipal.getName().split(",");
			// String[] cn = dnFields[0].substring(3, dnFields[0].length())
			// .split(":");
			// // ClientIdentifier used by the connecting client.
			// clientIdentifierCallback.setText(cn[0]);
			// // User name to be used by the connecting client.
			// nameCallback.setName(cn[1]);
			// // The channel definitions, MCAUSER( ) and USECLTID(NO) are
			// // applied using the new clientIdentifier and userNname.
			// principal = new JAASPrincipal(cn[1]);
			// } else {
			// principal = new JAASPrincipal(username);
			// }

			// 인증요청
			Response response = client.validate(clientId);
			logger.debug("토큰유효성체크결과=" + response);
			Validation data = (Validation) response.getResult().getData();
			if (data.isValidation()) {
				loggedIn = true;
			} else
				throw new FailedLoginException("Login failed");
			// principal = new JAASPrincipal(username);
		} catch (Exception e) {
			logger.error("에러발생", e);
			throw new LoginException(e.toString());
		}
		logger.debug("login종료(loggedIn=" + loggedIn + ")");
		return loggedIn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#abort()
	 */
	public boolean abort() throws LoginException {
		logger.debug("abort시작()");
		logger.debug("thread=" + Thread.currentThread());
		logout();
		logger.debug("abort종료(true)");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#commit()
	 */
	public boolean commit() throws LoginException {
		logger.debug("commit시작()");
		logger.debug("thread=" + Thread.currentThread());
		if (loggedIn && principal != null) {
			if (!subject.getPrincipals().contains(principal)) {
				subject.getPrincipals().add(principal);
				logger.debug("principal이추가되었습니다");
			}
		}
		logger.debug("commit종료(true)");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#logout()
	 */
	public boolean logout() throws LoginException {
		logger.debug("logout시작()");
		logger.debug("thread=" + Thread.currentThread());
		if (principal != null) {
			subject.getPrincipals().remove(principal);
			logger.debug("principal이제거되었습니다");
			principal = null;
		}
		loggedIn = false;
		logger.debug("logout종료()");
		return true;
	}
}
