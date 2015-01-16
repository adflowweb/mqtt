package kr.co.adflow.push.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import kr.co.adflow.push.PushPreference;
import kr.co.adflow.push.service.impl.PushServiceImpl;

public class PushIntentService extends IntentService {

	// TAG for debug
	public static final String TAG = "PushIntentService";

	public PushIntentService() {
		super("PushIntentService");
		Log.d(TAG, "PushIntentService생성자시작(name=PushIntentService)");
		Log.d(TAG, "PushIntentService=" + this);
		Log.d(TAG, "PushIntentService생성자종료()");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "onHandleIntent시작(intent=" + intent + ")");

		Bundle bundle = intent.getExtras();

		if (bundle == null) {
			Log.e(TAG, "bundle이 존재하지않습니다.");
			return;
		}

		// action 분기처리
		if (intent.getAction().equals("kr.co.adflow.push.service.START")) {
			Log.d(TAG, "푸시서비스시작");
			try {
				for (String key : bundle.keySet()) {
					Log.d(TAG, key + "=" + bundle.get(key));
				}
				String token = bundle.getString(PushPreference.TOKEN);
				Log.d(TAG, "token=" + token);

				String server = bundle.getString(PushPreference.SERVERURL);
				Log.d(TAG, "server=" + server);

				boolean cleanSession = bundle
						.getBoolean(PushPreference.CLEANSESSION);
				Log.d(TAG, "cleanSession=" + cleanSession);

				PushServiceImpl.getInstance().startPushHandler(token, server,
						cleanSession);
				// 처리후 결과 브로드캐스팅
				sendBroadcast(bundle.getString("serviceID"), "푸시서비스를시작하였습니다.",
						true);
			} catch (Exception e) {
				Log.e(TAG, "에러발생", e);
				// 처리후 결과 브로드캐스팅
				sendBroadcast(bundle.getString("serviceID"), e.getMessage(),
						false);
			}

		} else if (intent.getAction().equals("kr.co.adflow.push.service.STOP")) {

		} else if (intent.getAction().equals(
				"kr.co.adflow.push.service.PUBLISH")) {
			Log.d(TAG, "PUBLISH시작");
			try {
				for (String key : bundle.keySet()) {
					Log.d(TAG, key + "=" + bundle.get(key));
				}
				String topic = bundle.getString("topic");
				Log.d(TAG, "topic=" + topic);
				String message = bundle.getString("message");
				Log.d(TAG, "message=" + message);
				int qos = bundle.getInt("qos");
				Log.d(TAG, "qos=" + qos);

				if (topic == null || topic.equals("") || message == null
						|| message.equals("")) {
					Log.e(TAG, "데이터가적절하지않습니다.");
					return;
				}

				PushServiceImpl.getInstance().publish(topic,
						message.getBytes(), qos);
				// 처리후 결과 브로드캐스팅
				sendBroadcast(bundle.getString("serviceID"), "메시지를발송하였습니다.",
						true);
			} catch (Exception e) {
				Log.e(TAG, "에러발생", e);
				// 처리후 결과 브로드캐스팅
				sendBroadcast(bundle.getString("serviceID"), e.getMessage(),
						false);
			}
		} else if (intent.getAction().equals(
				"kr.co.adflow.push.service.SUBSCRIBE")) {
			Log.d(TAG, "SUBSCRIBE시작");
			// 처리후 결과 브로드캐스팅
		} else if (intent.getAction().equals(
				"kr.co.adflow.push.service.UNSUBSCRIBE")) {
			Log.d(TAG, "UNSUBSCRIBE시작");
			// 처리후 결과 브로드캐스팅
		} else {
			Log.e(TAG, "적절한처리핸들러가없습니다.");
		}
		Log.d(TAG, "onHandleIntent종료()");
	}

	private void sendBroadcast(String serviceID, String content, boolean success) {
		Log.d(TAG, "sendBroadcast시작(serviceID=" + serviceID + ")");
		Log.d(TAG, "serviceID=" + serviceID);
		Intent i = new Intent(serviceID);
		// sender
		// receiver
		// i.putExtra("topic", topic);
		i.putExtra("content", content);
		i.putExtra("success", success);
		// i.putExtra("qos", message.getQos());
		// i.putExtra("ack", ack);

		this.sendBroadcast(i);
		Log.d(TAG, "sendBroadcast종료()");
	}
}
