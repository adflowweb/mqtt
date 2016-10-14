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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.PrintWriter;

import kr.co.adflow.push.BuildConfig;
import kr.co.adflow.push.IPushService;
import kr.co.adflow.push.PushPreference;
import kr.co.adflow.push.exception.MsgSizeLimitException;
import kr.co.adflow.push.handler.PushHandler;
import kr.co.adflow.push.receiver.PushReceiver;
import kr.co.adflow.push.service.PushService;
import kr.co.adflow.push.util.DebugLog;
import kr.co.adflow.push.util.ErrLogger;
import kr.co.adflow.push.util.TRLogger;

public class PushServiceImpl extends Service implements PushService {

    public static final String TAG = "PushService";
    private static PowerManager.WakeLock wakeLock;

    private PushHandler pushHandler;
    private PushPreference preference;

    // Binder given to clients
    // private final IBinder binder = new LocalBinder();
    public static PushServiceImpl instance;

    public PushServiceImpl() {
        DebugLog.d("PushService 생성자 시작()");
        DebugLog.d("PushService = " + this);
        pushHandler = new PushHandler(this);
        DebugLog.d("pushHandler = " + pushHandler);
        preference = new PushPreference(this);
        DebugLog.d("preference = " + preference);
        instance = this;

//        //testCode
//        //알람설정
//        DebugLog.d("keepAlive 알람을 설정합니다");
//        AlarmManager service = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent i = new Intent(this, PushReceiver.class);
//        i.setAction("kr.co.adflow.push.service.KEEPALIVE");
//        PendingIntent pending = PendingIntent.getBroadcast(this, 0,
//                i, PendingIntent.FLAG_UPDATE_CURRENT);
//        service.setRepeating(AlarmManager.RTC_WAKEUP,
//                System.currentTimeMillis() + 1000,
//                1000 * PushHandler.ALARM_INTERVAL, pending);
//        DebugLog.d("keepAlive 알람이 설정되었습니다");
//        //testEnd

        DebugLog.d("PushService 생성자 종료()");
    }

    public static PushServiceImpl getInstance() {
        return instance;
    }

    @Override
    public IBinder onBind(Intent intent) {
        DebugLog.d("onBind 시작(intent = " + intent + ")");
        DebugLog.d("onBind 종료(binder = " + binder + ")");
        return binder;
    }

