package kr.co.adflow.push.service;


import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.test.ServiceTestCase;
import android.util.Log;

import kr.co.adflow.push.PushPreference;
import kr.co.adflow.push.handler.PushHandler;
import kr.co.adflow.push.receiver.PushReceiver;
import kr.co.adflow.push.service.impl.PushServiceImpl;
import kr.co.adflow.push.util.DebugLog;

/**
 * This test should be executed on an actual device as recommended in the testing fundamentals.
 * http://developer.android.com/tools/testing/testing_android.html#WhatToTest
 * <p>
 * The following page describes tests that should be written for a service.
 * http://developer.android.com/tools/testing/service_testing.html
 * TODO: Write tests that check the proper execution of the service's life cycle.
 */
public class PushServiceTest extends ServiceTestCase<PushServiceImpl> {

    /**
     * Tag for logging
     */
    private final static String TAG = PushServiceTest.class.getName();

    public PushServiceTest() {
        super(PushServiceImpl.class);
    }

    private BroadcastReceiver broadcastReceiver = null;

    private Intent result = null;
    private Intent mqttChannelChanged = null;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Log.d("PushServiceTest========", "setUp 시작()");

        //register broadcastreceiver
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                DebugLog.d("onReceive 시작(context = " + context + ",intent = " + intent + ")");

                String eventMsg = intent.getStringExtra("eventMsg");
                Log.d("event received ========", "eventMsg = " + eventMsg);
                int eventCode = intent.getIntExtra("eventCode", 0);
                if (eventCode == 1003) {
                    mqttChannelChanged = intent;
                } else {
                    result = intent;
                }
                Log.d("event received ========", "eventCode = " + eventCode);
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("kr.co.ktpowertel.push.connStatus");
        getSystemContext().registerReceiver(broadcastReceiver, filter);

//        DebugLog.d("알람을 설정합니다");
//        AlarmManager service = (AlarmManager) getSystemContext()
//                .getSystemService(Context.ALARM_SERVICE);
//        Intent i = new Intent(getSystemContext(), PushReceiver.class);
//        i.setAction("kr.co.adflow.push.service.KEEPALIVE");
//        PendingIntent pending = PendingIntent.getBroadcast(getSystemContext(), 0, i,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        service.setRepeating(AlarmManager.RTC_WAKEUP, 30,
//                PushHandler.ALARM_INTERVAL * 1000, pending);
//        DebugLog.d("알람이 설정되었습니다");

