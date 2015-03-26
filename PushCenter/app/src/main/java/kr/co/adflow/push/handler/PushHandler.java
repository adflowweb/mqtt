package kr.co.adflow.push.handler;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyStore;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import kr.co.adflow.push.PingSender;
import kr.co.adflow.push.PushPreference;
import kr.co.adflow.push.db.Job;
import kr.co.adflow.push.db.Message;
import kr.co.adflow.push.db.PushDBHelper;
import kr.co.adflow.push.service.impl.PushServiceImpl;
import kr.co.adflow.ssl.ADFSSLSocketFactory;
import kr.co.adflow.ssl.SFSSLSocketFactory;

/**
 * @author nadir93
 */
public class PushHandler implements MqttCallback {

    public static final int FIRST_MQTT_CONNECTED = 1000;
    public static final String FIRST_MQTT_CONNECTED_MESSAGE = "초기 MQTT연결이 성공하였습니다.";
    public static final int MQTT_CONNECTED = 1001;
    public static final String MQTT_CONNECTED_MESSAGE = "MQTT연결이 성공하였습니다.";
    public static final int MQTT_DISCONNECTED = 1002;
    public static final String MQTT_DISCONNECTED_MESSAGE = "MQTT연결이 유실되었습니다.";

    public static final int SET_KEEP_ALIVE_MESSAGE = 102;
    public static final int FIRMWARE_UPDATE_MESSAGE = 104;
    public static final int USER_MESSAGE = 106;
    public static final int DIG_ACCOUNT_INFO_MESSAGE = 105;
    public static final int CONTROL_MESSAGE = 10;

    private static final int MQTTVERSION_3 = 3;

    // TAG for debug
    public static final String TAG = "PushHandler";

    public static final int ALARM_INTERVAL = 60;
    public static final int DEFAULT_KEEP_ALIVE_TIME_OUT = 240;
    public static final boolean CLEAN_SESSION = false;
    public static final String AUTH_URL = "https://14.63.217.141/v1/auth";
    public static final String MQTT_SERVER_URL = "ssl://14.63.217.141";
    public static final String PRECHECK_URL = "https://14.63.217.141/v1/precheck";
    public static final String GET_SUBSCRIPTIONS_URL = "https://14.63.217.141/v1/subscriptions/";
    public static final String EXISTPMABYUSERID_URL = "https://14.63.217.141/v1/users/userID/validation";
    public static final String EXISTPMABYUFMI_URL = "https://14.63.217.141/v1/users/ufmi/validation";
    public static final String MESSAGE_URI = "https://14.63.217.141/v1/users/message";
    public static final String UPDATEUFMI_URL = "https://14.63.217.141/v1/users/ufmi";

    public static final String ACK_TOPIC = "mms/ack";
    public static final int HTTP_PORT = 80;
    public static final int HTTPS_PORT = 8080;
    public static final String HTTP_PROTOCOL = "http";
    public static final String HTTPS_PROTOCOL = "https";
    public static final int HTTP_RESPONSE_CODE_SUCCESS = 200;

    public static PushDBHelper pushdb;

    //public static final int DEFAULT_KEEP_ALIVE_TIME_OUT = 60; //for test
    private static final String MQTT_PACKAGE = "org.eclipse.paho.client.mqttv3";
    private static final int MQTT_CONNECTION_TIMEOUT = 10; // second
    private static final int HTTP_CONNTCTION_TIMEOUT = 3000; // 3초
    private static final int HTTP_SOCKET_TIMEOUT = 5000; // 5초

    // mqttClient 세션로그
    private static final boolean CLIENT_SESSION_DEBUG = false; // default false

    private static int CONN_LOST_COUNT;

    private static Context context;
    private MqttAsyncClient mqttClient;
    private PushPreference preference;
    private PingSender pingSender;
    private String phoneModel = android.os.Build.MODEL;
    private String currentToken = null;
    public static final String CONN_STATUS_ACTION = "kr.co.ktpowertel.push.connStatus";
    private static DBWorker dbworker;

    /**
     * @param cxt
     */
    public PushHandler(Context cxt) {
        Log.d(TAG, "PushHandler생성자시작(context=" + cxt + ")");
        context = cxt;
        pushdb = new PushDBHelper(context);
        Log.d(TAG, "pushdb=" + pushdb);
        Log.d(TAG, "Handler=" + this);
        if (CLIENT_SESSION_DEBUG) {
            setMqttClientLog();
        }
        preference = new PushPreference(context);
        Log.d(TAG, "preference=" + preference);
        dbworker = new DBWorker();
        dbworker.start();
        Log.d(TAG, "dbworker=" + dbworker);
        Log.d(TAG, "PushHandler생성자종료()");
    }

