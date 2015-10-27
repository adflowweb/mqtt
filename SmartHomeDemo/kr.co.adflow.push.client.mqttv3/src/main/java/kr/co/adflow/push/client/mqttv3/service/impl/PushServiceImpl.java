/*
 * Copyright (C) ADFlow, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by nadir93 <typark@adflow.co.kr>, October 2015
 */

package kr.co.adflow.push.client.mqttv3.service.impl;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Date;

import kr.co.adflow.push.client.mqttv3.BuildConfig;
import kr.co.adflow.push.client.mqttv3.Client;
import kr.co.adflow.push.client.mqttv3.binder.PushLocalBinder;
import kr.co.adflow.push.client.mqttv3.broadcast.PushBroadcaster;
import kr.co.adflow.push.client.mqttv3.domain.Data;
import kr.co.adflow.push.client.mqttv3.domain.Response;
import kr.co.adflow.push.client.mqttv3.receiver.PushReceiver;
import kr.co.adflow.push.client.mqttv3.service.PushService;
import kr.co.adflow.push.client.mqttv3.util.SharedPreferenceEntry;
import kr.co.adflow.push.client.mqttv3.util.SharedPreferencesHelper;

/**
 * Created by nadir93 on 15. 10. 13..
 */
public class PushServiceImpl extends Service implements PushService {

    // Logger for this class.
    private static final String TAG = "PushServiceImpl";

    public static final String SHARED_PREFERENCES_NAME = BuildConfig.SHARED_PREFERENCES_NAME;
    private static PushServiceImpl instance;

    // Binder given to clients
    private final IBinder mBinder;


    // The helper that manages writing to SharedPreferences.
    private SharedPreferencesHelper mSharedPreferencesHelper;
    private static PowerManager.WakeLock wakeLock;
    private Client client;
    private PushBroadcaster broadcaster;

    /**
     * 생성자
     */
    public PushServiceImpl() {
        Log.d(TAG, "PushServiceImpl 생성자 시작()");
        Log.d(TAG, "this : " + this);
        instance = this;
        client = new Client(this);
        mBinder = new PushLocalBinder(this);
        broadcaster = new PushBroadcaster(this);
        Log.d(TAG, "PushServiceImpl 생성자 종료()");
    }

    public static PushServiceImpl getInstance() {
        return instance;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind 시작(intent=" + intent + ")");
        Log.d(TAG, "onBind 종료(binder=" + mBinder + ")");
        return mBinder;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate 시작()");
        initSharedPreferences();
        Log.d(TAG, "onCreate 종료()");
    }

    /**
     * preferences 초기화
     */
    private void initSharedPreferences() {
        Log.d(TAG, "initSharedPreferences 시작()");
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        // Instantiate a SharedPreferencesHelper.
        mSharedPreferencesHelper = new SharedPreferencesHelper(sharedPreferences);
        Log.d(TAG, "initSharedPreferences 종료()");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(TAG, "onStart 시작(intent=" + intent + ", startId=" + startId + ")");
        Log.d(TAG, "onStart 종료()");
    }

