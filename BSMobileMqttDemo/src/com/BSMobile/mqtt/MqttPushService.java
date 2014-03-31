package com.BSMobile.mqtt;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.GregorianCalendar;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClientWithPing;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;

/**
 * @author nadir93
 * 
 */
public class MqttPushService extends Service implements MqttCallback {

	// Debug TAG
	public static final String DEBUGTAG = "Mqtt푸시서비스";
	public static String deviceID;
	public static final String MQTT_PACKAGE = "org.eclipse.paho.client.mqttv3";
	// private static final String TOPIC = "user/nadir93";
	private static final String TOPIC = "testTopic";
	// private static final String SERVERURL = "tcp://adflow.net:1883";
	private static final String SERVERURL = "tcp://175.209.8.188:1883";
	// private static final byte[] MQTT_KEEP_ALIVE_MESSAGE = { 123 };
	private static final String ADDCALENDAR = "캘린더추가";
	private static final String DETAILVIEW = "자세히보기";
	private static final int BIG_PICTURE_STYLE = 0;
	private static final int BIG_TEXT_STYLE = 1;
	private static final int CLIENT_ID_LENGTH = 23; // mqtt 3.1 스펙에 clientid
													// 23 character 로 제한됨

	public static PowerManager.WakeLock wakeLock;

	private MqttClientWithPing mqttClient;
	private int connectionTimeout = 10; // second
	private int keepAliveInterval = 480; // second
	private int msgCnt = 0;

	public static PowerManager.WakeLock getWakeLock() {
		return wakeLock;
	}

	public static void setWakeLock(PowerManager.WakeLock wakeLock) {
		MqttPushService.wakeLock = wakeLock;
	}

	public MqttPushService() {
		Log.d(DEBUGTAG, "MqttPushService생성자시작() this=" + this);
		try {

			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(android.os.Build.SERIAL.getBytes());
			byte[] mdbytes = md.digest();

			// convert the byte to hex format method 1
			StringBuffer sb = new StringBuffer();
			Log.d(DEBUGTAG, "mdbytes.length=" + mdbytes.length);
			for (int i = 0; i < mdbytes.length; i++) {
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			Log.d(DEBUGTAG, "deviceIDHexString=" + sb);
			deviceID = sb.toString().substring(0, CLIENT_ID_LENGTH);
			Log.d(DEBUGTAG, "deviceID=" + deviceID);
			mqttClient = new MqttClientWithPing(SERVERURL, deviceID,
					new MemoryPersistence());
			Log.d(DEBUGTAG, "mqttClient가초기화되었습니다");
		} catch (Exception e) {
			Log.e(DEBUGTAG, "예외상황발생", e);
		}
		Log.d(DEBUGTAG, "MqttPushService생성자종료");
	}

	@Override
	public void onCreate() {
		Log.d(DEBUGTAG, "onCreate시작()");
		setMqttClientLog();
		Log.d(DEBUGTAG, "onCreate종료()");
	}

	private void setMqttClientLog() {
		Log.d(DEBUGTAG, "setMqttClientLog시작()");
		Handler defaultHandler = new ConsoleHandler();
		LogManager logManager = LogManager.getLogManager();
		Logger logger = Logger.getLogger(MQTT_PACKAGE);
		defaultHandler.setFormatter(new SimpleFormatter());
		defaultHandler.setLevel(Level.ALL);
		logger.setLevel(Level.ALL);
		logger.addHandler(defaultHandler);
		logManager.addLogger(logger);
		Log.d(DEBUGTAG, "setMqttClientLog종료()");
	}

	@Override
	public void onDestroy() {
		Log.d(DEBUGTAG, "onDestroy시작()");
		Log.d(DEBUGTAG, "onDestroy종료()");
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(DEBUGTAG, "onBind시작(intent=" + intent + ")");
		Log.d(DEBUGTAG, "onBind종료(리턴=null)");
		return null;
	}

	@Override
	protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
		Log.d(DEBUGTAG, "dump시작(fd=" + fd + "||writer=" + writer + "||args="
				+ args + ")");
		super.dump(fd, writer, args);
		Log.d(DEBUGTAG, "dump종료()");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.d(DEBUGTAG, "onConfigurationChanged시작(config=" + newConfig + ")");
		super.onConfigurationChanged(newConfig);
		Log.d(DEBUGTAG, "onConfigurationChanged종료()");
	}

	@Override
	public void onLowMemory() {
		Log.d(DEBUGTAG, "onLowMemory시작()");
		Log.d(DEBUGTAG, "onLowMemory종료()");
	}