    static Context getContext() {
        return context;
    }

    /**
     * 푸시핸들러시작
     * <p/>
     * 시작이 호출되면 기존 채널을 중지후 다시 시작한다.
     */
    public void start() {
        Log.d(TAG, "start시작()");
        stop();
        keepAlive();
        Log.d(TAG, "start종료()");
    }

    /**
     * 푸시핸들러종료
     */
    public void stop() {
        Log.d(TAG, "stop시작()");
        // disconnect mqtt session
        Log.d(TAG, "mqttClient=" + mqttClient);
        if (mqttClient != null) {
            try {
                IMqttToken token = mqttClient.disconnect();
                token.waitForCompletion();
                //dbworker thread 종료
                dbworker.discard();
            } catch (MqttException e) {
                Log.e(TAG, "에러발생", e);
            }
            mqttClient = null;
        }
        Log.d(TAG, "stop종료()");
    }

    /**
     * keepAlive
     */
    public void keepAlive() {
        Log.d(TAG, "keepAlive시작()");
        Log.d(TAG, "mqttClient=" + mqttClient);

        try {
            String token = preference.getValue(PushPreference.TOKEN, null);

            Log.d(TAG, "token=" + token);
            // token이 null일 경우 토큰을발급한다.
            if (token == null || token.equals("")) {
                currentToken = issueToken();
                Log.d(TAG, "발급된토큰=" + currentToken);
            } else {
                currentToken = token;
            }

            if (mqttClient == null) {
                String server = preference.getValue(PushPreference.SERVERURL,
                        null);
                Log.d(TAG, "server=" + server);
                pingSender = new PingSender(context);
                mqttClient = new MqttAsyncClient(server, currentToken,
                        new MemoryPersistence(), pingSender);
                // 연결
                connect();
            } else {
                // Log.d(TAG, "현재연결시도중이거나연결되어있는토큰=" + client.getClientId());
                Log.d(TAG, "mqttClient연결상태="
                        + ((mqttClient.isConnected()) ? "이미연결됨" : "끊어짐"));
                if (!mqttClient.isConnected()) {
                    // 연결
                    connect();
                }
            }
            // ping
            pingSender.ping();
            // 할일처리
            synchronized (dbworker) {
                Log.d(TAG, "dbworker를깨웁니다.");
                dbworker.notify();
            }
        } catch (Exception e) {
            Log.e(TAG, "keepAlive처리시예외상황발생", e);
            if (PushServiceImpl.getWakeLock() != null) {
                try {
                    PushServiceImpl.getWakeLock().release();
                    Log.d(TAG, "웨이크락을해재했습니다." + PushServiceImpl.getWakeLock());
                } catch (Exception ex) {
                    Log.e(TAG, "웨이크락해제시예외상황발생", ex);
                }
            }
        }
        Log.d(TAG, "keepAlive종료()");
    }

    /**
     * 오래된 메시지 삭제
     */
    private void expireMsg() throws Exception {
        Log.d(TAG, "expireMsg시작()");
        pushdb.testQuery();
        Log.d(TAG, "expireMsg종료()");
    }

    /**
     *
     */
    class DBWorker extends Thread {

        private boolean running = true;

        public void discard() {
            Log.d(TAG, "discard시작()");
            running = false;
            synchronized (this) {
                Log.d(TAG, "dbworker를깨웁니다.");
                this.notify();
            }
            Log.d(TAG, "discard종료()");
        }