    /**
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand 시작(intent=" + intent + ", flags=" + flags
                + ", startId=" + startId + ")");

        if (intent == null) {
            Log.v(TAG, "intent가 존재하지 않습니다");
            return START_STICKY;
        }

        try {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Log.v(TAG, key + " : " + bundle.get(key));
                }
            }

            // action 분기처리
            if (intent.getAction().equals("kr.co.adflow.push.service.START")) {
                Log.d(TAG, "푸시서비스를 시작합니다");
                // 설정 저장
                SharedPreferenceEntry mqttInfo = new SharedPreferenceEntry(bundle.getString(SharedPreferenceEntry.TOKEN),
                        bundle.getString(SharedPreferenceEntry.SERVER), bundle.getInt(SharedPreferenceEntry.PORT),
                        bundle.getInt(SharedPreferenceEntry.KEEP_ALIVE), bundle.getBoolean(SharedPreferenceEntry.CLEAN_SESSION),
                        bundle.getBoolean(SharedPreferenceEntry.SSL));
                mSharedPreferencesHelper.saveMqttInfo(mqttInfo);

                // 알람 설정
                Log.d(TAG, "알람을 설정합니다");
                AlarmManager service = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                Intent i = new Intent(getApplicationContext(), PushReceiver.class);
                i.setAction("kr.co.adflow.push.service.KEEPALIVE");
                PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), 0, i,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                service.setRepeating(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + (1000 * BuildConfig.ALARM_INTERVAL),
                        1000 * BuildConfig.ALARM_INTERVAL, pending);
                Log.d(TAG, "알람이 설정되었습니다");

                // 푸시 시작
                client.keepAlive();
                Log.d(TAG, "푸시서비스가 시작되었습니다");
            } else if (intent.getAction().equals(
                    "kr.co.adflow.push.service.KEEPALIVE")) {
                client.keepAlive();
            }
//            else if (intent.getAction().equals(
//                    "kr.co.adflow.push.service.STOP")) {
//                Log.d(TAG, "푸시서비스를 종료합니다");
//                client.stop();
//                this.stopSelf();
//                Log.d(TAG, "푸시서비스가 종료되었습니다");
//            }
            else {
                Log.e(TAG, "적절한 처리핸들러가 없습니다");
            }
        } catch (Exception e) {
            Log.e(TAG, "onStartCommand 처리중 에러발생", e);
        }

        Log.d(TAG, "onStartCommand 종료(value=" + START_STICKY + ")");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy 시작()");

        //알람 제거
        AlarmManager service = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getApplicationContext(), PushReceiver.class);
        i.setAction("kr.co.adflow.push.service.KEEPALIVE");
        PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        service.cancel(pending);
        Log.d(TAG, "알람이 제거되었습니다");

        //client 종료
        client.stop();

        Log.d(TAG, "onDestroy 종료()");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged 시작(newConfig=" + newConfig + ")");
        Log.d(TAG, "onConfigurationChanged 종료()");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG, "onLowMemory 시작()");
        Log.d(TAG, "onLowMemory 종료()");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.d(TAG, "onTrimMemory 시작(level=" + level + ")");
        Log.d(TAG, "onTrimMemory 종료()");

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind 시작(intent=" + intent + ")");
        boolean value = super.onUnbind(intent);
        Log.d(TAG, "onUnbind 종료(value=" + value + ")");
        return value;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "onRebind 시작(intent=" + intent + ")");
        Log.d(TAG, "onRebind 종료()");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(TAG, "onTaskRemoved 시작(rootIntent=" + rootIntent + ")");
        Log.d(TAG, "onTaskRemoved 종료()");
    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(fd, writer, args);
        Log.d(TAG, "dump 시작(fd=" + fd + ", writer=" + writer + ", args=" + args + ")");
        Log.d(TAG, "dump 종료()");
    }

    /**
     * 웨이크락 가져 오기
     *
     * @return
     */
    public static PowerManager.WakeLock getWakeLock() {
        return wakeLock;
    }

    /**
     * @param lock
     */
    public static void setWakeLock(PowerManager.WakeLock lock) {
        Log.d(TAG, "setWakeLock 시작(lock=" + lock + ")");
        wakeLock = lock;
        Log.d(TAG, "setWakeLock 종료(wakeLock=" + wakeLock + ")");
    }

    /**
     * @return
     */
    public SharedPreferencesHelper getmSharedPreferencesHelper() {
        return mSharedPreferencesHelper;
    }

    /**
     * @return
     */
    public PushBroadcaster getBroadcaster() {
        return broadcaster;
    }

