package kr.co.adflow.receiver;

import kr.co.adflow.push.handler.PushHandler;
import kr.co.adflow.service.PushService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

/**
 * @author nadir93
 * @date 2014. 6. 18.
 */
public class PushBroadcastReceiver extends BroadcastReceiver {

	// Debug TAG
	private static final String TAG = "푸시브로드캐스트리시버";
	private static final int wakeLockHoldTime = 10000; // 10초

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
				// 부팅시
				Log.d(TAG, "부팅완료작업을시작합니다.");

				// 토큰을 저장소 에서 가져와 토큰이 있으면 알람을 설정 없으면 skip
				// SharedPreferences sp = context.getSharedPreferences("token",
				// Context.MODE_PRIVATE);
				// String tokenID = sp.getString("tokenID", null);
				// Log.d(TAG, "tokenID=" + tokenID);

				// if (tokenID != null) {
				// 알람설정(헬스체크)
				Log.d(TAG, "헬스체크알람을설정합니다.");
				AlarmManager service = (AlarmManager) context
						.getSystemService(Context.ALARM_SERVICE);
				Intent i = new Intent(context, PushBroadcastReceiver.class);
				i.setAction("kr.co.adflow.action.healthCheck");
				PendingIntent pending = PendingIntent.getBroadcast(context, 0,
						i, PendingIntent.FLAG_CANCEL_CURRENT);
				service.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0,
						1000 * PushHandler.alarmInterval, pending);
				Log.d(TAG, "헬스체크알람이설정되었습니다");
				// }

				Log.d(TAG, "부팅완료작업을종료합니다.");

			} else if (intent.getAction().equals(
					"kr.co.adflow.action.healthCheck")) {
				// 헬스체크
				Log.d(TAG, "헬스체크를시작합니다.");

				PowerManager pm = (PowerManager) context
						.getSystemService(Context.POWER_SERVICE);
				WakeLock wakeLock = pm.newWakeLock(
						PowerManager.PARTIAL_WAKE_LOCK,
						"MqttPushServiceReceiverWakeLock");
				PushService.setWakeLock(wakeLock);
				Log.d(TAG, "웨이크락상태=" + wakeLock.isHeld());
				if (!wakeLock.isHeld()) {
					wakeLock.acquire(wakeLockHoldTime);
					// ..screen will stay on during this section..
					Log.d(TAG, "웨이크락=" + wakeLock);
					Intent healthChkIntent = new Intent(context,
							PushService.class);
					healthChkIntent
							.setAction("kr.co.adflow.action.healthCheck");
					context.startService(healthChkIntent);
					// wakeLock.release();
				}
				Log.d(TAG, "헬스체크를종료합니다.");
			} else {
				Log.e(TAG, "적절한처리핸들러가없습니다.");
			}
		} catch (Exception e) {
			Log.e(TAG, "예외상황발생", e);
		}
		Log.d(TAG, "onReceive종료()");
	}
}
