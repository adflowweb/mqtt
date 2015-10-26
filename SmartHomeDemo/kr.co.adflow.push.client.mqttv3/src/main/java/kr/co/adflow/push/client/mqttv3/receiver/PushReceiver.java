/*
 * Copyright (C) ADFlow, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by nadir93 <typark@adflow.co.kr>, October 2015
 */

package kr.co.adflow.push.client.mqttv3.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import kr.co.adflow.push.client.mqttv3.BuildConfig;
import kr.co.adflow.push.client.mqttv3.service.impl.PushServiceImpl;

public class PushReceiver extends BroadcastReceiver {
    // Logger for this class.
    private static final String TAG = "PushReceiver";

    public PushReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive 시작(context=" + context + ", intent=" + intent + ")");

        if (intent == null || intent.getAction() == null) {
            Log.e(TAG, "action이 존재하지 않습니다");
            return;
        }

        try {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Log.v(TAG, key + " : " + bundle.get(key));
                }
            }

            if (intent.getAction().equals(
                    "android.intent.action.BOOT_COMPLETED")) {
                Log.d(TAG, "부팅 완료작업을 시작합니다");

                //알람설정
                Log.d(TAG, "keepAlive 알람을 설정합니다.");
                AlarmManager service = (AlarmManager) context
                        .getSystemService(Context.ALARM_SERVICE);
                Intent i = new Intent(context, PushReceiver.class);
                i.setAction("kr.co.adflow.push.service.KEEPALIVE");
                PendingIntent pending = PendingIntent.getBroadcast(context, 0,
                        i, PendingIntent.FLAG_UPDATE_CURRENT);
                service.setRepeating(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis(),
                        1000 * BuildConfig.ALARM_INTERVAL, pending);
                Log.d(TAG, "keepAlive 알람이 설정되었습니다");
                Log.d(TAG, "부팅 완료작업을 종료합니다");
            } else if (intent.getAction().equals(
                    "kr.co.adflow.push.service.KEEPALIVE")) {
                //웨이크락 얻어오기
                PowerManager pm = (PowerManager) context
                        .getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wakeLock = pm.newWakeLock(
                        PowerManager.PARTIAL_WAKE_LOCK, "ADFWakeLock");
                PushServiceImpl.setWakeLock(wakeLock);
                Log.d(TAG, "웨이크락상태=" + wakeLock.isHeld());

                if (!wakeLock.isHeld()) {
                    wakeLock.acquire(BuildConfig.WAKE_LOCK_HOLD_TIME);
                    // ..screen will stay on during this section..
                    Log.d(TAG, "웨이크락=" + wakeLock);

                    //서비스 호출
                    Intent i = new Intent(context, PushServiceImpl.class);
                    i.setAction("kr.co.adflow.push.service.KEEPALIVE");
                    context.startService(i);
                    // wakeLock.release();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "onReceive 처리중 에러발생", e);
        }
        Log.d(TAG, "onReceive 종료()");
    }
}