    /**
     * 토큰등록 (IF_A_101)
     * <p/>
     * 설명 : Mqtt 세션 인증 토큰을 등록한다
     * <p/>
     * 제약사항 : 토큰 등록없이 MQTT Push는 동작하지 않습니다
     *
     * @param token
     * @return
     * @throws Exception
     */
    @Override
    public String registerToken(String token, boolean rightAway) {
        Log.d(TAG, "registerToken 시작(token=" + token + ", rightAway=" + rightAway + ")");
        String response = null;
        try {

            // TODO: 15. 10. 20.
            // validation token
            // 23자리
            // not null || not ""

            if (mSharedPreferencesHelper.saveToken(token)) {
                /*{
                    "status": "ok",
                    "code": 101200,
                    "message": "토큰 등록이 완료되었습니다"
                }*/

                if (rightAway) {
                    // push 재시작
                    client.keepAlive();
                }

                Response res = new Response();
                res.setStatus("ok");
                res.setCode(101200);
                res.setMessage("토큰 등록이 완료되었습니다");
                Data data = new Data();
                data.setToken(token);
                data.setRightAway(rightAway);
                res.setData(data);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                response = gson.toJson(res);
                Log.d(TAG, "registerToken 종료(response=" + response + ")");
                return response;
            } else {
                Log.e(TAG, "토큰 등록이 실패하였습니다");
                /*{
                    "status": "fail",
                    "code": 101404,
                    "message": "토큰 등록이 실패하였습니다",
                }*/
                Response res = new Response();
                res.setStatus("fail");
                res.setCode(101404);
                res.setMessage("토큰 등록이 실패하였습니다");
                Data data = new Data();
                data.setToken(token);
                data.setRightAway(rightAway);
                res.setData(data);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                response = gson.toJson(res);
                Log.d(TAG, "registerToken 종료(response=" + response + ")");
                return response;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e(TAG, "토큰 등록 처리중 에러발생", e);
            /*{
                "status": "fail",
                "code": 101500,
                "message": e.getMessage(),
            }*/
            Response res = new Response();
            res.setStatus("fail");
            res.setCode(101500);
            res.setMessage(e.getMessage());
            Data data = new Data();
            data.setToken(token);
            data.setRightAway(rightAway);
            res.setData(data);
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            response = gson.toJson(res);
            Log.d(TAG, "registerToken 종료(response=" + response + ")");
            return response;
        }
    }

    /**
     * 토큰 가져오기 (IF_A_102)
     * <p/>
     * 설명 : mqtt 세션 인증 토큰을 가져온다
     *
     * @return
     */
    @Override
    public String getToken() {
        Log.d(TAG, "getToken 시작()");
        String response = null;
        try {
            SharedPreferenceEntry sharedPreferenceEntry = mSharedPreferencesHelper.getMqttInfo();

            if (sharedPreferenceEntry.getToken() != null) {

                /*{
                    "status": "ok",
                    "data": {
                        "token":"1c7253e191334f1e9a09915"
                    },
                    "code": 102200,
                    "message": "토큰 가져오기가 완료되었습니다"
                }*/

                Response res = new Response();
                res.setStatus("ok");
                res.setCode(102200);
                res.setMessage("토큰 가져오기가 완료되었습니다");
                Data data = new Data();
                data.setToken(sharedPreferenceEntry.getToken());
                res.setData(data);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                response = gson.toJson(res);
                Log.d(TAG, "getToken 종료(response=" + response + ")");
                return response;
            } else {
                Log.e(TAG, "토큰이 존재하지 않습니다");
                /*{
                    "status": "fail",
                    "code": 102404,
                    "message": "토큰이 존재하지 않습니다",
                }*/
                Response res = new Response();
                res.setStatus("fail");
                res.setCode(102404);
                res.setMessage("토큰이 존재하지 않습니다");
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                response = gson.toJson(res);
                Log.d(TAG, "getToken 종료(response=" + response + ")");
                return response;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e(TAG, "토큰 가져오기 처리중 에러발생", e);
            /*{
                "status": "fail",
                "code": 102500,
                "message": e.getMessage(),
            }*/
            Response res = new Response();
            res.setStatus("fail");
            res.setCode(102500);
            res.setMessage(e.getMessage());
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            response = gson.toJson(res);
            Log.d(TAG, "getToken 종료(response=" + response + ")");
            return response;
        }
    }

    /**
     * send ping (IF_A_105)
     * <p/>
     * 설명 : 실제 메시지를 보내본다
     *
     * @return
     */
    @Override
    public String ping() {
        Log.d(TAG, "ping 시작()");
        String response = null;
        try {
            client.ping();

            //{
            //    "status": "ok",
            //    "code": 105200,
            //    "message": "핑 메시지가 성공하였습니다"
            //}

            Response res = new Response();
            res.setStatus("ok");
            res.setCode(105200);
            res.setMessage("핑 메시지가 성공하였습니다");
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            response = gson.toJson(res);
            Log.d(TAG, "ping 종료(response=" + response + ")");
            return response;
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e(TAG, "ping 처리중 에러발생", e);
            /*{
                "status": "fail",
                "code": 105500,
                "message": e.getMessage(),
            }*/
            Response res = new Response();
            res.setStatus("fail");
            res.setCode(105500);
            res.setMessage(e.getMessage());
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            response = gson.toJson(res);
            Log.d(TAG, "ping 종료(response=" + response + ")");
            return response;
        }
    }

    /**
     * 메시지수신확인 (IF_A_107)
     * <p/>
     * 설명 : 메시지 수신 확인 정보를 서버로 전송한다
     *
     * @return
     */
    @Override
    public String ack(String msgId, Date ackTime) {
        Log.d(TAG, "ack 시작(msgId=" + msgId + ", ackTime=" + ackTime + ")");
        String response = null;

        try {
            client.ack(msgId, ackTime, "app");

            //{
            //  "status": "ok",
            //  "code": 107200,
            //  "message": "메시지 수신확인이 완료되었습니다"
            //}
            Response res = new Response();
            res.setStatus("ok");
            res.setCode(107200);
            res.setMessage("메시지 수신확인이 완료되었습니다");
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            response = gson.toJson(res);
            Log.d(TAG, "response : " + response);
            Log.d(TAG, "ack 종료(response=" + response + ")");
            return response;
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e(TAG, "ack 처리중 에러발생", e);

            Response res = new Response();
            res.setStatus("fail");
            res.setCode(107500);
            res.setMessage(e.getMessage());
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            response = gson.toJson(res);
            Log.d(TAG, "response : " + response);
            Log.d(TAG, "ack 종료(response=" + response + ")");
            return response;
        }
    }


    /**
     * 토픽 구독 (IF_A_108)
     * <p/>
     * 설명 : 토픽을 구독한다
     *
     * @param topic
     * @param qos
     * @return
     */
    @Override
    public String subscribe(String topic, int qos) {
        Log.d(TAG, "subscribe 시작(토픽=" + topic + ", qos=" + qos + ")");
        String response = null;
        try {
            client.subscribe(topic, qos);
            // {
            //      "status": "ok",
            //      "code": 108200,
            //      "message": "토픽 구독이 완료되었습니다"
            // }
            Response res = new Response();
            res.setStatus("ok");
            res.setCode(108200);
            res.setMessage("토픽 구독이 완료되었습니다");
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            response = gson.toJson(res);
            Log.d(TAG, "response : " + response);
            Log.d(TAG, "subscribe 종료(response=" + response + ")");
            return response;
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e(TAG, "subscribe 처리중 에러발생 ", e);

            Response res = new Response();
            res.setStatus("fail");
            res.setCode(108500);
            res.setMessage(e.getMessage());
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            response = gson.toJson(res);
            Log.d(TAG, "response : " + response);
            Log.d(TAG, "subscribe 종료(response=" + response + ")");
            return response;
        }
    }


    /**
     * 토픽 구독 취소 (IF_A_109)
     * <p/>
     * 설명 : 토픽 구독을 취소한다
     *
     * @param topic
     * @return
     */
    @Override
    public String unsubscribe(String topic) {
        Log.d(TAG, "unsubscribe 시작(토픽=" + topic + ")");
        String response = null;
        try {
            client.unsubscribe(topic);
            // {
            //      "status": "ok",
            //      "code": 109200,
            //      "message": "토픽 구독취소가 완료되었습니다"
            // }
            Response res = new Response();
            res.setStatus("ok");
            res.setCode(109200);
            res.setMessage("토픽 구독취소가 완료되었습니다");
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            response = gson.toJson(res);
            Log.d(TAG, "response : " + response);
            Log.d(TAG, "unsubscribe 종료(response=" + response + ")");
            return response;
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e(TAG, "unsubscribe 처리중 에러발생 ", e);

            Response res = new Response();
            res.setStatus("fail");
            res.setCode(109500);
            res.setMessage(e.getMessage());
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            response = gson.toJson(res);
            Log.d(TAG, "response : " + response);
            Log.d(TAG, "unsubscribe 종료(response=" + response + ")");
            return response;
        }
    }

    /**
     * 토픽 구독 정보 가져오기 (IF_A_110) REST
     * <p/>
     * 설명 : 토픽 구독 정보를 가져온다
     *
     * @return
     */
    @Override
    public String getSubscriptions() {
        Log.d(TAG, "getSubscriptions 시작()");
        String response = null;

        try {
            response = client.getSubscriptions();
            Log.d(TAG, "getSubscriptions 종료(response=" + response + ")");
            return response;
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e(TAG, "getSubscriptions 처리중 에러발생", e);

            Response res = new Response();
            res.setStatus("fail");
            res.setCode(110500);
            res.setMessage(e.getMessage());
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            response = gson.toJson(res);
            Log.d(TAG, "getSubscriptions 종료(response=" + response + ")");
            return response;
        }
    }

    /**
     * 세션 상태 보기 (IF_A_111)
     * <p/>
     * 설명 : 세션 상태 보기
     *
     * @return
     */
    @Override
    public String getSession() {
        Log.d(TAG, "getSession 시작()");
        String response = null;
        try {

            // {
            //      "status": "ok",
            //      "data": {
            //          "connected": true,
            //          "ssl": true,
            //          "token" : "1c7253e191334f1e9a09915"
            //      },
            //      "code": 111200,
            //      "message": "세션정보 가져오기가 완료되었습니다"
            // }
            Response res = new Response();
            res.setStatus("ok");
            res.setCode(111200);
            res.setMessage("세션정보 가져오기가 완료되었습니다");
            Data data = new Data();
            data.setConnected(client.isConnected());
            SharedPreferenceEntry mqttInfo = mSharedPreferencesHelper.getMqttInfo();
            data.setToken(mqttInfo.getToken());
            data.setSsl(mqttInfo.isSsl());
            res.setData(data);
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            response = gson.toJson(res);
            Log.d(TAG, "getSession 종료(response=" + response + ")");
            return response;
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e(TAG, "getSession 처리중 에러발생", e);

            Response res = new Response();
            res.setStatus("fail");
            res.setCode(111500);
            res.setMessage(e.getMessage());
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            response = gson.toJson(res);
            Log.d(TAG, "getSession 종료(response=" + response + ")");
            return response;
        }
    }
}
