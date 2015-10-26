/*
 * Copyright (C) ADFlow, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by nadir93 <typark@adflow.co.kr>, October 2015
 */

package kr.co.adflow.smarthomedemo;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.Date;

import kr.co.adflow.push.client.mqttv3.binder.PushLocalBinder;
import kr.co.adflow.push.client.mqttv3.service.PushService;
import kr.co.adflow.push.client.mqttv3.service.impl.PushServiceImpl;

public class MyReceiver extends BroadcastReceiver {

    // Logger for this class.
    private static final String TAG = "MyReceiver";

    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive 시작(context=" + context + ", intent=" + intent + ")");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Log.v(TAG, key + " : " + bundle.get(key));
            }
        }
        //intent.addFlag(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //            V  sender : sktsmarthome
        //            V  receiver : testTopic
        //            V  contentType : text/plain
        //            V  msgId : 2015102a6ae422f87243f3ba54c3dee22bf8f0
        //            V  token : 0f463d7ccffb1034ab9716a
        //            V  content : 테스트 케이스 메시지 전송 !!
        i.putExtra("msgId", bundle.getString("msgId"));
        context.startActivity(i);

//        // bind service
//        Intent i = new Intent(context, PushServiceImpl.class);
//        Log.d(TAG, "로컬 푸시서비스를 연결합니다");
//        context.bindService(i, mConnection, Context.BIND_AUTO_CREATE);
//        Log.d(TAG, "로컬 푸시서비스 연결상태=" + (mBound ? "바인드" : "언바인드"));
//
//        if (mBound) {
//
////            V  sender : sktsmarthome
////            V  receiver : testTopic
////            V  contentType : text/plain
////            V  msgId : 2015102a6ae422f87243f3ba54c3dee22bf8f0
////            V  token : 0f463d7ccffb1034ab9716a
////            V  content : 테스트 케이스 메시지 전송 !!
//
//            mService.ack(bundle.getString("msgId"), new Date());
//        }
//
//
//        // Unbind from the service
//        if (mBound) {
//            Log.d(TAG, "로컬 푸시서비스 연결을 종료합니다");
//            context.unbindService(mConnection);
//            mBound = false;
//            Log.d(TAG, "로컬 푸시서비스가 연결 종료되었습니다");
//        }

        Log.d(TAG, "onReceive 종료()");
    }
}
