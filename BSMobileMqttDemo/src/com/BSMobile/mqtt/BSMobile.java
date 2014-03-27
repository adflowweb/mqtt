package com.BSMobile.mqtt;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;

import com.worklight.androidgap.WLDroidGap;

public class BSMobile extends WLDroidGap {

	// Debug TAG
	public static final String DEBUGTAG = "부산모바일액티비티";

	@Override
	public void onPause() {
		Log.d(DEBUGTAG, "onPause시작()");
		super.onPause();
		Log.d(DEBUGTAG, "onPause종료()");
	}

	@Override
	public void onResume() {
		Log.d(DEBUGTAG, "onResume시작()");
		Bundle extras = getIntent().getExtras();
		Log.d(DEBUGTAG, "intent::" + getIntent());
		Log.d(DEBUGTAG, "extras::" + extras);
		// if (extras != null) {
		// Log.d(DEBUGTAG, "image::" + extras.get("image"));
		// }

		super.onResume();
		Log.d(DEBUGTAG, "onResume종료()");
	}

	@Override
	protected void onRestart() {
		Log.d(DEBUGTAG, "onRestart시작()");
		super.onRestart();
		Log.d(DEBUGTAG, "onRestart종료()");
	}

	@Override
	protected void onStart() {
		Log.d(DEBUGTAG, "onStart시작()");
		String deviceID = Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);
		Log.d(DEBUGTAG, "deviceID::" + deviceID);
		super.onStart();
		Log.d(DEBUGTAG, "onStart종료()");
	}

	@Override
	protected void onStop() {
		Log.d(DEBUGTAG, "onStop시작()");
		super.onStop();
		Log.d(DEBUGTAG, "onStop종료()");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(DEBUGTAG, "onCreate시작(bundle=" + savedInstanceState + ")");
		super.onCreate(savedInstanceState);

		// this.appView.setWebViewClient(new WebViewClient() {
		//
		// public void onPageFinished(WebView view, String url) {
		// // do your stuff here
		// pageLoad = true;
		// Log.d(DEBUGTAG, "onPageFinished");
		// }
		// });
		onNewIntent(getIntent());
		Log.d(DEBUGTAG, "onCreate종료()");
	}

	@Override
	public void onNewIntent(Intent intent) {
		Log.d(DEBUGTAG, "onNewIntent시작(intent=" + intent + ")");

		Bundle extras = intent.getExtras();
		if (extras != null) {
			if (extras != null) {
				// Log.d(DEBUGTAG, "image::" + extras.get("image"));
				// super.loadUrl("javascript:$.mobile.changePage('#education',{transition:'slide'});");

				super.loadUrl("javascript:loadEducation('"
						+ extras.get("image") + "');");
				Log.d(DEBUGTAG, "calledLoadEducation");
				// super.loadUrl("javascript:alert('hello');");
				// webView.loadUrl("javascript:alert('hello');")
			}
		}
		Log.d(DEBUGTAG, "onNewIntent종료()");
	}

	/**
	 * onWLInitCompleted is called when the Worklight runtime framework
	 * initialization is complete
	 */
	@Override
	public void onWLInitCompleted(Bundle savedInstanceState) {
		Log.d(DEBUGTAG, "onWLInitCompleted시작(bundle=" + savedInstanceState
				+ ")");
		super.loadUrl(getWebMainFilePath());
		// Add custom initialization code after this line
		// use this to start and trigger a service

		// Intent i = new Intent(this, MqttPushService.class);
		// startService(i);

		AlarmManager service = (AlarmManager) this
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(this, MqttPushServiceReceiver.class);
		PendingIntent pending = PendingIntent.getBroadcast(this, 0, i,
				PendingIntent.FLAG_CANCEL_CURRENT);
		// Calendar cal = Calendar.getInstance();
		// start 30 seconds after boot completed
		// cal.add(Calendar.SECOND, 1);
		// fetch every 30 seconds
		// InexactRepeating allows Android to optimize the energy consumption
		service.setInexactRepeating(AlarmManager.RTC_WAKEUP,
		/* cal.getTimeInMillis() */0, 1000 * 240, pending);

		Log.d(DEBUGTAG, "푸시알람이설정되었습니다");

		// service.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
		// REPEAT_TIME, pending);
		Log.d(DEBUGTAG, "onWLInitCompleted종료()");
	}

	@Override
	public void onDestroy() {
		Log.d(DEBUGTAG, "onDestroy시작()");

		// System.out
		// .println("stopMqttClientService!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		// // use this to start and trigger a service
		// Intent i = new Intent(this, MqttClientService.class);
		// // potentially add data to the intent
		// i.putExtra("KEY1", "Value to be used by the service");
		// stopService(i);
		super.onDestroy();
		Log.d(DEBUGTAG, "onDestroy종료()");
	}
}