        @Override
        public void run() {
            while (running) {
                try {
                    Log.d(TAG, "dbworker작업시작");
                    if (mqttClient != null && mqttClient.isConnected()) {
                        Job[] jobs = pushdb.getJobList();
                        Log.d(TAG, "jobs=" + jobs);
                        if (jobs != null) {
                            Log.d(TAG, "작업해야할메시지개수=" + jobs.length);
                            for (int i = 0; i < jobs.length; i++) {
                                int jobType = jobs[i].getType();
                                switch (jobType) {
                                    case 0: // PUBLISH
                                        break;
                                    case 1:  // SUBSCRIBE
                                        break;
                                    case 2: // UNSUBSCRIBE
                                        break;
                                    case 3: // BROADCAST
                                        Log.d(TAG, "브로드캐스트작업시작");
                                        String content = jobs[i].getContent();
                                        JSONObject data = new JSONObject(content);
                                        String msgId = data.getString("msgId");
                                        Message msg = pushdb.getMessage(msgId);
                                        // sendbroadcast
                                        sendBroadcast(new JSONObject(new String(msg.getPayload())), msg.getToken(), msg.getMsgID());
                                        pushdb.deteletJob(jobs[i].getId());
                                        Log.d(TAG, "브로드캐스트작업이수행되었습니다.jodID=" + jobs[i].getId());
                                        break;
                                    case 4: //ACK
                                        Log.d(TAG, "ACK작업시작");
                                        String ackContent = jobs[i].getContent();
                                        data = new JSONObject(ackContent);
                                        msgId = data.getString("msgId");
                                        publish(PushHandler.ACK_TOPIC, ackContent.getBytes(), 1 /* qos 2 로 전송 */, false /* retain */);
                                        pushdb.deteletJob(jobs[i].getId());
                                        //delete msg
                                        pushdb.deleteMessage(msgId);
                                        Log.i(TAG, "ACK작업이수행되었습니다.jobID=" + jobs[i].getId());
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                    Log.d(TAG, "dbworker작업종료");
                } catch (Exception e) {
                    Log.e(TAG, "예외상황발생", e);
                }

                synchronized (this) {
                    Log.d(TAG, "dbworker가wait상태로전환됩니다.");
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        Log.e(TAG, "예외상황발생", e);
                    }
                }
            }
            Log.d(TAG, "dbworker쓰레드가종료됩니다.");
        }
    }

//    private void handleJob() throws Exception {
//        Log.d(TAG, "handleJob시작()");
//        Job[] jobs = pushdb.getJobList();
//        Log.d(TAG, "jobs=" + jobs);
//        if (jobs != null) {
//            Log.d(TAG, "작업해야할메시지개수=" + jobs.length);
//            for (int i = 0; i < jobs.length; i++) {
//                int jobType = jobs[i].getType();
//                switch (jobType) {
//                    case 0: // PUBLISH
//                        break;
//                    case 1:  // SUBSCRIBE
//                        break;
//                    case 2: // UNSUBSCRIBE
//                        break;
//                    case 3: // BROADCAST
//                        Log.d(TAG, "브로드캐스트작업시작");
//                        String content = jobs[i].getContent();
//                        JSONObject data = new JSONObject(content);
//                        String msgId = data.getString("msgId");
//                        Message msg = pushdb.getMessage(msgId);
//                        // sendbroadcast
//                        sendBroadcast(new JSONObject(new String(msg.getPayload())), msg.getToken(), msg.getMsgID());
//                        pushdb.deteletJob(jobs[i].getId());
//                        Log.d(TAG, "브로드캐스트작업이수행되었습니다.jodID=" + jobs[i].getId());
//                        break;
//                    case 4: //ACK
//                        Log.d(TAG, "ACK작업시작");
//                        String ackContent = jobs[i].getContent();
//                        publish(PushHandler.ACK_TOPIC, ackContent.getBytes(), 1 /* qos 2 로 전송 */, false /* retain */);
//                        pushdb.deteletJob(jobs[i].getId());
//
//                        publishCount++;
//                        Log.i(TAG, "ACK작업이수행되었습니다.jobID=" + jobs[i].getId());
//                        Log.i(TAG, "publishCount=" + publishCount);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        }
//        Log.d(TAG, "handleJob종료()");
//    }

    private String issueToken() throws Exception {
        String token = null;
        Log.d(TAG, "토큰을발급합니다.");
        String phoneNum = preference.getValue(PushPreference.PHONENUM, null);
        Log.d(TAG, "phoneNum=" + phoneNum);

        if (phoneNum == null) {
            throw new Exception("전화번호가없습니다.");
        }

        //토큰발급
        JSONObject returnData = new JSONObject();
        JSONObject result = new JSONObject();
        String res = null;

        //인증
        String androidID = Secure.getString(getContext().getContentResolver(),
                Secure.ANDROID_ID);
        Log.d(TAG, "androidID=" + androidID);
        //{"result":{"success":true,"data":{"tokenID":"b0dbcd2fe4ce4c58940e33e","userID":"testUser","issue":1420715174000}}}
        res = this.auth(PushHandler.AUTH_URL, phoneNum, androidID);
        JSONObject obj = new JSONObject(res);
        JSONObject rst = obj.getJSONObject("result");

        if (!rst.getBoolean("success")) {
            //에러데이타 추출후 응답메시지에 추가요망
            throw new Exception("인증문제가발생하였습니다(bizException)");
        }
        JSONObject data = rst.getJSONObject("data");
        token = data.getString("tokenID");
        Log.d(TAG, "token=" + token);
        //토큰저장
        preference.put(PushPreference.TOKEN, token);
        return token;
    }

    /**
     * eclipse paho client용 로거
     */
    private void setMqttClientLog() {
        Log.d(TAG, "setMqttClientLog시작()");
        java.util.logging.Handler defaultHandler = new ConsoleHandler();
        LogManager logManager = LogManager.getLogManager();
        Logger logger = Logger.getLogger(MQTT_PACKAGE);
        defaultHandler.setFormatter(new SimpleFormatter());
        defaultHandler.setLevel(Level.ALL);
        logger.setLevel(Level.ALL);
        logger.addHandler(defaultHandler);
        logManager.addLogger(logger);
        Log.d(TAG, "setMqttClientLog종료()");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.
     * Throwable)
     */
    @Override
    public void connectionLost(Throwable throwable) {
        Log.d(TAG, "connectionLost시작(에러=" + throwable + ")");
        Log.e(TAG, "TCP세션연결이끊겼습니다", throwable);
        CONN_LOST_COUNT++;
        Log.d(TAG, "connectionLostCount=" + CONN_LOST_COUNT);
        //sendBroadcast
        try {
            sendBroadcast(MQTT_DISCONNECTED_MESSAGE, MQTT_DISCONNECTED);
        } catch (Exception e) {
            Log.e(TAG, "예외상황발생", e);
        }
        Log.d(TAG, "connectionLost종료()");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.paho.client.mqttv3.MqttCallback#deliveryComplete(org.eclipse
     * .paho.client.mqttv3.IMqttDeliveryToken)
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d(TAG, "deliveryComplete시작(토큰=" + token + ")");
        Log.d(TAG, "deliveryComplete종료()");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.
     * String, org.eclipse.paho.client.mqttv3.MqttMessage)
     */
    @Override
    public void messageArrived(String topic, MqttMessage message)
            throws Exception {
        Log.d(TAG, "messageArrived시작(토픽=" + topic + ",메시지=" + message + ",qos="
                + message.getQos() + ")");
        try {
            //메시지 파싱
            JSONObject data = new JSONObject(new String(message.getPayload()));
            Log.d(TAG, "data=" + data);

            // message 분기처리
            int msgType = data.getInt("msgType");

            //precheck
            if (msgType == 103) {
                Log.d(TAG, "preCheck received");
                return;
            }

            String serviceId = data.getString("serviceId");
            Log.d(TAG, "serviceId=" + serviceId);

            String msgId = data.getString("msgId");
            Log.d(TAG, "msgId=" + msgId);

            int ack = 0;
            if (data.has("ack")) {
                ack = data.getInt("ack");
            }
            Log.d(TAG, "ack=" + ack);

            if (ack == 1) {
                //받은메시지 저장
                pushdb.addMessage(msgId, serviceId, topic, message.getPayload(), message.getQos(), ack, currentToken);
                // ack job 저장
                JSONObject ackJson = new JSONObject();
                ackJson.put("msgId", msgId);
                ackJson.put("token", currentToken);
                ackJson.put("ackType", "pma");
                ackJson.put("ackTime", System.currentTimeMillis());
                addJob(ackJson);
            }

            switch (msgType) {
                case SET_KEEP_ALIVE_MESSAGE: // keepAlive 설정변경
                    JSONObject content = data.getJSONObject("content");
                    Log.d(TAG, "content=" + content);
                    int keepAlive = content.getInt("keepAlive");
                    // store keepalive
                    preference.put(PushPreference.KEEPALIVE, keepAlive);
                    Log.d(TAG, "PushServiceClass=" + context.getClass());
                    Intent restart = new Intent(context, /* PushServiceImpl.class */
                            context.getClass());
                    restart.setAction("kr.co.adflow.push.service.RESTART");
                    context.startService(restart);
                    break;
                case CONTROL_MESSAGE:
                case FIRMWARE_UPDATE_MESSAGE:
                case DIG_ACCOUNT_INFO_MESSAGE:
                case USER_MESSAGE:
                    //broadcast 작업추가
                    //int jobID = pushdb.addJob(BROADCAST, "", "{\"msgId\":\"" + msgId + "\"}");
                    //Log.d(TAG, "broadcast작업이추가되었습니다.jobID=" + jobID);
                    sendBroadcast(data, currentToken, msgId);
                    //broadcast 작업제거
                    //pushdb.deteletJob(jobID);
                    //Log.d(TAG, "broadcast작업이삭제되었습니다.jobID=" + jobID);
                    break;
                default:
                    Log.e(TAG, "메시지타입이없습니다.");
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "메시지처리중예외상황발생", e);
        }
        Log.d(TAG, "messageArrived종료()");
    }

    public void addJob(JSONObject ackJson) throws Exception {
        Log.d(TAG, "addJob시작(ackJson=" + ackJson + ")");
        int ackJob = pushdb.addJob(Job.ACK, ACK_TOPIC, ackJson.toString());
        Log.i(TAG, "ack작업이추가되었습니다.jobID=" + ackJob);
        Log.d(TAG, "addJob종료()");
    }

    /**
     * @param data
     * @throws JSONException`
     */
    private void sendBroadcast(JSONObject data, String token, String msgId) throws Exception {
        Log.d(TAG, "sendBroadcast시작(data=" + data + ", token=" + token + ", msgId=" + msgId + ")");
        String serviceId = data.getString("serviceId");
        Log.d(TAG, "serviceId=" + serviceId);

        Intent i = new Intent(serviceId);
        i.putExtra("msgId", msgId);
        i.putExtra("token", token);
        i.putExtra("contentType", data.getString("contentType"));
        i.putExtra("content", data.getString("content"));

        if (data.has("ack")) {
            i.putExtra("ack", data.getInt("ack"));
        } else {
            i.putExtra("ack", 0 /* false */);
        }

        i.putExtra("sender", data.getString("sender"));
        i.putExtra("receiver", data.getString("receiver"));

        if (data.has("resendCount")) {
            i.putExtra("resendCount", data.getInt("resendCount"));
        } else {
            i.putExtra("resendCount", 0);
        }

        Log.d(TAG, "intent=" + i);
        context.sendBroadcast(i);
        Log.d(TAG, "sendBroadcast종료()");
    }

    /**
     * @param eventMsg
     * @param eventCode
     * @throws Exception
     */
    private void sendBroadcast(String eventMsg, int eventCode) throws Exception {
        Log.d(TAG, "sendBroadcast시작(eventMsg=" + eventMsg + ", eventCode=" + eventCode + ")");
        Intent i = new Intent(CONN_STATUS_ACTION);
        i.putExtra("eventMsg", eventMsg);
        i.putExtra("eventCode", eventCode);
        Log.d(TAG, "event=" + i);
        context.sendBroadcast(i);
        Log.d(TAG, "sendBroadcast종료()");
    }

    /**
     * @throws MqttException
     */
    protected synchronized void connect() throws MqttException {
        Log.d(TAG, "connect시작()");
        if (mqttClient.isConnected()) {
            Log.d(TAG, "이미세션이미연결되었습니다");
            Log.d(TAG, "connect종료()");
            return;
        }

        String server = preference.getValue(PushPreference.SERVERURL, null);
        Log.d(TAG, "server=" + server);

        MqttConnectOptions mOpts = new MqttConnectOptions();
        // mOpts.setUserName("testUser");
        // mOpts.setPassword("testPasswd".toCharArray());
        mOpts.setConnectionTimeout(MQTT_CONNECTION_TIMEOUT);
        int keepAlive = preference.getValue(PushPreference.KEEPALIVE,
                DEFAULT_KEEP_ALIVE_TIME_OUT);
        Log.d(TAG, "keepAlive=" + keepAlive);
        mOpts.setKeepAliveInterval(keepAlive);
        boolean cleanSession = preference.getValue(PushPreference.CLEANSESSION,
                false);
        Log.d(TAG, "cleanSession=" + cleanSession);
        mOpts.setCleanSession(cleanSession);
        mOpts.setMqttVersion(MQTTVERSION_3);
        // ssl 처리
        if (server.startsWith("ssl")) {
            mOpts.setSocketFactory(ADFSSLSocketFactory.getSSLSokcetFactory());
        }
        // mOpts.setServerURIs(new String[] { "ssl://adflow.net" });

        Log.d(TAG, "연결옵션=" + mOpts);
        Log.d(TAG, "mqttClient=" + mqttClient);
        Log.d(TAG, "콜백인스턴스=" + this);
        mqttClient.setCallback(this);

        Log.d(TAG, "mqtt서버에연결합니다.server=" + server);
        IMqttToken token = mqttClient.connect(mOpts);
        token.waitForCompletion();
        Log.d(TAG, "세션이연결되었습니다.");

        //sendBroadcast
        boolean firstConn = preference.getValue(PushPreference.FIRSTCONNECTION,
                false);
        try {
            if (firstConn) {
                //부팅후 최초 연결시
                sendBroadcast(FIRST_MQTT_CONNECTED_MESSAGE, FIRST_MQTT_CONNECTED);
                preference.put(PushPreference.FIRSTCONNECTION, false);
            } else {
                sendBroadcast(MQTT_CONNECTED_MESSAGE, MQTT_CONNECTED);
            }

        } catch (Exception e) {
            Log.e(TAG, "예외상황발생", e);
        }
        Log.d(TAG, "connect종료()");
    }

    /**
     * @param topic
     * @param qos
     * @throws MqttException
     */
    public synchronized void subscribe(String topic, int qos) throws Exception {
        Log.d(TAG, "subScribe시작(토픽=" + topic + ", qos=" + qos + ")");

        if (mqttClient == null || !mqttClient.isConnected()) {
            throw new Exception("토픽구독에실패하였습니다(mqttClient가없거나연결이안되어있습니다)");
        }

        // 토픽구독
        IMqttToken token = mqttClient.subscribe(topic, qos);
        token.waitForCompletion();
        Log.d(TAG, "토픽구독을완료하였습니다.");
        Log.d(TAG, "subscribe종료()");
    }

    /**
     * @param topic
     * @throws MqttException
     */
    public synchronized void unsubscribe(String topic) throws Exception {
        Log.d(TAG, "unsubscribe시작(토픽=" + topic + ")");

        if (mqttClient == null || !mqttClient.isConnected()) {
            throw new Exception("토픽구독취소에실패하였습니다(mqttClient가없거나연결이안되어있습니다)");
        }

        // 토픽구독
        IMqttToken token = mqttClient.unsubscribe(topic);
        token.waitForCompletion();
        Log.d(TAG, "토픽구독을취소하였습니다.");
        Log.d(TAG, "unsubscribe종료()");
    }

    /**
     * @param topic
     * @param payload
     * @param qos
     * @param retained
     * @throws Exception
     */
    public synchronized void publish(String topic, byte[] payload, int qos,
                                     boolean retained) throws Exception {
        Log.d(TAG, "publish시작(토픽=" + topic + ", payload=" + new String(payload)
                + ", qos=" + qos + ", retained=" + retained + ")");
        if (mqttClient != null) {
            IMqttDeliveryToken token = mqttClient.publish(topic, payload, qos,
                    retained);
            token.waitForCompletion();
        } else {
            Log.d(TAG, "mqttClient=" + mqttClient);
            throw new Exception("메시지전송실패");
        }

        Log.d(TAG, "publish종료()");
        return;
    }

    /**
     * @return
     */
    public boolean isConnected() {
        Log.d(TAG, "isConnected시작()");
        boolean value = false;
        if (mqttClient != null) {
            value = mqttClient.isConnected();
        }
        Log.d(TAG, "isConnected종료(value=" + value + ")");
        return value;
    }

    /**
     * @return
     */
    public int getLostCout() {
        return CONN_LOST_COUNT;
    }


    /**
     * @param sender
     * @param topic
     * @return
     * @throws Exception
     */
    public void preCheck(String sender, String topic) throws Exception {
        Log.d(TAG, "preCheck시작(sender=" + sender + ", topic=" + topic + ")");
        JSONObject data = new JSONObject();
        data.put("sender", sender);
        data.put("receiver", topic);
        Log.d(TAG, "data=" + data);

        HttpClient httpClient = getHttpClient();

        HttpPost post = new HttpPost(PRECHECK_URL);
        post.setHeader("Content-Type", "application/json");
        //set Header token
        //String token = preference.getValue(PushPreference.TOKEN, null);
        Log.d(TAG, "currentToken=" + currentToken);
        if (currentToken == null) {
            throw new Exception("토큰이존재하지않습니다.");
        }
        post.setHeader("X-ApiKey", currentToken);
        post.setEntity(new StringEntity(data.toString(), "utf-8"));
        HttpResponse response = httpClient.execute(post);

        if (response.getStatusLine().getStatusCode() != HTTP_RESPONSE_CODE_SUCCESS) {
            throw new IOException("Unexpected code "
                    + response.getStatusLine().getStatusCode());
        }

        //String responseStr = EntityUtils.toString(response.getEntity());
        //Log.d(TAG, "preCheck종료(value=" + responseStr + ")");
        //return responseStr;
    }

    /**
     * @param url
     * @param userID
     * @param deviceID
     * @return
     * @throws Exception
     */
    public String auth(String url, String userID, String deviceID)
            throws Exception {
        Log.d(TAG, "auth시작(url=" + url + ", userID=" + userID + ", deviceID="
                + deviceID + ")");
        JSONObject data = new JSONObject();
        data.put("userID", userID);
        data.put("deviceID", deviceID);

        HttpClient httpClient = getHttpClient();

        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json;charset=UTF-8");
        post.setEntity(new StringEntity(data.toString(), "utf-8"));
        HttpResponse response = httpClient.execute(post);

        if (response.getStatusLine().getStatusCode() != HTTP_RESPONSE_CODE_SUCCESS) {
            throw new IOException("Unexpected code "
                    + response.getStatusLine().getStatusCode());
        }

        String responseStr = EntityUtils.toString(response.getEntity());
        Log.d(TAG, "auth종료(value=" + responseStr + ")");
        return responseStr;
    }

    public String getSubscriptions() throws Exception {
        Log.d(TAG, "getSubscriptions시작()");
        HttpClient httpClient = getHttpClient();
        //String token = preference.getValue(PushPreference.TOKEN, null);
        Log.d(TAG, "currentToken=" + currentToken);
        if (currentToken == null) {
            throw new Exception("토큰이존재하지않습니다.");
        }
        HttpGet httpGet = new HttpGet(GET_SUBSCRIPTIONS_URL + currentToken);
        httpGet.setHeader("Content-Type", "application/json");
        //set Header token
        httpGet.setHeader("X-ApiKey", currentToken);
        //post.setEntity(new StringEntity(data.toString(), "utf-8"));
        HttpResponse response = httpClient.execute(httpGet);

        if (response.getStatusLine().getStatusCode() != HTTP_RESPONSE_CODE_SUCCESS) {
            throw new IOException("Unexpected code "
                    + response.getStatusLine().getStatusCode());
        }

        String responseStr = EntityUtils.toString(response.getEntity());
        Log.d(TAG, "getSubscriptions종료(value=" + responseStr + ")");
        return responseStr;
    }

    /**
     * @return
     */
    private HttpClient getHttpClient() throws Exception {
        Log.d(TAG, "getHttpClient시작()");
        //setMode
        setStrictMode();

        KeyStore trustStore = KeyStore.getInstance(KeyStore
                .getDefaultType());
        trustStore.load(null, null);

        SSLSocketFactory sf = new SFSSLSocketFactory(trustStore);
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        HttpConnectionParams.setConnectionTimeout(params, HTTP_CONNTCTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, HTTP_SOCKET_TIMEOUT);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme(HTTP_PROTOCOL, PlainSocketFactory
                .getSocketFactory(), HTTP_PORT));
        // 현재 https 인데 8080으로 서비스하고 있음
        registry.register(new Scheme(HTTPS_PROTOCOL, sf, HTTPS_PORT));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                params, registry);
        Log.d(TAG, "getHttpClient종료()");
        return new DefaultHttpClient(ccm, params);
    }

