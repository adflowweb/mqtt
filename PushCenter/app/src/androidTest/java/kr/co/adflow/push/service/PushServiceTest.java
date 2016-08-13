package kr.co.adflow.push.service;


import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import android.content.Intent;
import android.test.ServiceTestCase;
import android.util.Log;

import kr.co.adflow.push.PushPreference;
import kr.co.adflow.push.handler.PushHandler;
import kr.co.adflow.push.service.impl.PushServiceImpl;

/**
 * This test should be executed on an actual device as recommended in the testing fundamentals.
 * http://developer.android.com/tools/testing/testing_android.html#WhatToTest
 * <p/>
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

    //
    @Before
    public void setUp() throws Exception {
        super.setUp();
        Log.d("PushServiceTest========", "setUp 시작()");
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
        assertNotNull(getService());
        Log.d("PushServiceTest========", "testOnCreate 종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testIsConnected() throws Exception {
        Log.d("PushServiceTest========", "testIsConnected 시작()");
        PushHandler handler = getService().getPushHandler();
        assertTrue("mqtt 세션이 없습니다", handler.isConnected());
        Log.d("PushServiceTest========", "testIsConnected 종료()");
    }

    @Test
    public void testSendMessage() throws Exception {
//        Intent intent = new Intent(getSystemContext(), PushServiceImpl.class);
//        intent.setAction("kr.co.adflow.push.service.KEEPALIVE");
//        startService(intent);
//        assertNotNull(getService());
    }

    @Test
    public void testAck() throws Exception {
        Log.d("PushServiceTest========", "testAck 시작()");
        Log.d(TAG, "service = " + getService());
        PushHandler handler = getService().getPushHandler();
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
        //System.out.println("response=" + response);
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
        //System.out.println("response=" + response);
        assertTrue("데이터가 일치하지 않습니다", response.equals("{\"result\":{\"success\":true}}"));
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
        System.out.println("response=" + response);
        JSONAssert.assertEquals("{\"result\":{\"success\":true,\"data\":{\"validation\":true}}}", response, true);
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
        System.out.println("response=" + response);
        JSONAssert.assertEquals("{\"result\":{\"success\":true,\"data\":{\"validation\":true}}}", response, true);
        Log.d("PushServiceTest========", "testExistPMAByUFMI 종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testSendMsgWithOpts() throws Exception {
        Log.d("PushServiceTest========", "testSendMsgWithOpts 시작()");
        PushHandler handler = getService().getPushHandler();
        String response = handler.sendMsgWithOpts("mms/P1/82/50/p1206", "mms/P1/82/50/p1212", 2, "application/base64", "eyJzdHIiOiLqsIAiLCJyY3YiOiI4Mio1MCoxMjEyIiwic25kIjoiODIqNTAqMTIxMiJ9", 100 /*contentLength*/, 600, false);
        System.out.println("response=" + response);
        JSONAssert.assertEquals("{\"result\":{\"success\":true,\"info\":[\"receiver=mms/P1/82/50/p1212\",\"content=eyJzdHIiOiLqsIAiLCJyY3YiOiI4Mio1MCoxMjEyIiwic25kIjoiODIqNTAqMTIxMiJ9\"]}}", response, true);
        Log.d("PushServiceTest========", "testSendMsgWithOpts 종료()");
    }

    /**
     * 50*1212에서만 테스트해야함!!!!!
     *
     * @throws Exception
     */
    @Test
    public void testUpdateUFMI() throws Exception {
        Log.d("PushServiceTest========", "testUpdateUFMI 시작()");
        PushHandler handler = getService().getPushHandler();
        String response = handler.updateUFMI("+821021805840", "82*50*1212");
        System.out.println("response=" + response);
        JSONAssert.assertEquals("{\"result\":{\"success\":true}}", response, true);
        Log.d("PushServiceTest========", "testUpdateUFMI 종료()");
    }

    /**
     * 잘못된 토큰 연결
     *
     * @throws Exception
     */
    @Test
    public void testInvalidServerAddress() throws Exception {
        Log.d("PushServiceTest========", "testInvalidServerAddress시작()");
        PushHandler handler = getService().getPushHandler();
        //{"token":"a0b285aa4fba490793a80ee","mqttbroker":["ssl://14.63.217.141:18831","ssl://14.63.217.141:28831"],"created":"2016-08-09T09:13:11.465Z"}
        PushPreference preference = new PushPreference(getContext());
        //서버정보저장
        preference.put(PushPreference.SERVERURL, "{\"token\":\"a0b285aa4fba490793a80ee\",\"mqttbroker\":[\"ssl://14.63.217.141:18832\",\"ssl://14.63.217.141:28832\"],\"created\":\"2016-08-09T09:13:11.465Z\"}");
        //preference.put(PushPreference.SERVERURL, "{\"token\":\"a0b285aa4fba490793a80ee\",\"mqttbroker\":[\"ssl://14.63.217.141:38083\",\"ssl://14.63.217.141:38083\"],\"created\":\"2016-08-09T09:13:11.465Z\"}");
        handler.stop();
        handler.keepAlive();
        handler.isConnected();
        assertTrue("mqtt 세션 연결이 실패여야 합니다", !handler.isConnected());
        Log.d("PushServiceTest========", "testInvalidServerAddress종료()");
    }
}