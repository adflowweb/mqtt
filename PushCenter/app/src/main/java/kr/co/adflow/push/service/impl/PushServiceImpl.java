package kr.co.adflow.push.service.impl;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.PrintWriter;

import kr.co.adflow.push.IPushService;
import kr.co.adflow.push.PushPreference;
import kr.co.adflow.push.exception.MsgSizeLimitException;
import kr.co.adflow.push.handler.PushHandler;
import kr.co.adflow.push.receiver.PushReceiver;
import kr.co.adflow.push.service.PushService;

public class PushServiceImpl extends Service implements PushService {

    public static final String TAG = "PushService";
    private static PowerManager.WakeLock wakeLock;
    private PushHandler pushHandler;
    private PushPreference preference;

    // Binder given to clients
    // private final IBinder binder = new LocalBinder();
    public static PushServiceImpl instance;

    public PushServiceImpl() {
        Log.d(TAG, "PushService생성자시작()");
        Log.d(TAG, "PushService=" + this);
        pushHandler = new PushHandler(this);
        Log.d(TAG, "pushHandler=" + pushHandler);
        preference = new PushPreference(this);
        Log.d(TAG, "preference=" + preference);
        instance = this;
        Log.d(TAG, "PushService생성자종료()");
    }

    public static PushServiceImpl getInstance() {
        return instance;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind시작(intent=" + intent + ")");
        Log.d(TAG, "onBind종료(binder=" + binder + ")");
        return binder;
    }