    public String existPMAByUserID(String userID) throws Exception {
        Log.d(TAG, "existPMAByUserID시작(userID=" + userID + ")");
        HttpClient httpClient = getHttpClient();
        //String token = preference.getValue(PushPreference.TOKEN, null);
        Log.d(TAG, "currentToken=" + currentToken);
        if (currentToken == null) {
            throw new Exception("토큰이존재하지않습니다.");
        }
        HttpPost post = new HttpPost(EXISTPMABYUSERID_URL);
        JSONObject data = new JSONObject();
        data.put("userID", userID);
        Log.d(TAG, "data=" + data);

        post.setHeader("Content-Type", "application/json;charset=UTF-8");
        post.setEntity(new StringEntity(data.toString(), "utf-8"));
        //set Header token
        post.setHeader("X-ApiKey", currentToken);
        //post.setEntity(new StringEntity(data.toString(), "utf-8"));
        HttpResponse response = httpClient.execute(post);

        if (response.getStatusLine().getStatusCode() != HTTP_RESPONSE_CODE_SUCCESS) {
            throw new IOException("Unexpected code "
                    + response.getStatusLine().getStatusCode());
        }

        String responseStr = EntityUtils.toString(response.getEntity());
        Log.d(TAG, "existPMAByUserID종료(value=" + responseStr + ")");
        return responseStr;
    }

