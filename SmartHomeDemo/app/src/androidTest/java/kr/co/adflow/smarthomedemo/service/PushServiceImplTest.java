/*
 * Copyright (C) ADFlow, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by nadir93 <typark@adflow.co.kr>, October 2015
 */

package kr.co.adflow.smarthomedemo.service;

import android.content.Intent;
import android.os.IBinder;
import android.test.ServiceTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import kr.co.adflow.push.client.mqttv3.BuildConfig;
import kr.co.adflow.push.client.mqttv3.service.impl.PushServiceImpl;

/**
 * Created by nadir93 on 15. 10. 15..
 */
public class PushServiceImplTest extends ServiceTestCase<PushServiceImpl> {

    // Logger for this class.
    private static final String TAG = "PushServiceImplTest";

    public PushServiceImplTest() {
        super(PushServiceImpl.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Log.d(TAG, "setUp시작()");
        Log.d(TAG, "setUp종료()");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Log.d(TAG, "tearDown시작()");
        Log.d(TAG, "tearDown종료()");
    }


    /**
     * IF_A_113
     * <p/>
     * 푸시 서비스 시작 테스트
     *
     * @throws Exception
     */
    public void test푸시서비스시작테스트() throws Exception {

        Intent i = new Intent(getContext(), PushServiceImpl.class);
        i.setAction("kr.co.adflow.push.service.START");
        i.putExtra("token", "0f463d7ccffb1034ab9716a");
        i.putExtra("server", "112.223.76.75");
        i.putExtra("port", 11883);
        i.putExtra("ssl", false);
        i.putExtra("cleanSession", false);
        i.putExtra("keepAlive", 240);
        Log.d(TAG, "푸시서비스가 시작됩니다");
        startService(i);

        String response = getService().getSession();
        Log.e(TAG, "테스트응답=" + response);
        JSONObject sessionInfo = new JSONObject(response);

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

        assertTrue(sessionInfo.getJSONObject("data").getBoolean("connected"));
    }

    /**
     * IF_A_113
     * <p/>
     * 푸시 서비스 시작 실패 테스트
     *
     * @throws Exception
     */
    public void test푸시서비스시작실패테스트() throws Exception {

        Intent i = new Intent(getContext(), PushServiceImpl.class);
        i.setAction("kr.co.adflow.push.service.START");
        i.putExtra("token", "0f463d7ccffb1034ab9716a");
        i.putExtra("server", "112.223.76.75");
        i.putExtra("port", 11884);
        i.putExtra("ssl", false);
        i.putExtra("cleanSession", false);
        i.putExtra("keepAlive", 240);
        Log.d(TAG, "푸시서비스가 시작됩니다");
        startService(i);

        String response = getService().getSession();
        Log.e(TAG, "테스트응답=" + response);
        JSONObject sessionInfo = new JSONObject(response);

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

        assertFalse(sessionInfo.getJSONObject("data").getBoolean("connected"));
    }

    /**
     * IF_A_113
     * <p/>
     * 푸시 서비스 시작 보안 연결 테스트
     *
     * @throws Exception
     */
    public void test푸시서비스시작_보안연결_테스트() throws Exception {

        Intent i = new Intent(getContext(), PushServiceImpl.class);
        i.setAction("kr.co.adflow.push.service.START");
        i.putExtra("token", "0f463d7ccffb1034ab9716a");
        i.putExtra("server", "112.223.76.75");
        i.putExtra("port", 18883);
        i.putExtra("ssl", true);
        i.putExtra("cleanSession", false);
        i.putExtra("keepAlive", 240);
        Log.d(TAG, "푸시서비스가 시작됩니다");
        startService(i);

        String response = getService().getSession();
        Log.e(TAG, "테스트응답=" + response);
        JSONObject sessionInfo = new JSONObject(response);

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

        assertTrue(sessionInfo.getJSONObject("data").getBoolean("connected"));
    }

    /**
     * IF_A_102
     * <p/>
     * 토큰 가져오기 테스트
     *
     * @throws Exception
     */
    public void test토큰가져오기테스트() throws Exception {
        Intent i = new Intent(getContext(), PushServiceImpl.class);
        i.setAction("kr.co.adflow.push.service.START");
        i.putExtra("token", "0f463d7ccffb1034ab9716a");
        i.putExtra("server", "112.223.76.75");
        i.putExtra("port", 11883);
        i.putExtra("ssl", false);
        i.putExtra("cleanSession", false);
        i.putExtra("keepAlive", 240);
        Log.d(TAG, "푸시서비스가 시작됩니다");
        startService(i);
        String response = getService().getToken();
        Log.e(TAG, "테스트응답=" + response);
        // {
        //      "status": "ok",
        //      "data": {
        //          "token":"1c7253e191334f1e9a09915"
        //      },
        //      "code": 102200,
        //      "message": "토큰 가져오기가 완료되었습니다"
        // }
        String token = new JSONObject(response).getJSONObject("data").getString("token");
        assertEquals("0f463d7ccffb1034ab9716a", token);
    }

    /**
     * 핑 테스트 (IF_A_105)
     *
     * @throws Exception
     */
    public void test핑테스트() throws Exception {
        Intent i = new Intent(getContext(), PushServiceImpl.class);
        i.setAction("kr.co.adflow.push.service.START");
        i.putExtra("token", "0f463d7ccffb1034ab9716a");
        i.putExtra("server", "112.223.76.75");
        i.putExtra("port", 11883);
        i.putExtra("ssl", false);
        i.putExtra("cleanSession", false);
        i.putExtra("keepAlive", 240);
        Log.d(TAG, "푸시서비스가 시작됩니다");
        startService(i);

        String response = getService().ping();
        Log.e(TAG, "테스트응답=" + response);
        //{
        //    "status": "ok",
        //    "code": 105200,
        //    "message": "핑 메시지가 성공하였습니다"
        //}
        String status = new JSONObject(response).getString("status");
        assertEquals("ok", status);
    }

    /**
     * 핑 테스트 (IF_A_105)
     *
     * @throws Exception
     */
    public void test핑실패테스트() throws Exception {
        Intent i = new Intent(getContext(), PushServiceImpl.class);
//        i.setAction("kr.co.adflow.push.service.START");
//        i.putExtra("token", "0f463d7ccffb1034ab9716a");
//        i.putExtra("server", "112.223.76.75");
//        i.putExtra("port", 11883);
//        i.putExtra("ssl", false);
//        i.putExtra("cleanSession", false);
//        i.putExtra("keepAlive", 240);
//        Log.d(TAG, "푸시서비스가 시작됩니다");
        startService(i);

        String response = getService().ping();
        Log.e(TAG, "테스트응답=" + response);
        //{
        //    "status": "ok",
        //    "code": 105200,
        //    "message": "핑 메시지가 성공하였습니다"
        //}
        String status = new JSONObject(response).getString("status");
        assertEquals("fail", status);
    }

    /**
     * 토픽 구독 테스트 (IF_A_108)
     *
     * @throws Exception
     */
    public void test토픽구독테스트() throws Exception {

        Intent i = new Intent(getContext(), PushServiceImpl.class);
        i.setAction("kr.co.adflow.push.service.START");
        i.putExtra("token", "0f463d7ccffb1034ab9716a");
        i.putExtra("server", "112.223.76.75");
        i.putExtra("port", 11883);
        i.putExtra("ssl", false);
        i.putExtra("cleanSession", false);
        i.putExtra("keepAlive", 240);
        Log.d(TAG, "푸시서비스가 시작됩니다");
        startService(i);

        String response = getService().subscribe("testTopic", 2);
        Log.e(TAG, "테스트응답=" + response);
        // {
        //      "status": "ok",
        //      "code": 108200,
        //      "message": "토픽 구독이 완료되었습니다"
        // }
        String status = new JSONObject(response).getString("status");
        assertEquals("ok", status);
    }

    /**
     * 토픽 구독 실패 테스트 (IF_A_108)
     *
     * @throws Exception
     */
    public void test토픽구독실패테스트() throws Exception {

        Intent i = new Intent(getContext(), PushServiceImpl.class);
        startService(i);

        String response = getService().subscribe("testTopic", 2);
        Log.e(TAG, "테스트응답=" + response);
        // {
        //      "status": "ok",
        //      "code": 108200,
        //      "message": "토픽 구독이 완료되었습니다"
        // }
        String status = new JSONObject(response).getString("status");
        assertEquals("fail", status);
    }


//    /**
//     * @throws Exception
//     */
//    public void test토픽구독취소테스트() throws Exception {
//
//        Thread.sleep(1000);
//
//        Intent i = new Intent(getContext(), PushServiceImpl.class);
//        i.setAction("kr.co.adflow.push.service.START");
//        i.putExtra("token", "0f463d7ccffb1034ab9716a");
//        i.putExtra("server", "112.223.76.75");
//        i.putExtra("port", 11883);
//        i.putExtra("ssl", false);
//        i.putExtra("cleanSession", false);
//        i.putExtra("keepAlive", 240);
//        Log.d(TAG, "푸시서비스가 시작됩니다");
//        startService(i);
//
//        Thread.sleep(500);
//
//        String response = getService().unsubscribe("testTopic");
//        Log.e(TAG, "테스트응답=" + response);
//        // {
//        //      "status": "ok",
//        //      "code": 109200,
//        //      "message": "토픽 구독취소가 완료되었습니다"
//        // }
//        String status = new JSONObject(response).getString("status");
//        assertEquals("ok", status);
//    }

    /**
     * 토픽 구독 정보 가져오기 테스트 (IF_A_110) REST
     *
     * @throws Exception
     */
    public void test토픽구독정보가져오기테스트() throws Exception {
        Intent i = new Intent(getContext(), PushServiceImpl.class);
        i.setAction("kr.co.adflow.push.service.START");
        i.putExtra("token", "0f463d7ccffb1034ab9716a");
        i.putExtra("server", "112.223.76.75");
        i.putExtra("port", 11883);
        i.putExtra("ssl", false);
        i.putExtra("cleanSession", false);
        i.putExtra("keepAlive", 240);
        Log.d(TAG, "푸시서비스가 시작됩니다");
        startService(i);

        String response = getService().getSubscriptions();
        Log.e(TAG, "테스트응답=" + response);
        // {
        //      "status": "ok",
        //      "code": 109200,
        //      "message": "토픽 구독취소가 완료되었습니다"
        // }
        String status = new JSONObject(response).getString("status");
        assertEquals("ok", status);
    }

    /**
     * 메시지 수신 확인 테스트 (IF_A_107)
     *
     * @throws Exception
     */
    public void test메시지수신확인테스트() throws Exception {
        Intent i = new Intent(getContext(), PushServiceImpl.class);
        i.setAction("kr.co.adflow.push.service.START");
        i.putExtra("token", "0f463d7ccffb1034ab9716a");
        i.putExtra("server", "112.223.76.75");
        i.putExtra("port", 11883);
        i.putExtra("ssl", false);
        i.putExtra("cleanSession", false);
        i.putExtra("keepAlive", 240);
        Log.d(TAG, "푸시서비스가 시작됩니다");
        startService(i);

        // 메시지 조립
        JSONObject data = new JSONObject();
        //data.put("sender", sender);
        data.put("receiver", "testTopic");
        data.put("qos", 2);
        data.put("contentType", "base64");
        data.put("content", "this is test");
        //data.put("contentLength", contentLength);
        //data.put("expiry", expiry);

        Log.d(TAG, "data=" + data);

        //메시지 전송
        Log.d(TAG, "X-Application-Key=0f463d7ccffb1034ab9716a");
        Log.d(TAG, "POST_MESSAGE_URL=http://112.223.76.75:18080/v1/messages");
        HttpRequest request = HttpRequest.post("http://112.223.76.75:18080/v1/messages")
                .trustAllCerts() //Accept all certificates
                .trustAllHosts() //Accept all hostnames
                .header("X-Application-Key", "0f463d7ccffb1034ab9716a")
                .header("Content-Type", "application/json;charset=utf-8").send(data.toString());

        Log.d(TAG, "responseCode=" + request.code());
        if (!request.ok()) {
            throw new IOException("Unexpected code "
                    + request.code());
        }
        String result = request.body();
        Log.d(TAG, "result=" + result);

        String msgId = new JSONObject(result).getJSONObject("data").getString("msgId");
        Log.d(TAG, "msgId=" + msgId);

        // 메시지 수신 기다림
        Thread.sleep(3000);

        // 메시지 수신확인
        String response = getService().ack(msgId, new Date());
        Log.e(TAG, "테스트응답=" + response);
        //{
        //  "status": "ok",
        //  "code": 107200,
        //  "message": "메시지 수신확인이 완료되었습니다"
        //}
        String status = new JSONObject(response).getString("status");
        assertEquals("ok", status);
    }

//    public void test메시지수신테스트() throws Exception {
//        Intent i = new Intent(getContext(), PushServiceImpl.class);
//        i.setAction("kr.co.adflow.push.service.START");
//        i.putExtra("token", "0f463d7ccffb1034ab9716a");
//        i.putExtra("server", "112.223.76.75");
//        i.putExtra("port", 11883);
//        i.putExtra("ssl", false);
//        i.putExtra("cleanSession", false);
//        i.putExtra("keepAlive", 240);
//        Log.d(TAG, "푸시서비스가 시작됩니다");
//        startService(i);
//
//        JSONObject data = new JSONObject();
//        data.put("sender", sender);
//        data.put("receiver", receiver);
//        data.put("qos", qos);
//        data.put("contentType", contentType);
//        data.put("content", content);
//        data.put("contentLength", contentLength);
//        data.put("expiry", expiry);
//
//        Log.d(TAG, "data=" + data);
//
//
//        Log.d(TAG, "X-Application-Key=0f463d7ccffb1034ab9716a");
//        Log.d(TAG, "POST_MESSAGE_URL=http://112.223.76.75:18080/v1/message/0f463d7ccffb1034ab9716a");
//        HttpRequest request = HttpRequest.post("http://112.223.76.75:18080/v1/message/0f463d7ccffb1034ab9716a")
//                .trustAllCerts() //Accept all certificates
//                .trustAllHosts() //Accept all hostnames
//                .header("X-Application-Key", "0f463d7ccffb1034ab9716a")
//                .header("Content-Type", "application/json;charset=utf-8").send(data.toString());
//
//        Log.d(TAG, "responseCode=" + request.code());
//        if (!request.ok()) {
//            throw new IOException("Unexpected code "
//                    + request.code());
//        }
//        String response = request.body();
//        Log.d(TAG, "response=" + response + ")");
//
//        String ping = new JSONObject(response).getString("status");
//        assertEquals("ok", ping);
//    }

//    public void test토큰등록테스트() throws Exception {
//        Intent i = new Intent(getContext(), PushServiceImpl.class);
//        i.setAction("kr.co.adflow.push.service.START");
//        i.putExtra("token", "1c7253e191334f1e9a09915");
//        i.putExtra("server", "112.223.76.75");
//        i.putExtra("port", 8883);
//        i.putExtra("ssl", true);
//        i.putExtra("cleanSession", false);
//        Log.d(TAG, "푸시서비스가 시작됩니다");
//        startService(i);
//        String response = getService().registerToken("1c7253e191334f1e9a09915", false);
//        assertEquals("ok", new JSONObject(response).getString("status"));
//    }


}
