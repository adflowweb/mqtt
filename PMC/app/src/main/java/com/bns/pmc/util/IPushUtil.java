package com.bns.pmc.util;

import android.os.RemoteException;

import kr.co.adflow.push.IPushService;

/**
 * adflow PMA와 연결 Class
 *
 * @author kyu
 */
public class IPushUtil {

    public static final int EXPIRY = 600; //사용자 메시지 기본 유지 시간 600초
    public static final int QOS = 2; //사용자 메시지 기본 QOS

    /**
     * MqttSession 연결상태 확인.
     *
     * @param binder
     * @return boolean
     */
    public static boolean isConnectedMqttSession(IPushService binder) {
        Log.d(PMCType.TAG, "MqttSession Connect");

        boolean bConnected = false;
        if (binder != null) {
            try {
                long start = System.currentTimeMillis();
                bConnected = binder.isConnected();
                long stop = System.currentTimeMillis();
                Log.d(PMCType.TAG, "MqttSession [Connect]" + bConnected +
                        " [Time]" + (stop - start) + "ms");
            } catch (RemoteException e) {
                Log.e(PMCType.TAG, "[Error]" + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Log.d(PMCType.TAG, "Disconnected Push Service");
        }

        return bConnected;
    }

    /**
     * Subscriptions 목록을 가져온다.
     *
     * @param binder
     * @return String
     */
    public static String getSubscriptions(IPushService binder) {
        Log.d(PMCType.TAG, "getSubscription start");

        String strResult = null;
        if (binder != null) {
            try {
                long start = System.currentTimeMillis();
                strResult = binder.getSubscriptions();
                long stop = System.currentTimeMillis();
                Log.d(PMCType.TAG, "[Result]" + strResult +
                        " [Time]" + (stop - start) + "ms");
            } catch (RemoteException e) {
                Log.e(PMCType.TAG, "[Error]" + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Log.d(PMCType.TAG, "Disconnected Push Service");
        }

        return strResult;
    }

    /**
     * subscribe
     *
     * @param binder
     * @param topic
     * @return String
     */
    public static String subscribe(IPushService binder, String topic) throws Exception {
        Log.d(PMCType.TAG, "subscribe시작(binder=" + binder + ", topic=" + topic + ")");
        long start = System.currentTimeMillis();
        String response = binder.subscribe(topic, 2);
        long stop = System.currentTimeMillis();
        Log.d(PMCType.TAG, "[Result]" + response +
                " [Time]" + (stop - start) + "ms");
        Log.d(PMCType.TAG, "subscribe종료(response=" + response + ")");
        return response;
    }

    /**
     * @param binder
     * @param topic
     * @return
     * @throws Exception
     */
    public static String unsubscribe(IPushService binder, String topic) throws Exception {
        Log.d(PMCType.TAG, "unsubscribe시작(binder=" + binder + ", topic=" + topic + ")");
        long start = System.currentTimeMillis();
        String response = binder.unsubscribe(topic);
        long stop = System.currentTimeMillis();
        Log.d(PMCType.TAG, "[Result]" + response +
                " [Time]" + (stop - start) + "ms");
        Log.d(PMCType.TAG, "unsubscribe종료(response=" + response + ")");
        return response;
    }

    /**
     * UFMI정보로 Push 메시지를 수신 가능한 대상인지 확인.
     * UFMI정보는 국가코드(82)를 포함한 형식.
     *
     * @param binder
     * @param ufmi
     * @return
     */
    public static String existPMAByUFMI(IPushService binder, String ufmi) {
        Log.i(PMCType.TAG, "existPMAByUFMI start " + ufmi);
        String strResult = null;
        if (binder != null) {
            try {
                long start = System.currentTimeMillis();
                strResult = binder.existPMAByUFMI(ufmi);
                long stop = System.currentTimeMillis();
                Log.d(PMCType.TAG, "[Result]" + strResult +
                        " [Time]" + (stop - start) + "ms");
            } catch (Exception e) {
                Log.e(PMCType.TAG, "[Error]" + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Log.d(PMCType.TAG, "Disconnected Push Service");
        }

        return strResult;
    }

    /**
     * 이동전화 번호로 Push 메시지를 수신 가능한 대상인지 확인.
     * 이동전화 정보는 국가코드(82)를 포함한 형식.
     *
     * @param binder
     * @param userID
     * @return String
     */
    public static String existPMAByUserID(IPushService binder, String userID) {
        Log.i(PMCType.TAG, "existPMAByUserID start " + userID);
        String strResult = null;
        if (binder != null) {
            try {
                long start = System.currentTimeMillis();
                strResult = binder.existPMAByUserID(userID);
                long stop = System.currentTimeMillis();
                Log.d(PMCType.TAG, "[Result]" + strResult +
                        " [Time]" + (stop - start) + "ms");
            } catch (Exception e) {
                Log.e(PMCType.TAG, "[Error]" + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Log.d(PMCType.TAG, "Disconnected Push Service");
        }

        return strResult;
    }

//    public static void registerPMC(IPushService binder) {
//        Log.i(PMCType.TAG, "registerPMC시작(binder=" + binder + ")");
//        if (binder != null) {
//            try {
//                binder.registerPMC();
//            } catch (RemoteException e) {
//                Log.e(PMCType.TAG, "PMC앱등록중에러발생=" + e);
//            }
//        }
//    }

    /**
     * 클라이언트 간 메시지 전송.
     *
     * @param binder
     * @param sender      발신대상토픽
     * @param receiver    수신 대상 토픽
     * @param contentType application/base64
     * @param content     base64로 인코딩 된 문자열
     * @return
     */
    public static String sendMsg(IPushService binder, String sender, String receiver,
                                 String contentType, String content, int contentLength, boolean mms) {
        Log.i(PMCType.TAG, "sendMsg시작(binder=" + binder + ", sender=" + sender + ", receiver=" + receiver + ", contentType=" + contentType + ", content=" + content + ", contentLength=" + contentLength + ", mms=" + mms + ")");

        String strResult = null;
        if (binder != null) {
            try {
                long start = System.currentTimeMillis();
                // mq publish를 rest로 변경
                //strResult = binder.sendMsg(sender, receiver, contentType, content);
                strResult = binder.sendMsgWithOpts(sender, receiver, QOS, contentType, content, contentLength, EXPIRY, mms);


                long stop = System.currentTimeMillis();
                Log.d(PMCType.TAG, "[Result]" + strResult +
                        " [Time]" + (stop - start) + "ms");
            } catch (RemoteException e) {
                Log.e(PMCType.TAG, "[Error]" + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Log.d(PMCType.TAG, "Disconnected Push Service");
        }
        Log.i(PMCType.TAG, "sendMsg종료(result=" + strResult + ")");
        return strResult;
    }

    /**
     * 개별 앱에서 특정 메시지에 대한 수신확인.
     *
     * @param binder
     * @param msgID
     * @param tokenID
     * @return string
     */
    public static String ack(IPushService binder, String msgID, String tokenID) {
        Log.i(PMCType.TAG, "=== Ack Start ===");
        Log.i(PMCType.TAG, "msgID= " + msgID + " tokenID= " + tokenID);

        String strResult = null;
        if (binder != null) {
            try {
                long start = System.currentTimeMillis();
                strResult = binder.ack(msgID, tokenID);
                long stop = System.currentTimeMillis();
                Log.d(PMCType.TAG, "[Result]" + strResult +
                        " [Time]" + (stop - start) + "ms");
            } catch (RemoteException e) {
                Log.e(PMCType.TAG, "[Error]" + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Log.d(PMCType.TAG, "Disconnected Push Service");
        }

        return strResult;
    }

    public static String getGrpSubscribers(IPushService binder, String topic) {
        Log.i(PMCType.TAG, "binder=" + binder + ",topic= " + topic);
        String strResult = null;
        if (binder != null) {
            try {
                long start = System.currentTimeMillis();
                strResult = binder.getGrpSubscribers(topic);
                long stop = System.currentTimeMillis();
                Log.d(PMCType.TAG, "[Result]" + strResult +
                        " [Time]" + (stop - start) + "ms");
            } catch (RemoteException e) {
                Log.e(PMCType.TAG, "[Error]" + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Log.d(PMCType.TAG, "Disconnected Push Service");
        }

        return strResult;
    }

    /**
     * @param binder
     * @return
     */
    public static String getToken(IPushService binder) {
        Log.i(PMCType.TAG, "binder=" + binder);
        String strResult = null;
        if (binder != null) {
            try {
                long start = System.currentTimeMillis();
                strResult = binder.getToken();
                long stop = System.currentTimeMillis();
                Log.d(PMCType.TAG, "[Result]" + strResult +
                        " [Time]" + (stop - start) + "ms");
            } catch (RemoteException e) {
                Log.e(PMCType.TAG, "[Error]" + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Log.d(PMCType.TAG, "Disconnected Push Service");
        }

        return strResult;
    }
}