    public String existPMAByUFMI(String ufmi) throws Exception {
        Log.d(TAG, "existPMAByUFMI시작(ufmi=" + ufmi + ")");
        HttpClient httpClient = getHttpClient();

        JSONObject data = new JSONObject();
        data.put("ufmi", ufmi);
        Log.d(TAG, "data=" + data);

        //String token = preference.getValue(PushPreference.TOKEN, null);
        Log.d(TAG, "currentToken=" + currentToken);
        if (currentToken == null) {
            throw new Exception("토큰이존재하지않습니다.");
        }
        HttpPost post = new HttpPost(EXISTPMABYUFMI_URL);
        post.setHeader("Content-Type", "application/json;charset=UTF-8");
        post.setEntity(new StringEntity(data.toString(), "utf-8"));
        //set Header token
        post.setHeader("X-ApiKey", currentToken);
        //post.setEntity(new StringEntity(data.toString(), "utf-8"));
        HttpResponse response = httpClient.execute(post);

        if (response.getStatusLine().getStatusCode() != HTTP_RESPONSE_CODE_SUCCESS) {
            throw new IOException("Unexpected code "
                    + response.getStatusLine().getStatusCode());
        }

        String responseStr = EntityUtils.toString(response.getEntity());
        Log.d(TAG, "existPMAByUFMI종료(value=" + responseStr + ")");
        return responseStr;
    }


