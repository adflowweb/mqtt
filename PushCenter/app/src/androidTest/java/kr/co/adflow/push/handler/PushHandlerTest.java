package kr.co.adflow.push.handler;

import android.test.AndroidTestCase;
import android.util.Log;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Created by nadir93 on 15. 5. 11..
 */
public class PushHandlerTest extends AndroidTestCase {

    PushHandler handler;

//    @BeforeClass
//    public static void oneTimeSetUp() {
//        // one-time initialization code
//        System.out.println("@BeforeClass - oneTimeSetUp");
//
//    }
//
//    @AfterClass
//    public static void oneTimeTearDown() {
//        // one-time cleanup code
//        System.out.println("@AfterClass - oneTimeTearDown");
//    }

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        Log.d("PushHandlerTest", "setUp시작()");
        handler = new PushHandler(getContext());
        handler.keepAlive();
        Log.d("PushHandlerTest", "setUp종료()");
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        Log.d("PushHandlerTest", "tearDown시작()");
        handler.stop();
        Log.d("PushHandlerTest", "tearDown종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testContext() {
        assertTrue("The context should not be null", getContext() != null);
    }

//    /**
//     * @throws Exception
//     */
//    @Test
//    public void testStart() throws Exception {
//        Log.d("PushHandlerTest", "testStart시작()");
//        Log.d("PushHandlerTest", "testStart종료()");
//    }
//
//    /**
//     * @throws Exception
//     */
//    @Test
//    public void testStop() throws Exception {
//
//    }

    /**
     * @throws Exception
     */
    @Test
    public void testKeepAlive() throws Exception {
        Log.d("PushHandlerTest", "testKeepAlive시작()");
        handler.keepAlive();
        Log.d("PushHandlerTest", "testKeepAlive종료()");
    }

//    /**
//     * @throws Exception
//     */
//    @Test
//    public void testConnectionLost() throws Exception {
//
//    }
//
//    /**
//     * @throws Exception
//     */
//    @Test
//    public void testDeliveryComplete() throws Exception {
//
//    }
//
//    /**
//     * @throws Exception
//     */
//    @Test
//    public void testMessageArrived() throws Exception {
//
//    }

