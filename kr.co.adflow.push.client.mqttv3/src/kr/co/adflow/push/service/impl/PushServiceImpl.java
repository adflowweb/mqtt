package kr.co.adflow.push.service.impl;

import java.io.FileDescriptor;
import java.io.PrintWriter;

import kr.co.adflow.push.PushPreference;
import kr.co.adflow.push.handler.PushHandler;
import kr.co.adflow.push.receiver.PushReceiver;
import kr.co.adflow.push.service.PushService;

import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class PushServiceImpl extends Service implements PushService {

	// TAG for debug
	public static final String TAG = "PushService";
	private static PowerManager.WakeLock wakeLock;
	private PushHandler pushHandler;
	private PushPreference preference;
	// Binder given to clients
	private final IBinder binder = new LocalBinder();

	public PushServiceImpl() {
		Log.d(TAG, "PushService생성자시작()");
		Log.d(TAG, "PushService=" + this);
		pushHandler = new PushHandler(this);
		Log.d(TAG, "pushHandler=" + pushHandler);
		preference = new PushPreference(this);
		Log.d(TAG, "preference=" + preference);
		Log.d(TAG, "PushService생성자종료()");
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "onBind시작(intent=" + intent + ")");
		Log.d(TAG, "onBind종료(binder=" + binder + ")");
		return binder;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate시작()");
		Log.d(TAG, "onCreate종료()");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.d(TAG, "onStart시작(intent=" + intent + ",startId=" + startId + ")");
		Log.d(TAG, "onStart종료()");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand시작(intent=" + intent + ", flags=" + flags
				+ ", startId=" + startId + ")");

		try {
			if (intent == null) {
				Log.e(TAG, "intent가 존재하지않습니다.");
				return Service.START_STICKY;
			}

			// action 분기처리
			if (intent.getAction().equals("kr.co.adflow.push.service.START")) {
				// 푸시서비스 시작
				Log.d(TAG, "푸시서비스를시작합니다.");
				Bundle bundle = intent.getExtras();

				if (bundle == null) {
					Log.e(TAG, "bundle이 존재하지않습니다.");
					return Service.START_STICKY;
				}

				for (String key : bundle.keySet()) {
					Log.d(TAG, key + " is a key in the bundle");
					Log.d(TAG, "value=" + bundle.get(key));
				}
				String token = bundle.getString(PushPreference.TOKEN);
				Log.d(TAG, "token=" + token);

				String server = bundle.getString(PushPreference.SERVERURL);
				Log.d(TAG, "server=" + server);

				boolean cleanSession = bundle
						.getBoolean(PushPreference.CLEANSESSION);
				Log.d(TAG, "cleanSession=" + cleanSession);

				if (token != null) {
					// 토큰저장
					preference.put(PushPreference.TOKEN, token);
					// server url 저장
					preference.put(PushPreference.SERVERURL, server);
					// cleanSession 저장
					preference.put(PushPreference.CLEANSESSION, cleanSession);
					pushHandler.start();

					Log.d(TAG, "알람을설정합니다.");
					AlarmManager service = (AlarmManager) this
							.getSystemService(Context.ALARM_SERVICE);
					Intent i = new Intent(this, PushReceiver.class);
					i.setAction("kr.co.adflow.push.service.KEEPALIVE");
					PendingIntent pending = PendingIntent.getBroadcast(this, 0,
							i, PendingIntent.FLAG_UPDATE_CURRENT);
					service.setRepeating(AlarmManager.RTC_WAKEUP,
							System.currentTimeMillis() + 1000,
							1000 * PushHandler.alarmInterval, pending);
					Log.d(TAG, "알람이설정되었습니다");
				} else {

				}
			} else if (intent.getAction().equals(
					"kr.co.adflow.push.service.STOP")) {
				Log.d(TAG, "푸시서비스를종료합니다.");
				pushHandler.stop();
			} else if (intent.getAction().equals(
					"kr.co.adflow.push.service.KEEPALIVE")) {
				Log.d(TAG, "keepalive체크를시작합니다.");
				pushHandler.keepAlive();

			}
			// else if (intent.getAction().equals("kr.co.adflow.action.login"))
			// {
			// // 로그인시
			// Log.d(TAG, "로그인처리를시작합니다.");
			// pushHandler.login(intent);
			// Log.d(TAG, "로그인처리를종료합니다.");
			// }
			else {
				Log.e(TAG, "적절한처리핸들러가없습니다.");
			}
		} catch (Exception e) {
			Log.e(TAG, "예외상황발생", e);
		}

		int ret = super.onStartCommand(intent, flags, startId);
		Log.d(TAG, "onStartCommand종료(리턴코드=" + ret + ")");
		return ret;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy시작()");
		pushHandler.stop();
		// 알람제거해야함
		Log.d(TAG, "onDestroy종료()");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.d(TAG, "onConfigurationChanged시작(config=" + newConfig + ")");
		super.onConfigurationChanged(newConfig);
		Log.d(TAG, "onConfigurationChanged종료()");
	}

	@Override
	public void onLowMemory() {
		Log.d(TAG, "onLowMemory시작()");
		Log.d(TAG, "onLowMemory종료()");
	}

	@Override
	public void onTrimMemory(int level) {
		Log.d(TAG, "onTrimMemory시작(level=" + level + ")");
		Log.d(TAG, "onTrimMemory종료()");
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(TAG, "onUnbind시작(intent=" + intent + ")");
		Log.d(TAG, "onUnbind종료()");
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		Log.d(TAG, "onRebind시작(intent=" + intent + ")");
		Log.d(TAG, "onRebind종료()");
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		Log.d(TAG, "onTaskRemoved시작(intent=" + rootIntent + ")");
		Log.d(TAG, "onTaskRemoved종료()");
	}

	@Override
	protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
		Log.d(TAG, "dump시작(fd=" + fd + "||writer=" + writer + "||args=" + args
				+ ")");
		super.dump(fd, writer, args);
		Log.d(TAG, "dump종료()");
	}

	/**
	 * @return
	 */
	public PowerManager.WakeLock getWakeLock() {
		Log.d(TAG, "getWakeLock시작()");
		Log.d(TAG, "getWakeLock종료(wakeLock=" + wakeLock + ")");
		return wakeLock;
	}

	/**
	 * @param lock
	 */
	public void setWakeLock(PowerManager.WakeLock lock) {
		Log.d(TAG, "setWakeLock시작(lock=" + lock + ")");
		wakeLock = lock;
		Log.d(TAG, "setWakeLock종료(wakeLock=" + wakeLock + ")");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.service.PushService#publish(java.lang.String,
	 * byte[], int)
	 */
	@Override
	public void publish(String topic, byte[] payload, int qos) throws Exception {
		Log.d(TAG, "publish시작(토픽=" + topic + ", qos=" + qos + ")");
		Log.d(TAG, "service=" + this);
		Log.d(TAG, "pushHandler=" + pushHandler);
		if (pushHandler != null || pushHandler.isConnected()) {
			// 일단 retained = false로 세팅
			pushHandler.publish(topic, payload, qos, false);
		} else {
			throw new Exception("푸시핸들러문제로전송에실패하였습니다.");
		}
		Log.d(TAG, "publish종료()");
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.service.PushService#subscribe(java.lang.String,
	 * int)
	 */
	@Override
	public void subscribe(String topic, int qos) throws Exception {
		Log.d(TAG, "subScribe시작(토픽=" + topic + ", qos=" + qos + ")");
		pushHandler.subscribe(topic, qos);
		Log.d(TAG, "subscribe종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.service.PushService#unsubscribe(java.lang.String)
	 */
	@Override
	public void unsubscribe(String topic) throws Exception {
		Log.d(TAG, "unsubscribe시작(토픽=" + topic + ")");
		pushHandler.unsubscribe(topic);
		Log.d(TAG, "unsubscribe종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.service.PushService#preCheck(java.lang.String)
	 */
	@Override
	public void preCheck(String sender, String topic) throws Exception {
		Log.d(TAG, "preCheck시작(sender=" + sender + ", 토픽=" + topic + ")");
		JSONObject data = new JSONObject();

		// modified by nadir
		// 메시지 크기를 줄이기 위해 삭제 요청됨
		// data.put("sender", sender);
		// data.put("receiver", topic);
		// modified end
		data.put("type", 103); // 103 : precheck
		Log.d(TAG, "보낼메시지=" + data.toString());
		// qos:0
		pushHandler.publish(topic, data.toString().getBytes(), 0, false);
		Log.d(TAG, "preCheck종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.service.PushService#isConnected()
	 */
	@Override
	public boolean isConnected() {
		Log.d(TAG, "isConnected시작()");
		boolean value = false;
		if (pushHandler != null) {
			value = pushHandler.isConnected();
		}
		Log.d(TAG, "isConnected종료(value=" + value + ")");
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.service.PushService#getLostCout()
	 */
	public int getLostCout() {
		Log.d(TAG, "getLostCout시작()");
		int lostCount = pushHandler.getLostCout();
		Log.d(TAG, "getLostCout종료(lostCount=" + lostCount + ")");
		return lostCount;
	}

	/**
	 * Class used for the client Binder. Because we know this service always
	 * runs in the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		public PushService getService() {
			Log.d(TAG, "getService시작()");
			// Return this instance of LocalService so clients can call public
			// methods
			Log.d(TAG, "getService종료(value=" + PushServiceImpl.this + ")");
			return PushServiceImpl.this;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.service.PushService#auth(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String auth(String url, String userID, String deviceID)
			throws Exception {
		Log.d(TAG, "auth시작(url=" + url + ", userID=" + userID + ", deviceID="
				+ deviceID + ")");
		String ret = pushHandler.auth(url, userID, deviceID);
		Log.d(TAG, "auth종료(return=" + ret + ")");
		return ret;
	}

}