	@Override
	public void onRebind(Intent intent) {
		Log.d(DEBUGTAG, "onRebind시작(intent=" + intent + ")");
		Log.d(DEBUGTAG, "onRebind종료()");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.d(DEBUGTAG, "onStart시작(intent=" + intent + "||startId=" + startId
				+ ")");
		Log.d(DEBUGTAG, "onStart종료()");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(DEBUGTAG, "onStartCommand시작(intent=" + intent + "||flags="
				+ flags + "||startID=" + startId + ")");

		// intent에 따른 분기필요

		//
		try {
			Log.d(DEBUGTAG, "mqttClient=" + mqttClient);
			Log.d(DEBUGTAG, "mqttClient연결상태="
					+ ((mqttClient.isConnected()) ? "연결됨" : "끊어짐"));
			if (!mqttClient.isConnected()) {
				connectAndSubscribe();
				Log.d(DEBUGTAG, "mqttClient가접속되었습니다.");
			}

			// publish alivemsg
			// MqttMessage message = new MqttMessage(MQTT_KEEP_ALIVE_MESSAGE);
			// message.setQos(0);
			// mqttClient.publish("/users/nadir93/keepalive", message);
			// Log.d(DEBUGTAG, "PING메시지가전송되었습니다.메시지=" + message);

			// ping
			MqttDeliveryToken tocken = mqttClient.sendPING();
			// 테스트필요 ???
			// tocken.waitForCompletion(5000);
			Log.d(DEBUGTAG, "PINGReq메시지가전송되었습니다.토큰=" + tocken);
		} catch (Exception e) {
			Log.e(DEBUGTAG, "예외상황발생", e);
		}
		int ret = super.onStartCommand(intent, flags, startId);
		Log.d(DEBUGTAG, "onStartCommand종료(리턴=" + ret + ")");
		return ret;
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		Log.d(DEBUGTAG, "onTaskRemoved시작(intent=" + rootIntent + ")");
		// Intent restartServiceIntent = new Intent(getApplicationContext(),
		// this.getClass());
		// restartServiceIntent.setPackage(getPackageName());
		//
		// PendingIntent restartServicePendingIntent = PendingIntent.getService(
		// getApplicationContext(), 1, restartServiceIntent,
		// PendingIntent.FLAG_ONE_SHOT);
		// AlarmManager alarmService = (AlarmManager) getApplicationContext()
		// .getSystemService(Context.ALARM_SERVICE);
		// alarmService.set(AlarmManager.ELAPSED_REALTIME,
		// SystemClock.elapsedRealtime() + 1000,
		// restartServicePendingIntent);
		Log.d(DEBUGTAG, "onTaskRemoved종료()");
	}

	@Override
	public void onTrimMemory(int level) {
		Log.d(DEBUGTAG, "onTrimMemory시작(level=" + level + ")");
		Log.d(DEBUGTAG, "onTrimMemory종료()");
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(DEBUGTAG, "onUnbind시작(intent=" + intent + ")");
		Log.d(DEBUGTAG, "onUnbind종료()");
		return super.onUnbind(intent);
	}

	@Override
	public void connectionLost(Throwable th) {
		Log.d(DEBUGTAG, "connectionLost시작(에러=" + th + ")");
		Log.e(DEBUGTAG, "mqttTCP세션연결이끊겼습니다", th);
		try {
			if (!mqttClient.isConnected()) {
				connectAndSubscribe();
				Log.d(DEBUGTAG, "mqttClient가접속되었습니다.");
			}
		} catch (MqttException e) {
			Log.e(DEBUGTAG, "예외상황발생(MqttException)", e);
		}
		Log.d(DEBUGTAG, "connectionLost종료()");
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		Log.d(DEBUGTAG, "deliveryComplete시작(토큰=" + token + ")");
		try {
			Log.d(DEBUGTAG, "웨이크락=" + wakeLock);
			if (wakeLock != null && wakeLock.isHeld()) {
				wakeLock.release();
				Log.d(DEBUGTAG, "웨이크락이해재되었습니다");
			} else {
				Log.d(DEBUGTAG, "웨이크락이null이거나이미해재되었습니다");
			}
		} catch (Exception e) {
			Log.e(DEBUGTAG, "예외상황발생", e);
		}
		Log.d(DEBUGTAG, "deliveryComplete종료()");
	}

	@Override
	public void messageArrived(String topic, MqttMessage message)
			throws Exception {
		Log.d(DEBUGTAG, "messageArrived시작(토픽=" + topic + "||메시지=" + message
				+ "||qos=" + message.getQos() + ")");
		try {
			showNotification(message);
		} catch (Exception e) {
			Log.e(DEBUGTAG, "예외상황발생", e);
		}
		Log.d(DEBUGTAG, "messageArrived종료()");
	}

