/*
 * 
 */
package kr.co.adflow.push.handler;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.ibatis.session.SqlSession;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.adflow.push.dao.MessageDao;
import kr.co.adflow.push.mapper.MessageMapper;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractMqttServiceImpl.
 *
 * @author nadir93
 * @date 2014. 3. 21.
 */
@Service
public class MqttSessionCleanClient {

	/** The Constant CONFIG_PROPERTIES. */
	private static final String CONFIG_PROPERTIES = "/config.properties";

	/** The formatter. */
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MqttSessionCleanClient.class);

	public static int MQCONNCOUNT = 0;

	/** The prop. */
	private static Properties prop = new Properties();

	static {
		try {
			prop.load(MqttSessionCleanClient.class.getResourceAsStream(CONFIG_PROPERTIES));
			logger.debug("속성값=" + prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** The Constant MQTT_PACKAGE. */
	public static final String MQTT_PACKAGE = "org.eclipse.paho.client.mqttv3";

	/** The Constant SERVERURL. */
	public static final String[] SERVERURL = prop.getProperty("mq.server.url").split(",");

	public static final String PROVISIONINGURL = prop.getProperty("provisioning.url");

	/** The Constant ssl. */
	private static final boolean ssl = Boolean.parseBoolean(prop.getProperty("mq.server.ssl"));

	/** The req cnt. */
	protected double reqCnt; // receive message count

	/** The object mapper. */
	protected ObjectMapper objectMapper = new ObjectMapper();

	/** The msg mapper. */
	protected MessageMapper msgMapper;

	/** The message dao. */
	@Resource
	protected MessageDao messageDao;

	/** The sql session. */
	@Autowired
	protected SqlSession sqlSession;

	/** The mqtt client. */
	private MqttAsyncClient mqttClient;

	/** The connection timeout. */
	private int connectionTimeout = Integer.parseInt(prop.getProperty("connection.timeout"));

	/** The keep alive interval. */
	private int keepAliveInterval = Integer.parseInt(prop.getProperty("keep.alive.interval"));

	/** The clean session. */
	private boolean cleanSession = Boolean.parseBoolean(prop.getProperty("clean.session"));

	/** The m opts. */
	private MqttConnectOptions mOpts;

	/** The error msg. */
	private String errorMsg;

	/** The log level. */
	private Level logLevel = Level.parse(prop.getProperty("paho.log.level"));

	// mqtt wait timeout
	/** The wait timeout. */
	private static long WAIT_TIMEOUT = 10000;

	/**
	 * initialize.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@PostConstruct
	public void initialize() throws Exception {
		logger.info("mqttService초기화시작()");
		setMqttClientLog();
		mOpts = makeMqttOpts();
		msgMapper = sqlSession.getMapper(MessageMapper.class);
		// grpMapper = bsBanksqlSession.getMapper(GroupMapper.class);
		logger.info("mqttService초기화종료()");
	}

	/**
	 * 모든리소스정리.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@PreDestroy
	public void cleanUp() throws Exception {
		logger.info("cleanUp시작()");
		destroy();
		logger.info("cleanUp종료()");
	}

	/**
	 * Sets the mqtt client log.
	 */
	private void setMqttClientLog() {
		logger.debug("setMqttClientLog시작()");
		Handler defaultHandler = new ConsoleHandler();
		LogManager logManager = LogManager.getLogManager();
		Logger rootLogger = Logger.getLogger(MQTT_PACKAGE);
		defaultHandler.setFormatter(new SimpleFormatter());
		defaultHandler.setLevel(logLevel);
		rootLogger.setLevel(logLevel);
		rootLogger.addHandler(defaultHandler);
		logManager.addLogger(rootLogger);
		logger.debug("setMqttClientLog종료()");
	}

	/**
	 * Make mqtt opts.
	 *
	 * @return the mqtt connect options
	 */
	private MqttConnectOptions makeMqttOpts() {
		logger.debug("makeMqttOpts시작()");
		MqttConnectOptions mOpts = new MqttConnectOptions();
		mOpts.setConnectionTimeout(connectionTimeout);
		mOpts.setKeepAliveInterval(keepAliveInterval);
		mOpts.setCleanSession(cleanSession);

		// 이중화시 필요함
		// mOpts.setServerURIs(SERVERURL);

		// mOpts.setServerURIs(new String[] { "ssl://adflow.net:1111",
		// "ssl://adflow.net:2222" });

		// mOpts.setUserName("mqttPushServer");
		// mOpts.setPassword("password".toCharArray());

		// java.security.Security.addProvider(new AcceptAllProvider());
		// java.util.Properties sslClientProperties = new Properties();
		// sslClientProperties.setProperty("com.ibm.ssl.trustManager",
		// "TrustAllCertificates");
		// sslClientProperties.setProperty("com.ibm.ssl.trustStoreProvider",
		// "AcceptAllProvider");
		// mOpts.setSSLProperties(sslClientProperties);

		// Imports: javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager
		if (ssl) {
			try {
				// Create a trust manager that does not validate certificate
				// chains
				final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
					@Override
					public void checkClientTrusted(final X509Certificate[] chain, final String authType)
							throws CertificateException {
					}

					@Override
					public void checkServerTrusted(final X509Certificate[] chain, final String authType)
							throws CertificateException {
					}

					@Override
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}
				} };

				// Install the all-trusting trust manager
				final SSLContext sslContext = SSLContext.getInstance("SSL");
				sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
				// Create an ssl socket factory with our all-trusting manager
				final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
				mOpts.setSocketFactory(sslSocketFactory);
			} catch (Exception e) {
				logger.error("에러발생", e);
			}
		}
		logger.debug("makeMqttOpts종료(mOpts=" + mOpts + ")");
		return mOpts;
	}

	public synchronized void destroy() throws Exception {
		logger.debug("destroy시작()");
		logger.debug("mqttClient=" + mqttClient);
		if (mqttClient == null) {
			logger.debug("destroy종료()");
			return;
		}
		logger.debug("mqttClient세션연결상태=" + (mqttClient.isConnected() ? "연결됨" : "끊어짐"));
		if (mqttClient.isConnected()) {
			IMqttToken token = mqttClient.disconnect();
			token.waitForCompletion(WAIT_TIMEOUT);
			logger.debug("mqttClient연결을끊었습니다.");
		}
		mqttClient.close();
		mqttClient = null;
		logger.debug("mqttClient가종료되었습니다.");
		logger.debug("destroy종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.service.MqttService#getErrorMsg()
	 */
	public String getErrorMsg() throws Exception {
		logger.debug("getErrorMsg시작()");
		logger.debug("getErrorMsg종료(errorMsg=" + errorMsg + ")");
		return errorMsg;
	}

	/**
	 * Mqtt connection clean.
	 *
	 * @param clientID
	 *            the client id
	 * @throws MqttException
	 *             the mqtt exception
	 */
	public void mqttConnectionClean(String clientID, String serverUrl) {
		logger.debug("mqttConnectionClean시작()");
		logger.debug("serverURL=" + SERVERURL);
		logger.debug("clientID=" + clientID);
		MqttAsyncClient mqttClient = null;
		try {

			mqttClient = new MqttAsyncClient(serverUrl, clientID, new MemoryPersistence());
			logger.debug("mqttClient2인스턴스가생성되었습니다.mqttClient=" + mqttClient);
			IMqttToken token = mqttClient.connect(mOpts);
			token.waitForCompletion();
			logger.debug("mqttConnectionClean 세션연결을완료하였습니다.");
			if (mqttClient.isConnected()) {
				token = mqttClient.disconnect();
				token.waitForCompletion(WAIT_TIMEOUT);
				logger.debug("mqttClient연결을끊었습니다.");
			}
			MQCONNCOUNT++;
		} catch (MqttException e) {
			logger.debug("연결에라");

		} finally {
			try {
				if (mqttClient != null) {
					mqttClient.close();
					mqttClient = null;
				}
			} catch (MqttException e) {

			}
		}

		logger.debug("mqttClient가종료되었습니다.");

		logger.debug("mqttConnectionClean종료()");
	}

}
