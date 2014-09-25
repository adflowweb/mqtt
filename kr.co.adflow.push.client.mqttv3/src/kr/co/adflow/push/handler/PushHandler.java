package kr.co.adflow.push.handler;

import java.io.IOException;
import java.security.KeyStore;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import kr.co.adflow.push.PingSender;
import kr.co.adflow.push.PushPreference;
import kr.co.adflow.push.service.PushService;
import kr.co.adflow.push.service.impl.PushServiceImpl;
import kr.co.adflow.ssl.ADFSSLSocketFactory;
import kr.co.adflow.ssl.SFSSLSocketFactory;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author nadir93
 */
public class PushHandler implements MqttCallback {

	private static final int MQTTVERSION_3 = 3;

	// TAG for debug
	public static final String TAG = "PushHandler";

	public static final int alarmInterval = 60;
	// public static final int DEFAULT_KEEP_ALIVE_TIME_OUT = 240;
	public static final int DEFAULT_KEEP_ALIVE_TIME_OUT = 60;
	private static final String MQTT_PACKAGE = "org.eclipse.paho.client.mqttv3";
	private static final int connectionTimeout = 10; // second
	private static final boolean cleanSession = false;
	// mqttClient 세션로그
	private static final boolean clientSessionDebug = false; // default false
	private static int connlostCount;

	private static Context context;
	private MqttAsyncClient mqttClient;
	private PushPreference preference;
	private PingSender pingSender;

	/**
	 * @param cxt
	 */
	public PushHandler(Context cxt) {
		Log.d(TAG, "PushHandler생성자시작(context=" + cxt + ")");
		context = cxt;
		Log.d(TAG, "Handler=" + this);
		if (clientSessionDebug) {
			setMqttClientLog();
		}
		preference = new PushPreference(context);
		Log.d(TAG, "preference=" + preference);
		Log.d(TAG, "PushHandler생성자종료()");
	}

	public static Context getContext() {
		return context;
	}

	/**
	 * 푸시핸들러시작
	 * 
	 * 시작이 호출되면 기존 채널을 중지후 다시 시작한다.
	 */
	public void start() {
		Log.d(TAG, "start시작()");
		stop();
		keepAlive();
		Log.d(TAG, "start종료()");
	}

	/**
	 * 푸시핸들러종료
	 */
	public void stop() {
		Log.d(TAG, "stop시작()");
		// disconnect mqtt session
		Log.d(TAG, "mqttClient=" + mqttClient);
		if (mqttClient != null) {
			try {
				IMqttToken token = mqttClient.disconnect();
				token.waitForCompletion();
			} catch (MqttException e) {
				Log.e(TAG, "에러발생", e);
			}
			mqttClient = null;
		}
		Log.d(TAG, "stop종료()");
	}

	/**
	 * keepAlive
	 */
	public void keepAlive() {
		Log.d(TAG, "keepAlive시작()");
		Log.d(TAG, "mqttClient=" + mqttClient);

		try {
			if (mqttClient == null) {
				String token = preference.getValue(PushPreference.TOKEN, null);
				Log.d(TAG, "token=" + token);

				// token이 null일경우 종료
				if (token == null) {
					Log.d(TAG, "keepAlive종료(token=null)");
					return;
				}

				String server = preference.getValue(PushPreference.SERVERURL,
						null);
				Log.d(TAG, "server=" + server);
				pingSender = new PingSender(context);
				mqttClient = new MqttAsyncClient(server, token,
						new MemoryPersistence(), pingSender);
				// 연결
				connect();

				// testCode
				// subscribe("/users", 2);
				// testCodeEnd

				// default subscribe
				// subscribe("/users", 2);
				// subscribe("/users/" + userID, 2);
			} else {
				// Log.d(TAG, "현재연결시도중이거나연결되어있는토큰=" + client.getClientId());
				Log.d(TAG, "mqttClient연결상태="
						+ ((mqttClient.isConnected()) ? "연결됨" : "끊어짐"));
				if (!mqttClient.isConnected()) {
					// 연결
					connect();
				}
			}

			// ping
			pingSender.ping();
		} catch (Exception e) {
			Log.e(TAG, "예외상황발생", e);
			if (PushServiceImpl.getWakeLock() != null) {
				try {
					PushServiceImpl.getWakeLock().release();
					Log.d(TAG, "웨이크락을해재했습니다." + PushServiceImpl.getWakeLock());
				} catch (Exception ex) {
					Log.e(TAG, "예외상황발생", e);
				}
			}

			// send event
			// Intent i = new Intent(context, /* PushServiceImpl.class */
			// context.getClass());
			// i.setAction("kr.co.adflow.push.service.EVENT");
			// i.putExtra("event", e.getMessage());
			// context.startService(i);
			// send event end
		}
		Log.d(TAG, "keepAlive종료()");
	}

