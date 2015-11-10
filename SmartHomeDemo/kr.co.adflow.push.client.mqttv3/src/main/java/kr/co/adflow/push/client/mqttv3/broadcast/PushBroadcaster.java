/*
 * Copyright (C) ADFlow, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by nadir93 <typark@adflow.co.kr>, October 2015
 */

package kr.co.adflow.push.client.mqttv3.broadcast;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import kr.co.adflow.push.client.mqttv3.util.DebugLog;

/**
 * Created by nadir93 on 15. 10. 20..
 */
public class PushBroadcaster {

    private Context context;

    /**
     * @param context
     */
    public PushBroadcaster(Context context) {
        DebugLog.d("PushBroadcaster 생성자 시작(context=" + context + ")");
        this.context = context;
        DebugLog.d("PushBroadcaster 생성자 종료()");
    }

    /**
     * @param intent
     * @throws Exception
     */
    public void sendBroadcast(Intent intent) throws Exception {
        DebugLog.d("sendBroadcast 시작(intent=" + intent + ")");
        //LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        context.sendBroadcast(intent);
        DebugLog.d("sendBroadcast 종료()");
    }
}
