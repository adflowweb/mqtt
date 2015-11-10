/*
 * Copyright (C) ADFlow, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by nadir93 <typark@adflow.co.kr>, October 2015
 */

package kr.co.adflow.push.client.mqttv3.binder;

import android.os.Binder;
import android.util.Log;

import kr.co.adflow.push.client.mqttv3.service.PushService;
import kr.co.adflow.push.client.mqttv3.util.DebugLog;

/**
 * Created by nadir93 on 15. 10. 19..
 */
public class PushLocalBinder extends Binder {

    /**
     *
     */
    PushService pushService;

    /**
     * @param pushService
     */
    public PushLocalBinder(PushService pushService) {
        DebugLog.d("PushLocalBinder 생성자 시작(pushService=" + pushService + ")");
        this.pushService = pushService;
        DebugLog.d("PushLocalBinder 생성자 종료()");
    }

    /**
     * @return
     */
    public PushService getService() {
        DebugLog.d("getService 시작()");
        DebugLog.d("getService 종료(pushService=" + pushService + ")");
        // Return this instance of LocalService so clients can call public methods
        return pushService;
    }
}