	private void showNotification(MqttMessage message) throws Exception {
		JSONObject jsonObj = new JSONObject(new String(message.getPayload()));
		JSONObject noti = jsonObj.getJSONObject("notification");

		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = null;
		Intent resultIntent = new Intent(this, com.BSMobile.mqtt.BSMobile.class);
		resultIntent.putExtra("image", noti.getString("image"));
		resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// Intent.FLAG_ACTIVITY_NEW_TASK
		// resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
		// | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent intent = PendingIntent.getActivity(
				getApplicationContext(), 0, resultIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		// PendingIntent intent = PendingIntent.getActivity(
		// getApplicationContext(), 0, new Intent(Intent.ACTION_VIEW)
		// .setData(Uri.parse("http://www.adflow.co.kr")), 0);

		// ACTION_INSERT does not work on all phones
		// use Intent.ACTION_EDIT in this case
		// PendingIntent intent2 = PendingIntent.getActivity(
		// getApplicationContext(), 0, new Intent(Intent.ACTION_INSERT)
		// .setData(CalendarContract.Events.CONTENT_URI), 0);

		PendingIntent addCalPendingIntent = null;
		Log.d(DEBUGTAG, "event=" + jsonObj.has("event"));
		if (jsonObj.has("event")) {
			JSONObject event = jsonObj.getJSONObject("event");
			addCalPendingIntent = PendingIntent.getActivity(
					getApplicationContext(), 0, makeAddCalIntent(event),
					PendingIntent.FLAG_UPDATE_CURRENT);
		}

		byte[] data = Base64.decodeBase64(noti.getString("image").getBytes());
		Bitmap bmBigPicture = BitmapFactory.decodeByteArray(data, 0,
				data.length);

		int notificationStyle = noti.getInt("notificationStyle");
		Uri alarmSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		switch (notificationStyle) {
		case BIG_PICTURE_STYLE:
			// bmBigPicture = BitmapFactory.decodeResource(getResources(),
			// R.drawable.android_jellybean);
			// byte[] data = message.getPayload();

			// bigPictureStyle
			notification = new Notification.BigPictureStyle(
					new Notification.Builder(getApplicationContext())
							.setSound(alarmSound)
							// .setLights(Color.parseColor("#5858FA"), 500, 500)
							.setLights(Color.BLUE, 500, 500)
							.setContentTitle(noti.getString("contentTitle"))
							.setContentText(noti.getString("contentText"))
							.setSmallIcon(R.drawable.ic_action_event)
							.setLargeIcon(bmBigPicture)
							.setTicker(noti.getString("ticker"))
							.addAction(R.drawable.ic_action_search, DETAILVIEW,
									intent)
							.addAction(R.drawable.ic_action_new_event,
									ADDCALENDAR, addCalPendingIntent))
					.bigPicture(bmBigPicture)
					.setBigContentTitle(noti.getString("contentTitle"))
					.setSummaryText(noti.getString("summaryText")).build();

			break;
		case BIG_TEXT_STYLE:
			// bigTextStyle
			notification = new Notification.BigTextStyle(
					new Notification.Builder(getApplicationContext())
							.setSound(alarmSound)
							.setLights(Color.BLUE, 500, 500)
							.setContentTitle(noti.getString("contentTitle"))
							.setContentText(noti.getString("contentText"))
							.setSmallIcon(R.drawable.icon)
							.setTicker(noti.getString("ticker"))
							.setLargeIcon(bmBigPicture)
							.setLargeIcon(bmBigPicture)).bigText(
					noti.getString("contentText")).build();
			break;
		default:
		}

		// notification.ledARGB = Color.YELLOW;
		manager.notify(msgCnt++, notification);
	}

	private Intent makeAddCalIntent(JSONObject event) throws Exception {
		Log.d(DEBUGTAG, "makeAddCalIntent시작(event=" + event + ")");
		Intent addCalIntent = new Intent(Intent.ACTION_INSERT);
		addCalIntent.setType("vnd.android.cursor.item/event");
		addCalIntent.putExtra(Events.TITLE, event.getString("title"));
		addCalIntent.putExtra(Events.EVENT_LOCATION,
				event.getString("location"));
		addCalIntent.putExtra(Events.DESCRIPTION, event.getString("desc"));

		// Setting dates
		GregorianCalendar calDate = new GregorianCalendar(event.getInt("year"),
				event.getInt("month"), event.getInt("day"));
		addCalIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
				calDate.getTimeInMillis());
		addCalIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
				calDate.getTimeInMillis());

		// make it a full day event
		// addCalIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

		// make it a recurring Event
		// intent2.putExtra(Events.RRULE,
		// "FREQ=WEEKLY;COUNT=11;WKST=SU;BYDAY=TU,TH");

		// Making it private and shown as busy
		// addCalIntent.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);
		// addCalIntent.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
		Log.d(DEBUGTAG, "makeAddCalIntent종료(intent=" + addCalIntent + ")");
		return addCalIntent;
	}

	protected synchronized void connectAndSubscribe() throws MqttException {
		Log.d(DEBUGTAG, "connectAndSubscribe시작");

		if (mqttClient.isConnected()) {
			Log.d(DEBUGTAG, "이미세션이미연결되었습니다");
			return;
		}

		MqttConnectOptions mOpts = new MqttConnectOptions();
		mOpts.setConnectionTimeout(connectionTimeout);
		mOpts.setKeepAliveInterval(keepAliveInterval);
		mOpts.setCleanSession(false);
		Log.d(DEBUGTAG, "연결옵션=" + mOpts);
		Log.d(DEBUGTAG, "mqttClient=" + mqttClient);
		Log.d(DEBUGTAG, "콜백인스턴스=" + this);
		mqttClient.setCallback(this);
		mqttClient.connect(mOpts);
		mqttClient.subscribe(TOPIC, 2);
		mqttClient.subscribe("user/" + deviceID, 2);
		Log.d(DEBUGTAG, "세션연결및토픽구독을완료하였습니다.");
		Log.d(DEBUGTAG, "connectAndSubscribe종료()");
	}
}
