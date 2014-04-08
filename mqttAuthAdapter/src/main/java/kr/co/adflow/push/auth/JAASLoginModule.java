package kr.co.adflow.push.auth;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.SSLSession;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextInputCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import javax.security.auth.x500.X500Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.mq.mqxr.AuthCallback;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

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

	private static Properties prop = new Properties();

	static {
		try {
			prop.load(JAASLoginModule.class
					.getResourceAsStream("/config.properties"));
			logger.debug("properties=" + prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Subject subject;
	private CallbackHandler callbackHandler;
	private Map<String, ?> sharedState;
	private Map<String, ?> options;

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
			logger.debug("username=" + username);
			char[] password = passwordCallback.getPassword();
			logger.debug("password="
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

			logger.debug("sslSession=" + sslSession);
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
			// if (true) {
			// todo 푸시서버에 인증요청을 보내고 결과를 리턴한다.
			logger.debug("인증요청을전송합니다.");
			ClientConfig clientConfig = new DefaultClientConfig();
			Client client = Client.create();
			WebResource webResource = client.resource(
					"http://" + prop.getProperty("server") + ":"
							+ prop.getProperty("server.port") + "/push/users/"
							+ username).queryParam("clientID",
					new String(password));

			ClientResponse response = webResource.accept("application/json")
					.get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			String output = response.getEntity(String.class);

			logger.debug("Output from Server .... \n");
			logger.debug(output);
			loggedIn = true;
			// } else
			// throw new FailedLoginException("Login failed");

			principal = new JAASPrincipal(username);

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
		if (loggedIn) {
			if (!subject.getPrincipals().contains(principal))
				subject.getPrincipals().add(principal);
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
		subject.getPrincipals().remove(principal);
		principal = null;
		loggedIn = false;
		logger.debug("logout종료()");
		return true;
	}
}
