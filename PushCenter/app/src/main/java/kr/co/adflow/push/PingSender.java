package kr.co.adflow.push;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager.WakeLock;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttPingSender;
import org.eclipse.paho.client.mqttv3.internal.ClientComms;

import java.text.SimpleDateFormat;

import kr.co.adflow.push.handler.PushHandler;
import kr.co.adflow.push.receiver.PushReceiver;
import kr.co.adflow.push.service.impl.PushServiceImpl;
import kr.co.adflow.push.util.DebugLog;

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
        DebugLog.d("init 시작()");
        this.comms = comms;
        DebugLog.d("init 종료()");

    }

    @Override
    public void schedule(long delayInMilliseconds) {
        DebugLog.d("schedule 시작(delayInMilliseconds = " + delayInMilliseconds
                + ")");

        //keepAlive 시간과 상관없이 알람주기(60초)마다 알람은 일어나야함.
        if (delayInMilliseconds > BuildConfig.ALARM_INTERVAL * 1000) {
            delayInMilliseconds = BuildConfig.ALARM_INTERVAL * 1000;
        }

        DebugLog.d("다음 알람은 " + (delayInMilliseconds / 1000) + "초 후 입니다");

        long nextAlarmInMilliseconds = System.currentTimeMillis()
                + delayInMilliseconds;
        //DebugLog.d(TAG,
        //        "다음알람설정시간 = " + dayTime.format(new Date(nextAlarmInMilliseconds)));
        // 알람설정
        DebugLog.d("알람을 설정합니다");
        AlarmManager service = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, PushReceiver.class);
        i.setAction("kr.co.adflow.push.service.KEEPALIVE");
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        service.setRepeating(AlarmManager.RTC_WAKEUP, nextAlarmInMilliseconds,
                BuildConfig.ALARM_INTERVAL * 1000, pending);
        DebugLog.d("알람이 설정되었습니다");
        DebugLog.d("schedule 종료()");
    }

    @Override
    public void start() {
        DebugLog.d("start 시작()");
        schedule(comms.getKeepAlive());
        DebugLog.d("start 종료()");
    }

    @Override
    public void stop() {
        DebugLog.d("stop 시작()");
        DebugLog.d("stop 종료()");
    }

    /**
     * 핑
     */
    public void ping() {
        DebugLog.d("ping 시작()");
        pingSent = System.currentTimeMillis();
        IMqttToken token = comms.checkForActivity();
        DebugLog.d("핑 토큰 = " + token);

        // No ping has been sent.
        if (token == null) {
            DebugLog.d("핑 보낼 타이밍이 아닙니다");
            releaseLock();
            DebugLog.d("ping 종료()");
            return;
        }

        token.setActionCallback(new IMqttActionListener() {

            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                DebugLog.d("핑 성공 핑 토큰 = " + asyncActionToken);
                DebugLog.d("소요 시간 = " + (System.currentTimeMillis() - pingSent)
                        + "ms");
                releaseLock();
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken,
                                  Throwable exception) {
                DebugLog.d("핑 실패 핑 토큰 = " + asyncActionToken);
                DebugLog.d("소요 시간 = " + (System.currentTimeMillis() - pingSent)
                        + "ms");
                releaseLock();
            }
        });
        DebugLog.d("ping 종료()");
    }

    /**
     * release wake lock
     */
    private void releaseLock() {
        DebugLog.d("releaseLock 시작()");
        DebugLog.d("context = " + context);
        WakeLock lock = PushServiceImpl.getWakeLock();
        DebugLog.d("웨이크락 = " + lock);
        if (lock != null && lock.isHeld()) {
            try {
                lock.release();
                DebugLog.d("웨이크락이 해제되었습니다");
            } catch (Exception e) {
                DebugLog.e("예외 상황 발생", e);
            }
        } else {
            DebugLog.d("웨이크락이 없거나 이미 해재되었습니다");
        }
        DebugLog.d("releaseLock 종료()");
    }
}
