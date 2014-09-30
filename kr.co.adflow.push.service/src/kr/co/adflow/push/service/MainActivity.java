package kr.co.adflow.push.service;

import kr.co.adflow.push.handler.PushHandler;
import kr.co.adflow.push.receiver.PushReceiver;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class MainActivity extends Activity {
	// TAG for debug
	public static final String TAG = "PushMainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(TAG, "keepAlive알람을설정합니다.");
		AlarmManager service = (AlarmManager) this
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(this, PushReceiver.class);
		i.setAction("kr.co.adflow.push.service.KEEPALIVE");
		PendingIntent pending = PendingIntent.getBroadcast(this, 0, i,
				PendingIntent.FLAG_UPDATE_CURRENT);
		service.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis() + 1000,
				1000 * PushHandler.alarmInterval, pending);
		Log.d(TAG, "keepAlive알람이설정되었습니다");

		// display toast
		Toast toast = Toast.makeText(getApplicationContext(),
				"푸시서비스가 시작되었습니댜.", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		// finish
		this.finish();
	}
}
