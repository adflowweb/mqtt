package kr.co.adflow.push.receiver;

import kr.co.adflow.push.PushPreference;
import kr.co.adflow.push.handler.PushHandler;
import kr.co.adflow.push.service.impl.PushServiceImpl;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

/**
 * @author nadir93
 */
public class PushReceiver extends BroadcastReceiver {

	// Debug TAG
	private static final String TAG = "PushReceiver";
	private static final int wakeLockHoldTime = 10000; // 웨이크락홀드타임 10초

	public PushReceiver() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive시작(context=" + context + ",intent=" + intent + ")");
		try {
			if (intent == null || intent.getAction() == null) {
				Log.e(TAG, "action이 존재하지않습니다.");
				return;
			}

			if (intent.getAction().equals(
					"android.intent.action.BOOT_COMPLETED")) {
				Log.d(TAG, "부팅완료작업을시작합니다.");

				// // testCode
				// PushPreference preference = new PushPreference(context);
				// // 토큰저장
				// preference.put(PushPreference.TOKEN, "ADF_CLIENT_NADIR");
				// Log.d(TAG, "토큰저장 token=ADF_CLIENT_NADIR");
				// // server url 저장
				// preference.put(PushPreference.SERVERURL,
				// "ssl://14.63.216.249");
				// Log.d(TAG, "서버주소저장 server=ssl://14.63.216.249");
				// // testCodeEnd

				Log.d(TAG, "keepAlive알람을설정합니다.");
				AlarmManager service = (AlarmManager) context
						.getSystemService(Context.ALARM_SERVICE);
				Intent i = new Intent(context, PushReceiver.class);
				i.setAction("kr.co.adflow.push.service.KEEPALIVE");
				PendingIntent pending = PendingIntent.getBroadcast(context, 0,
						i, PendingIntent.FLAG_UPDATE_CURRENT);
				service.setRepeating(AlarmManager.RTC_WAKEUP,
						System.currentTimeMillis() + 1000,
						1000 * PushHandler.alarmInterval, pending);
				Log.d(TAG, "keepAlive알람이설정되었습니다");
				Log.d(TAG, "부팅완료작업을종료합니다.");
			} else if (intent.getAction().equals(
					"kr.co.adflow.push.service.KEEPALIVE")) {

				// keepalive
				Log.d(TAG, "keepAlive체크를시작합니다.");
				Bundle bundle = intent.getExtras();
				for (String key : bundle.keySet()) {
					Log.d(TAG, key + "=" + bundle.get(key));
				}

				// get wakelock
				PowerManager pm = (PowerManager) context
						.getSystemService(Context.POWER_SERVICE);
				WakeLock wakeLock = pm.newWakeLock(
						PowerManager.PARTIAL_WAKE_LOCK, "KTPWakeLock");
				PushServiceImpl.setWakeLock(wakeLock);
				Log.d(TAG, "웨이크락상태=" + wakeLock.isHeld());
				if (!wakeLock.isHeld()) {
					wakeLock.acquire(wakeLockHoldTime);
					// ..screen will stay on during this section..
					Log.d(TAG, "웨이크락=" + wakeLock);
					Intent i = new Intent(context, PushServiceImpl.class);
					i.setAction("kr.co.adflow.push.service.KEEPALIVE");
					context.startService(i);
					// wakeLock.release();
				}
				Log.d(TAG, "keepAlive체크를종료합니다.");
			} else {
				Log.e(TAG, "적절한처리핸들러가없습니다.");
			}
		} catch (Exception e) {
			Log.e(TAG, "예외상황발생", e);
		}
		Log.d(TAG, "onReceive종료()");
	}
}
