package kr.co.adflow.push.handler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import kr.co.adflow.push.PushApp;
import kr.co.adflow.service.PushService;
import kr.co.adflow.sqlite.Job;
import kr.co.adflow.sqlite.PushDBHelper;
import kr.co.adflow.sqlite.Topic;
import kr.co.adflow.sqlite.User;
import kr.co.adflow.ssl.ADFSSLSocketFactory;

import org.eclipse.paho.client.mqttv3.ADFMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.shortcutBadger.ShortcutBadgeException;
import com.shortcutBadger.ShortcutBadger;

/**
 * @author nadir93
 * @date 2014. 6. 19.
 */
public class PushHandler implements MqttCallback {

	// TAG for debug
	public static final String TAG = "푸시핸들러";
	// public static final int alarmInterval = 240; // 4분
	// public static final int alarmInterval = 60; // 1분 for debug
	public static final int alarmInterval = 30; // 4분 for production
	// private static final String SERVERURL = "ssl://adflow.net:8883";
	// private static final String SERVERURL = "ssl://adflow.net:8883";
	// private static final String SERVERURL = "tcp://175.209.8.188:1883";
	private static final String SERVERURL = "tcp://adflow.net:31883";
	private static final String MQTT_PACKAGE = "org.eclipse.paho.client.mqttv3";
	private static final int connectionTimeout = 10; // second
	// private static final int connectionTimeout = 5; // second
	// private static final int connectionTimeout = 2; // second
	private static final int keepAliveInterval = 30; // second
	private static final boolean cleanSession = false;
	// mqttClient 세션로그
	private static final boolean clientSessionLog = false;

	private static SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy.MM.dd HH:mm:ss");

	private static Calendar cal = Calendar.getInstance();

	private Context context;
	private ADFMqttClient client;
	private MqttDeliveryToken token;
	public static PushDBHelper pushdb;
	private String tokenID;
	private String userID;

