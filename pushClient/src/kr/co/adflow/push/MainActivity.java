package kr.co.adflow.push;

import kr.co.adflow.push.handler.PushHandler;
import kr.co.adflow.receiver.PushBroadcastReceiver;
import kr.co.adflow.sqlite.PushDBHelper;
import kr.co.adflow.sqlite.User;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;

import com.worklight.androidgap.WLDroidGap;

/**
 * @author nadir93
 * @date 2014. 6. 19.
 */
public class MainActivity extends WLDroidGap {

	// Debug TAG
	public static final String DEBUGTAG = "메인액티비티";

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

		// /////임시코드
		final PushDBHelper pushdb = new PushDBHelper(this);
		pushdb.onUpgrade(pushdb.getWritableDatabase(), 0, 1);
		final User user = new User();
		try {

			user.setUserID("nadir93");
			user.setPassword("passw0rd");
			user.setTokenID("08be01ba2e1f4e2e8216725");
			user.setCurrentUser(true);
			pushdb.addUser(user);
			Log.d(TAG, "임시유저가등록되었습니다. user=" + user);

		} catch (Exception e) {
			Log.e(TAG, "예외상황발생", e);
		}

		// 알람설정
		Log.d(TAG, "헬스체크알람을설정합니다.");
		AlarmManager service = (AlarmManager) this
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(this, PushBroadcastReceiver.class);
		i.setAction("kr.co.adflow.action.healthCheck");
		PendingIntent pending = PendingIntent.getBroadcast(this, 0, i,
				PendingIntent.FLAG_CANCEL_CURRENT);
		service.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0,
				1000 * PushHandler.alarmInterval, pending);
		Log.d(TAG, "헬스체크알람이설정되었습니다");

		// /////임시코드종료
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

	// // Debug TAG
	// public static final String TAG = "액티비티";
	//
	// @Override
	// protected void onCreate(Bundle savedInstanceState) {
	// Log.d(TAG, "onCreate시작(bundle=" + savedInstanceState + ")");
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.activity_main);
	//
	// if (savedInstanceState == null) {
	// getSupportFragmentManager().beginTransaction()
	// .add(R.id.container, new PlaceholderFragment()).commit();
	// }
	//
	// // /////임시코드
	// final PushDBHelper pushdb = new PushDBHelper(this);
	// pushdb.onUpgrade(pushdb.getWritableDatabase(), 0, 1);
	// final User user = new User();
	// try {
	//
	// user.setUserID("nadir93");
	// user.setPassword("passw0rd");
	// user.setTokenID("08be01ba2e1f4e2e8216725");
	// user.setCurrentUser(true);
	// pushdb.addUser(user);
	// Log.d(TAG, "임시유저가등록되었습니다. user=" + user);
	//
	// Button button = (Button) findViewById(R.id.button1);
	//
	// button.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View view) {
	//
	// user.setCurrentUser(false);
	// pushdb.updateUser(user);
	// user.setUserID("testUser");
	// user.setCurrentUser(true);
	// user.setTokenID("1234567890");
	// pushdb.addUser(user);
	//
	// // Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
	// // .parse("http://www.mkyong.com"));
	// // startActivity(browserIntent);
	// }
	// });
	// } catch (Exception e) {
	// Log.e(TAG, "예외상황발생", e);
	// }
	// // /////임시코드종료
	//
	// // 알람설정
	// Log.d(TAG, "헬스체크알람을설정합니다.");
	// AlarmManager service = (AlarmManager) this
	// .getSystemService(Context.ALARM_SERVICE);
	// Intent i = new Intent(this, PushBroadcastReceiver.class);
	// i.setAction("kr.co.adflow.action.healthCheck");
	// PendingIntent pending = PendingIntent.getBroadcast(this, 0, i,
	// PendingIntent.FLAG_CANCEL_CURRENT);
	// service.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0,
	// 1000 * PushHandler.alarmInterval, pending);
	// Log.d(TAG, "헬스체크알람이설정되었습니다");
	//
	// Log.d(TAG, "onCreate종료()");
	// }
	//
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	//
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle action bar item clicks here. The action bar will
	// // automatically handle clicks on the Home/Up button, so long
	// // as you specify a parent activity in AndroidManifest.xml.
	// int id = item.getItemId();
	// if (id == R.id.action_settings) {
	// return true;
	// }
	// return super.onOptionsItemSelected(item);
	// }
	//
	// /**
	// * A placeholder fragment containing a simple view.
	// */
	// public static class PlaceholderFragment extends Fragment {
	//
	// public PlaceholderFragment() {
	// }
	//
	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// View rootView = inflater.inflate(R.layout.fragment_main, container,
	// false);
	// return rootView;
	// }
	// }

}
