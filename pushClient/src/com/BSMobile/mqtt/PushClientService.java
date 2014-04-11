package com.BSMobile.mqtt;

import java.util.GregorianCalendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
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
import android.os.SystemClock;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.Settings.Secure;
import android.util.Log;

public class PushClientService extends Service implements MqttCallback,
		Runnable {

	Logger logger;

	// Debug TAG
	public static final String DEBUGTAG = "PushClientService";
	private static final String MQTT_THREAD_NAME = "MqttService[" + DEBUGTAG
			+ "]"; // Handler Thread ID
	private String deviceID;
	public static final String MQTT_PACKAGE = "org.eclipse.paho.client.mqttv3";
	private static final String TOPIC = "testTopic";
	private static final String SERVERURL = "tcp://adflow.net:1883";
	private ScheduledExecutorService scheduledExecutorService;
	private MqttClient mqttClient;
	private int healthCheckInterval = 60; // second
	private int connectionTimeout = 10; // second
	private int keepAliveInterval = 60; // second
	private String errorMsg;

	private static final String ADDCALENDAR = "캘린더추가";
	private static final String DETAILVIEW = "자세히보기";

	private int msgCnt = 0;

	@Override
	public IBinder onBind(Intent paramIntent) {
		Log.d(DEBUGTAG, "onBind" + paramIntent);
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		deviceID = Secure.getString(getApplicationContext()
				.getContentResolver(), Secure.ANDROID_ID);
		Log.d(DEBUGTAG, "mqtt커넥션서비스를시작합니다.");
		setMqttClientLog();
		scheduledExecutorService = Executors.newScheduledThreadPool(1);
		scheduledExecutorService.scheduleWithFixedDelay(this, 0,
				healthCheckInterval, TimeUnit.SECONDS);
		Log.d(DEBUGTAG, "onCreate");
	}

	private void setMqttClientLog() {
		Handler defaultHandler = new ConsoleHandler();
		LogManager logManager = LogManager.getLogManager();
		Logger logger = Logger.getLogger(MQTT_PACKAGE);
		defaultHandler.setFormatter(new SimpleFormatter());
		defaultHandler.setLevel(Level.SEVERE);
		logger.setLevel(Level.SEVERE);
		logger.addHandler(defaultHandler);
		logManager.addLogger(logger);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(DEBUGTAG, "onStartCommand::intent::" + intent + "::flags::"
				+ flags + "::startId::" + startId);

		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.d(DEBUGTAG, "onDestroy");
		try {
			release();
		} catch (Exception e) {
			e.printStackTrace();
		}
		scheduledExecutorService.shutdown();
		logger.info("mqtt커넥션서비스를종료합니다.");
		super.onDestroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.d(DEBUGTAG, "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onLowMemory() {
		Log.d(DEBUGTAG, "onLowMemory");
		super.onLowMemory();
	}

	@Override
	public void onTrimMemory(int level) {
		Log.d(DEBUGTAG, "onTrimMemory");
		super.onTrimMemory(level);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(DEBUGTAG, "onUnbind::intent::" + intent);
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		Log.d(DEBUGTAG, "onRebind::intent::" + intent);
		super.onRebind(intent);
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		Log.d(DEBUGTAG, "onTaskRemoved::intent::" + rootIntent);
		Intent restartServiceIntent = new Intent(getApplicationContext(),
				this.getClass());
		restartServiceIntent.setPackage(getPackageName());

		PendingIntent restartServicePendingIntent = PendingIntent.getService(
				getApplicationContext(), 1, restartServiceIntent,
				PendingIntent.FLAG_ONE_SHOT);
		AlarmManager alarmService = (AlarmManager) getApplicationContext()
				.getSystemService(Context.ALARM_SERVICE);
		alarmService.set(AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime() + 1000,
				restartServicePendingIntent);
		super.onTaskRemoved(rootIntent);
	}

	@Override
	public void run() {
		Log.d(DEBUGTAG, "mqtt헬스체크를시작합니다.");
		try {
			if (mqttClient == null) {

				Log.d(DEBUGTAG, "mqtt연결을처음시도합니다.");
				connectAndSubscribe();
			} else if (!mqttClient.isConnected()) {
				Log.d(DEBUGTAG, "서버와연결되어있지않으므로접속을시도합니다.");
				reconnect();
			}
			errorMsg = null;
		} catch (Exception e) {
			errorMsg = e.toString();
			Log.e(DEBUGTAG, "문제가발생하였습니다.", e);
		}
		Log.d(DEBUGTAG, "mqtt헬스체크를종료합니다.");
	}

	private void reconnect() throws MqttException {
		mqttClient.connect();
		mqttClient.subscribe(TOPIC, 2);
		// 리커넥트시에 초기시도했던 상태값들이 정상적으로 되어있는지
		// 커넥트옵션이나 서브스크라이브가 정확히 되어있는지 상태 체크바람
		Log.d(DEBUGTAG, "세션연결및토픽구독?(구독연부확인바람)을완료하였습니다.");
	}

	protected void connectAndSubscribe() throws MqttException {
		MqttConnectOptions mOpts = new MqttConnectOptions();
		mOpts.setConnectionTimeout(connectionTimeout);
		mOpts.setKeepAliveInterval(keepAliveInterval);
		mOpts.setCleanSession(false);
		mqttClient = new MqttClient(SERVERURL, deviceID, null);
		Log.d(DEBUGTAG, "mqttClient인스턴스가생성되었습니다.::" + mqttClient);
		mqttClient.setCallback(this);
		mqttClient.connect(mOpts);
		mqttClient.subscribe(TOPIC, 2);
		Log.d(DEBUGTAG, "세션연결및토픽구독을완료하였습니다.");
	}

	public void release() throws Exception {
		Log.d(DEBUGTAG, "mqttClient::" + mqttClient);
		if (mqttClient == null) {
			return;
		}

		Log.d(DEBUGTAG, "mqttClient세션연결상태::" + mqttClient.isConnected());
		if (mqttClient.isConnected()) {
			mqttClient.disconnect();
			Log.d(DEBUGTAG, "mqttClient연결을끊었습니다.");
		}
		mqttClient.close();
		Log.d(DEBUGTAG, "mqttClient가종료되었습니다.");
	}

	@Override
	public void connectionLost(Throwable throwable) {
		errorMsg = throwable.toString();
		Log.e(DEBUGTAG, "mqttTCP세션연결이끊겼습니다.", throwable);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		Log.d(DEBUGTAG, "deliveryComplete::" + token);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message)
			throws Exception {
		Log.i(DEBUGTAG,
				"messageArrived::topic::" + topic + " qos::" + message.getQos()
						+ " length::" + message.getPayload().length);

		if (message.getPayload().length < 100) {
			Log.i(DEBUGTAG, "message::" + new String(message.getPayload()));
			return;
		}

		JSONObject jsonObj = new JSONObject(new String(message.getPayload()));

		JSONObject noti = jsonObj.getJSONObject("notification");
		JSONObject event = jsonObj.getJSONObject("event");

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

		PendingIntent addCalPendingIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, makeAddCalIntent(event),
				PendingIntent.FLAG_UPDATE_CURRENT);

		Bitmap bmBigPicture;
		// bmBigPicture = BitmapFactory.decodeResource(getResources(),
		// R.drawable.android_jellybean);
		// byte[] data = message.getPayload();

		// Log.d(DEBUGTAG, "image::" + noti.getString("image"));

		byte[] data = Base64.decodeBase64(noti.getString("image").getBytes());
		bmBigPicture = BitmapFactory.decodeByteArray(data, 0, data.length);

		Uri alarmSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
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
						.addAction(R.drawable.ic_action_new_event, ADDCALENDAR,
								addCalPendingIntent)).bigPicture(bmBigPicture)
				.setBigContentTitle(noti.getString("contentTitle"))
				.setSummaryText(noti.getString("summaryText")).build();

		// notification.ledARGB = Color.YELLOW;
		manager.notify(msgCnt++, notification);
	}

	private Intent makeAddCalIntent(JSONObject event) throws JSONException {
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
		return addCalIntent;
	}
}