        //start service
        Intent intent = new Intent(getSystemContext(), PushServiceImpl.class);
        intent.setAction("kr.co.adflow.push.service.KEEPALIVE");
        startService(intent);
        //Thread.sleep(3000);
        Log.d("PushServiceTest========", "setUp 종료()");
    }

    @After
    public void tearDown() throws Exception {
        Log.d("PushServiceTest========", "tearDown 시작()");
        Intent intent = new Intent(getSystemContext(), PushServiceImpl.class);
        intent.setAction("kr.co.adflow.push.service.STOP");
        startService(intent);
        super.tearDown();
        Log.d("PushServiceTest========", "tearDown 종료()");
    }

    @Test
    public void testOnCreate() throws Exception {
        Log.d("PushServiceTest========", "testOnCreate 시작()");
        assertNotNull("푸시서비스가 존재하지 않습니다", getService());
        Log.d("PushServiceTest========", "testOnCreate 종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testIsConnected() throws Exception {
        Log.d("PushServiceTest========", "testIsConnected 시작()");
        PushHandler handler = getService().getPushHandler();
        handler.keepAlive();
        assertTrue("mqtt 세션이 없습니다", handler.isConnected());
        Log.d("PushServiceTest========", "testIsConnected 종료()");
    }

    @Test
    public void testAck() throws Exception {
        Log.d("PushServiceTest========", "testAck 시작()");
        Log.d(TAG, "service = " + getService());
        PushHandler handler = getService().getPushHandler();
        //Thread.sleep(3000);
        JSONObject ack = new JSONObject();
        ack.put("msgId", "2015059795e6a7f12a414fa9ff800fb3a2979d");
        ack.put("token", "1c7253e191334f1e9a09915");
        ack.put("ackTime", "1431332299604");
        ack.put("ackType", "pma");
        handler.addAckJob(ack);
        Log.d("PushServiceTest========", "testAck 종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testPreCheck() throws Exception {
        Log.d("PushServiceTest========", "testPreCheck 시작()");
        getService().preCheck("test", "test");
        //Thread.sleep(3000);
        Log.d("PushServiceTest========", "testPreCheck 종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testAuth() throws Exception {
        //auth시작(url=https://push4.ktp.co.kr:8080/v1/auth, userID=+821021805840, deviceID=53a1c1596efbd9a)
        //auth종료(response={"result":{"success":true,"data":{"tokenID":"1c7253e191334f1e9a09915","userID":"+821021805840","issue":1425542032000}}})
        Log.d("PushServiceTest========", "testAuth 시작()");
        PushHandler handler = getService().getPushHandler();
        String response = handler.auth("http://push4.ktp.co.kr:3000/v1/auth", "+821021805840", "53a1c1596efbd9a");
        assertTrue("데이터가 일치하지 않습니다", response.equals("{\"result\":{\"success\":true,\"data\":{\"tokenID\":\"b2fbc548fb0a4e2eb3749ab\",\"userID\":\"+821021805840\",\"issue\":1470796968000}}}"));
        Log.d("PushServiceTest========", "testAuth 종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetSubscriptions() throws Exception {
        Log.d("PushServiceTest========", "testGetSubscriptions 시작()");
        PushHandler handler = getService().getPushHandler();
        String response = handler.getSubscriptions();
        //Thread.sleep(3000);
        assertTrue("데이터가 일치하지 않습니다", response.equals("{\"result\":{\"success\":true,\"data\":[\"mms/P2/1/200/p7353\",\"mms/P2/1/b200/g8\",\"mms/P2/DH-B210K\",\"mms/P2/1\",\"mms/821021808463\",\"mms/P2/1/b200/g9\"]}}"));
        //JSONAssert.assertEquals("{\"result\":{\"success\":true}}", response, false);
        Log.d("PushServiceTest========", "testGetSubscriptions 종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testExistPMAByUserID() throws Exception {
        Log.d("PushServiceTest========", "testExistPMAByUserID 시작()");
        PushHandler handler = getService().getPushHandler();
        String response = handler.existPMAByUserID("+821021804709");
        //Thread.sleep(3000);
        assertTrue("데이터가 일치하지 않습니다", response.equals("{\"result\":{\"success\":true,\"data\":{\"validation\":true}}}"));
        //JSONAssert.assertEquals("{\"result\":{\"success\":true,\"data\":{\"validation\":true}}}", response, true);
        Log.d("PushServiceTest========", "testExistPMAByUserID 종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testExistPMAByUFMI() throws Exception {
        Log.d("PushServiceTest========", "testExistPMAByUFMI 시작()");
        PushHandler handler = getService().getPushHandler();
        String response = handler.existPMAByUFMI("82*50*1212");
        //Thread.sleep(3000);
        assertTrue("데이터가 일치하지 않습니다", response.equals("{\"result\":{\"success\":true,\"data\":{\"validation\":true}}}"));
        //JSONAssert.assertEquals("{\"result\":{\"success\":true,\"data\":{\"validation\":true}}}", response, true);
        Log.d("PushServiceTest========", "testExistPMAByUFMI 종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testSendMsgWithOpts() throws Exception {
        Log.d("PushServiceTest========", "testSendMsgWithOpts 시작()");
        PushHandler handler = getService().getPushHandler();
        PushPreference preference = new PushPreference(getContext());
        String phonenum = preference.getValue(PushPreference.PHONENUM, null);
        phonenum = phonenum.replace("+", "");
        String sender = "mms/" + phonenum;
        String response = handler.sendMsgWithOpts(sender, sender, 2, "application/base64", "eyJzdHIiOiLqsIAiLCJyY3YiOiI4Mio1MCoxMjEyIiwic25kIjoiODIqNTAqMTIxMiJ9", 100 /*contentLength*/, 600, false);
        Thread.sleep(100);
        assertTrue("데이터가 일치하지 않습니다", response.equals("{\"result\":{\"success\":true,\"info\":[\"receiver=mms/" + phonenum + "\",\"content=eyJzdHIiOiLqsIAiLCJyY3YiOiI4Mio1MCoxMjEyIiwic25kIjoiODIqNTAqMTIxMiJ9\"]}}"));
        //JSONAssert.assertEquals("{\"result\":{\"success\":true,\"info\":[\"receiver=mms/P1/82/50/p1212\",\"content=eyJzdHIiOiLqsIAiLCJyY3YiOiI4Mio1MCoxMjEyIiwic25kIjoiODIqNTAqMTIxMiJ9\"]}}", response, true);
        Log.d("PushServiceTest========", "testSendMsgWithOpts 종료()");
    }

//    /**
//     * 50*1212에서만 테스트해야함!!!!!
//     *
//     * @throws Exception
//     */
//    @Test
//    public void testUpdateUFMI() throws Exception {
//        Log.d("PushServiceTest========", "testUpdateUFMI 시작()");
//        PushHandler handler = getService().getPushHandler();
//        String response = handler.updateUFMI("+821021805840", "82*50*1212");
//        System.out.println("response=" + response);
//        JSONAssert.assertEquals("{\"result\":{\"success\":true}}", response, true);
//        Log.d("PushServiceTest========", "testUpdateUFMI 종료()");
//    }

    /**
     * 잘못된 서버정보 접속 테스트
     *
     * @throws Exception
     */
    @Test
    public void testInvalidServerAddress() throws Exception {
        Log.d("PushServiceTest========", "testInvalidServerAddress 시작()");
        PushHandler handler = getService().getPushHandler();
        //{"token":"a0b285aa4fba490793a80ee","mqttbroker":["ssl://14.63.217.141:18831","ssl://14.63.217.141:28831"],"created":"2016-08-09T09:13:11.465Z"}
        PushPreference preference = new PushPreference(getContext());
        //서버정보저장
        preference.put(PushPreference.SERVERURL, "{\"token\":\"a0b285aa4fba490793a80ee\",\"mqttbroker\":[\"ssl://14.63.217.12\"],\"created\":\"2016-08-09T09:13:11.465Z\"}");
        //preference.put(PushPreference.SERVERURL, "{\"token\":\"a0b285aa4fba490793a80ee\",\"mqttbroker\":[\"ssl://14.63.217.141:38083\",\"ssl://14.63.217.141:38083\"],\"created\":\"2016-08-09T09:13:11.465Z\"}");
        handler.stop();
        handler.keepAlive();
        //handler.isConnected();
        //Thread.sleep(3000);
        assertTrue("mqtt세션 연결이 실패여야 합니다", !handler.isConnected());
        Log.d("PushServiceTest========", "testInvalidServerAddress 종료()");
    }

    /**
     * mqtt 채널 변경 테스트
     *
     * @throws Exception
     */
    @Test
    public void testMqttChannelChanged() throws Exception {
        Log.d("testMqttChannelChanged-", "testMqttChannelChanged 시작()");
        PushHandler handler = getService().getPushHandler();
        //{"token":"a0b285aa4fba490793a80ee","mqttbroker":["ssl://14.63.217.141:18831","ssl://14.63.217.141:28831"],"created":"2016-08-09T09:13:11.465Z"}
        PushPreference preference = new PushPreference(getContext());
        //서버정보저장
        preference.put(PushPreference.SERVERURL, null);
        handler.stop();
        handler.keepAlive();
        preference.put(PushPreference.SERVERURL, "{\"token\":\"a0b285aa4fba490793a80ee\",\"mqttbroker\":[\"ssl://14.63.217.141:18831\",\"ssl://14.63.217.141:28831\"],\"created\":\"2016-08-09T09:13:11.465Z\"}");

        for (int i = 0; i < 100; i++) {
            if (mqttChannelChanged != null) {
                String eventMsg = mqttChannelChanged.getStringExtra("eventMsg");
                DebugLog.d("eventMsg = " + eventMsg);
                int eventCode = mqttChannelChanged.getIntExtra("eventCode", 0);
                DebugLog.d("eventCode = " + eventCode);
                Log.d("testMqttChannelChanged-", "testMqttChannelChanged 종료()");
                assertEquals(1003, eventCode);
                return;
            }
            DebugLog.d("슬립 10초");
            Thread.sleep(10000);
        }
    }

    /**
     * 잘못된 토큰
     *
     * @throws Exception
     */
    @Test
    public void testInvalidToken() throws Exception {
        Log.d("testMqttChannelChanged-", "testMqttChannelChanged 시작()");
        PushHandler handler = getService().getPushHandler();
        PushPreference preference = new PushPreference(getContext());
        //토큰정보저장
        preference.put(PushPreference.TOKEN, "a0b285aa4fba490793a8000");
        handler.stop();
        handler.keepAlive();
        Thread.sleep(10000);

//        for (int i = 0; i < 100; i++) {
//            if (mqttChannelChanged != null) {
//                String eventMsg = mqttChannelChanged.getStringExtra("eventMsg");
//                DebugLog.d("eventMsg = " + eventMsg);
//                int eventCode = mqttChannelChanged.getIntExtra("eventCode", 0);
//                DebugLog.d("eventCode = " + eventCode);
//                Log.d("testMqttChannelChanged-", "testMqttChannelChanged 종료()");
//                assertEquals(1003, eventCode);
//                return;
//            }
//            DebugLog.d("슬립 10초");
//            Thread.sleep(10000);
//        }
    }

//    /**
//     * mqtt 채널 변경 테스트
//     *
//     * @throws Exception
//     */
//    @Test
//    public void testExponentionBackOff() throws Exception {
//        Log.d("testMqttChannelChanged-", "testMqttChannelChanged 시작()");
//        PushHandler handler = getService().getPushHandler();
//        PushPreference preference = new PushPreference(getContext());
//
//        for (int i = 0; i < 100; i++) {
//            double result = Math.pow((double) 2, (double) i);
//            Log.d("testMqttChannelChanged-", "result = " + result);
//            Thread.sleep(1000);
//        }
//
//
////        for (int i = 0; i < 100; i++) {
////            if (mqttChannelChanged != null) {
////                String eventMsg = mqttChannelChanged.getStringExtra("eventMsg");
////                DebugLog.d("eventMsg = " + eventMsg);
////                int eventCode = mqttChannelChanged.getIntExtra("eventCode", 0);
////                DebugLog.d("eventCode = " + eventCode);
////                Log.d("testMqttChannelChanged-", "testMqttChannelChanged 종료()");
////                assertEquals(1003, eventCode);
////                return;
////            }
////            DebugLog.d("슬립 10초");
////            Thread.sleep(10000);
////        }
//    }


}