    /**
     * @throws Exception
     */
    @Test
    public void testAddAckJob() throws Exception {
        //addAckJob시작(ackJson={"msgId":"2015059795e6a7f12a414fa9ff800fb3a2979d","token":"1c7253e191334f1e9a09915","ackTime":1431332299604,"ackType":"pma"})
        //05-11 17:18:19.594  26062-27346/kr.co.adflow.push D/푸시디비헬퍼﹕ addJob시작(type=4, topic=mms/ack, content={"msgId":"2015059795e6a7f12a414fa9ff800fb3a2979d","token":"1c7253e191334f1e9a09915","ackTime":1431332299604,"ackType":"pma"})
        Log.d("PushHandlerTest", "testAddAckJob시작()");
        JSONObject ack = new JSONObject();
        ack.put("msgId", "2015059795e6a7f12a414fa9ff800fb3a2979d");
        ack.put("token", "1c7253e191334f1e9a09915");
        ack.put("ackTime", "1431332299604");
        ack.put("ackType", "pma");
        handler.addAckJob(ack);
        Log.d("PushHandlerTest", "testAddAckJob종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testAddSubscribeJob() throws Exception {
        //subScribe시작(토픽=mms/821029998341, qos=2)
        //subscribe종료(result={"result":{"success":true}})
        Log.d("PushHandlerTest", "testAddSubscribeJob시작()");
        handler.addSubscribeJob("mms/821029998341");
        Log.d("PushHandlerTest", "testAddSubscribeJob종료()");
    }

//    /**
//     * @throws Exception
//     */
//    @Test
//    public void testConnect() throws Exception {
//
//    }

    /**
     * @throws Exception
     */
    @Test
    public void testSubscribe() throws Exception {
        Log.d("PushHandlerTest", "testSubscribe시작()");
        handler.subscribe("test", 2);
        Log.d("PushHandlerTest", "testSubscribe종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testUnsubscribe() throws Exception {
        Log.d("PushHandlerTest", "testUnsubscribe시작()");
        handler.unsubscribe("test");
        Log.d("PushHandlerTest", "testUnsubscribe종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testPublish() throws Exception {
        Log.d("PushHandlerTest", "testPublish시작()");
        handler.publish("test", "test".getBytes(), 2, false);
        Log.d("PushHandlerTest", "testPublish종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testIsConnected() throws Exception {
        Log.d("PushHandlerTest", "testPublish시작()");
        assertTrue("mqtt연결이없습니다.", handler.isConnected());
        Log.d("PushHandlerTest", "testPublish종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetLostCount() throws Exception {
        Log.d("PushHandlerTest", "testGetLostCount시작()");
        int response = handler.getLostCount();
        assertTrue("연결실패횟수가0이아닙니다.", response == 0);
        Log.d("PushHandlerTest", "testGetLostCount종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testPreCheck() throws Exception {
        Log.d("PushHandlerTest", "testPreCheck시작()");
        handler.preCheck("test", "test");
        Log.d("PushHandlerTest", "testPreCheck종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testAuth() throws Exception {
        //auth시작(url=https://push4.ktp.co.kr:8080/v1/auth, userID=+821021805840, deviceID=53a1c1596efbd9a)
        //auth종료(response={"result":{"success":true,"data":{"tokenID":"1c7253e191334f1e9a09915","userID":"+821021805840","issue":1425542032000}}})
        Log.d("PushHandlerTest", "testAuth시작()");
        String response = handler.auth("https://push4.ktp.co.kr:8080/v1/auth", "+821021805840", "53a1c1596efbd9a");
        System.out.println("response=" + response);
        JSONAssert.assertEquals("{\"result\":{\"success\":true,\"data\":{\"tokenID\":\"1c7253e191334f1e9a09915\",\"userID\":\"+821021805840\",\"issue\":1425542032000}}}", response, true);
        Log.d("PushHandlerTest", "testAuth종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetSubscriptions() throws Exception {
        Log.d("PushHandlerTest", "testGetSubscriptions시작()");
        String response = handler.getSubscriptions();
        System.out.println("response=" + response);
        JSONAssert.assertEquals("{\"result\":{\"success\":true}}", response, false);
        Log.d("PushHandlerTest", "testGetSubscriptions종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetGrpSubscribers() throws Exception {
        Log.d("PushHandlerTest", "testGetGrpSubscribers시작()");
        String response = handler.getGrpSubscribers("mms/P1/82/50/g130");
        System.out.println("response=" + response);
        JSONAssert.assertEquals("{\"result\":{\"success\":true}}", response, false);
        Log.d("PushHandlerTest", "testGetGrpSubscribers종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testExistPMAByUserID() throws Exception {
        Log.d("PushHandlerTest", "testExistPMAByUserID시작()");
        String response = handler.existPMAByUserID("+821021802702");
        System.out.println("response=" + response);
        JSONAssert.assertEquals("{\"result\":{\"success\":true,\"data\":{\"validation\":true}}}", response, true);
        Log.d("PushHandlerTest", "testExistPMAByUserID종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testExistPMAByUFMI() throws Exception {
        Log.d("PushHandlerTest", "testExistPMAByUFMI시작()");
        String response = handler.existPMAByUFMI("82*50*1212");
        System.out.println("response=" + response);
        JSONAssert.assertEquals("{\"result\":{\"success\":true,\"data\":{\"validation\":true}}}", response, true);
        Log.d("PushHandlerTest", "testExistPMAByUFMI종료()");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testSendMsgWithOpts() throws Exception {
        Log.d("PushHandlerTest", "testSendMsgWithOpts시작()");
        String response = handler.sendMsgWithOpts("mms/P1/82/50/p1206", "mms/P1/82/50/p1212", 2, "application/base64", "eyJzdHIiOiLqsIAiLCJyY3YiOiI4Mio1MCoxMjEyIiwic25kIjoiODIqNTAqMTIxMiJ9", 100 /*contentLength*/, 600, false);
        System.out.println("response=" + response);
        JSONAssert.assertEquals("{\"result\":{\"success\":true,\"info\":[\"receiver=mms/P1/82/50/p1212\",\"content=eyJzdHIiOiLqsIAiLCJyY3YiOiI4Mio1MCoxMjEyIiwic25kIjoiODIqNTAqMTIxMiJ9\"]}}", response, true);
        Log.d("PushHandlerTest", "testSendMsgWithOpts종료()");
    }

    /**
     * 50*1212에서만 테스트해야함!!!!!
     *
     * @throws Exception
     */
    @Test
    public void testUpdateUFMI() throws Exception {
        Log.d("PushHandlerTest", "testUpdateUFMI시작()");
        String response = handler.updateUFMI("+821021805840", "82*50*1212");
        System.out.println("response=" + response);
        JSONAssert.assertEquals("{\"result\":{\"success\":true}}", response, true);
        Log.d("PushHandlerTest", "testUpdateUFMI종료()");
    }
}