    //AIDL 구현체
    IPushService.Stub binder = new IPushService.Stub() {

        @Override
        public String sendMsg(String sender, String receiver, String contentType, String content) throws RemoteException {
            Log.d(TAG, "sendMsg시작(sender=" + sender + ", receiver=" + receiver + ", contentType=" + contentType + ", content=" + content + ")");
            long start = System.currentTimeMillis();
            JSONObject returnData = new JSONObject();
            JSONObject result = new JSONObject();

            try {
                if (sender == null || sender.equals("") || receiver == null || receiver.equals("")) {
                    Log.e(TAG, "데이터가적절하지않습니다.");
                    throw new Exception("데이터가적절하지않습니다.");
                }

                //{"msgId":2209,"sender":"mms/P1/82/50/p2000","receiver":"mms/P1/82/50/p2014","content":"7YWM7Iqk7Yq4IOuplOyLnOyngA==",
                // "contentType":"application/base64", "msgType":106,"serviceId":"kr.co.ktpowertel.push.userMessage"}

                JSONObject sendData = new JSONObject();
                sendData.put("msgId", System.currentTimeMillis());
                sendData.put("sender", sender);
                sendData.put("receiver", receiver);
                sendData.put("content", content);
                sendData.put("contentType", contentType);
                sendData.put("msgType", 106);
                sendData.put("serviceId", "kr.co.ktpowertel.push.userMessage");

                String sendMsg = sendData.toString();
                Log.d(TAG, "메시지크기=" + sendMsg.length());
                if (sendMsg.length() > 500000) {
                    Log.e(TAG, "데이터가적절하지않습니다.");
                    throw new MsgSizeLimitException();
                }

                PushServiceImpl.getInstance().publish(receiver,
                        sendMsg.getBytes(), 2 /* qos */);
                result.put("success", true);
                returnData.put("result", result);
                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");
                Log.d(TAG, "sendMsg종료");
                return returnData.toString();
            } catch (MsgSizeLimitException e) {
                Log.e(TAG, "sendMsg중에러발생", e);
                try {
                    result.put("success", false);
                    result.put("error", e.errorMsg);
                    result.put("errorCode", e.errorCode);
                    returnData.put("result", result);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");
                //return "{\"result\":{\"success\":false, \"error\":\"" + e + "\"}}";
                return returnData.toString();
            } catch (Exception e) {
                Log.e(TAG, "sendMsg중에러발생", e);
                try {
                    result.put("success", false);
                    result.put("error", e.toString());
                    returnData.put("result", result);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");
                //return "{\"result\":{\"success\":false, \"error\":\"" + e + "\"}}";
                return returnData.toString();
            }
        }

        @Override
        public String sendMsgWithOpts(String sender, String receiver, int qos, String contentType, String content, int contentLength, int expiry, boolean mms) throws RemoteException {
            Log.d(TAG, "sendMsgWithOpts시작(sender=" + sender + ", receiver=" + receiver + ", qos="
                    + qos + ", contentType=" + contentType + ", content=" + content + ", contentLength=" + contentLength + ", expiry=" + expiry + ", mms=" + mms + ")");

            long start = System.currentTimeMillis();
            JSONObject returnData = new JSONObject();
            JSONObject res = new JSONObject();
            try {
                String result = PushServiceImpl.getInstance().sendMsgWithOpts(sender, receiver, qos, contentType, content, contentLength, expiry, mms);
                Log.d(TAG, "sendMsgWithOpts종료(result=" + result + ")");
                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");
                return result;
            } catch (Exception e) {
                Log.e(TAG, "sendMsgWithOpts처리중에러발생", e);

                try {
                    res.put("success", false);
                    res.put("error", e.toString());
                    returnData.put("result", res);

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                //return "{\"result\":{\"success\":false, \"error\":\"" + e + "\"}}";
                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");
                return returnData.toString();
            }
        }

        @Override
        public String updateUFMI(String ufmi) throws RemoteException {
            Log.d(TAG, "updateUFMI시작(ufmi=" + ufmi + ")");
            long start = System.currentTimeMillis();

            JSONObject returnData = new JSONObject();
            JSONObject res = new JSONObject();
            try {
                String phoneNum = preference.getValue(PushPreference.PHONENUM, null);
                String result = PushServiceImpl.getInstance().updateUFMI(phoneNum, ufmi);
                Log.d(TAG, "updateUFMI종료(result=" + result + ")");
                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");
                return result;
            } catch (Exception e) {
                Log.e(TAG, "updateUFMI중에러발생", e);

                try {
                    res.put("success", false);
                    res.put("error", e.toString());
                    returnData.put("result", res);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                //return "{\"result\":{\"success\":false, \"error\":\"" + e + "\"}}";
                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");
                return returnData.toString();
            }
        }

        /**
         * 해당그룹사용자수를 가져온다.
         *
         * @param topic
         * @return
         * @throws RemoteException
         */
        @Override
        public String getGrpSubscribers(String topic) throws RemoteException {
            Log.d(TAG, "getGrpSubscribers(topic=" + topic + ")");
            long start = System.currentTimeMillis();

            JSONObject returnData = new JSONObject();
            JSONObject res = new JSONObject();
            try {
                String result = PushServiceImpl.getInstance().getGrpSubscribers(topic);
                Log.d(TAG, "getGrpSubscribers종료(result=" + result + ")");
                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");
                return result;
            } catch (Exception e) {
                Log.e(TAG, "getGrpSubscribers중에러발생", e);

                try {
                    res.put("success", false);
                    res.put("error", e.toString());
                    returnData.put("result", res);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                //return "{\"result\":{\"success\":false, \"error\":\"" + e + "\"}}";
                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");
                return returnData.toString();
            }
        }

        @Override
        public String getToken() throws RemoteException {
            Log.d(TAG, "getToken시작()");
            String result = PushServiceImpl.getInstance().getToken();
            Log.d(TAG, "getToken종료(result=" + result + ")");
            return result;
        }

//        @Override
//        public void registerPMC() throws RemoteException {
//            Log.d(TAG, "registerPMC시작()");
//            //PMC 등록
//            preference.put(PushPreference.REGISTERED_PMC, true);
//            Log.d(TAG, "registerPMC종료()");
//        }

        @Override
        public String existPMAByUFMI(String ufmi) throws RemoteException {
            Log.d(TAG, "existPMAByUFMI시작(ufmi=" + ufmi + ")");
            long start = System.currentTimeMillis();

            JSONObject returnData = new JSONObject();
            JSONObject res = new JSONObject();
            try {
                String result = PushServiceImpl.getInstance().existPMAByUFMI(ufmi);
                Log.d(TAG, "existPMAByUFMI종료(result=" + result + ")");
                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");
                return result;
            } catch (Exception e) {
                Log.e(TAG, "existPMAByUFMI중에러발생", e);

                try {
                    res.put("success", false);
                    res.put("error", e.toString());
                    returnData.put("result", res);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                //return "{\"result\":{\"success\":false, \"error\":\"" + e + "\"}}";
                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");
                return returnData.toString();
            }
        }

        @Override
        public String existPMAByUserID(String userID) throws RemoteException {
            Log.d(TAG, "existPMAByUserID시작(userID=" + userID + ")");
            long start = System.currentTimeMillis();

            JSONObject returnData = new JSONObject();
            JSONObject res = new JSONObject();
            try {
                String result = PushServiceImpl.getInstance().existPMAByUserID(userID);
                Log.d(TAG, "existPMAByUserID종료(result=" + result + ")");
                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");
                return result;
            } catch (Exception e) {
                Log.e(TAG, "existPMAByUserID중에러발생", e);

                try {
                    res.put("success", false);
                    res.put("error", e.toString());
                    returnData.put("result", res);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                //return "{\"result\":{\"success\":false, \"error\":\"" + e + "\"}}";
                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");
                return returnData.toString();
            }
        }


//        @Override
//        public String connect(String userID, String deviceID, String ufmi) throws RemoteException {
//            Log.d(TAG, "connect시작(userID=" + userID + ", deviceID=" + deviceID + ", ufmi=" + ufmi + ")");
//            Log.d(TAG, "pushHandler=" + pushHandler);
//
//            //메인쓰레드에서 네트웍연결부분이 발생하여
//            //테스트중
//            setStrictMode();
//            //테스트코드END
//
//
//            JSONObject returnData = new JSONObject();
//            JSONObject result = new JSONObject();
//            String res = null;
//            try {
//                if (pushHandler != null) {
//                    //인증
//                    //{"result":{"success":true,"data":{"tokenID":"b0dbcd2fe4ce4c58940e33e","userID":"testUser","issue":1420715174000}}}
//                    res = pushHandler.auth(AUTH_URI, userID, deviceID, ufmi);
//                    JSONObject obj = new JSONObject(res);
//                    JSONObject rst = obj.getJSONObject("result");
//
//                    if (!rst.getBoolean("success")) {
//                        //에러데이타 추출후 응답메시지에 추가요망
//                        throw new Exception("인증문제가발생하였습니다(bizException)");
//                    }
//                    JSONObject data = rst.getJSONObject("data");
//                    String token = data.getString("tokenID");
//                    Log.d(TAG, "token=" + token);
//
//                    //연결
//                    if (token != null) {
//                        startPushHandler(token, MQTTSERVERIP, cleanSession);
//                    } else {
//                        // 토큰이널값일경우
//                        throw new Exception("인증문제가발생하였습니다(토큰값이없습니다)");
//                    }
//                } else {
//                    throw new Exception("푸시핸들러문제로전송에실패하였습니다.");
//                }
//                Log.d(TAG, "connect종료()");
//                result.put("success", true);
//                returnData.put("result", result);
//                return returnData.toString();
//            } catch (Exception e) {
//                Log.e(TAG, "mqtt세션연결중에러발생", e);
//                try {
//                    result.put("success", false);
//                    result.put("error", e.toString());
//                    returnData.put("result", result);
//                } catch (JSONException ex) {
//                    ex.printStackTrace();
//                }
//                //return "{\"result\":{\"success\":false, \"error\":\"" + e + "\"}}";
//                return returnData.toString();
//            }
//        }

        /**
         *
         * @param sender
         * @param topic
         * @return
         * @throws RemoteException
         */
        @Override
        public String preCheck(String sender, String topic) throws RemoteException {
            Log.d(TAG, "preCheck시작(sender=" + sender + ", topic=" + topic + ")");
            long start = System.currentTimeMillis();
            JSONObject returnData = new JSONObject();
            JSONObject result = new JSONObject();
            try {
                if (sender == null || sender.equals("") || topic == null || topic.equals("")) {
                    Log.e(TAG, "데이터가적절하지않습니다.");
                    throw new Exception("데이터가적절하지않습니다.");
                }
                PushServiceImpl.getInstance().preCheck(sender, topic);
                Log.d(TAG, "preCheck종료");
                result.put("success", true);
                returnData.put("result", result);
                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");
                return returnData.toString();
                //return "{\"result\":{\"success\":true}}";
            } catch (Exception e) {
                Log.e(TAG, "preCheck중에러발생", e);
                try {
                    result.put("success", false);
                    result.put("error", e.toString());
                    returnData.put("result", result);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");
                //return "{\"result\":{\"success\":false, \"error\":\"" + e + "\"}}";
                return returnData.toString();
            }
        }

        @Override
        public String getSubscriptions() throws RemoteException {
            Log.d(TAG, "getSubscriptions시작()");
            long start = System.currentTimeMillis();

            JSONObject returnData = new JSONObject();
            JSONObject res = new JSONObject();
            try {
                String result = PushServiceImpl.getInstance().getSubscriptions();
                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");
                //{"result":{"success":true, "data":{"result":{"success":true,"info":["subscription not found"]}}}}
                Log.d(TAG, "getSubscriptions종료(result=" + result + ")");
                return result;
            } catch (Exception e) {
                Log.e(TAG, "getSubscriptions중에러발생", e);

                try {
                    res.put("success", false);
                    res.put("error", e.toString());
                    returnData.put("result", res);

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");
                //return "{\"result\":{\"success\":false, \"error\":\"" + e + "\"}}";
                return returnData.toString();
            }
        }

        @Override
        public boolean isConnected() throws RemoteException {
            Log.d(TAG, "isConnected시작()");
            Log.d(TAG, "isConnected종료()");
            return PushServiceImpl.getInstance().isConnected();
        }

        @Override
        public String unsubscribe(String topic) throws RemoteException {
            Log.d(TAG, "unsubscribe시작(topic=" + topic + ")");
            long start = System.currentTimeMillis();
            JSONObject returnData = new JSONObject();
            JSONObject result = new JSONObject();
            try {
                PushServiceImpl.getInstance().unsubscribe(topic);
                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");

                result.put("success", true);
                returnData.put("result", result);
                Log.d(TAG, "unsubscribe종료(result=" + returnData + ")");
                return returnData.toString();
                // return "{\"result\":{\"success\":true}}";
            } catch (Exception e) {
                Log.e(TAG, "unsubscribe중에러발생", e);
                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");
                try {
                    result.put("success", false);
                    result.put("error", e.toString());
                    returnData.put("result", result);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                //return "{\"result\":{\"success\":false, \"error\":\"" + e + "\"}}";
                return returnData.toString();
            }
        }

        @Override
        public String subscribe(String topic, int qos) throws RemoteException {
            Log.d(TAG, "subscribe시작(topic=" + topic + ", qos=" + qos + ")");
            long start = System.currentTimeMillis();
            JSONObject returnData = new JSONObject();
            JSONObject result = new JSONObject();
            try {
                PushServiceImpl.getInstance().subscribe(topic, qos);
                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");
                result.put("success", true);
                returnData.put("result", result);
                Log.d(TAG, "subscribe종료(result=" + returnData + ")");
                return returnData.toString();
            } catch (Exception e) {
                Log.e(TAG, "subscribe중에러발생", e);
                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");
                try {
                    result.put("success", false);
                    result.put("error", e.toString());
                    returnData.put("result", result);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                return returnData.toString();
            }
        }

        @Override
        public String ack(String msgID, String tokenID) throws RemoteException {
            Log.d(TAG, "ack시작(msgID=" + msgID + ", tokenID=" + tokenID + ")");
            long start = System.currentTimeMillis();
            JSONObject returnData = new JSONObject();
            JSONObject result = new JSONObject();
            try {
                PushServiceImpl.getInstance().ack(msgID, tokenID);
                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");
                result.put("success", true);
                returnData.put("result", result);
                Log.d(TAG, "ack종료(result=" + returnData + ")");
                return returnData.toString();
            } catch (Exception e) {
                Log.e(TAG, "subscribe중에러발생", e);
                long stop = System.currentTimeMillis();
                Log.d(TAG, "걸린시간=" + (stop - start) + "ms");
                try {
                    result.put("success", false);
                    result.put("error", e.toString());
                    returnData.put("result", result);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                return returnData.toString();
            }
        }

//        @Override
//        public void publish(String topic, byte[] payload, int qos) throws RemoteException {
//            try {
//                Log.d(TAG, "publish시작");
//                Log.d(TAG, "topic=" + topic);
//                Log.d(TAG, "payload=" + new String(payload));
//                Log.d(TAG, "qos=" + qos);
//
//                if (topic == null || topic.equals("") || payload == null) {
//                    Log.e(TAG, "데이터가적절하지않습니다.");
//                    throw new Exception("데이터가적절하지않습니다.");
//                }
//                publish(topic,
//                        payload, qos);
//                Log.d(TAG, "publish종료");
//            } catch (Exception e) {
//                throw new RemoteException(e.getMessage());
//            }
//        }
    };

    private String getToken() {
        Log.d(TAG, "getToken시작()");
        String result = pushHandler.getToken();
        Log.d(TAG, "getToken종료(result=" + result + ")");
        return result;
    }

    /**
     * @param topic
     * @return
     * @throws Exception
     */
    private String getGrpSubscribers(String topic) throws Exception {
        Log.d(TAG, "getGrpSubscribers시작(topic=" + topic + ")");
        String result = pushHandler.getGrpSubscribers(topic);
        Log.d(TAG, "getGrpSubscribers종료(result=" + result + ")");
        return result;
    }

    /**
     * @param msgID
     * @param tokenID
     * @throws Exception
     */
    private void ack(String msgID, String tokenID) throws Exception {
        Log.d(TAG, "ack시작(msgID=" + msgID + ", tokenID=" + tokenID + ")");
        JSONObject ack = new JSONObject();
        ack.put("msgId", msgID);
        ack.put("token", tokenID);
        ack.put("ackType", "app");
        ack.put("ackTime", System.currentTimeMillis());
        pushHandler.addAckJob(ack);
        //publish(PushHandler.ACK_TOPIC, ack.toString().getBytes(), 2); // qos 2 로 전송
        Log.d(TAG, "ack종료()");
    }

    /**
     * @param userID
     * @return
     * @throws Exception
     */
    private String existPMAByUserID(String userID) throws Exception {
        Log.d(TAG, "existPMAByUserID시작(userID=" + userID + ")");
        String result = pushHandler.existPMAByUserID(userID);
        Log.d(TAG, "existPMAByUserID종료(result=" + result + ")");
        return result;
    }

    /**
     * @param ufmi
     * @return
     * @throws Exception
     */
    private String existPMAByUFMI(String ufmi) throws Exception {
        Log.d(TAG, "existPMAByUFMI시작(ufmi=" + ufmi + ")");
        String result = pushHandler.existPMAByUFMI(ufmi);
        Log.d(TAG, "existPMAByUFMI종료(result=" + result + ")");
        return result;
    }

    /**
     *
     */
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate시작()");
        Log.d(TAG, "onCreate종료()");
    }

    /**
     * @param intent
     * @param startId
     */
    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(TAG, "onStart시작(intent=" + intent + ",startId=" + startId + ")");
        Log.d(TAG, "onStart종료()");
    }

    /**
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand시작(intent=" + intent + ", flags=" + flags
                + ", startId=" + startId + ")");

        try {
            if (intent == null) {
                Log.e(TAG, "intent가 존재하지않습니다.");
                return Service.START_STICKY;
            }

            // action 분기처리
            if (intent.getAction().equals("kr.co.adflow.push.service.RESTART")) {
                // 푸시서비스 시작
                Log.d(TAG, "푸시핸들러를재시작합니다.");
                restartPushHandler();
                Log.d(TAG, "푸시핸들러를재시작되었습니다.");
            }

//            else if (intent.getAction().equals("kr.co.adflow.push.service.ACK")) {
//                Log.d(TAG, "메시지에크를시작합니다.");
//                Bundle bundle = intent.getExtras();
//
//                if (bundle == null) {
//                    Log.e(TAG, "bundle이 존재하지않습니다.");
//                    return Service.START_STICKY;
//                }
//
//                for (String key : bundle.keySet()) {
//                    Log.d(TAG, key + "=" + bundle.get(key));
//                }
//
//                String msgID = bundle.getString(PushPreference.MSGID);
//                Log.d(TAG, "msgID=" + msgID);
//
//                String token = bundle.getString(PushPreference.TOKEN);
//                Log.d(TAG, "token=" + token);
//
//                if (msgID != null && token != null) {
//                    JSONObject ack = new JSONObject();
//                    ack.put("msgID", msgID);
//                    ack.put("token", token);
//                    ack.put("ackType", "pma");
//                    ack.put("ackTime", System.currentTimeMillis());
//                    Log.d(TAG, "ack=" + ack);
//                    publish(PushHandler.ACK_TOPIC, ack.toString().getBytes(), 2); // qos 2 로 전송
//                }
//                Log.d(TAG, "메시지에크를종료합니다.");
//            }

//            else if (intent.getAction().equals(
//                    "kr.co.adflow.push.service.FWUPDATE")) {
//                Log.d(TAG, "firmUpdate시작");
//                // Device model
//                String phoneModel = android.os.Build.MODEL;
//                Log.d(TAG, "phoneModel=" + phoneModel);
//
//                Bundle bundle = intent.getExtras();
//                String msg = bundle.getString("Message");
//                Log.d(TAG, "msg=" + msg);
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("firmware update");
//                //builder.setIcon(R.drawable.icon);
//                builder.setMessage(msg);
//
//                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        //Do something
//                        Log.d(TAG, "펌웨어업데이트를 선택하였습니다.");
//                        Intent intent = new Intent();
//                        intent.setAction("android.settings.SYSTEM_UPDATE_SETTINGS");
//                        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
//                        //intent.setComponent(ComponentName
//                        //       .unflattenFromString("com.google.android.gsf/.update.SystemUpdateActivity"));
//                        startActivity(intent);
//                        dialog.dismiss();
//                    }
//                });
//
//                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        //Do something
//                        Log.d(TAG, "펌웨어업데이트를 취소하였습니다.");
//                        dialog.dismiss();
//                    }
//                });
//
//                AlertDialog alert = builder.create();
//                alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//                alert.show();
            //}
//            } else if (intent.getAction().equals(
//                    "kr.co.adflow.push.service.STOP")) {
//                Log.d(TAG, "푸시서비스를종료합니다.");
//                pushHandler.stop();
            else if (intent.getAction().equals(
                    "kr.co.adflow.push.service.KEEPALIVE")) {
                Log.d(TAG, "keepAlive체크를시작합니다.");
                pushHandler.keepAlive();
                Log.d(TAG, "keepAlive체크를종료합니다.");
            } else if (intent.getAction().equals(
                    "kr.co.adflow.push.service.STOP")) {
                Log.d(TAG, "푸시서비스를종료합니다.");
                pushHandler.stop();
                this.stopSelf();
                Log.d(TAG, "푸시서비스를종료되었습니다.");
            }
            // else if (intent.getAction().equals("kr.co.adflow.action.login"))
            // {
            // // 로그인시
            // Log.d(TAG, "로그인처리를시작합니다.");
            // pushHandler.login(intent);
            // Log.d(TAG, "로그인처리를종료합니다.");
            // }
            else {
                Log.e(TAG, "적절한처리핸들러가없습니다.");
            }
        } catch (
                Exception e
                )

        {
            Log.e(TAG, "예외상황발생", e);
        }

        int ret = super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStartCommand종료(리턴코드=" + ret + ")");
        return ret;
    }


    /**
     * @throws Exception
     */
    public void restartPushHandler() throws Exception {
        Log.d(TAG, "restartPushHandler시작()");
        Log.d(TAG, "알람을설정합니다.");
        AlarmManager service = (AlarmManager) this
                .getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, PushReceiver.class);
        i.setAction("kr.co.adflow.push.service.KEEPALIVE");
        PendingIntent pending = PendingIntent.getBroadcast(this, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        service.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 1000,
                1000 * PushHandler.ALARM_INTERVAL, pending);
        Log.d(TAG, "알람이설정되었습니다");
        // 푸시핸들러 시작
        pushHandler.start();
        Log.d(TAG, "restartPushHandler종료()");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy시작()");
        pushHandler.stop();
        // 알람제거해야함
        Log.d(TAG, "onDestroy종료()");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged시작(config=" + newConfig + ")");
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged종료()");
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, "onLowMemory시작()");
        Log.d(TAG, "onLowMemory종료()");
    }

    @Override
    public void onTrimMemory(int level) {
        Log.d(TAG, "onTrimMemory시작(level=" + level + ")");
        Log.d(TAG, "onTrimMemory종료()");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind시작(intent=" + intent + ")");
        Log.d(TAG, "onUnbind종료()");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind시작(intent=" + intent + ")");
        Log.d(TAG, "onRebind종료()");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "onTaskRemoved시작(intent=" + rootIntent + ")");
        Log.d(TAG, "onTaskRemoved종료()");
    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        Log.d(TAG, "dump시작(fd=" + fd + "||writer=" + writer + "||args=" + args
                + ")");
        super.dump(fd, writer, args);
        Log.d(TAG, "dump종료()");
    }

    /**
     * @return
     */
    public static PowerManager.WakeLock getWakeLock() {
        Log.d(TAG, "getWakeLock시작()");
        Log.d(TAG, "getWakeLock종료(wakeLock=" + wakeLock + ")");
        return wakeLock;
    }

    /**
     * @param lock
     */
    public static void setWakeLock(PowerManager.WakeLock lock) {
        Log.d(TAG, "setWakeLock시작(lock=" + lock + ")");
        wakeLock = lock;
        Log.d(TAG, "setWakeLock종료(wakeLock=" + wakeLock + ")");
    }

    /**
     * @param topic
     * @param payload
     * @param qos
     * @throws Exception
     */
    @Override
    public void publish(String topic, byte[] payload, int qos) throws Exception {
        Log.d(TAG, "publish시작(토픽=" + topic + ", qos=" + qos + ")");
        Log.d(TAG, "service=" + this);
        Log.d(TAG, "pushHandler=" + pushHandler);
        if (pushHandler != null || pushHandler.isConnected()) {
            // 일단 retained = false로 세팅
            pushHandler.publish(topic, payload, qos, false);
        } else {
            throw new Exception("푸시핸들러문제로전송에실패하였습니다.");
        }
        Log.d(TAG, "publish종료()");
    }

    /**
     * @param topic
     * @param qos
     * @throws Exception
     */
    @Override
    public void subscribe(String topic, int qos) throws Exception {
        Log.d(TAG, "subScribe시작(토픽=" + topic + ", qos=" + qos + ")");
        //pushHandler.subscribe(topic, qos);
        pushHandler.addSubscribeJob(topic);
        Log.d(TAG, "subscribe종료()");
    }

    /**
     * @param topic
     * @throws Exception
     */
    @Override
    public void unsubscribe(String topic) throws Exception {
        Log.d(TAG, "unsubscribe시작(토픽=" + topic + ")");
        //pushHandler.unsubscribe(topic);
        pushHandler.addUnsubscribeJob(topic);
        Log.d(TAG, "unsubscribe종료()");
    }

    /**
     * @param sender
     * @param topic
     * @throws Exception
     */
    @Override
    public void preCheck(String sender, String topic) throws Exception {
        Log.d(TAG, "preCheck시작(sender=" + sender + ", 토픽=" + topic + ")");
        pushHandler.preCheck(sender, topic);
        Log.d(TAG, "preCheck종료()");
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    public String getSubscriptions() throws Exception {
        Log.d(TAG, "getSubscriptions시작()");
        String ret = pushHandler.getSubscriptions();
        Log.d(TAG, "getSubscriptions종료(return=" + ret + ")");
        return ret;
    }

    /*
     * (non-Javadoc)
     *
     * @see kr.co.adflow.push.service.PushService#isConnected()
     */
    @Override
    public boolean isConnected() {
        Log.d(TAG, "isConnected시작()");
        boolean value = false;
        if (pushHandler != null) {
            value = pushHandler.isConnected();
        }
        Log.d(TAG, "isConnected종료(value=" + value + ")");
        return value;
    }

    /**
     * @return
     */
    @Override
    public int getLostCount() {
        Log.d(TAG, "getLostCount시작()");
        int lostCount = pushHandler.getLostCount();
        Log.d(TAG, "getLostCount종료(lostCount=" + lostCount + ")");
        return lostCount;
    }

    /*
     * (non-Javadoc)
     *
     * @see kr.co.adflow.push.service.PushService#auth(java.lang.String,
     * java.lang.String)
     */
    @Override
    public String auth(String url, String userID, String deviceID)
            throws Exception {
        Log.d(TAG, "auth시작(url=" + url + ", userID=" + userID + ", deviceID="
                + deviceID + ")");
        String ret = pushHandler.auth(url, userID, deviceID);
        Log.d(TAG, "auth종료(return=" + ret + ")");
        return ret;
    }

    public String sendMsgWithOpts(String sender, String receiver, int qos, String contentType, String content, int contentLength, int expiry, boolean mms) throws Exception {
        Log.d(TAG, "sendMsgWithOpts시작(sender=" + sender + ", receiver=" + receiver + ", qos="
                + qos + ", contentType=" + contentType + ", content=" + content + ", contentLength=" + contentLength + ", expiry=" + expiry + ", mms=" + mms + ")");
        String ret = pushHandler.sendMsgWithOpts(sender, receiver, qos, contentType, content, contentLength, expiry, mms);
        Log.d(TAG, "sendMsgWithOpts종료(return=" + ret + ")");
        return ret;
    }

    private String updateUFMI(String phoneNum, String ufmi) throws Exception {
        Log.d(TAG, "updateUFMI시작(phoneNum=" + phoneNum + ", ufmi=" + ufmi + ")");
        String ret = pushHandler.updateUFMI(phoneNum, ufmi);
        Log.d(TAG, "updateUFMI종료(return=" + ret + ")");
        return ret;
    }
}
