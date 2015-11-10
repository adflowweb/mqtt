/*
 * Copyright (C) ADFlow, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by nadir93 <typark@adflow.co.kr>, October 2015
 */

package kr.co.adflow.push.client.mqttv3.handler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPingSender;
import org.eclipse.paho.client.mqttv3.internal.ClientComms;

import java.text.SimpleDateFormat;

import kr.co.adflow.push.client.mqttv3.BuildConfig;
import kr.co.adflow.push.client.mqttv3.receiver.PushReceiver;
import kr.co.adflow.push.client.mqttv3.service.impl.PushServiceImpl;
import kr.co.adflow.push.client.mqttv3.util.DebugLog;

/**
 * Created by nadir93 on 15. 10. 16..
 */
public class PingHandler implements MqttPingSender {

    private ClientComms comms;
    private Context context;
    private static SimpleDateFormat dayTime = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:SS");
    private long pingSent;

    public PingHandler(Context context) {
        this.context = context;
    }


    @Override
    public void init(ClientComms clientComms) {
        DebugLog.d("init 시작(clientComms=" + clientComms + ")");
        this.comms = clientComms;
        DebugLog.d("init 종료()");
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

    @Override
    public void schedule(long delayInMilliseconds) {
        DebugLog.d("schedule 시작(delayInMilliseconds=" + delayInMilliseconds
                + ")");

        //keepAlive 시간과 상관없이 알람주기(60초)마다 알람은 일어나야함.
        if (delayInMilliseconds > BuildConfig.ALARM_INTERVAL * 1000) {
            delayInMilliseconds = BuildConfig.ALARM_INTERVAL * 1000;
        }

        DebugLog.i("다음 알람은 " + (delayInMilliseconds / 1000) + "초 후 입니다");

        long nextAlarmInMilliseconds = System.currentTimeMillis()
                + delayInMilliseconds;
        //DebugLog.d(TAG,
        //        "다음알람설정시간=" + dayTime.format(new Date(nextAlarmInMilliseconds)));
        // 알람설정
        DebugLog.d("알람을 설정합니다");
        AlarmManager service = (AlarmManager) context.getApplicationContext()
                .getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context.getApplicationContext(), PushReceiver.class);
        i.setAction("kr.co.adflow.push.service.KEEPALIVE");
        PendingIntent pending = PendingIntent.getBroadcast(context.getApplicationContext(), 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        service.setRepeating(AlarmManager.RTC_WAKEUP, nextAlarmInMilliseconds,
                BuildConfig.ALARM_INTERVAL * 1000, pending);
        DebugLog.d("알람이 설정되었습니다");
        DebugLog.d("schedule 종료()");
    }

    /**
     * 핑
     */
    public void ping() throws Exception {
        DebugLog.d("ping 시작()");
        DebugLog.d("thread=" + Thread.currentThread());
        pingSent = System.currentTimeMillis();

//        DebugLog.d("comms.isClosed()=" + comms.isClosed());
//        DebugLog.d("comms.isConnected()=" + comms.isConnected());
//        DebugLog.d("comms.isDisconnected()=" + comms.isDisconnected());
//        DebugLog.d("comms.isConnecting()=" + comms.isConnecting());
//        DebugLog.d("comms.isDisconnecting()=" + comms.isDisconnecting());

//        if(comms.isClosed())
//        {
//            DebugLog.d("mqtt 연결이 종료되었습니다");
//            releaseLock();
//            DebugLog.d("ping 종료()");
//            return;
//        }

        IMqttToken token = comms.checkForActivity();
        DebugLog.d("핑토큰=" + token);

        // No ping has been sent.
        if (token == null) {
            DebugLog.d("핑 보낼 타이밍이 아닙니다");
            releaseLock();
            DebugLog.d("ping 종료()");
            return;
        }

        try {
            token.waitForCompletion(BuildConfig.MQTT_INTERNAL_PING_TIMEOUT);
            DebugLog.i("핑 소요시간=" + (System.currentTimeMillis() - pingSent)
                    + "ms");
        } catch (MqttException e) {
            //e.printStackTrace();
            DebugLog.e("핑 처리중 에러발생");
            e.printStackTrace();
        } finally {
            releaseLock();
        }

//        token.setActionCallback(new IMqttActionListener() {
//
//            @Override
//            public void onSuccess(IMqttToken asyncActionToken) {
//                DebugLog.d("핑성공 핑토큰=" + asyncActionToken);
//                DebugLog.i("핑 소요시간=" + (System.currentTimeMillis() - pingSent)
//                        + "ms");
//                releaseLock();
//            }
//
//            @Override
//            public void onFailure(IMqttToken asyncActionToken,
//                                  Throwable exception) {
//                DebugLog.d("핑실패. 핑토큰=" + asyncActionToken);
//                DebugLog.i("핑 소요시간=" + (System.currentTimeMillis() - pingSent)
//                        + "ms");
//                releaseLock();
//            }
//        });
        DebugLog.d("ping 종료()");
    }


    /**
     * release wake lock
     */
    private void releaseLock() {
        DebugLog.d("releaseLock 시작()");
        DebugLog.d("context=" + context);
        PowerManager.WakeLock lock = PushServiceImpl.getWakeLock();
        DebugLog.d("웨이크락=" + lock);
        if (lock != null && lock.isHeld()) {
            try {
                lock.release();
                DebugLog.d("웨이크락이 해제되었습니다");
            } catch (Exception e) {
                DebugLog.e("releaseLock 처리중 에러발생");
                e.printStackTrace();
            }
        } else {
            DebugLog.d("웨이크락이 없거나 이미 해제되었습니다");
        }
        DebugLog.d("releaseLock 종료()");
    }
}
