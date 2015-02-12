package kr.co.adflow.push;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttPingSender;
import org.eclipse.paho.client.mqttv3.internal.ClientComms;

import java.text.SimpleDateFormat;

import kr.co.adflow.push.handler.PushHandler;
import kr.co.adflow.push.receiver.PushReceiver;
import kr.co.adflow.push.service.impl.PushServiceImpl;

/**
 * @author nadir93
 */
public class PingSender implements MqttPingSender {

    // TAG for debug
    public static final String TAG = "PingSender";
    private ClientComms comms;
    private Context context;
    private static SimpleDateFormat dayTime = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:SS");
    private long pingSent;

    public PingSender(Context context) {
        this.context = context;
    }

    @Override
    public void init(ClientComms comms) {
        Log.d(TAG, "init시작()");
        this.comms = comms;
        Log.d(TAG, "init종료()");

    }

    @Override
    public void schedule(long delayInMilliseconds) {
        Log.d(TAG, "schedule시작(delayInMilliseconds=" + delayInMilliseconds
                + ")");

        //keepAlive 시간과 상관없이 알람주기(60초)마다 알람은 일어나야함.
        if (delayInMilliseconds > PushHandler.ALARM_INTERVAL * 1000) {
            delayInMilliseconds = PushHandler.ALARM_INTERVAL * 1000;
        }

        Log.d(TAG, "다음알람은" + (delayInMilliseconds / 1000) + "초후입니다.");

        long nextAlarmInMilliseconds = System.currentTimeMillis()
                + delayInMilliseconds;
        //Log.d(TAG,
        //        "다음알람설정시간=" + dayTime.format(new Date(nextAlarmInMilliseconds)));
        // 알람설정
        Log.d(TAG, "알람을설정합니다.");
        AlarmManager service = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, PushReceiver.class);
        i.setAction("kr.co.adflow.push.service.KEEPALIVE");
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        service.setRepeating(AlarmManager.RTC_WAKEUP, nextAlarmInMilliseconds,
                PushHandler.ALARM_INTERVAL * 1000, pending);
        Log.d(TAG, "알람이설정되었습니다");
        Log.d(TAG, "schedule종료()");
    }

    @Override
    public void start() {
        Log.d(TAG, "start시작()");
        schedule(comms.getKeepAlive());
        Log.d(TAG, "start종료()");
    }

    @Override
    public void stop() {
        Log.d(TAG, "stop시작()");
        Log.d(TAG, "stop종료()");
    }

    /**
     * 핑
     */
    public void ping() {
        Log.d(TAG, "ping시작()");
        pingSent = System.currentTimeMillis();
        IMqttToken token = comms.checkForActivity();
        Log.d(TAG, "핑토큰=" + token);

        // No ping has been sent.
        if (token == null) {
            Log.d(TAG, "핑보낼타이밍이아닙니다.");
            releaseLock();
            Log.d(TAG, "ping종료()");
            return;
        }

        token.setActionCallback(new IMqttActionListener() {

            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.d(TAG, "핑성공. 핑토큰=" + asyncActionToken);
                Log.d(TAG, "소요시간=" + (System.currentTimeMillis() - pingSent)
                        + "ms");
                releaseLock();
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken,
                                  Throwable exception) {
                Log.d(TAG, "핑실패. 핑토큰=" + asyncActionToken);
                Log.d(TAG, "소요시간=" + (System.currentTimeMillis() - pingSent)
                        + "ms");
                releaseLock();
            }
        });
        Log.d(TAG, "ping종료()");
    }

    /**
     * release wake lock
     */
    private void releaseLock() {
        Log.d(TAG, "releaseLock시작()");
        Log.d(TAG, "context=" + context);
        WakeLock lock = PushServiceImpl.getWakeLock();
        Log.d(TAG, "웨이크락=" + lock);
        if (lock != null && lock.isHeld()) {
            try {
                lock.release();
                Log.d(TAG, "웨이크락이해재되었습니다");
            } catch (Exception e) {
                Log.e(TAG, "예외상황발생", e);
            }
        } else {
            Log.d(TAG, "웨이크락없거나이미해재되었습니다");
        }
        Log.d(TAG, "releaseLock종료()");
    }
}
