package kr.co.adflow.push.service;

import kr.co.adflow.push.PushPreference;
import kr.co.adflow.push.service.impl.PushServiceImpl;

import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

		for (String key : bundle.keySet()) {
			Log.d(TAG, key + "=" + bundle.get(key));
		}

		if (bundle.getString("serviceID") == null
				|| bundle.getString("serviceID").equals("")) {
			Log.e(TAG, "serviceID가 존재하지않습니다.");
			return;
		}

		String msgID = bundle.getString("msgID");
		Log.d(TAG, "msgID=" + msgID);

		// action 분기처리
		if (intent.getAction().equals("kr.co.adflow.push.service.START")) {
			Log.d(TAG, "푸시서비스시작");
			try {
				String token = bundle.getString(PushPreference.TOKEN);
				Log.d(TAG, "token=" + token);

				String server = bundle.getString(PushPreference.SERVERURL);
				Log.d(TAG, "server=" + server);

				boolean cleanSession = bundle
						.getBoolean(PushPreference.CLEANSESSION);
				Log.d(TAG, "cleanSession=" + cleanSession);

				if (token == null || token.equals("") || server == null
						|| server.equals("")) {
					Log.e(TAG, "입력데이터가적절하지않습니다.");
					sendBroadcast(bundle.getString("serviceID"), msgID,
							intent.getAction(),
							"{\"errMsg\":\"입력데이터가적절하지않습니다.\"}", false);
					return;
				}

				PushServiceImpl.getInstance().startPushHandler(token, server,
						cleanSession);
				// 처리후 결과 브로드캐스팅
				sendBroadcast(bundle.getString("serviceID"), msgID,
						intent.getAction(), "{\"msg\":\"푸시서비스를시작하였습니다.\"}",
						true);
			} catch (Exception e) {
				Log.e(TAG, "에러발생", e);
				// 처리후 결과 브로드캐스팅
				sendBroadcast(bundle.getString("serviceID"), msgID,
						intent.getAction(), "{\"errMsg\":\"" + e.getMessage()
								+ "\"}", false);
			}
		} else if (intent.getAction()
				.equals("kr.co.adflow.push.service.STATUS")) {
			// 푸시서비스 상태확인
			try {
				// 처리후 결과 브로드캐스팅
				sendBroadcast(bundle.getString("serviceID"), msgID,
						intent.getAction(), PushServiceImpl.getStatus(), true);
			} catch (Exception e) {
				Log.e(TAG, "에러발생", e);
				// 처리후 결과 브로드캐스팅
				sendBroadcast(bundle.getString("serviceID"), msgID,
						intent.getAction(), "{\"errMsg\":\"" + e.getMessage()
								+ "\"}", false);
			}
		} else if (intent.getAction().equals("kr.co.adflow.push.service.STOP")) {
			// 푸시서비스 종료
			// 구현에대해 고민해야함
		} else if (intent.getAction().equals("kr.co.adflow.push.service.TOKEN")) {
			Log.d(TAG, "토큰발급시작");
			// String url, String userID, String deviceID
			try {
				String url = bundle.getString("url");
				Log.d(TAG, "url=" + url);

				String userID = bundle.getString("userID");
				Log.d(TAG, "userID=" + userID);

				String deviceID = bundle.getString("deviceID");
				Log.d(TAG, "deviceID=" + deviceID);

				if (url == null || url.equals("") || userID == null
						|| userID.equals("") || deviceID == null
						|| deviceID.equals("")) {
					Log.e(TAG, "입력데이터가적절하지않습니다.");
					sendBroadcast(bundle.getString("serviceID"), msgID,
							intent.getAction(),
							"{\"errMsg\":\"입력데이터가적절하지않습니다.\"}", false);
					return;
				}

				String token = PushServiceImpl.getInstance().auth(url, userID,
						deviceID);
				Log.d(TAG, "token=" + token);
				// 처리후 결과 브로드캐스팅
				sendBroadcast(bundle.getString("serviceID"), msgID,
						intent.getAction(), token, true);
			} catch (Exception e) {
				Log.e(TAG, "에러발생", e);
				// 처리후 결과 브로드캐스팅
				sendBroadcast(bundle.getString("serviceID"), msgID,
						intent.getAction(), "{\"errMsg\":\"" + e.getMessage()
								+ "\"}", false);
			}
		} else if (intent.getAction().equals(
				"kr.co.adflow.push.service.PUBLISH")) {
			Log.d(TAG, "PUBLISH시작");
			try {
				String topic = bundle.getString("topic");
				Log.d(TAG, "topic=" + topic);
				String message = bundle.getString("message");
				Log.d(TAG, "message=" + message);
				int qos = bundle.getInt("qos");
				Log.d(TAG, "qos=" + qos);

				if (topic == null || topic.equals("") || message == null
						|| message.equals("") || 0 > qos || qos > 2) {
					Log.e(TAG, "입력데이터가적절하지않습니다.");
					sendBroadcast(bundle.getString("serviceID"), msgID,
							intent.getAction(),
							"{\"errMsg\":\"입력데이터가적절하지않습니다.\"}", false);
					return;
				}

				// 메시지처리
				JSONObject msg = new JSONObject();
				msg.put("serviceID", bundle.getString("serviceID"));
				msg.put("type", 0);
				msg.put("content", message);

				PushServiceImpl.getInstance().publish(
						bundle.getString("serviceID") + topic,
						msg.toString().getBytes(), qos);
				// 처리후 결과 브로드캐스팅
				sendBroadcast(bundle.getString("serviceID"), msgID,
						intent.getAction(), "{\"msg\":\"메시지를발송하였습니다.\"}", true);
			} catch (Exception e) {
				Log.e(TAG, "에러발생", e);
				// 처리후 결과 브로드캐스팅
				sendBroadcast(bundle.getString("serviceID"), msgID,
						intent.getAction(), "{\"errMsg\":\"" + e.getMessage()
								+ "\"}", false);
			}
		} else if (intent.getAction().equals(
				"kr.co.adflow.push.service.SUBSCRIBE")) {
			Log.d(TAG, "SUBSCRIBE시작");
			// 토픽구독
			// intent.setAction("kr.co.adflow.push.service.SUBSCRIBE");
			// intent.putExtra("qos", 2);
			// intent.putExtra("topic", "groups/g100");
			try {
				String topic = bundle.getString("topic");
				Log.d(TAG, "topic=" + topic);
				int qos = bundle.getInt("qos");
				Log.d(TAG, "qos=" + qos);

				if (topic == null || topic.equals("") || 0 > qos || qos > 2) {
					Log.e(TAG, "입력데이터가적절하지않습니다.");
					sendBroadcast(bundle.getString("serviceID"), msgID,
							intent.getAction(),
							"{\"errMsg\":\"입력데이터가적절하지않습니다.\"}", false);
					return;
				}

				PushServiceImpl.getInstance().subscribe(
						bundle.getString("serviceID") + topic, qos);
				// 처리후 결과 브로드캐스팅
				sendBroadcast(bundle.getString("serviceID"), msgID,
						intent.getAction(), "{\"msg\":\"" + topic
								+ " 구독이완료되었습니다." + "\"}", true);
			} catch (Exception e) {
				Log.e(TAG, "에러발생", e);
				// 처리후 결과 브로드캐스팅
				sendBroadcast(bundle.getString("serviceID"), msgID,
						intent.getAction(), "{\"errMsg\":\"" + e.getMessage()
								+ "\"}", false);
			}
		} else if (intent.getAction().equals(
				"kr.co.adflow.push.service.UNSUBSCRIBE")) {
			Log.d(TAG, "UNSUBSCRIBE시작");
			// 토픽구독해제
			// intent.setAction("kr.co.adflow.push.service.UNSUBSCRIBE");
			// intent.putExtra("topic", "groups/g100");
			try {
				String topic = bundle.getString("topic");
				Log.d(TAG, "topic=" + topic);

				if (topic == null || topic.equals("")) {
					Log.e(TAG, "입력데이터가적절하지않습니다.");
					sendBroadcast(bundle.getString("serviceID"), msgID,
							intent.getAction(),
							"{\"errMsg\":\"입력데이터가적절하지않습니다.\"}", false);
					return;
				}

				PushServiceImpl.getInstance().unsubscribe(
						bundle.getString("serviceID") + topic);
				// 처리후 결과 브로드캐스팅
				sendBroadcast(bundle.getString("serviceID"), msgID,
						intent.getAction(), "{\"msg\":\"" + topic
								+ " 구독이취소되었습니다." + "\"}", true);
			} catch (Exception e) {
				Log.e(TAG, "에러발생", e);
				// 처리후 결과 브로드캐스팅
				sendBroadcast(bundle.getString("serviceID"), msgID,
						intent.getAction(), "{\"errMsg\":\"" + e.getMessage()
								+ "\"}", false);
			}
		} else {
			Log.e(TAG, "적절한처리핸들러가없습니다.");
		}
		Log.d(TAG, "onHandleIntent종료()");
	}

	/**
	 * @param serviceID
	 * @param msgID
	 * @param action
	 * @param content
	 * @param success
	 */
	private void sendBroadcast(String serviceID, String msgID, String action,
			String content, boolean success) {
		Log.d(TAG, "sendBroadcast시작(serviceID=" + serviceID + ")");
		Log.d(TAG, "serviceID=" + serviceID);
		Intent i = new Intent(serviceID);
		// i.setAction(action);
		// sender
		// receiver
		// i.putExtra("topic", topic);
		i.putExtra("action", action);
		i.putExtra("content", content);
		i.putExtra("success", success);
		i.putExtra("msgID", msgID);
		// i.putExtra("qos", message.getQos());
		// i.putExtra("ack", ack);

		this.sendBroadcast(i);
		Log.d(TAG, "sendBroadcast종료()");
	}

	/**
	 * @param serviceID
	 * @param msgID
	 * @param action
	 * @param content
	 * @param success
	 */
	private void sendBroadcast(String serviceID, String msgID, String action,
			int content, boolean success) {
		Log.d(TAG, "sendBroadcast시작(serviceID=" + serviceID + ")");
		Log.d(TAG, "serviceID=" + serviceID);
		Intent i = new Intent(serviceID);
		// i.setAction(action);
		// sender
		// receiver
		// i.putExtra("topic", topic);
		i.putExtra("action", action);
		i.putExtra("content", content);
		i.putExtra("success", success);
		i.putExtra("msgID", msgID);
		// i.putExtra("qos", message.getQos());
		// i.putExtra("ack", ack);

		this.sendBroadcast(i);
		Log.d(TAG, "sendBroadcast종료()");
	}
}