    //AIDL 구현체
    IPushService.Stub binder = new IPushService.Stub() {

        @Override
        public String sendMsg(String sender, String receiver, String contentType, String content) throws RemoteException {
            DebugLog.d("sendMsg 시작(sender = " + sender + ", receiver = " + receiver + ", contentType = " + contentType + ", content = " + content + ")");
            long start = System.currentTimeMillis();
            JSONObject returnData = new JSONObject();
            JSONObject result = new JSONObject();

            try {
                //sendMsg 트랜잭션 저장
                TRLogger.i(TAG, "[" + receiver + "] sendMsg 요청 시작");
                if (sender == null || sender.equals("") || receiver == null || receiver.equals("")) {
                    DebugLog.e("데이터가 적절하지 않습니다");
                    throw new Exception("데이터가 적절하지 않습니다");
                }

                //{"msgId":2209,"sender":"mms/P1/82/50/p2000","receiver":"mms/P1/82/50/p2014","content":"7YWM7Iqk7Yq4IOuplOyLnOyngA= = ",
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
                DebugLog.d("메시지 크기 = " + sendMsg.length());
                if (sendMsg.length() > 500000) {
                    DebugLog.e("데이터가 적절하지 않습니다");
                    throw new MsgSizeLimitException();
                }

                PushServiceImpl.getInstance().publish(receiver,
                        sendMsg.getBytes(), 2 /* qos */);
                result.put("success", true);
                returnData.put("result", result);
                long stop = System.currentTimeMillis();
                DebugLog.d("걸린 시간 = " + (stop - start) + "ms");
                DebugLog.d("sendMsg 종료(" + returnData.toString() + ")");
                //sendMsg 트랜잭션 저장
                TRLogger.i(TAG, "[" + receiver + "] sendMsg 요청 완료");
                return returnData.toString();
            } catch (MsgSizeLimitException e) {
                DebugLog.e("sendMsg중 에러 발생", e);
                ErrLogger.e(TAG, "sendMsg중 에러 발생", e);
                try {
                    result.put("success", false);
                    result.put("error", e.errorMsg);
                    result.put("errorCode", e.errorCode);
                    returnData.put("result", result);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                long stop = System.currentTimeMillis();
                DebugLog.d("걸린 시간 = " + (stop - start) + "ms");
                //return "{\"result\":{\"success\":false, \"error\":\"" + e + "\"}}";
                return returnData.toString();
            } catch (Exception e) {
                DebugLog.e("sendMsg중 에러 발생", e);
                ErrLogger.e(TAG, "sendMsg중 에러 발생", e);
                try {
                    result.put("success", false);
                    result.put("error", e.toString());
                    returnData.put("result", result);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                long stop = System.currentTimeMillis();
                DebugLog.d("걸린 시간 = " + (stop - start) + "ms");
                //return "{\"result\":{\"success\":false, \"error\":\"" + e + "\"}}";
                DebugLog.d("sendMsg 종료(" + returnData.toString() + ")");
                return returnData.toString();
            }
        }

        @Override
        public String sendMsgWithOpts(String sender, String receiver, int qos, String contentType, String content, int contentLength, int expiry, boolean mms) throws RemoteException {
            DebugLog.d("sendMsgWithOpts 시작(sender = " + sender + ", receiver = " + receiver + ", qos = "
                    + qos + ", contentType = " + contentType + ", content = " + content + ", contentLength = " + contentLength + ", expiry = " + expiry + ", mms = " + mms + ")");

            long start = System.currentTimeMillis();
            JSONObject returnData = new JSONObject();
            JSONObject res = new JSONObject();
            try {
                //sendMsgWithOpts 트랜잭션 저장
                TRLogger.i(TAG, "[" + receiver + "] sendMsgWithOpts 요청 시작 contentLength = " + contentLength);
                String result = PushServiceImpl.getInstance().sendMsgWithOpts(sender, receiver, qos, contentType, content, contentLength, expiry, mms);
                DebugLog.d("sendMsgWithOpts 종료(result = " + result + ")");
                long stop = System.currentTimeMillis();
                DebugLog.d("걸린 시간 = " + (stop - start) + "ms");
                //sendMsgWithOpts 트랜잭션 저장
                TRLogger.i(TAG, "[" + receiver + "] sendMsgWithOpts 요청 완료 contentLength = " + contentLength);
                return result;
            } catch (Exception e) {
                DebugLog.e("sendMsgWithOpts 처리중 에러 발생", e);
                ErrLogger.e(TAG, "sendMsgWithOpts 처리중 에러 발생", e);

                try {
                    res.put("success", false);
                    res.put("error", e.toString());
                    returnData.put("result", res);

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                //return "{\"result\":{\"success\":false, \"error\":\"" + e + "\"}}";
                long stop = System.currentTimeMillis();
                DebugLog.d("걸린 시간 = " + (stop - start) + "ms");
                DebugLog.d("sendMsgWithOpts 종료(result = " + returnData.toString() + ")");
                return returnData.toString();
            }
        }

        @Override
        public String updateUFMI(String ufmi) throws RemoteException {
            DebugLog.d("updateUFMI 시작(ufmi = " + ufmi + ")");
            long start = System.currentTimeMillis();

            JSONObject returnData = new JSONObject();
            JSONObject res = new JSONObject();
            try {
                //updateUFMI 트랜잭션 저장
                TRLogger.i(TAG, "[" + ufmi + "] updateUFMI 요청 시작 ");

                String phoneNum = preference.getValue(PushPreference.PHONENUM, null);
                String result = PushServiceImpl.getInstance().updateUFMI(phoneNum, ufmi);
                DebugLog.d("updateUFMI 종료(result = " + result + ")");
                long stop = System.currentTimeMillis();
                DebugLog.d("걸린 시간 = " + (stop - start) + "ms");
                //updateUFMI 트랜잭션 저장
                TRLogger.i(TAG, "[" + ufmi + "] updateUFMI 요청 완료 ");
                return result;
            } catch (Exception e) {
                DebugLog.e("updateUFMI중 에러 발생", e);
                ErrLogger.e(TAG, "updateUFMI중 에러 발생", e);

                try {
                    res.put("success", false);
                    res.put("error", e.toString());
                    returnData.put("result", res);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                //return "{\"result\":{\"success\":false, \"error\":\"" + e + "\"}}";
                long stop = System.currentTimeMillis();
                DebugLog.d("걸린 시간 = " + (stop - start) + "ms");
                DebugLog.d("updateUFMI 종료(result = " + returnData.toString() + ")");
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
            DebugLog.d("getGrpSubscribers 시작(topic = " + topic + ")");
            long start = System.currentTimeMillis();

            JSONObject returnData = new JSONObject();
            JSONObject res = new JSONObject();
            try {
                //getGrpSubscribers 트랜잭션 저장
                TRLogger.i(TAG, "[" + topic + "] getGrpSubscribers 요청 시작 ");
                String result = PushServiceImpl.getInstance().getGrpSubscribers(topic);
                DebugLog.d("getGrpSubscribers 종료(result = " + result + ")");
                long stop = System.currentTimeMillis();
                DebugLog.d("걸린 시간 = " + (stop - start) + "ms");
                //getGrpSubscribers 트랜잭션 저장
                TRLogger.i(TAG, "[" + topic + "] getGrpSubscribers 요청 완료 ");
                return result;
            } catch (Exception e) {
                DebugLog.e("getGrpSubscribers중 에러 발생", e);
                ErrLogger.e(TAG, "getGrpSubscribers중 에러 발생", e);

                try {
                    res.put("success", false);
                    res.put("error", e.toString());
                    returnData.put("result", res);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                //return "{\"result\":{\"success\":false, \"error\":\"" + e + "\"}}";
                long stop = System.currentTimeMillis();
                DebugLog.d("걸린 시간 = " + (stop - start) + "ms");
                DebugLog.d("getGrpSubscribers 종료(result = " + returnData.toString() + ")");
                return returnData.toString();
            }
        }

        @Override
        public String getToken() throws RemoteException {
            DebugLog.d("getToken 시작()");
            //getToken 트랜잭션 저장
            TRLogger.i(TAG, "getToken 요청 시작 ");
            String result = PushServiceImpl.getInstance().getToken();
            DebugLog.d("getToken 종료(result = " + result + ")");
            //getToken 트랜잭션 저장
            TRLogger.i(TAG, "getToken 요청 완료 result = " + result);
            return result;
        }

//        @Override
//        public void registerPMC() throws RemoteException {
//            DebugLog.d("registerPMC시작()");
//            //PMC 등록
//            preference.put(PushPreference.REGISTERED_PMC, true);
//            DebugLog.d("registerPMC종료()");
//        }

        @Override
        public String existPMAByUFMI(String ufmi) throws RemoteException {
            DebugLog.d("existPMAByUFMI 시작(ufmi = " + ufmi + ")");
            long start = System.currentTimeMillis();

            JSONObject returnData = new JSONObject();
            JSONObject res = new JSONObject();
            try {
                //existPMAByUFMI 트랜잭션 저장
                TRLogger.i(TAG, "[" + ufmi + "] existPMAByUFMI 요청 시작 ");
                String result = PushServiceImpl.getInstance().existPMAByUFMI(ufmi);
                DebugLog.d("existPMAByUFMI 종료(result = " + result + ")");
                long stop = System.currentTimeMillis();
                DebugLog.d("걸린 시간 = " + (stop - start) + "ms");
                //existPMAByUFMI 트랜잭션 저장
                TRLogger.i(TAG, "[" + ufmi + "] existPMAByUFMI 요청 완료 result = " + result + ", duration = " + (stop - start) + "ms");
                return result;
            } catch (Exception e) {
                DebugLog.e("existPMAByUFMI중 에러 발생", e);
                ErrLogger.e(TAG, "existPMAByUFMI중 에러 발생", e);

                try {
                    res.put("success", false);
                    res.put("error", e.toString());
                    returnData.put("result", res);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                //return "{\"result\":{\"success\":false, \"error\":\"" + e + "\"}}";
                long stop = System.currentTimeMillis();
                DebugLog.d("걸린 시간 = " + (stop - start) + "ms");
                DebugLog.d("existPMAByUFMI 종료(result = " + returnData.toString() + ")");
                return returnData.toString();
            }
        }

        @Override
        public String existPMAByUserID(String userID) throws RemoteException {
            DebugLog.d("existPMAByUserID 시작(userID = " + userID + ")");
            long start = System.currentTimeMillis();

            JSONObject returnData = new JSONObject();
            JSONObject res = new JSONObject();
            try {
                //existPMAByUserID 트랜잭션 저장
                TRLogger.i(TAG, "[" + userID + "] existPMAByUserID 요청 시작 ");
                String result = PushServiceImpl.getInstance().existPMAByUserID(userID);
                DebugLog.d("existPMAByUserID 종료(result = " + result + ")");
                long stop = System.currentTimeMillis();
                DebugLog.d("걸린 시간 = " + (stop - start) + "ms");
                //existPMAByUserID 트랜잭션 저장
                TRLogger.i(TAG, "[" + userID + "] existPMAByUserID 요청 완료 duration = " + (stop - start) + "ms");
                return result;
            } catch (Exception e) {
                DebugLog.e("existPMAByUserID중 에러 발생", e);
                ErrLogger.e(TAG, "existPMAByUserID중 에러 발생", e);

                try {
                    res.put("success", false);
                    res.put("error", e.toString());
                    returnData.put("result", res);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                //return "{\"result\":{\"success\":false, \"error\":\"" + e + "\"}}";
                long stop = System.currentTimeMillis();
                DebugLog.d("걸린 시간 = " + (stop - start) + "ms");
                DebugLog.d("existPMAByUserID 종료(result = " + returnData.toString() + ")");
                return returnData.toString();
            }
        }


//        @Override
//        public String connect(String userID, String deviceID, String ufmi) throws RemoteException {
//            DebugLog.d("connect시작(userID = " + userID + ", deviceID = " + deviceID + ", ufmi = " + ufmi + ")");
//            DebugLog.d("pushHandler = " + pushHandler);
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
//                    DebugLog.d("token = " + token);
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
//                DebugLog.d("connect종료()");
//                result.put("success", true);
//                returnData.put("result", result);
//                return returnData.toString();
//            } catch (Exception e) {
//                DebugLog.e("mqtt세션연결중에러발생", e);
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
            DebugLog.d("preCheck 시작(sender = " + sender + ", topic = " + topic + ")");
            long start = System.currentTimeMillis();
            JSONObject returnData = new JSONObject();
            JSONObject result = new JSONObject();
            try {
                //preCheck 트랜잭션 저장
                TRLogger.i(TAG, "[" + topic + "] preCheck 요청 시작 ");

                if (sender == null || sender.equals("") || topic == null || topic.equals("")) {
                    DebugLog.e("데이터가 적절하지 않습니다");
                    throw new Exception("데이터가 적절하지 않습니다");
                }
                PushServiceImpl.getInstance().preCheck(sender, topic);
                DebugLog.d("preCheck 종료");
                result.put("success", true);
                returnData.put("result", result);
                long stop = System.currentTimeMillis();
                DebugLog.d("걸린 시간 = " + (stop - start) + "ms");
                DebugLog.d("preCheck 종료(result = " + returnData.toString() + ")");
                //preCheck 트랜잭션 저장
                TRLogger.i(TAG, "[" + topic + "] preCheck 요청 완료 duration = " + (stop - start) + "ms");
                return returnData.toString();
                //return "{\"result\":{\"success\":true}}";
            } catch (Exception e) {
                DebugLog.e("preCheck중 에러 발생", e);
                ErrLogger.e(TAG, "preCheck중 에러 발생", e);
                try {
                    result.put("success", false);
                    result.put("error", e.toString());
                    returnData.put("result", result);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                long stop = System.currentTimeMillis();
                DebugLog.d("걸린 시간 = " + (stop - start) + "ms");
                //return "{\"result\":{\"success\":false, \"error\":\"" + e + "\"}}";
                DebugLog.d("preCheck 종료(result = " + returnData.toString() + ")");
                return returnData.toString();
            }
        }

        @Override
        public String getSubscriptions() throws RemoteException {
            DebugLog.d("getSubscriptions 시작()");
            long start = System.currentTimeMillis();

            JSONObject returnData = new JSONObject();
            JSONObject res = new JSONObject();
            try {
                //getSubscriptions 트랜잭션 저장
                TRLogger.i(TAG, "getSubscriptions 요청 시작 ");
                String result = PushServiceImpl.getInstance().getSubscriptions();
                long stop = System.currentTimeMillis();
                DebugLog.d("걸린 시간 = " + (stop - start) + "ms");
                //{"result":{"success":true, "data":{"result":{"success":true,"info":["subscription not found"]}}}}
                DebugLog.d("getSubscriptions 종료(result = " + result + ")");
                //getSubscriptions 트랜잭션 저장
                TRLogger.i(TAG, "getSubscriptions 요청 완료 result = " + result + ", duration = " + (stop - start) + "ms");
                return result;
            } catch (Exception e) {
                DebugLog.e("getSubscriptions중 에러 발생", e);
                ErrLogger.e(TAG, "getSubscriptions중 에러 발생", e);

                try {
                    res.put("success", false);
                    res.put("error", e.toString());
                    returnData.put("result", res);

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                long stop = System.currentTimeMillis();
                DebugLog.d("걸린 시간 = " + (stop - start) + "ms");
                //return "{\"result\":{\"success\":false, \"error\":\"" + e + "\"}}";
                DebugLog.d("getSubscriptions 종료(result = " + returnData.toString() + ")");
                return returnData.toString();
            }
        }

        @Override
        public boolean isConnected() throws RemoteException {
            DebugLog.d("isConnected 시작()");
            DebugLog.d("isConnected 종료()");
            return PushServiceImpl.getInstance().isConnected();
        }

        @Override
        public String unsubscribe(String topic) throws RemoteException {
            DebugLog.d("unsubscribe 시작(topic = " + topic + ")");
            long start = System.currentTimeMillis();
            JSONObject returnData = new JSONObject();
            JSONObject result = new JSONObject();
            try {
                //unsubscribe 트랜잭션 저장
                TRLogger.i(TAG, "[" + topic + "] unsubscribe 요청 시작 ");
                PushServiceImpl.getInstance().unsubscribe(topic);
                long stop = System.currentTimeMillis();
                DebugLog.d("걸린 시간 = " + (stop - start) + "ms");

                result.put("success", true);
                returnData.put("result", result);
                DebugLog.d("unsubscribe 종료(result = " + returnData + ")");
                //unsubscribe 트랜잭션 저장
                TRLogger.i(TAG, "[" + topic + "] unsubscribe 요청 완료 duration = " + (stop - start) + "ms");
                return returnData.toString();
                // return "{\"result\":{\"success\":true}}";
            } catch (Exception e) {
                DebugLog.e("unsubscribe중 에러 발생", e);
                ErrLogger.e(TAG, "unsubscribe중 에러 발생", e);
                long stop = System.currentTimeMillis();
                DebugLog.d("걸린 시간 = " + (stop - start) + "ms");
                try {
                    result.put("success", false);
                    result.put("error", e.toString());
                    returnData.put("result", result);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                //return "{\"result\":{\"success\":false, \"error\":\"" + e + "\"}}";
                DebugLog.d("unsubscribe 종료(result = " + returnData + ")");
                return returnData.toString();
            }
        }

        @Override
        public String subscribe(String topic, int qos) throws RemoteException {
            DebugLog.d("subscribe 시작(topic = " + topic + ", qos = " + qos + ")");
            long start = System.currentTimeMillis();
            JSONObject returnData = new JSONObject();
            JSONObject result = new JSONObject();
            try {
                //subscribe 트랜잭션 저장
                TRLogger.i(TAG, "[" + topic + "] subscribe 요청 시작 qos = " + qos);
                PushServiceImpl.getInstance().subscribe(topic, qos);
                long stop = System.currentTimeMillis();
                DebugLog.d("걸린 시간 = " + (stop - start) + "ms");
                result.put("success", true);
                returnData.put("result", result);
                DebugLog.d("subscribe 종료(result = " + returnData + ")");
                //subscribe 트랜잭션 저장
                TRLogger.i(TAG, "[" + topic + "] subscribe 요청 완료 duration = " + (stop - start) + "ms");
                return returnData.toString();
            } catch (Exception e) {
                DebugLog.e("subscribe중 에러 발생", e);
                ErrLogger.e(TAG, "subscribe중 에러 발생", e);
                long stop = System.currentTimeMillis();
                DebugLog.d("걸린시간 = " + (stop - start) + "ms");
                try {
                    result.put("success", false);
                    result.put("error", e.toString());
                    returnData.put("result", result);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                DebugLog.d("subscribe 종료(result = " + returnData + ")");
                return returnData.toString();
            }
        }

        @Override
        public String ack(String msgID, String tokenID) throws RemoteException {
            DebugLog.d("ack 시작(msgID = " + msgID + ", tokenID = " + tokenID + ")");
            long start = System.currentTimeMillis();
            JSONObject returnData = new JSONObject();
            JSONObject result = new JSONObject();
            try {
                //subscribe 트랜잭션 저장
                TRLogger.i(TAG, "[" + msgID + "] ack 요청 시작 tokenID = " + tokenID);
                PushServiceImpl.getInstance().ack(msgID, tokenID);
                long stop = System.currentTimeMillis();
                DebugLog.d("걸린 시간 = " + (stop - start) + "ms");
                result.put("success", true);
                returnData.put("result", result);
                DebugLog.d("ack 종료(result = " + returnData + ")");
                //subscribe 트랜잭션 저장
                TRLogger.i(TAG, "[" + msgID + "] ack 요청 완료 duration = " + (stop - start) + "ms");
                return returnData.toString();
            } catch (Exception e) {
                DebugLog.e("ack중 에러 발생", e);
                ErrLogger.e(TAG, "ack중 에러 발생", e);
                long stop = System.currentTimeMillis();
                DebugLog.d("걸린 시간 = " + (stop - start) + "ms");
                try {
                    result.put("success", false);
                    result.put("error", e.toString());
                    returnData.put("result", result);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                DebugLog.d("ack 종료(result = " + returnData + ")");
                return returnData.toString();
            }
        }

//        @Override
//        public void publish(String topic, byte[] payload, int qos) throws RemoteException {
//            try {
//                DebugLog.d("publish시작");
//                DebugLog.d("topic = " + topic);
//                DebugLog.d("payload = " + new String(payload));
//                DebugLog.d("qos = " + qos);
//
//                if (topic == null || topic.equals("") || payload == null) {
//                    DebugLog.e("데이터가적절하지않습니다.");
//                    throw new Exception("데이터가적절하지않습니다.");
//                }
//                publish(topic,
//                        payload, qos);
//                DebugLog.d("publish종료");
//            } catch (Exception e) {
//                throw new RemoteException(e.getMessage());
//            }
//        }
    };

    private String getToken() {
        DebugLog.d("getToken 시작()");
        String result = pushHandler.getToken();
        DebugLog.d("getToken 종료(result = " + result + ")");
        return result;
    }

    /**
     * @param topic
     * @return
     * @throws Exception
     */
    private String getGrpSubscribers(String topic) throws Exception {
        DebugLog.d("getGrpSubscribers 시작(topic = " + topic + ")");
        String result = pushHandler.getGrpSubscribers(topic);
        DebugLog.d("getGrpSubscribers 종료(result = " + result + ")");
        return result;
    }

    /**
     * @param msgID
     * @param tokenID
     * @throws Exception
     */
    private void ack(String msgID, String tokenID) throws Exception {
        DebugLog.d("ack 시작(msgID = " + msgID + ", tokenID = " + tokenID + ")");
        JSONObject ack = new JSONObject();
        ack.put("msgId", msgID);
        ack.put("token", tokenID);
        ack.put("ackType", "app");
        ack.put("ackTime", System.currentTimeMillis());
        pushHandler.addAckJob(ack);
        //publish(PushHandler.ACK_TOPIC, ack.toString().getBytes(), 2); // qos 2 로 전송
        DebugLog.d("ack 종료()");
    }

    /**
     * @param userID
     * @return
     * @throws Exception
     */
    private String existPMAByUserID(String userID) throws Exception {
        DebugLog.d("existPMAByUserID 시작(userID = " + userID + ")");
        String result = pushHandler.existPMAByUserID(userID);
        DebugLog.d("existPMAByUserID 종료(result = " + result + ")");
        return result;
    }

    /**
     * @param ufmi
     * @return
     * @throws Exception
     */
    private String existPMAByUFMI(String ufmi) throws Exception {
        DebugLog.d("existPMAByUFMI 시작(ufmi = " + ufmi + ")");
        String result = pushHandler.existPMAByUFMI(ufmi);
        DebugLog.d("existPMAByUFMI 종료(result = " + result + ")");
        return result;
    }

    /**
     *
     */
    @Override
    public void onCreate() {
        DebugLog.d("onCreate 시작()");
        DebugLog.d("onCreate 종료()");
    }

    /**
     * @param intent
     * @param startId
     */
    @Override
    public void onStart(Intent intent, int startId) {
        DebugLog.d("onStart 시작(intent = " + intent + ",startId = " + startId + ")");
        DebugLog.d("onStart 종료()");
    }

    /**
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DebugLog.d("onStartCommand 시작(intent = " + intent + ", flags = " + flags
                + ", startId = " + startId + ")");

        try {
            if (intent == null) {
                DebugLog.e("intent가 존재하지 않습니다");
                return Service.START_STICKY;
            }

            // action 분기처리
            if (intent.getAction().equals("kr.co.adflow.push.service.RESTART")) {
                // 푸시서비스 시작
                DebugLog.d("푸시핸들러를 재시작합니다");
                restartPushHandler();
                DebugLog.d("푸시핸들러가 재시작되었습니다");
            }

//            else if (intent.getAction().equals("kr.co.adflow.push.service.ACK")) {
//                DebugLog.d("메시지에크를시작합니다.");
//                Bundle bundle = intent.getExtras();
//
//                if (bundle == null) {
//                    DebugLog.e("bundle이 존재하지않습니다.");
//                    return Service.START_STICKY;
//                }
//
//                for (String key : bundle.keySet()) {
//                    DebugLog.d(key + " = " + bundle.get(key));
//                }
//
//                String msgID = bundle.getString(PushPreference.MSGID);
//                DebugLog.d("msgID = " + msgID);
//
//                String token = bundle.getString(PushPreference.TOKEN);
//                DebugLog.d("token = " + token);
//
//                if (msgID != null && token != null) {
//                    JSONObject ack = new JSONObject();
//                    ack.put("msgID", msgID);
//                    ack.put("token", token);
//                    ack.put("ackType", "pma");
//                    ack.put("ackTime", System.currentTimeMillis());
//                    DebugLog.d("ack = " + ack);
//                    publish(PushHandler.ACK_TOPIC, ack.toString().getBytes(), 2); // qos 2 로 전송
//                }
//                DebugLog.d("메시지에크를종료합니다.");
//            }

//            else if (intent.getAction().equals(
//                    "kr.co.adflow.push.service.FWUPDATE")) {
//                DebugLog.d("firmUpdate시작");
//                // Device model
//                String phoneModel = android.os.Build.MODEL;
//                DebugLog.d("phoneModel = " + phoneModel);
//
//                Bundle bundle = intent.getExtras();
//                String msg = bundle.getString("Message");
//                DebugLog.d("msg = " + msg);
//                AlertDiaDebugLog.Builder builder = new AlertDiaDebugLog.Builder(this);
//                builder.setTitle("firmware update");
//                //builder.setIcon(R.drawable.icon);
//                builder.setMessage(msg);
//
//                builder.setPositiveButton("예", new DiaDebugLogInterface.OnClickListener() {
//                    public void onClick(DiaDebugLogInterface diaDebugLog, int whichButton) {
//                        //Do something
//                        DebugLog.d("펌웨어업데이트를 선택하였습니다.");
//                        Intent intent = new Intent();
//                        intent.setAction("android.settings.SYSTEM_UPDATE_SETTINGS");
//                        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
//                        //intent.setComponent(ComponentName
//                        //       .unflattenFromString("com.google.android.gsf/.update.SystemUpdateActivity"));
//                        startActivity(intent);
//                        diaDebugLog.dismiss();
//                    }
//                });
//
//                builder.setNegativeButton("아니오", new DiaDebugLogInterface.OnClickListener() {
//                    public void onClick(DiaDebugLogInterface diaDebugLog, int whichButton) {
//                        //Do something
//                        DebugLog.d("펌웨어업데이트를 취소하였습니다.");
//                        diaDebugLog.dismiss();
//                    }
//                });
//
//                AlertDiaDebugLog alert = builder.create();
//                alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//                alert.show();
            //}
//            } else if (intent.getAction().equals(
//                    "kr.co.adflow.push.service.STOP")) {
//                DebugLog.d("푸시서비스를종료합니다.");
//                pushHandler.stop();
            else if (intent.getAction().equals(
                    "kr.co.adflow.push.service.KEEPALIVE")) {
                DebugLog.d("keepAlive 체크를 시작합니다");
                pushHandler.keepAlive();
                DebugLog.d("keepAlive 체크를 종료합니다");
            } else if (intent.getAction().equals(
                    "kr.co.adflow.push.service.STOP")) {
                DebugLog.d("푸시서비스를 종료합니다");
                pushHandler.stop();
                this.stopSelf();
                DebugLog.d("푸시서비스가 종료되었습니다");
            }
            // else if (intent.getAction().equals("kr.co.adflow.action.DebugLogin"))
            // {
            // // 로그인시
            // DebugLog.d("로그인처리를시작합니다.");
            // pushHandler.DebugLogin(intent);
            // DebugLog.d("로그인처리를종료합니다.");
            // }
            else {
                DebugLog.e("적절한 처리 핸들러가 없습니다");
            }
        } catch (Exception e) {
            DebugLog.e("예외 상황 발생", e);
            ErrLogger.e(TAG, "예외 상황 발생", e);
        }

        int ret = super.onStartCommand(intent, flags, startId);
        DebugLog.d("onStartCommand 종료(리턴코드 = " + ret + ")");
        return ret;
    }


    /**
     * @throws Exception
     */
    public void restartPushHandler() throws Exception {
        DebugLog.d("restartPushHandler 시작()");
        DebugLog.d("알람을 설정 합니다");
        AlarmManager service = (AlarmManager) this
                .getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, PushReceiver.class);
        i.setAction("kr.co.adflow.push.service.KEEPALIVE");
        PendingIntent pending = PendingIntent.getBroadcast(this, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        service.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 1000,
                1000 * BuildConfig.ALARM_INTERVAL, pending);
        DebugLog.d("알람이 설정 되었습니다");
        // 푸시핸들러 시작
        pushHandler.start();
        DebugLog.d("restartPushHandler 종료()");
    }

    @Override
    public void onDestroy() {
        DebugLog.d("onDestroy 시작()");
        pushHandler.stop();
        // 알람제거해야함
        DebugLog.d("onDestroy 종료()");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        DebugLog.d("onConfigurationChanged 시작(config = " + newConfig + ")");
        super.onConfigurationChanged(newConfig);
        DebugLog.d("onConfigurationChanged 종료()");
    }

    @Override
    public void onLowMemory() {
        DebugLog.d("onLowMemory 시작()");
        DebugLog.d("onLowMemory 종료()");
    }

    @Override
    public void onTrimMemory(int level) {
        DebugLog.d("onTrimMemory 시작(level = " + level + ")");
        DebugLog.d("onTrimMemory 종료()");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        DebugLog.d("onUnbind 시작(intent = " + intent + ")");
        DebugLog.d("onUnbind 종료()");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        DebugLog.d("onRebind 시작(intent = " + intent + ")");
        DebugLog.d("onRebind 종료()");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        DebugLog.d("onTaskRemoved 시작(intent = " + rootIntent + ")");
        DebugLog.d("onTaskRemoved 종료()");
    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        DebugLog.d("dump 시작(fd = " + fd + "||writer = " + writer + "||args = " + args
                + ")");
        super.dump(fd, writer, args);
        DebugLog.d("dump 종료()");
    }

    /**
     * @return
     */
    public static PowerManager.WakeLock getWakeLock() {
        DebugLog.d("getWakeLock 시작()");
        DebugLog.d("getWakeLock 종료(wakeLock = " + wakeLock + ")");
        return wakeLock;
    }

    /**
     * @param lock
     */
    public static void setWakeLock(PowerManager.WakeLock lock) {
        DebugLog.d("setWakeLock 시작(lock = " + lock + ")");
        wakeLock = lock;
        DebugLog.d("setWakeLock 종료(wakeLock = " + wakeLock + ")");
    }

    /**
     * @param topic
     * @param payload
     * @param qos
     * @throws Exception
     */
    @Override
    public void publish(String topic, byte[] payload, int qos) throws Exception {
        DebugLog.d("publish 시작(토픽 = " + topic + ", qos = " + qos + ")");
        DebugLog.d("service = " + this);
        DebugLog.d("pushHandler = " + pushHandler);
        if (pushHandler != null || pushHandler.isConnected()) {
            // 일단 retained = false로 세팅
            pushHandler.publish(topic, payload, qos, false);
        } else {
            throw new Exception("푸시핸들러 문제로 전송에 실패하였습니다");
        }
        DebugLog.d("publish 종료()");
    }

    /**
     * @param topic
     * @param qos
     * @throws Exception
     */
    @Override
    public void subscribe(String topic, int qos) throws Exception {
        DebugLog.d("subScribe 시작(토픽 = " + topic + ", qos = " + qos + ")");
        pushHandler.subscribe(topic, qos);
        //pushHandler.addSubscribeJob(topic);
        DebugLog.d("subscribe 종료()");
    }

    /**
     * @param topic
     * @throws Exception
     */
    @Override
    public void unsubscribe(String topic) throws Exception {
        DebugLog.d("unsubscribe 시작(토픽 = " + topic + ")");
        pushHandler.unsubscribe(topic);
        //pushHandler.addUnsubscribeJob(topic);
        DebugLog.d("unsubscribe 종료()");
    }

    /**
     * @param sender
     * @param topic
     * @throws Exception
     */
    @Override
    public void preCheck(String sender, String topic) throws Exception {
        DebugLog.d("preCheck 시작(sender = " + sender + ", 토픽 = " + topic + ")");
        pushHandler.preCheck(sender, topic);
        DebugLog.d("preCheck 종료()");
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    public String getSubscriptions() throws Exception {
        DebugLog.d("getSubscriptions 시작()");
        String ret = pushHandler.getSubscriptions();
        DebugLog.d("getSubscriptions 종료(return = " + ret + ")");
        return ret;
    }

    /*
     * (non-Javadoc)
     *
     * @see kr.co.adflow.push.service.PushService#isConnected()
     */
    @Override
    public boolean isConnected() {
        DebugLog.d("isConnected 시작()");
        boolean value = false;
        if (pushHandler != null) {
            value = pushHandler.isConnected();
        }
        DebugLog.d("isConnected 종료(value = " + value + ")");
        return value;
    }

    /**
     * @return
     */
    @Override
    public int getLostCount() {
        DebugLog.d("getLostCount 시작()");
        int lostCount = pushHandler.getLostCount();
        DebugLog.d("getLostCount 종료(lostCount = " + lostCount + ")");
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
        DebugLog.d("auth 시작(url = " + url + ", userID = " + userID + ", deviceID = "
                + deviceID + ")");
        String ret = pushHandler.auth(url, userID, deviceID);
        DebugLog.d("auth 종료(return = " + ret + ")");
        return ret;
    }

    public String sendMsgWithOpts(String sender, String receiver, int qos, String contentType, String content, int contentLength, int expiry, boolean mms) throws Exception {
        DebugLog.d("sendMsgWithOpts 시작(sender = " + sender + ", receiver = " + receiver + ", qos = "
                + qos + ", contentType = " + contentType + ", content = " + content + ", contentLength = " + contentLength + ", expiry = " + expiry + ", mms = " + mms + ")");
        String ret = pushHandler.sendMsgWithOpts(sender, receiver, qos, contentType, content, contentLength, expiry, mms);
        DebugLog.d("sendMsgWithOpts 종료(return = " + ret + ")");
        return ret;
    }

    private String updateUFMI(String phoneNum, String ufmi) throws Exception {
        DebugLog.d("updateUFMI 시작(phoneNum = " + phoneNum + ", ufmi = " + ufmi + ")");
        String ret = pushHandler.updateUFMI(phoneNum, ufmi);
        DebugLog.d("updateUFMI 종료(return = " + ret + ")");
        return ret;
    }

    public PushHandler getPushHandler() {
        return pushHandler;
    }
}