	/**
	 * eclipse paho client용 로거
	 */
	private void setMqttClientLog() {
		Log.d(TAG, "setMqttClientLog시작()");
		java.util.logging.Handler defaultHandler = new ConsoleHandler();
		LogManager logManager = LogManager.getLogManager();
		Logger logger = Logger.getLogger(MQTT_PACKAGE);
		defaultHandler.setFormatter(new SimpleFormatter());
		defaultHandler.setLevel(Level.ALL);
		logger.setLevel(Level.ALL);
		logger.addHandler(defaultHandler);
		logManager.addLogger(logger);
		Log.d(TAG, "setMqttClientLog종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.
	 * Throwable)
	 */
	@Override
	public void connectionLost(Throwable throwable) {
		Log.d(TAG, "connectionLost시작(에러=" + throwable + ")");
		Log.e(TAG, "TCP세션연결이끊겼습니다", throwable);
		connlostCount++;
		Log.d(TAG, "connectionLostCount=" + connlostCount);
		Log.d(TAG, "connectionLost종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.paho.client.mqttv3.MqttCallback#deliveryComplete(org.eclipse
	 * .paho.client.mqttv3.IMqttDeliveryToken)
	 */
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		Log.d(TAG, "deliveryComplete시작(토큰=" + token + ")");
		Log.d(TAG, "deliveryComplete종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.
	 * String, org.eclipse.paho.client.mqttv3.MqttMessage)
	 */
	@Override
	public void messageArrived(String topic, MqttMessage message)
			throws Exception {
		Log.d(TAG, "messageArrived시작(토픽=" + topic + ",메시지=" + message + ",qos="
				+ message.getQos() + ")");
		try {
			// 알람설정주기 변경 command
			// 서비스별 메시지 broadcast

			JSONObject data = new JSONObject(new String(message.getPayload()));
			Log.d(TAG, "data=" + data);

			// message 분기처리
			int msgType = data.getInt("type");

			switch (msgType) {
			case 0: // 개인메시지
			case 1: // 전체메시지
			case 2: // 그룹메시지
				sendBroadcast(data);
				break;
			case 100: // subscribe
				// command msg
				break;
			case 101: // unsubscribe
				break;
			case 102: // keepAlive 설정변경
				JSONObject content = data.getJSONObject("content");
				Log.d(TAG, "content=" + content);
				int keepAlive = content.getInt("keepAlive");
				// store keepalive
				preference.put(PushPreference.KEEPALIVE, keepAlive);
				// restart mqtt session
				Log.d(TAG, "PushServiceClass=" + context.getClass());
				Intent i = new Intent(context, /* PushServiceImpl.class */
				context.getClass());
				i.setAction("kr.co.adflow.push.service.START");
				i.putExtra(PushPreference.TOKEN,
						preference.getValue(PushPreference.TOKEN, null));
				i.putExtra(PushPreference.SERVERURL,
						preference.getValue(PushPreference.SERVERURL, null));
				context.startService(i);
				break;
			case 103: // preCheck
				break;
			default:
				Log.e(TAG, "메시지타입이없습니다.");
				break;
			}
		} catch (Exception e) {
			Log.e(TAG, "예외상황발생", e);
		}
		Log.d(TAG, "messageArrived종료()");
	}

	/**
	 * @param message
	 * @param data
	 * @throws JSONException
	 */
	private void sendBroadcast(JSONObject data) throws JSONException {
		Log.d(TAG, "sendBroadcast시작(data=" + data + ")");
		Log.d(TAG, "serviceID=" + data.getString("serviceID"));
		Intent i = new Intent(data.getString("serviceID"));
		// sender
		// receiver
		// i.putExtra("topic", topic);
		i.putExtra("data", data.getString("content"));
		// i.putExtra("qos", message.getQos());
		i.putExtra("ack", data.getBoolean("ack"));

		context.sendBroadcast(i);
		Log.d(TAG, "sendBroadcast종료()");
	}

	/**
	 * @throws MqttException
	 */
	protected synchronized void connect() throws MqttException {
		Log.d(TAG, "connect시작()");
		if (mqttClient.isConnected()) {
			Log.d(TAG, "이미세션이미연결되었습니다");
			Log.d(TAG, "connect종료()");
			return;
		}

		String server = preference.getValue(PushPreference.SERVERURL, null);
		Log.d(TAG, "server=" + server);

		MqttConnectOptions mOpts = new MqttConnectOptions();
		// mOpts.setUserName("testUser");
		// mOpts.setPassword("testPasswd".toCharArray());
		mOpts.setConnectionTimeout(connectionTimeout);
		int keepAlive = preference.getValue(PushPreference.KEEPALIVE,
				DEFAULT_KEEP_ALIVE_TIME_OUT);
		Log.d(TAG, "keepAlive=" + keepAlive);
		mOpts.setKeepAliveInterval(keepAlive);
		boolean cleanSession = preference.getValue(PushPreference.CLEANSESSION,
				false);
		mOpts.setCleanSession(cleanSession);
		mOpts.setMqttVersion(MQTTVERSION_3);
		// ssl 처리
		if (server.startsWith("ssl")) {
			mOpts.setSocketFactory(ADFSSLSocketFactory.getSSLSokcetFactory());
		}
		// mOpts.setServerURIs(new String[] { "ssl://adflow.net" });

		Log.d(TAG, "연결옵션=" + mOpts);
		Log.d(TAG, "mqttClient=" + mqttClient);
		Log.d(TAG, "콜백인스턴스=" + this);
		mqttClient.setCallback(this);

		Log.d(TAG, "mqtt서버에연결합니다.server=" + server);
		IMqttToken token = mqttClient.connect(mOpts);
		token.waitForCompletion();
		Log.d(TAG, "세션이연결되었습니다.");
		// send event
		// Intent i = new Intent(context, /* PushServiceImpl.class */
		// context.getClass());
		// i.setAction("kr.co.adflow.push.service.EVENT");
		// i.putExtra("event", "connected");
		// context.startService(i);
		// send event end
		Log.d(TAG, "connect종료()");
	}

	/**
	 * @param topic
	 * @param qos
	 * @throws MqttException
	 */
	public synchronized void subscribe(String topic, int qos) throws Exception {
		Log.d(TAG, "subScribe시작(토픽=" + topic + ", qos=" + qos + ")");

		if (mqttClient == null || !mqttClient.isConnected()) {
			throw new Exception("토픽구독에실패하였습니다(mqttClient가없거나연결이안되어있습니다)");
		}

		// 토픽구독
		IMqttToken token = mqttClient.subscribe(topic, qos);
		token.waitForCompletion();
		Log.d(TAG, "토픽구독을완료하였습니다.");
		Log.d(TAG, "subscribe종료()");
	}

	/**
	 * @param topic
	 * @throws MqttException
	 */
	public synchronized void unsubscribe(String topic) throws Exception {
		Log.d(TAG, "unsubscribe시작(토픽=" + topic + ")");

		if (mqttClient == null || !mqttClient.isConnected()) {
			throw new Exception("토픽구독취소에실패하였습니다(mqttClient가없거나연결이안되어있습니다)");
		}

		// 토픽구독
		IMqttToken token = mqttClient.unsubscribe(topic);
		token.waitForCompletion();
		Log.d(TAG, "토픽구독을취소하였습니다.");
		Log.d(TAG, "unsubscribe종료()");
	}

	/**
	 * @param topic
	 * @param payload
	 * @param qos
	 * @param retained
	 * @throws Exception
	 */
	public synchronized void publish(String topic, byte[] payload, int qos,
			boolean retained) throws Exception {
		Log.d(TAG, "publish시작(토픽=" + topic + ", payload=" + new String(payload)
				+ ", qos=" + qos + ", retained=" + retained + ")");
		IMqttDeliveryToken token = mqttClient.publish(topic, payload, qos,
				retained);
		token.waitForCompletion();
		Log.d(TAG, "publish종료()");
		return;
	}

	/**
	 * @return
	 */
	public boolean isConnected() {
		Log.d(TAG, "isConnected시작()");
		boolean value = false;
		if (mqttClient != null) {
			value = mqttClient.isConnected();
		}
		Log.d(TAG, "isConnected종료(value=" + value + ")");
		return value;
	}

	/**
	 * @return
	 */
	public int getLostCout() {
		return connlostCount;
	}

	/**
	 * @param url
	 * @param userID
	 * @param deviceID
	 * @return
	 * @throws IOException
	 */
	public String auth(String url, String userID, String deviceID)
			throws Exception {
		Log.d(TAG, "auth시작(url=" + url + ", userID=" + userID + ", deviceID="
				+ deviceID + ")");
		JSONObject data = new JSONObject();
		data.put("userID", userID);
		data.put("deviceID", deviceID);

		HttpClient httpClient = getHttpClient();

		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "application/json;charset=UTF-8");
		post.setEntity(new StringEntity(data.toString(), "utf-8"));
		HttpResponse response = httpClient.execute(post);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new IOException("Unexpected code "
					+ response.getStatusLine().getStatusCode());
		}

		String responseStr = EntityUtils.toString(response.getEntity());
		Log.d(TAG, "auth종료(value=" + responseStr + ")");
		return responseStr;
	}

	private HttpClient getHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new SFSSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			// 현재 https 인데 8080으로 서비스하고 있음
			registry.register(new Scheme("https", sf, 8080));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}
}