	public PushHandler(Context cxt) {
		Log.d(TAG, "Handler생성자시작()");
		context = cxt;
		pushdb = new PushDBHelper(context);
		Log.d(TAG, "Handler=" + this);
		if (clientSessionLog) {
			setMqttClientLog();
		}
		Log.d(TAG, "Handler생성자종료()");
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

	@Override
	public void connectionLost(Throwable th) {
		Log.d(TAG, "connectionLost시작(에러=" + th + ")");
		Log.e(TAG, "TCP세션연결이끊겼습니다", th);
		// try {
		// if (!client.isConnected()) {
		// connect();
		// Log.d(TAG, "client가접속되었습니다.");
		// }
		// } catch (MqttException e) {
		// Log.e(TAG, "예외상황발생(MqttException)", e);
		// }
		Log.d(TAG, "connectionLost종료()");
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken tk) {
		Log.d(TAG, "deliveryComplete시작(토큰=" + tk + ")");
		try {
			if (token != null && token.equals(tk)) {
				// ping message 일때만 웨이크락 해재
				Log.d(TAG, "웨이크락=" + PushService.getWakeLock());
				if (PushService.getWakeLock() != null
						&& PushService.getWakeLock().isHeld()) {
					PushService.getWakeLock().release();
					Log.d(TAG, "웨이크락이해재되었습니다");
				} else {
					Log.d(TAG, "웨이크락이null이거나이미해재되었습니다");
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "예외상황발생", e);
		}
		Log.d(TAG, "deliveryComplete종료()");
	}

	@Override
	public void messageArrived(String topic, MqttMessage message)
			throws Exception {
		Log.d(TAG, "messageArrived시작(토픽=" + topic + ",메시지=" + message + ",qos="
				+ message.getQos() + ")");
		try {
			JSONObject msg = new JSONObject(new String(message.getPayload()));

			// showNotify
			NotificationHandler.notify(context, msg);
			if (true) {
				return;
			}

			final JSONObject content = msg.getJSONObject("content");
			int msgType = msg.getInt("type");

			Date sendDate = formatter.parse(msg.getString("sendDate"));
			Log.d(TAG, "sendDate=" + sendDate);

			// 메시지 저장 (SQLite)
			pushdb.addMessage(userID, msg);
			// Message result = pushdb.getMessage(msg.getInt("id"));
			// Log.d(TAG, "저장된메시지=" + result);

			// testCode
			// pushdb.getAllMessages();
			pushdb.getMsgCount();
			// end

			switch (msgType) {
			case 0: // 개인메시지
			case 1: // 전체메시지
			case 2: // 그룹메시지(계열사)
			case 3: // 그룹메시지(부서)
			case 4: // 그룹메시지(직급)
				// notification msg
				if (msg.getBoolean("ack")) {
					// ack message
					final int id = msg.getInt("id");

					final String ackMsg = "{\"userID\":\"" + userID
							+ "\",\"id\":" + id + "}";
					// ack 요청메시지이면 request테이블 저장
					pushdb.addJob(Job.PUBLISH, "/push/ack", ackMsg);

					// Request req = pushdb.getRequest(rst);
					// Log.d(TAG, "저장된리퀘스트=" + req);

					// 전송
					// new Thread() {
					// @Override
					// public void run() {
					//
					// try {
					// client.publish("/push/ack", ackMsg.getBytes(),
					// 1, false);
					// Log.d(TAG, "ack메시지를전송하였습니다. 메시지=" + ackMsg);
					// } catch (MqttPersistenceException e) {
					// // 예외상황발생시 큐에저장하고 알람이 깨어났을때 다시 시도 하도록 코드 추가요망
					// Log.e(TAG, "예외상황발생", e);
					// } catch (MqttException e) {
					// // 예외상황발생시 큐에저장하고 알람이 깨어났을때 다시 시도 하도록 코드 추가요망
					// Log.e(TAG, "예외상황발생", e);
					// }
					// }
					// }.start();
				}

				cal.setTime(new Date());
				cal.add(cal.HOUR_OF_DAY, -24); // 24시간전
				Date time = cal.getTime();
				Log.d(TAG, "기준시간=" + time);
				if (time.before(sendDate)) {
					JSONObject noti = content.getJSONObject("notification");
					// showNotify
					NotificationHandler.notify(context, noti);

					// testCode
					int badgeCount = pushdb.getUnreadCount();

					try {
						ShortcutBadger.setBadge(context, badgeCount);
						Log.d(TAG, "뱃지를달았습니다.badgeCount=" + badgeCount);
					} catch (ShortcutBadgeException e) {
						e.printStackTrace();
					}

					// testCodeEnd

					// sendIntent
					Log.d(TAG, "푸시앱상태=" + PushApp.isActivityVisible());

					if (PushApp.isActivityVisible()) {
						// 앱활성화 상태일 경우 자바스크립트 호출
						Log.d(TAG, "자바스크립트(refresh)호출합니다.");
						Intent intent = new Intent(context,
								kr.co.adflow.push.MainActivity.class);
						intent.putExtra("category", msg.getString("category"));
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						// context.sendBroadcast(intent);
						context.startActivity(intent);
					}

				} else {
					Log.d(TAG, "알림을표시하지않습니다.");
				}

				break;
			case 100: // subscribe
				// command msg
				// 그룹정보변경 (전체토픽을 구독취소하고 다시 전체토픽구독)
				// (토픽=/users/nadir93,메시지={"id":5,"ack":false,"type":1,"content":{"userID":"nadir93","groups":["dev","adflow"]}},qos=2)

				// 기존토픽 unsubcribe
				// 기존토픽 가져오기
				Topic[] tp = pushdb.getTopic(userID);
				if (tp != null) {
					for (int i = 0; i < tp.length; i++) {
						pushdb.addJob(Job.UNSUBSCRIBE, tp[i].getTopic(), null);
					}
				}

				JSONArray array = content.getJSONArray("groups");

				for (int i = 0; i < array.length(); i++) {
					pushdb.addJob(Job.SUBSCRIBE,
							"/groups/" + array.getString(i), null);
					// subscribe("/groups/" + array.getString(i), 2);
				}

				// 그룹정보구독
				// new Thread() {
				// @Override
				// public void run() {
				// try {
				// // 기존 그룹정보 unsubscribe 하도록 추가바람
				// JSONArray array = content.getJSONArray("groups");
				// for (int i = 0; i < array.length(); i++) {
				// subscribe("/groups/" + array.getString(i), 2);
				// }
				// } catch (JSONException e) {
				// Log.e(TAG, "예외상황발생", e);
				// } catch (MqttException e) {
				// // 예외상황발생시 큐에저장하고 알람이 깨어났을때 다시 시도 하도록 코드
				// // 추가요망
				// Log.e(TAG, "예외상황발생", e);
				// }
				// }
				// }.start();
				break;
			case 101: // unsubscribe
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

	public void healthCheck(Intent intent) {
		Log.d(TAG, "healthCheck시작()");
		Log.d(TAG, "client=" + client);

		try {
			User user = pushdb.getCurrentUser();
			Log.d(TAG, "user=" + user);

			if (user.getTokenID() == null || user.getUserID() == null) {
				throw new Exception("토큰 or 유저아이디가없습니다.");
			}

			// 토큰이변경된경우
			if (tokenID != null && !user.getTokenID().equals(tokenID)) {
				Log.d(TAG, "토큰이변경되었습니다.");
				if (client != null && client.isConnected()) {
					client.disconnect();
				}
				client.close();
				Log.d(TAG, "클라이언트를종료합니다.");
				client = null;
			}
			tokenID = user.getTokenID();
			userID = user.getUserID();

			if (client == null) {
				// MqttDefaultFilePersistence persistence = new
				// MqttDefaultFilePersistence(
				// "/mmt/sdcard/temp");
				// MqttDefaultFilePersistence fp = new
				// MqttDefaultFilePersistence();
				client = new ADFMqttClient(SERVERURL, tokenID,
						new MemoryPersistence() /* persistence */);
				connect();
				// default subscribe
				subscribe("/users", 2);
				subscribe("/users/" + userID, 2);

				subscribe("user/1c45de7cc1daa896bfd32dc/event", 2);

				// 최초로그인시 그룹정보동기화요청을 한번만 요청
				String grpReqMsg = "{\"userID\":\"" + userID + "\"}";
				Topic[] topic = pushdb.getTopic(userID);
				if (topic == null) {
					client.publish("/push/group", grpReqMsg.getBytes(), 2,
							false);
					Log.d(TAG, "그룹정보요청메시지를전송하였습니다. 메시지=" + grpReqMsg);
				}
			} else {
				// Log.d(TAG, "현재연결시도중이거나연결되어있는토큰=" + client.getClientId());
				Log.d(TAG, "client연결상태="
						+ ((client.isConnected()) ? "연결됨" : "끊어짐"));
				if (!client.isConnected()) {
					connect();
				}
			}

			// ping
			token = client.sendPING();
			Log.d(TAG, "PINGReq메시지가전송되었습니다.토큰=" + token);

			// 테스트필요 ???
			// tocken.waitForCompletion(5000);

			// db 저장된 작업수행
			// get jobs
			processJob();
		} catch (Exception e) {
			Log.e(TAG, "예외상황발생", e);
			if (PushService.getWakeLock() != null) {
				PushService.getWakeLock().release();
				Log.d(TAG, "웨이크락을해재했습니다." + PushService.getWakeLock());
			}
		}
		Log.d(TAG, "healthCheck종료()");
	}

	private void processJob() throws Exception, MqttException,
			MqttPersistenceException {
		Job[] job = pushdb.getJobList();
		if (job != null) {
			// push
			for (int i = 0; i < job.length; i++) {
				int jobType = job[i].getType();
				switch (jobType) {
				case 0:
					// PUBLISH
					client.publish(job[i].getTopic(), job[i].getContent()
							.getBytes(), 1, false);
					Log.d(TAG, "메시지를전송하였습니다. 메시지=" + job[i]);
					// 디비삭제
					pushdb.deteletJob(job[i].getId());
					Log.d(TAG, "작업이수행되었습니다.id=" + job[i].getId());
					break;
				case 1:
					// SUBSCRIBE
					subscribe(job[i].getTopic(), 2);

					// SQLiteConstraintException

					try {
						// 디비인서트
						pushdb.addTopic(userID, job[i].getTopic(), 1);
					} catch (SQLiteConstraintException e) {
						Log.e(TAG, "디비인서트중예외상황발생", e);
					}

					// 디비삭제
					pushdb.deteletJob(job[i].getId());
					Log.d(TAG, "작업이수행되었습니다.id=" + job[i].getId());
					break;
				case 2:
					// UNSUBSCRIBE
					unsubscribe(job[i].getTopic());
					// 디비삭제
					pushdb.deleteTopic(userID, job[i].getTopic());
					pushdb.deteletJob(job[i].getId());
					Log.d(TAG, "작업이수행되었습니다.id=" + job[i].getId());
					break;
				default:
					break;
				}
			}
		}
	}

	protected synchronized void connect() throws MqttException {
		Log.d(TAG, "connect시작()");
		if (client.isConnected()) {
			Log.d(TAG, "이미세션이미연결되었습니다");
			Log.d(TAG, "connect종료()");
			return;
		}

		MqttConnectOptions mOpts = new MqttConnectOptions();
		// mOpts.setUserName("testUser");
		// mOpts.setPassword("testPasswd".toCharArray());
		mOpts.setConnectionTimeout(connectionTimeout);
		mOpts.setKeepAliveInterval(keepAliveInterval);
		mOpts.setCleanSession(cleanSession);
		// mOpts.setSocketFactory(ADFSSLSocketFactory.getSSLSokcetFactory());
		// mOpts.setServerURIs(new String[] { "ssl://adflow.net" });

		Log.d(TAG, "연결옵션=" + mOpts);
		Log.d(TAG, "client=" + client);
		Log.d(TAG, "콜백인스턴스=" + this);
		client.setCallback(this);
		Log.d(TAG, "mq서버에연결합니다server=" + SERVERURL);
		client.connect(mOpts);
		Log.d(TAG, "세션이연결되었습니다.");
		Log.d(TAG, "connect종료()");
	}

	protected synchronized void subscribe(String topic, int qos)
			throws MqttException {
		Log.d(TAG, "subScribe시작(토픽=" + topic + ", qos=" + qos + ")");

		if (client == null || !client.isConnected()) {
			Log.e(TAG, "토픽구독에실패하였습니다.");
			Log.d(TAG, "subscribe종료()");
			return;
		}

		// 토픽구독
		client.subscribe(topic, qos);
		Log.d(TAG, "토픽구독을완료하였습니다.");
		Log.d(TAG, "subscribe종료()");
	}

	protected synchronized void unsubscribe(String topic) throws MqttException {
		Log.d(TAG, "unsubscribe시작(토픽=" + topic + ")");

		if (client == null || !client.isConnected()) {
			Log.e(TAG, "토픽구독취소에실패하였습니다.");
			Log.d(TAG, "unsubscribe종료()");
			return;
		}

		// 토픽구독
		client.unsubscribe("/groups/" + topic);
		Log.d(TAG, "토픽구독을취소하였습니다.");
		Log.d(TAG, "unsubscribe종료()");
	}
}
