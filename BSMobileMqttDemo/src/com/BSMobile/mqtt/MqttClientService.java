package com.BSMobile.mqtt;

import java.util.GregorianCalendar;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
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
import android.os.Debug;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.Settings.Secure;
import android.util.Log;

public class MqttClientService extends Service implements MqttCallback {

	private static final String ADDCALENDAR = "캘린더추가";
	private static final String DETAILVIEW = "자세히보기";

	// private static final String SERVERURL = "tcp://test.mosquitto.org:1883";
	// private static final String SERVERURL = "tcp://adflow.net:1883";
	private static final String SERVERURL = "tcp://175.209.8.188:1883";

	// Debug TAG
	public static final String DEBUGTAG = "MqttClientService";

	private static final String MQTT_THREAD_NAME = "MqttService[" + DEBUGTAG
			+ "]"; // Handler Thread ID

	private String deviceID;

	// Private instance variables
	private MqttClient client;
	private String brokerUrl;
	private boolean quietMode;
	private MqttConnectOptions conOpt;
	private boolean clean;
	private String password;
	private String userName;
	private Handler connHandler; // Seperate Handler thread for networking
	private int msgCnt = 0;
	private boolean started = false;
	private int intentCnt = 0;

	@Override
	public IBinder onBind(Intent paramIntent) {
		Log.d(DEBUGTAG, "onBind" + paramIntent);
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		HandlerThread thread = new HandlerThread(MQTT_THREAD_NAME);
		thread.start();
		deviceID = Secure.getString(getApplicationContext()
				.getContentResolver(), Secure.ANDROID_ID);
		connHandler = new Handler(thread.getLooper());
		Log.d(DEBUGTAG, "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(DEBUGTAG, "onStartCommand::intent::" + intent + "::flags::"
				+ flags + "::startId::" + startId);
		if (!started) {
			connect();
		}
		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.d(DEBUGTAG, "onDestroy");

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
	public void connectionLost(Throwable throwable) {
		throwable.printStackTrace();
		Log.e(DEBUGTAG, "connectionLost::" + throwable);

		started = false;
		while (!started) {
			connect();
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Received Message from broker
	 */
	public void messageArrived(MqttTopic topic, MqttMessage message)
			throws Exception {
		Log.i(DEBUGTAG, "messageArrived::topic::" + topic.getName() + " qos::"
				+ message.getQos() + " length::" + message.getPayload().length);

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

	private synchronized void connect() {
		Log.d(DEBUGTAG, "connect::-------------------------------------");
		this.clean = false;

		connHandler.post(new Runnable() {
			@Override
			public void run() {
				try {

					client = new MqttClient(SERVERURL, deviceID, null);
					client.setCallback(MqttClientService.this);
					client.log.dumpTrace();

					conOpt = new MqttConnectOptions();
					conOpt.setCleanSession(false);
					conOpt.setKeepAliveInterval(60000);
					Log.i(DEBUGTAG, "connectProp::" + conOpt.getDebug());

					// if (this.optionsComp.isLWTTopicSet()) {
					// this.opts.setWill(
					// this.mqtt.getTopic(this.optionsComp.getLWTTopic()),
					// this.optionsComp.getLWTData().getBytes(),
					// this.optionsComp.getLWTQoS(),
					// this.optionsComp.isLWTRetainSelected());
					// }

					client.connect(conOpt);

					client.getDebug().dumpClientDebug();

					client.subscribe("hello", 0);

					started = true; // 서비스시작

					Log.i(DEBUGTAG, "성공적으로 연결되었습니다.");

					// startKeepAlives();
				} catch (MqttException e) {
					e.printStackTrace();
					Log.e(DEBUGTAG, e.toString());
				}
			}
		});

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		Log.d(DEBUGTAG, "deliveryComplete::" + arg0);

	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		Log.d(DEBUGTAG, "messageArrived::" + arg0);

	}
}