    public String sendMsgWithOpts(String sender, String receiver, int qos, String contentType, String
            content, int expiry) throws Exception {
        Log.d(TAG, "sendMsgWithOpts시작(sender=" + sender + ", receiver=" + receiver + ", qos="
                + qos + ", contentType=" + contentType + ", content=" + content + ", expiry=" + expiry + ")");

        HttpClient httpClient = getHttpClient();

        JSONObject data = new JSONObject();
        data.put("sender", sender);
        data.put("receiver", receiver);
        data.put("qos", qos);
        data.put("contentType", contentType);
        data.put("content", content);
        data.put("expiry", expiry);
        Log.d(TAG, "data=" + data);

        //String token = preference.getValue(PushPreference.TOKEN, null);
        Log.d(TAG, "currentToken=" + currentToken);
        if (currentToken == null) {
            throw new Exception("토큰이존재하지않습니다.");
        }
        HttpPost post = new HttpPost(MESSAGE_URI);
        post.setHeader("Content-Type", "application/json;charset=UTF-8");
        post.setEntity(new StringEntity(data.toString(), "utf-8"));
        //set Header token
        post.setHeader("X-ApiKey", currentToken);
        //post.setEntity(new StringEntity(data.toString(), "utf-8"));
        HttpResponse response = httpClient.execute(post);

        if (response.getStatusLine().getStatusCode() != HTTP_RESPONSE_CODE_SUCCESS) {
            throw new IOException("Unexpected code "
                    + response.getStatusLine().getStatusCode());
        }

        String responseStr = EntityUtils.toString(response.getEntity());
        Log.d(TAG, "sendMsgWithOpts종료(value=" + responseStr + ")");
        return responseStr;
    }

