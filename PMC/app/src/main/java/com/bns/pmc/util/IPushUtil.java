package com.bns.pmc.util;

import android.os.RemoteException;

import kr.co.adflow.push.IPushService;

/**
 * adflow PMA와 연결 Class
 *
 * @author kyu
 */
public class IPushUtil {
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
    public static String subscribe(IPushService binder, String topic) {
        Log.d(PMCType.TAG, "Subscribe start");

        String strResult = null;
        if (binder != null) {
            try {
                long start = System.currentTimeMillis();
                strResult = binder.subscribe(topic, 2);
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
     * unsubscribe
     *
     * @param binder
     * @param topic
     * @return String
     */
    public static String UnSubscribe(IPushService binder, String topic) {
        Log.d(PMCType.TAG, "UnSubscribe start");

        String strResult = null;
        if (binder != null) {
            try {
                long start = System.currentTimeMillis();
                strResult = binder.unsubscribe(topic);
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
                                 String contentType, String content) {
        Log.i(PMCType.TAG, "=== SendMsg start ===");

        String strResult = null;
        if (binder != null) {
            try {
                long start = System.currentTimeMillis();
                // mq publish를 rest로 변경
                //strResult = binder.sendMsg(sender, receiver, contentType, content);
                strResult = binder.sendMsgWithOpts(sender, receiver, 2, contentType, content, 600);


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
}
