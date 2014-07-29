package kr.co.adflow.service;

import java.io.FileDescriptor;
import java.io.PrintWriter;

import kr.co.adflow.push.handler.PushHandler;
import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

/**
 * @author nadir93
 * @date 2014. 6. 18.
 */
public class PushService extends Service {

	// TAG for debug
	public static final String TAG = "푸시서비스";
	private static PowerManager.WakeLock wakeLock;
	public static PushHandler pushHandler;

	public PushService() {
		Log.d(TAG, "PushService생성자시작()");
		Log.d(TAG, "PushService=" + this);
		Log.d(TAG, "PushService생성자종료()");
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "onBind시작(intent=" + intent + ")");
		Log.d(TAG, "onBind종료(리턴=null)");
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate시작()");
		pushHandler = new PushHandler(this);
		Log.d(TAG, "pushHandler=" + pushHandler);
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
			// for galaxy s3
			if (intent == null) {
				Log.e(TAG, "intent가 존재하지않습니다.");
				return Service.START_STICKY;
			}

			// intent에 따른 분기처리
			if (intent.getAction().equals("kr.co.adflow.action.healthCheck")) {
				// 헬스체크
				Log.d(TAG, "헬스체크를시작합니다.");
				pushHandler.healthCheck(intent);
				Log.d(TAG, "헬스체크를종료합니다.");

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

	public static PowerManager.WakeLock getWakeLock() {
		return wakeLock;
	}

	public static void setWakeLock(PowerManager.WakeLock lock) {
		wakeLock = lock;
	}

}