    public String updateUFMI(String phoneNum, String ufmi) throws Exception {
        Log.d(TAG, "updateUFMI시작(phoneNum=" + phoneNum + ", ufmi=" + ufmi + ")");
        HttpClient httpClient = getHttpClient();
        JSONObject data = new JSONObject();
        data.put("phoneNum", phoneNum);
        data.put("ufmi", ufmi);
        Log.d(TAG, "data=" + data);

        //String token = preference.getValue(PushPreference.TOKEN, null);
        Log.d(TAG, "currentToken=" + currentToken);
        if (currentToken == null) {
            throw new Exception("토큰이존재하지않습니다.");
        }
        HttpPut put = new HttpPut(UPDATEUFMI_URL);
        put.setHeader("Content-Type", "application/json;charset=UTF-8");
        put.setEntity(new StringEntity(data.toString(), "utf-8"));
        //set Header token
        put.setHeader("X-ApiKey", currentToken);
        //post.setEntity(new StringEntity(data.toString(), "utf-8"));
        HttpResponse response = httpClient.execute(put);

        if (response.getStatusLine().getStatusCode() != HTTP_RESPONSE_CODE_SUCCESS) {
            throw new IOException("Unexpected code "
                    + response.getStatusLine().getStatusCode());
        }

        String responseStr = EntityUtils.toString(response.getEntity());
        Log.d(TAG, "updateUFMI종료(value=" + responseStr + ")");
        return responseStr;
    }

    /**
     *
     */
    private void setStrictMode() {
        Log.d(TAG, "setStrictMode시작()");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Log.d(TAG, "setStrictMode종료()");
    }


}
