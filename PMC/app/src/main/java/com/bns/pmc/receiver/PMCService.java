package com.bns.pmc.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.bns.pmc.MainActivity;
import com.bns.pmc.NotiPopupActivity;
import com.bns.pmc.R;
import com.bns.pmc.TalkActivity;
import com.bns.pmc.provider.DataColumn;
import com.bns.pmc.provider.DataColumn.MessageColumn;
import com.bns.pmc.util.CommonUtil;
import com.bns.pmc.util.CommonUtil.JSonItem;
import com.bns.pmc.util.Configure;
import com.bns.pmc.util.IPushUtil;
import com.bns.pmc.util.JSonUtil;
import com.bns.pmc.util.Log;
import com.bns.pmc.util.PMCType;
import com.bns.pmc.util.PttWakeUpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.adflow.push.IPushService;

public class PMCService extends Service {
    public static IPushService m_Binder = null;
    private static boolean m_bPttCall = false;

    private Context m_context;
    private Configure m_configure;
    private Thread m_accountThread = null;
    private Thread m_mqttThread = null;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(PMCType.TAG, "[onCreate]");

        m_context = getBaseContext();
        m_configure = Configure.getInstance(m_context);

        IntentFilter intentF = new IntentFilter();
        intentF.addAction(PMCType.BNS_PMC_PTT_CALL_START_END_MODE_ACTION);
        intentF.addAction(PMCType.UNI_PMC_PTT_SET_PTT_NUMBER_ACTION);
        intentF.addAction(PMCType.BNS_PMC_PTT_REG_STATE_ACTION_LOGOUT);
        intentF.addAction(PMCType.BNS_PMC_KTP_PUSH_USERMSG);
        intentF.addAction(PMCType.BNS_PMC_KTP_PUSH_MMS);
        intentF.addAction(PMCType.BNS_PMC_KTP_PUSH_FWUPDATE_INFO);
        intentF.addAction(PMCType.BNS_PMC_KTP_PUSH_CONN_STATE);
        intentF.addAction(PMCType.BNS_PMC_KTP_PUSH_DIG);
        registerReceiver(m_PMCServiceReceiver, intentF);

        boolean bStatus = ServiceCaller.getInstance().isCall(m_context);
        Log.d(PMCType.TAG, "Service Status= " + bStatus);
        if (bStatus == false) {
            ServiceCaller.getInstance().startCall(m_context);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(PMCType.TAG, "[onDestroy]");

        if (m_accountThread != null && m_accountThread.isAlive()) {
            Log.e(PMCType.TAG, "accountThread interrupt");
            m_accountThread.interrupt();
        }

        if (m_mqttThread != null && m_mqttThread.isAlive()) {
            Log.e(PMCType.TAG, "mqttThread interrupt");
            m_mqttThread.interrupt();
        }

        disconnectPushService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.e(PMCType.TAG, "[onStartCommand]= " + flags);
        if (intent == null) {
            Log.e(PMCType.TAG, "Intent is null");
            return Service.START_STICKY;
        }

        String action = intent.getAction();
        if (action.equals(PMCType.BNS_PMC_SERVICE_START_ACTION)) {
            connectPushService();
        } else if (action.equals(PMCType.BNS_PMC_SERVICE_REPEAT_ACTION)) {
            if (m_Binder == null) {
                connectPushService();
            }
            return Service.START_STICKY;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 푸시서비스 연결
     *
     * @return
     */
    private boolean connectPushService() {
        Log.d(PMCType.TAG, "=== Start ===");
        try {
            Intent explicitIntent = null;
            // Package 설치여부 확인
            PackageManager pm = getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(PMCType.BNS_PMC_ADFLOW_PUSH_PACKAGE, PackageManager.GET_META_DATA);
            Log.d(PMCType.TAG, "ApplicationInfo= " + appInfo);

            Intent implicitIntent = new Intent(PMCType.BNS_PMC_ADFLOW_PUSH_SERVICE_PUSHSERVICE);
            List<ResolveInfo> resolveInfos = pm.queryIntentServices(implicitIntent, 0);
            Log.d(PMCType.TAG, "ResolveInfo= " + resolveInfos);

            ResolveInfo resolveInfo = resolveInfos.get(0);
            String packageName = resolveInfo.serviceInfo.packageName;
            Log.d(PMCType.TAG, "PackageName= " + packageName);
            String className = resolveInfo.serviceInfo.name;
            Log.d(PMCType.TAG, "ClassName= " + className);
            ComponentName component = new ComponentName(packageName, className);
            explicitIntent = new Intent();
            explicitIntent.setComponent(component);
            Log.d(PMCType.TAG, "ExplicitIntent= " + explicitIntent);
            bindService(explicitIntent, m_Connection, Context.BIND_AUTO_CREATE);
            Log.d(PMCType.TAG, "PushService Bind");
            Log.d(PMCType.TAG, "=== End ===");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 푸시서비스 연결 해제
     *
     * @return
     */
    private boolean disconnectPushService() {
        Log.d(PMCType.TAG, "=== Start ===");
        if (m_Binder != null) {
            try {
                unbindService(m_Connection);
                Log.d(PMCType.TAG, "PushService unBind");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                m_Binder = null;
            }
        } else {
            Log.d(PMCType.TAG, "Disconnected Push Service");
        }
        Log.d(PMCType.TAG, "=== End ===");

        return true;
    }

    /**
     * subscribe.
     *
     * @param subscribe
     * @return boolean
     */
    private boolean subscribe(String subscribe) {
        IPushService binder = PMCService.m_Binder;

        if (binder != null && subscribe != null) {
            // MqttSession 연결상태 확인.
            boolean bIsConnected = IPushUtil.isConnectedMqttSession(binder);

            if (bIsConnected) {
                // MqttSession 연결 상태가 True.
                // Subscriptions 목록을 불러온다.
                String strSubscriptions_Json = IPushUtil.getSubscriptions(binder);
                boolean bSubscriptions_Success = false;
                ArrayList<String> listSubscriptions_Data = null;

                if (TextUtils.isEmpty(strSubscriptions_Json) == false) {
                    // Subscriptions Result값이 Null이 아닌경우.
                    // Success값을 불러온다.
                    bSubscriptions_Success = JSonUtil.responeSubscriptionsResultSuccess(strSubscriptions_Json);
                }

                if (bSubscriptions_Success) {
                    // Subscriptions Result Success값이 true인 경우.
                    // Data list값을 불러온다.
                    listSubscriptions_Data = JSonUtil.responeSubscriptionsResultData(strSubscriptions_Json);

                    if (listSubscriptions_Data != null) {

                        // Subscriptions에 포함되어 있는지 확인.
                        boolean bContains = JSonUtil.isSubscriptions(listSubscriptions_Data, subscribe);

                        if (bContains) {
                            // 등록된 서브스크라이브일 경우.
                            Log.d(PMCType.TAG, "Contains Save= " + subscribe);
                            return true;
                        } else {
                            // 등록된 서브스크라이브가 아닐경우.
                            String strResult = IPushUtil.subscribe(binder, subscribe);
                            if (TextUtils.isEmpty(strResult) == false) {
                                boolean bResult = JSonUtil.responeSubscribeResultSuccess(strResult);

                                if (bResult) {
                                    Log.d(PMCType.TAG, "Regist Save= " + subscribe);
                                    return true;
                                }
                            }
                        }
                    } // end listSubscriptions_Data
                } // end bSubscriptions_Success
            } // end bIsConnected
        } else {
            Log.d(PMCType.TAG, "Disconnected Push Service");
        }

        return false;
    }

    /**
     * unSubscribe.
     *
     * @param subscribe
     * @return boolean
     */
    private boolean unSubscribe(String subscribe) {
        IPushService binder = PMCService.m_Binder;

        if (binder != null) {
            // MqttSession 연결상태 확인.
            boolean bIsConnected = IPushUtil.isConnectedMqttSession(binder);

            if (bIsConnected) {
                // MqttSession 연결 상태가 True.
                String strResult = IPushUtil.UnSubscribe(binder, subscribe);
                if (TextUtils.isEmpty(strResult) == false) {
                    boolean bResult = JSonUtil.responeSubscribeResultSuccess(strResult);

                    if (bResult) {
                        Log.d(PMCType.TAG, "UnSubscribe= " + subscribe);
                        return true;
                    }
                }
            }
        } else {
            Log.d(PMCType.TAG, "Disconnected Push Service");
        }

        return false;
    }

    /**
     * Notification
     *
     * @param title
     * @param sender
     * @param content
     * @param control
     * @return int
     */
    private int createNotification(String title, String sender, String content, boolean control) {
        int _id = PMCType.BNS_PMC_NOTI_ID;
        /**
         * FLAG_CANCEL_CURRENT : 이전에 생성한 PendingIntent 는 취소하고 새롭게 만든다.
         * FLAG_NO_CREATE : 생성된 PendingIntent를 반환한다. 재사용 가능하다.
         * FLAG_ONE_SHOT : 이 flag 로 생성한 PendingIntent 는 일회용이다.
         * FLAG_UPDATE_CURRENT : 이미 생성된 PendingIntent 가 존재하면 해당 Intent 의 내용을 변경한다.
         */

        int total = 0;
        Cursor cursor = getContentResolver().query(DataColumn.CONTENT_URI_LIST_TOTAL_DONT_READ, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                total = cursor.getInt(cursor.getColumnIndex(MessageColumn.DB_COLUMN_SUM_DONT_READ));
            }
            cursor.close();
        }

        Intent intent = new Intent(m_context, MainActivity.class);
        intent.setAction(PMCType.BNS_PMC_PMCSERVICE_NOTIFICATION);
        intent.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_NUMBER, sender);
        intent.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_CONTROL, control);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        NotificationManager nm = (NotificationManager) m_context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();
        PendingIntent pendingIntent = PendingIntent.getActivity(m_context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder = new Notification.Builder(m_context);

        mBuilder.setSmallIcon(R.drawable.notice_icon);
        mBuilder.setTicker(title + ":" + content);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setNumber(total);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(content);

        Boolean bVibrate = m_configure.getVibrateNoti();
        Boolean bSoundNoti = m_configure.getSountNoti();
        if (bSoundNoti) {
            String strSound = m_configure.getSoundData();
            Uri uri = Uri.parse(strSound);
            mBuilder.setSound(uri);
        }
        if (bVibrate)
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        //mBuilder.setSound(uri);
        // mBuilder.setDefaults(/*Notification.DEFAULT_SOUND |*/ Notification.DEFAULT_VIBRATE);
        //mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);

        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);

        nm.notify(sender, _id, mBuilder.build());
        //nm.notify(_id, mBuilder.build());

        return _id;
    }

    /**
     * Build Model Subscribe.
     *
     * @param ver
     * @return String
     */
    private String getSubscribeString_BuildModel(int ver) {
        String strBuildModel = Build.MODEL;
        Log.i(PMCType.TAG, "BuildModel= " + strBuildModel);

        String strSubscribe = CommonUtil.conv_BuildModelToMMS(strBuildModel, ver);
        if (TextUtils.isEmpty(strSubscribe) == false)
            return strSubscribe;

        return null;
    }

    /**
     * PhoneNumber Subscribe.
     *
     * @param ver
     * @return String
     */
    private String getSubscribeString_MyPhoneNumber() {
        TelephonyManager telManager = (TelephonyManager) m_context.getSystemService(Context.TELEPHONY_SERVICE);
        String strPhoneNum = telManager.getLine1Number();
        Log.i(PMCType.TAG, "PhoneNumber= " + strPhoneNum);

        String strSubscribe = CommonUtil.conv_PhoneNumberToMMS(strPhoneNum);
        if (TextUtils.isEmpty(strSubscribe) == false)
            return strSubscribe;

        return null;
    }

    /**
     * Urban Subscribe.
     *
     * @param ufmi
     * @param ver
     * @return String
     */
    private String getSubscribeString_Urban(String ufmi, int ver) {
        String strSubscribe = CommonUtil.conv_UrbanToMMS(ufmi, ver);
        if (TextUtils.isEmpty(strSubscribe) == false)
            return strSubscribe;

        return null;
    }

    /**
     * UFMI Subscribe.
     *
     * @param ufmi
     * @param ver
     * @return String
     */
    private String getSubscribeString_UFMI(String ufmi, int ver) {
        Log.i(PMCType.TAG, "UFMI= " + ufmi);
        // Full UFMI를 MMS 형식으로 변환.
        // ex) 82*200*123 -> mms/P1/82/200/p123
        String strSubscribe = CommonUtil.conv_UFMIToMMS(ufmi, ver);
        if (TextUtils.isEmpty(strSubscribe) == false)
            return strSubscribe;

        return null;
    }

    /**
     * @param groupList
     * @param ufmi
     * @param bunchID
     * @param ver
     * @return ArrayList<String>
     */
    private ArrayList<String> getSubscribeString_Group(String groupList, String ufmi, String bunchID, int ver) {
        ArrayList<String> list = new ArrayList<String>();
        String[] arr = CommonUtil.conv_GroupListToGroup(groupList);
        for (int i = 0; i < arr.length; i++) {
            Log.i(PMCType.TAG, "Group= " + arr[i]);
            String strSubscribe = CommonUtil.conv_GroupToMMS(arr[i], ufmi, bunchID, ver);
            if (TextUtils.isEmpty(strSubscribe) == false)
                list.add(strSubscribe);
        }

        return list;
    }

    /**
     * MMS로 Subscribe된 목록 불러옴.
     *
     * @return ArrayList<String>
     */
    private ArrayList<String> getMMSSubscribes() {
        ArrayList<String> list = new ArrayList<String>();
        IPushService binder = PMCService.m_Binder;
        boolean bIsConnected = IPushUtil.isConnectedMqttSession(binder);

        if (bIsConnected) {
            String strSubscriptions_Json = IPushUtil.getSubscriptions(binder);
            boolean bSubscriptions_Success = false;
            ArrayList<String> listSubscriptions_Data = null;

            if (TextUtils.isEmpty(strSubscriptions_Json) == false) {
                // Subscriptions Result값이 Null이 아닌경우.
                // Success값을 불러온다.
                bSubscriptions_Success = JSonUtil.responeSubscriptionsResultSuccess(strSubscriptions_Json);
            }

            if (bSubscriptions_Success) {
                // Subscriptions Result Success값이 true인 경우.
                // Data list값을 불러온다.
                listSubscriptions_Data = JSonUtil.responeSubscriptionsResultData(strSubscriptions_Json);

                if (listSubscriptions_Data != null) {
                    for (int i = 0; i < listSubscriptions_Data.size(); i++) {
                        String strData = listSubscriptions_Data.get(i);
                        if (strData.startsWith("mms", 0)) {
                            list.add(strData);
                        }
                    }
                }
            }
        } else {
            Log.d(PMCType.TAG, "getMMSSubscribes Mqtt Session connected False");
        }

        return list;
    }

    /**
     * processReceivedPttNumber
     *
     * @param intent
     */
    private void processReceivedPttNumberAction(Intent intent) {
        Log.d("PMCService", "processReceivedPttNumberAction시작(intent=" + intent + ")");

        Bundle bundle = intent.getExtras();
        final String strFullUfmi = bundle.getString(PMCType.UNI_PMC_PTT_PTTFULLNUMBER_EXTRA_VALUE);
        final String strGroupList = bundle.getString(PMCType.UNI_PMC_PTT_PTTGROUP_EXTRA_VALUE);
        final String strBunchid = bundle.getString(PMCType.UNI_PMC_PTT_BUNCHID_EXTRA_VALUE);
        final int nPttVersion = bundle.getInt(PMCType.UNI_PMC_PTT_VERSION_EXTRA_VALUE);

        Log.i(PMCType.TAG, "Ptt FullNum: " + strFullUfmi + " Version: " + nPttVersion);
        Log.i(PMCType.TAG, "Group: " + strGroupList + " bunch: " + strBunchid);

        final String strSavedUFMI = m_configure.getUFMI();
        final String strSavedGroupList = m_configure.getGroupList();
        final String strSavedBunchid = m_configure.getBunchID();
        final int nSavedPttVersion = m_configure.getVersion();

        if (m_accountThread != null && m_accountThread.isAlive()) {
            Log.e(PMCType.TAG, "accountThread interrupt");
            m_accountThread.interrupt();
        }

        m_accountThread = new Thread(new Runnable() {

            @Override
            public void run() {


                IPushService binder = null;
                boolean bIsBinder = true;
                boolean bIsConnected = false;
                try {
                    while (bIsBinder) {
                        binder = PMCService.m_Binder;
                        if (binder == null) {
                            Log.i(PMCType.TAG, "Binder Fail");
                            Thread.sleep(3000);
                        } else {
                            bIsBinder = false;
                        }
                    }
                } catch (InterruptedException e) {
                    Log.e(PMCType.TAG, "Error= " + e.getMessage());
                    e.printStackTrace();
                }

                try {
                    while (!bIsConnected) {
                        bIsConnected = IPushUtil.isConnectedMqttSession(binder);
                        if (bIsConnected == false) {
                            Log.i(PMCType.TAG, "Mqtt Connection Fail");
                            Thread.sleep(3000);
                        }
                    }
                } catch (InterruptedException e) {
                    Log.e(PMCType.TAG, "Error= " + e.getMessage());
                    e.printStackTrace();
                }

                //testCode
                Log.d(PMCType.TAG, "strSavedUFMI=" + strSavedUFMI);
                Log.d(PMCType.TAG, "strFullUfmi=" + strFullUfmi);
                Log.d(PMCType.TAG, "strSavedGroupList=" + strSavedGroupList);
                Log.d(PMCType.TAG, "strGroupList=" + strGroupList);
                Log.d(PMCType.TAG, "strBunchid=" + strBunchid);
                Log.d(PMCType.TAG, "strSavedBunchid=" + strSavedBunchid);
                Log.d(PMCType.TAG, "nPttVersion=" + nPttVersion);
                Log.d(PMCType.TAG, "nSavedPttVersion=" + nSavedPttVersion);
                //testCodeEnd

                // 계정 정보 변경.
                if (strSavedUFMI.compareToIgnoreCase(strFullUfmi) != 0 ||
                        CommonUtil.comp_GroupList(strSavedGroupList, strGroupList) == false ||
                        strBunchid.compareToIgnoreCase(strSavedBunchid) != 0 ||
                        nPttVersion != nSavedPttVersion) {
                    Log.d(PMCType.TAG, "계정정보변경작업시작!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    // Valid DB 초기화.
                    Log.i(PMCType.TAG, "== Valid DB Init ==");
                    getContentResolver().delete(DataColumn.CONTENT_URI_DEL_VALID_ALL, null, null);
                    // UnSubscribe.
                    {
                        // 15.04.02 일반번호는 Pass 시킴.
                        {
                            String strNormalNum = getSubscribeString_MyPhoneNumber();

                            // MMS로 Subscribe 된 목록 불러옴.
                            Log.i(PMCType.TAG, "== unSubscribe Start ==");
                            ArrayList<String> listUnSubsc = getMMSSubscribes();
                            for (int i = 0; i < listUnSubsc.size(); i++) {
                                boolean bResult = false;
                                String strUnSubscribe = listUnSubsc.get(i);
                                if (strUnSubscribe.compareToIgnoreCase(strNormalNum) != 0) {
                                    try {
                                        while (!bResult) {
                                            bResult = unSubscribe(strUnSubscribe);
                                            if (bResult == false)
                                                Thread.sleep(3000);
                                        }
                                    } catch (InterruptedException e) {
                                        Log.e(PMCType.TAG, "Error= " + e.getMessage());
                                        e.printStackTrace();
                                    }
                                }
                            }
                            Log.i(PMCType.TAG, "== unSubscribe End ==");
                        }
                        /*{
                            // MMS로 Subscribe 된 목록 불러옴.
							Log.i(PMCType.TAG, "== unSubscribe Start ==");
							ArrayList<String> listUnSubsc = getMMSSubscribes();								
							for (int i=0; i<listUnSubsc.size(); i++) {
								boolean bResult = false;
								String strUnSubscribe = listUnSubsc.get(i);
								try {
									while (!bResult) {
										bResult = unSubscribe(strUnSubscribe);
										if (bResult == false)
											Thread.sleep(3000);
									}
								} catch (InterruptedException e) {
									Log.e(PMCType.TAG, "Error= "+e.getMessage());
									e.printStackTrace();
								}
							}
							Log.i(PMCType.TAG, "== unSubscribe End ==");
						}*/
                    }
                    // Subscribe.
                    {
                        ArrayList<String> listSubsc = new ArrayList<String>();
                        if (nPttVersion != PMCType.PMC_PTT_MODE_NONE) {
                            Log.i(PMCType.TAG, "== Subscribe Start ==");
                            // 단말모델 등록.
                            {
                                String str = getSubscribeString_BuildModel(nPttVersion);
                                if (TextUtils.isEmpty(str) == false)
                                    listSubsc.add(str);
                                else
                                    Log.i(PMCType.TAG, "Subscribe BuildModel NULL");
                            }
                            // 15.04.02 일반번호는 Pass 시킴.
                            /*// Phone 번호 등록.
                            {
								String str = getSubscribeString_MyPhoneNumber();
								if (TextUtils.isEmpty(str) == false)
									listSubsc.add(str);
								else
									Log.i(PMCType.TAG, "Subscribe MyPhoneNumber NULL");
							}*/

                            // Urban or 확장번호.
                            if (TextUtils.isEmpty(strFullUfmi) == false) {
                                String strSubsUrban = getSubscribeString_Urban(strFullUfmi, nPttVersion);
                                if (TextUtils.isEmpty(strSubsUrban) == false)
                                    listSubsc.add(strSubsUrban);
                                else
                                    Log.i(PMCType.TAG, "Subscribe Urban NULL");
                            }
                            // UFMI.
                            if (TextUtils.isEmpty(strFullUfmi) == false) {
                                String strSubsUFMI = getSubscribeString_UFMI(strFullUfmi, nPttVersion);
                                if (TextUtils.isEmpty(strSubsUFMI) == false)
                                    listSubsc.add(strSubsUFMI);
                                else
                                    Log.i(PMCType.TAG, "Subscribe UFMI NULL");
                            }
                            // Group.
                            if (TextUtils.isEmpty(strGroupList) == false) {
                                if (strGroupList.compareToIgnoreCase("noGroupList") != 0) {
                                    ArrayList<String> groupList = getSubscribeString_Group(strGroupList, strFullUfmi, strBunchid, nPttVersion);
                                    listSubsc.addAll(groupList);
                                }
                            }

                            for (int i = 0; i < listSubsc.size(); i++) {
                                boolean bResult = false;
                                String strSubscribe = listSubsc.get(i);

                                try {
                                    while (!bResult) {
                                        bResult = subscribe(strSubscribe);
                                        if (bResult == false)
                                            Thread.sleep(3000);
                                    }
                                } catch (InterruptedException e) {
                                    Log.d(PMCType.TAG, "Error= " + e.getMessage());
                                    e.printStackTrace();
                                }
                            }
                            Log.i(PMCType.TAG, "== Subscribe End ==");
                        }
                    }

                    // Configure.
                    {
                        Log.i(PMCType.TAG, "==Change Account Info==");
                        Log.i(PMCType.TAG, "UFMI= " + strFullUfmi + " Group= " + strGroupList +
                                " Bunch= " + strBunchid + " Ver= " + nPttVersion);
                        m_configure.setUFMI(strFullUfmi);
                        m_configure.setGroupList(strGroupList);
                        m_configure.setBunchID(strBunchid);
                        m_configure.setVersion(nPttVersion);
                        m_configure.saveSharedPreferences();
                    }
                } else {
                    Log.d(PMCType.TAG, "계정정보가변경되지않음!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    Log.i(PMCType.TAG, "==Account Info==");
                    Log.i(PMCType.TAG, "UFMI= " + m_configure.getUFMI() + " Group= " + m_configure.getGroupList() +
                            " Bunch= " + m_configure.getBunchID() + " Ver= " + m_configure.getVersion());
                }

                //testCode ??
                // 여기서 왜 서브스크립션을 가져오나 ???
                //ArrayList<String> listMMS = getMMSSubscribes();
                //for (int i = 0; i < listMMS.size(); i++)
                //   Log.i(PMCType.TAG, listMMS.get(i));

                Log.i(PMCType.TAG, "== Regist Subscribe Complete ==");
            }
        });

        m_accountThread.start();

        Log.d("PMCService", "processReceivedPttNumberAction종료()");
    }

    /**
     * processReceivedPttLogInOutAction
     *
     * @param intent
     */
    private void processReceivedPttLogInOutAction(Intent intent) {
        boolean flag = intent.getBooleanExtra(PMCType.BNS_PMC_PTT_REG_STATE_EXTRA_VALUE, false);
        Log.i(PMCType.TAG, "Ptt LogInOut State= " + flag);
    }

    /**
     * PMA에서 받은 User Message 처리
     *
     * @param strBase64
     */
    private void processReceivedUserMsg(Intent intent) {
        new asyncProcessUserMsg().execute(intent);
    }

    /**
     * PMA에서 받은 Control Message 처리
     *
     * @param intent
     */
    private void processReceivedControlMsg(Intent intent) {
        new asyncProcessControlMsg().execute(intent);
    }

    /**
     * PMA에서 받은 FW Message 처리
     *
     * @param intent
     */
    private void processReceivedFWMsg(Intent intent) {
        new asyncProcessControlMsg().execute(intent);
    }

    /**
     * PMA에서 받은 MQTT 세션 Message 처리
     *
     * @param intent
     */
    private void processReceivedMQTTMsg(Intent intent) {
        int nEventCode = intent.getIntExtra(PMCType.BNS_PMC_MQTT_INTENT_EXTRA_EVENTCODE, 0);
        String strEventMsg = intent.getStringExtra(PMCType.BNS_PMC_MQTT_INTENT_EXTRA_EVENTMSG);
        String strEventInfo = intent.getStringExtra(PMCType.BNS_PMC_MQTT_INTENT_EXTRA_EVENTINFO);
        Log.i(PMCType.TAG, "Event Code=" + nEventCode + " Msg=" + strEventMsg + " Info=" + strEventInfo);

        if (m_mqttThread != null && m_mqttThread.isAlive()) {
            Log.e(PMCType.TAG, "mqttThread interrupt");
            m_mqttThread.interrupt();
        }

        switch (nEventCode) {
            case PMCType.BNS_PMC_MQTT_EVENT_CODE_CONN_START_SUCCESS:
            case PMCType.BNS_PMC_MQTT_EVENT_CODE_CONN_SUCCESS:
                // 연결 성공
            {
                m_mqttThread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        IPushService binder = PMCService.m_Binder;

                        Cursor c = getContentResolver().query(DataColumn.CONTENT_URI_TALK_ACK_BY_READ, null, null, null, null);

                        if (c != null) {
                            while (c.moveToNext()) {
                                long id = c.getLong(c.getColumnIndex(MessageColumn.DB_COLUMN_ID));
                                String strMsgID = c.getString(c.getColumnIndex(MessageColumn.DB_COLUMN_MSG_ID));
                                String strToken = c.getString(c.getColumnIndex(MessageColumn.DB_COLUMN_TOKEN));
                                String strResult = IPushUtil.ack(binder, strMsgID, strToken);

                                if (TextUtils.isEmpty(strResult) == false) {
                                    boolean bResultSuccess = JSonUtil.responeAckResultSuccess(strResult);

                                    if (bResultSuccess) {
                                        Uri uri = Uri.withAppendedPath(DataColumn.CONTENT_URI_UPDATE_ACK_BY_ID, Long.toString(id));
                                        getContentResolver().update(uri, null, null, null);
                                    }
                                }
                            }
                            c.close();
                        }
                    }
                });

                m_mqttThread.start();
            }
            break;
            case PMCType.BNS_PMC_MQTT_EVENT_CODE_CONN_FAIL:
            case PMCType.BNS_PMC_MQTT_EVENT_CODE_SIM_DONT_EXIST:
            case PMCType.BNS_PMC_MQTT_EVENT_CODE_SIM_CHG:
            default:
                break;
        }
    }

    /**
     * PMA에서 받은 DIG 계정정보 처리.
     *
     * @param intent
     */
    private void processReceivedDIGMsg(Intent intent) {
        new asyncProcessDIGMsg().execute(intent);
    }

    private boolean processAck(String msgID, String token) {
        IPushService binder = PMCService.m_Binder;
        // 15.04.02 무조건 Ack 전송.. PMA가 처리하도록 변경.
        /*{
            boolean bIsConnected = IPushUtil.isConnectedMqttSession(binder);
			
			if (bIsConnected == false)
				return false;
						
			String strResult = IPushUtil.ack(binder, msgID, token);
			
			if (TextUtils.isEmpty(strResult) == false) {
				boolean bResultSuccess = JSonUtil.responeAckResultSuccess(strResult);
				
				if (bResultSuccess) {
					return true;
				} 
			}
		}*/

        {
            IPushUtil.ack(binder, msgID, token);
        }

        //return false;
        return true;
    }

    /**
     * MMS User 데이터 async 처리
     *
     * @author kyu
     */
    private class asyncProcessUserMsg extends AsyncTask<Intent, Integer, JSonItem> {
        /**
         * 이곳에 포함된 code는 AsyncTask가 execute 되자 마자 UI 스레드에서 실행됨.
         * 작업 시작을 UI에 표현하거나
         * background 작업을 위한 ProgressBar를 보여 주는 등의 코드를 작성.
         */
        @Override
        protected void onPreExecute() {
            Log.d(PMCType.TAG, "onPreExecute 작업 시작");
            super.onPreExecute();
        }

        /**
         * UI 스레드에서 AsynchTask객체.execute(...) 명령으로 실행되는 callback
         */
        @Override
        protected JSonItem doInBackground(Intent... params) {
            Log.d(PMCType.TAG, "doInBackground Start");

            Intent intent = params[0];
            int nAck = intent.getIntExtra(PMCType.BNS_PMC_MMS_RECV_ACK, 0);
            String strMsgID = intent.getStringExtra(PMCType.BNS_PMC_MMS_RECV_MSG_ID);
            String strToken = intent.getStringExtra(PMCType.BNS_PMC_MMS_RECV_TOKEN);
            String strSender = intent.getStringExtra(PMCType.BNS_PMC_MMS_RECV_SENDER);
            String strReceiver = intent.getStringExtra(PMCType.BNS_PMC_MMS_RECV_RECEIVER);
            String strContentType = intent.getStringExtra(PMCType.BNS_PMC_MMS_RECV_CONTENT_TYPE);
            String strContent = intent.getStringExtra(PMCType.BNS_PMC_MMS_RECV_CONTENT);
            int nResendCount = intent.getIntExtra(PMCType.BNS_PMC_MMS_RECV_RESEND_COUNT, 0);

            Log.i(PMCType.TAG, "[MsgID]" + strMsgID + " [Token]" + strToken +
                    " [Ack]" + nAck + " [resendCnt]" + nResendCount);
            Log.i(PMCType.TAG, "[Send]" + strSender + " [Recv]" + strReceiver + " [ContentType]"
                    + strContentType);
            Log.i(PMCType.TAG, "[Content]" + strContent);

            JSonItem item = null;
            if (TextUtils.isEmpty(strContent) == false) {
                String strDecode = CommonUtil.decodeBase64ToString(strContent);
                //Log.i(PMCType.TAG, "== " + strDecode);
                item = JSonUtil.decodeJSONStringToData(strDecode);

                if (item != null) {
                    String strItemSender = item.m_strSender;
                    String strItemReceiver = item.m_strReceiver;
                    Log.i(PMCType.TAG, "JSonItem Sender=" + strItemSender + " Receiver=" + strItemReceiver);

                    String strMyUFMI = m_configure.getUFMI();
                    int nNumberType = DataColumn.COLUMN_NUMBER_TYPE_PTT;
                    {
                        if (CommonUtil.checkPttGroupNumFormat(strItemReceiver)) {
                            nNumberType = DataColumn.COLUMN_NUMBER_TYPE_GROUP;
                            //testCode
                            //Log.i(PMCType.TAG, " Group 이면서 Sender가 자기자신이면 메시지 무시=" + strMyUFMI.compareToIgnoreCase(strItemSender));
                            //testCodeEnd
                            if (strMyUFMI.compareToIgnoreCase(strItemSender) == 0)
                                // Group 이면서 Sender가 자기자신이면 메시지 무시.
                                return item;
                        } else if (CommonUtil.checkPttCallNumFormat(strItemReceiver)) {
                            nNumberType = DataColumn.COLUMN_NUMBER_TYPE_PTT;
                        } else {
                            nNumberType = DataColumn.COLUMN_NUMBER_TYPE_NORMAL;
                        }
                    }

                    // DB용량이 Max 상태이면 제일 처음 DB를 지운다.
                    {
                        int nDBSaveMax = m_configure.getMessageSaveCount();
                        int nDBCount = -1;
                        Cursor cursor = getContentResolver().query(DataColumn.CONTENT_URI_LIST_TOTAL_COUNT, null, null, null, null);
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                nDBCount = cursor.getInt(cursor.getColumnIndex(MessageColumn.DB_COLUMN_COUNT_ALL));
                                Log.i(PMCType.TAG, "DBCount= " + nDBCount);
                            }
                            if (nDBCount >= nDBSaveMax) {
                                Log.i(PMCType.TAG, "DBMax= " + nDBSaveMax);
                                getContentResolver().delete(DataColumn.CONTENT_URI_DEL_VALID_FIRST, null, null);
                            }
                            cursor.close();
                        }
                    }
                    // 새로운 메시지를 DB에 등록.
                    {
                        String strItemMsg = item.m_strText;
                        String strItemURL = item.m_strURL;
                        String strItemDataName = item.m_strDataName;

                        ContentValues values = new ContentValues();
                        values.put(MessageColumn.DB_COLUMN_MSG_ID, strMsgID);
                        values.put(MessageColumn.DB_COLUMN_TOKEN, strToken);
                        values.put(MessageColumn.DB_COLUMN_RESEND_COUNT, nResendCount);

                        switch (nNumberType) {
                            case DataColumn.COLUMN_NUMBER_TYPE_PTT: {
                                values.put(MessageColumn.DB_COLUMN_NUMBER, strItemSender);
                            }
                            break;
                            case DataColumn.COLUMN_NUMBER_TYPE_GROUP: {
                                values.put(MessageColumn.DB_COLUMN_NUMBER, strItemReceiver);
                                values.put(MessageColumn.DB_COLUMN_GROUP_MEMBER, strItemSender);
                            }
                            break;
                            case DataColumn.COLUMN_NUMBER_TYPE_NORMAL: {
                                values.put(MessageColumn.DB_COLUMN_NUMBER, strItemSender);
                            }
                            break;
                            default:
                                break;
                        }
                        values.put(MessageColumn.DB_COLUMN_NUMBER_TYPE, nNumberType);
                        values.put(MessageColumn.DB_COLUMN_MSG, strItemMsg);
                        values.put(MessageColumn.DB_COLUMN_URL, strItemURL);

                        if (TextUtils.isEmpty(strItemDataName) == false) {
                            // 메시지에 Data가 들어 있는 경우.
                            int nDataType = DataColumn.COLUMN_DATA_TYPE_NONE;
                            String strItemDataFormat = item.m_strDataFormat;

                            if (strItemDataFormat.equalsIgnoreCase("jpg"))
                                nDataType = DataColumn.COLUMN_DATA_TYPE_JPG;
                            else if (strItemDataFormat.equalsIgnoreCase("bmp"))
                                nDataType = DataColumn.COLUMN_DATA_TYPE_BMP;
                            else if (strItemDataFormat.equalsIgnoreCase("png"))
                                nDataType = DataColumn.COLUMN_DATA_TYPE_PNG;
                            else if (strItemDataFormat.equalsIgnoreCase("mp3"))
                                nDataType = DataColumn.COLUMN_DATA_TYPE_MP3;
                            else
                                nDataType = DataColumn.COLUMN_DATA_TYPE_NONE;

                            values.put(MessageColumn.DB_COLUMN_DATA_TYPE, nDataType);
                            values.put(MessageColumn.DB_COLUMN_DATA, item.m_strDataByteArr);
                        }
                        values.put(MessageColumn.DB_COLUMN_CREATETIME, System.currentTimeMillis());

                        if (TalkActivity.s_strCurrNumber != null &&
                                (TalkActivity.s_strCurrNumber.compareTo(strItemSender) == 0 ||
                                        TalkActivity.s_strCurrNumber.compareTo(strItemReceiver) == 0)) {
                            // 현재 들어온 메시지가 사용자가 보고 있는 방에 들어오는 메시지 이면,
                            // 읽은 표시를 해 줘야 함.
                            values.put(MessageColumn.DB_COLUMN_DONT_READ, DataColumn.COLUMN_DONT_READ_READ);
                            Log.i(PMCType.TAG, "Open Talk Room, Read!!");

                            // Ack가 필요한 경우 Ack 전송.
                            if (nAck == DataColumn.COLUMN_ACK_ING) {
                                boolean bRetAck = processAck(strMsgID, strToken);
                                Log.i(PMCType.TAG, "bRetAck=" + bRetAck);
                                if (bRetAck)
                                    values.put(MessageColumn.DB_COLUMN_ACK, DataColumn.COLUMN_ACK_SEND);
                                else
                                    values.put(MessageColumn.DB_COLUMN_ACK, DataColumn.COLUMN_ACK_ING);
                            }
                        } else {
                            values.put(MessageColumn.DB_COLUMN_ACK, nAck);
                        }

                        getContentResolver().insert(DataColumn.CONTENT_URI_INSERT_MSG, values);
                    }
                } else {
                    Log.d(PMCType.TAG, "JSonItem is Empty");
                }
            } else {
                Log.d(PMCType.TAG, "Contents is Empty");
            }

            // onProgressUpdate callback을 호출 해
            // background작업의 실행경과를 UI에 표현함
            // publishProgress((int)(((i+1)/(float)numberOfParams)*100));
            return item;
        }

        /**
         * onInBackground(...)에서 publishProgress(...)를 사용하면,
         * 자동 호출되는 callback으로
         * 이곳에서 ProgressBar를 증가 시키고, text 정보를 update하는 등의
         * background 작업 진행 상황을 UI에 표현함.
         */
        @Override
        protected void onProgressUpdate(Integer... progress) {
            //progressBar.setProgress(progress[0]);
        }

        /**
         * onInBackground(...)가 완료되면 자동으로 실행되는 callback
         * 이곳에서 onInBackground가 리턴한 정보를 UI위젯에 표시 하는 등의 작업을 수행함.
         */
        @Override
        protected void onPostExecute(JSonItem result) {
            Log.d(PMCType.TAG, "onPostExecute");

            if (result != null) {
                String strItemSender = result.m_strSender;
                String strItemReceiver = result.m_strReceiver;
                String strItemMsg = result.m_strText;
                String strNumberName = null;
                String strFleetMemberNum = null;

                String strMyUFMI = m_configure.getUFMI();
                int nNumberType = DataColumn.COLUMN_NUMBER_TYPE_PTT;
                {
                    if (CommonUtil.checkPttGroupNumFormat(strItemReceiver)) {
                        nNumberType = DataColumn.COLUMN_NUMBER_TYPE_GROUP;

                        if (strMyUFMI.compareToIgnoreCase(strItemSender) == 0)
                            // Group 이면서 Sender가 자기자신이면 메시지 무시.
                            return;
                    } else {
                        nNumberType = DataColumn.COLUMN_NUMBER_TYPE_PTT;
                    }
                }

                if (nNumberType == DataColumn.COLUMN_NUMBER_TYPE_GROUP) {
                    strNumberName = CommonUtil.searchName_FromContact_ByPtt(m_context, strItemReceiver, strMyUFMI);
                    strItemSender = strItemReceiver;
                } else {
                    strFleetMemberNum = CommonUtil.conv_UrbanFleetMemberNumToFleetMemberNum(strItemSender);
                    strNumberName = CommonUtil.searchName_FromContact_ByPtt(m_context, strFleetMemberNum, strMyUFMI);
                }

                String strNotiTitle = null;
                if (TextUtils.isEmpty(strNumberName)) {
                    if (nNumberType == DataColumn.COLUMN_NUMBER_TYPE_GROUP)
                        strNotiTitle = strItemSender;
                    else
                        strNotiTitle = strFleetMemberNum;
                } else {
                    strNotiTitle = strNumberName;
                }

                boolean bPopupNoti = m_configure.getPopupNoti();
                Log.i(PMCType.TAG, "Popup Alam State= " + bPopupNoti);

                if (bPopupNoti == true) {
                    // Popup noti가 True 상태일 때.

                    if (CommonUtil.checkTopClass(m_context, PMCType.BNS_PMC_ACTIVITY_PACKAGE_MAIN) == false &&
                            CommonUtil.checkTopClass(m_context, PMCType.BNS_PMC_ACTIVITY_PACKAGE_NEW) == false &&
                            CommonUtil.checkTopClass(m_context, PMCType.BNS_PMC_ACTIVITY_PACKAGE_TALK) == false &&
                            CommonUtil.checkTopClass(m_context, PMCType.BNS_PMC_ACTIVITY_PACKAGE_SET) == false) {
                        // 앱이 떠 있는 상태가 아니라면 팝업이 발생한다.
                        Log.i(PMCType.TAG, "Popup!!");

                        Intent intent = new Intent();
                        intent.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_TITLE, strNotiTitle);
                        intent.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_SENDER, strItemSender);
                        intent.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_CONTENT, strItemMsg);
                        intent.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_CONTROL, false);
                        intent.setClass(m_context, NotiPopupActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        PendingIntent pendingIntent = PendingIntent.getActivity(m_context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                        try {
                            pendingIntent.send();
                        } catch (CanceledException e) {
                            Log.e(PMCType.TAG, "Pending Error= " + e);
                            e.printStackTrace();
                        }
                    }
                }
                if (TextUtils.equals(TalkActivity.s_strCurrNumber, strItemSender) == false) {
                    // 현재 대화방이 떠 있는 경우,
                    // 새로운 메시지가 현재 대화방 상대가 아니면 Notification을 호출한다.

                    Log.i(PMCType.TAG, "Noti!!");
                    createNotification(strNotiTitle, strItemSender, strItemMsg, false);
                }

                // 화면 ON
                {
                    PttWakeUpUtil.acquire(m_context);

                    //testCode
                    //화면을 키고 1초후 자동으로 락해제로 변경
//                    int defTimeOut = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 15000);
//                    Log.d(PMCType.TAG, "defTimeOut=" + defTimeOut);
//                    TimerTask task = new TimerTask() {
//
//                        @Override
//                        public void run() {
//                            PttWakeUpUtil.release();
//
//                        }
//                    };
//
//                    Timer timer = new Timer();
//                    timer.schedule(task, defTimeOut);
                    //testCodeEnd
                }
            }
        }

        /**
         * AsyncTask.cancel(boolean) 메소드가 true 인자로 실행되면 호출되는 콜백.
         * background 작업이 취소될때 꼭 해야될 작업은  여기에 구현.
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    /**
     * 관제 메시지 async 처리
     *
     * @author kyu
     */
    private class asyncProcessControlMsg extends AsyncTask<Intent, Integer, JSonItem> {
        /**
         * 이곳에 포함된 code는 AsyncTask가 execute 되자 마자 UI 스레드에서 실행됨.
         * 작업 시작을 UI에 표현하거나
         * background 작업을 위한 ProgressBar를 보여 주는 등의 코드를 작성.
         */
        @Override
        protected void onPreExecute() {
            Log.d(PMCType.TAG, "onPreExecute start");
            super.onPreExecute();
        }

        /**
         * UI 스레드에서 AsynchTask객체.execute(...) 명령으로 실행되는 callback
         */
        @Override
        protected JSonItem doInBackground(Intent... params) {
            Log.d(PMCType.TAG, "doInBackground Start");

            Intent intent = params[0];
            int nAck = intent.getIntExtra(PMCType.BNS_PMC_MMS_RECV_ACK, 0);
            String strMsgID = intent.getStringExtra(PMCType.BNS_PMC_MMS_RECV_MSG_ID);
            String strToken = intent.getStringExtra(PMCType.BNS_PMC_MMS_RECV_TOKEN);
            String strSender = intent.getStringExtra(PMCType.BNS_PMC_MMS_RECV_SENDER);
            String strReceiver = intent.getStringExtra(PMCType.BNS_PMC_MMS_RECV_RECEIVER);
            String strContentType = intent.getStringExtra(PMCType.BNS_PMC_MMS_RECV_CONTENT_TYPE);
            String strContent = intent.getStringExtra(PMCType.BNS_PMC_MMS_RECV_CONTENT);
            int nResendCount = intent.getIntExtra(PMCType.BNS_PMC_MMS_RECV_RESEND_COUNT, 0);

            Log.i(PMCType.TAG, "[MsgID]" + strMsgID + " [Token]" + strToken +
                    " [Ack]" + nAck + " [resendCnt]" + nResendCount);
            Log.i(PMCType.TAG, "[Send]" + strSender + " [Recv]" + strReceiver + " [ContentType]"
                    + strContentType);
            Log.i(PMCType.TAG, "[Content]" + strContent);

            JSonItem item = null;
            if (TextUtils.isEmpty(strContent) == false) {
                String strDecode = CommonUtil.decodeBase64ToString(strContent);
                //item = JSonUtil.decodeJSONStringToData(strDecode);
                // 15.03.12 관제 메시지는 JSON 형식으로 안 내려 온다.
                {
                    item = new JSonItem();
                    item.m_strSender = strSender;
                    item.m_strReceiver = strReceiver;
                    item.m_strText = strDecode;
                }

                if (item != null) {
                    String strItemSender = item.m_strSender;
                    String strItemReceiver = item.m_strReceiver;

                    Log.i(PMCType.TAG, "JSonItem Sender= " + strItemSender + " Receiver= " + strItemReceiver);

                    // DB용량이 Max 상태이면 제일 처음 DB를 지운다.
                    {
                        int nDBSaveMax = m_configure.getMessageSaveCount();
                        int nDBCount = -1;
                        Cursor cursor = getContentResolver().query(DataColumn.CONTENT_URI_LIST_TOTAL_COUNT, null, null, null, null);
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                nDBCount = cursor.getInt(cursor.getColumnIndex(MessageColumn.DB_COLUMN_COUNT_ALL));
                                Log.i(PMCType.TAG, "DBCount= " + nDBCount);
                            }
                            if (nDBCount >= nDBSaveMax) {
                                Log.i(PMCType.TAG, "DBMax= " + nDBSaveMax);
                                getContentResolver().delete(DataColumn.CONTENT_URI_DEL_VALID_FIRST, null, null);
                            }
                            cursor.close();
                        }
                    }
                    // 새로운 메시지를 DB에 등록.
                    {
                        String strItemMsg = item.m_strText;
                        String strItemURL = item.m_strURL;
                        String strItemDataName = item.m_strDataName;

                        if (nResendCount != 0) {
                            strItemMsg = item.m_strText + " [" + m_context.getResources().getString(R.string.resend_msg) +
                                    ":+" + nResendCount + "]";
                            item.m_strText = strItemMsg;
                        }

                        ContentValues values = new ContentValues();
                        values.put(MessageColumn.DB_COLUMN_MSG_ID, strMsgID);
                        values.put(MessageColumn.DB_COLUMN_TOKEN, strToken);
                        values.put(MessageColumn.DB_COLUMN_RESEND_COUNT, nResendCount);
                        if (TextUtils.isEmpty(strItemSender))
                            values.put(MessageColumn.DB_COLUMN_NUMBER,
                                    m_context.getResources().getString(R.string.no_sender));
                        else
                            values.put(MessageColumn.DB_COLUMN_NUMBER, strItemSender);
                        values.put(MessageColumn.DB_COLUMN_NUMBER_TYPE, DataColumn.COLUMN_NUMBER_TYPE_CONTROL);
                        values.put(MessageColumn.DB_COLUMN_MSG, strItemMsg);
                        values.put(MessageColumn.DB_COLUMN_URL, strItemURL);

                        if (TextUtils.isEmpty(strItemDataName) == false) {
                            // 메시지에 Data가 들어 있는 경우.
                            int nDataType = DataColumn.COLUMN_DATA_TYPE_NONE;
                            String strItemDataFormat = item.m_strDataFormat;

                            if (strItemDataFormat.equalsIgnoreCase("jpg"))
                                nDataType = DataColumn.COLUMN_DATA_TYPE_JPG;
                            else if (strItemDataFormat.equalsIgnoreCase("bmp"))
                                nDataType = DataColumn.COLUMN_DATA_TYPE_BMP;
                            else if (strItemDataFormat.equalsIgnoreCase("png"))
                                nDataType = DataColumn.COLUMN_DATA_TYPE_PNG;
                            else if (strItemDataFormat.equalsIgnoreCase("mp3"))
                                nDataType = DataColumn.COLUMN_DATA_TYPE_MP3;
                            else
                                nDataType = DataColumn.COLUMN_DATA_TYPE_NONE;

                            values.put(MessageColumn.DB_COLUMN_DATA_TYPE, nDataType);
                            values.put(MessageColumn.DB_COLUMN_DATA, item.m_strDataByteArr);
                        }
                        values.put(MessageColumn.DB_COLUMN_CREATETIME, System.currentTimeMillis());

                        if (TalkActivity.s_strCurrNumber != null &&
                                (TalkActivity.s_strCurrNumber.compareTo(strItemSender) == 0 ||
                                        TalkActivity.s_strCurrNumber.compareTo(strItemReceiver) == 0)) {
                            // 현재 들어온 메시지가 사용자가 보고 있는 방에 들어오는 메시지 이면,
                            // 읽은 표시를 해 줘야 함.
                            values.put(MessageColumn.DB_COLUMN_DONT_READ, DataColumn.COLUMN_DONT_READ_READ);
                            Log.i(PMCType.TAG, "Open Talk Room, Read!!");

                            // Ack가 필요한 경우 Ack 전송.
                            if (nAck == DataColumn.COLUMN_ACK_ING) {
                                boolean bRetAck = processAck(strMsgID, strToken);

                                if (bRetAck)
                                    values.put(MessageColumn.DB_COLUMN_ACK, DataColumn.COLUMN_ACK_SEND);
                                else
                                    values.put(MessageColumn.DB_COLUMN_ACK, DataColumn.COLUMN_ACK_ING);
                            }

                        } else {
                            values.put(MessageColumn.DB_COLUMN_ACK, nAck);
                        }

                        getContentResolver().insert(DataColumn.CONTENT_URI_INSERT_MSG, values);
                    }
                } /*else {
                    Log.d(PMCType.TAG, "JSonItem is Empty");
				}*/
            } else {
                Log.d(PMCType.TAG, "Contents is Empty");
            }

            // onProgressUpdate callback을 호출 해
            // background작업의 실행경과를 UI에 표현함
            // publishProgress((int)(((i+1)/(float)numberOfParams)*100));
            return item;
        }

        /**
         * onInBackground(...)에서 publishProgress(...)를 사용하면,
         * 자동 호출되는 callback으로
         * 이곳에서 ProgressBar를 증가 시키고, text 정보를 update하는 등의
         * background 작업 진행 상황을 UI에 표현함.
         */
        @Override
        protected void onProgressUpdate(Integer... progress) {
            //progressBar.setProgress(progress[0]);
        }

        /**
         * onInBackground(...)가 완료되면 자동으로 실행되는 callback
         * 이곳에서 onInBackground가 리턴한 정보를 UI위젯에 표시 하는 등의 작업을 수행함.
         */
        @Override
        protected void onPostExecute(JSonItem result) {
            Log.d(PMCType.TAG, "onPostExecute");

            if (result != null) {
                String strItemSender = result.m_strSender;
                //String strItemReceiver = result.m_strReceiver;
                String strItemMsg = result.m_strText;

                String strNotiTitle = null;
                if (TextUtils.isEmpty(strItemSender))
                    strNotiTitle = m_context.getResources().getString(R.string.no_sender);
                else
                    strNotiTitle = strItemSender;

                boolean bPopupNoti = m_configure.getPopupNoti();
                Log.i(PMCType.TAG, "Popup Alam State= " + bPopupNoti);

                if (bPopupNoti == true) {
                    // Popup noti가 True 상태일 때.

                    if (CommonUtil.checkTopClass(m_context, PMCType.BNS_PMC_ACTIVITY_PACKAGE_MAIN) == false &&
                            CommonUtil.checkTopClass(m_context, PMCType.BNS_PMC_ACTIVITY_PACKAGE_NEW) == false &&
                            CommonUtil.checkTopClass(m_context, PMCType.BNS_PMC_ACTIVITY_PACKAGE_TALK) == false &&
                            CommonUtil.checkTopClass(m_context, PMCType.BNS_PMC_ACTIVITY_PACKAGE_SET) == false) {
                        // 앱이 떠 있는 상태가 아니라면 팝업이 발생한다.
                        Log.i(PMCType.TAG, "Popup!!");

                        Intent intent = new Intent();
                        intent.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_TITLE, strNotiTitle);
                        intent.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_SENDER, strItemSender);
                        intent.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_CONTENT, strItemMsg);
                        intent.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_CONTROL, true);
                        intent.setClass(m_context, NotiPopupActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        PendingIntent pendingIntent = PendingIntent.getActivity(m_context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                        try {
                            pendingIntent.send();
                        } catch (CanceledException e) {
                            Log.e(PMCType.TAG, "Pending Error= " + e);
                            e.printStackTrace();
                        }
                    }
                }
                if (TextUtils.equals(TalkActivity.s_strCurrNumber, strItemSender) == false) {
                    // 현재 대화방이 떠 있는 경우,
                    // 새로운 메시지가 현재 대화방 상대가 아니면 Notification을 호출한다.

                    Log.i(PMCType.TAG, "Noti!!");
                    createNotification(strNotiTitle, strItemSender, strItemMsg, true);
                }

                // 화면 ON
                {
                    PttWakeUpUtil.acquire(m_context);
                    //testCode
                    //화면을 키고 1초후 자동으로 락해제로 변경
//                    int defTimeOut = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 15000);
//                    TimerTask task = new TimerTask() {
//
//                        @Override
//                        public void run() {
//                            PttWakeUpUtil.release();
//
//                        }
//                    };
//
//                    Timer timer = new Timer();
//                    timer.schedule(task, defTimeOut);
                }
            }
        }

        /**
         * AsyncTask.cancel(boolean) 메소드가 true 인자로 실행되면 호출되는 콜백.
         * background 작업이 취소될때 꼭 해야될 작업은  여기에 구현.
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    /**
     * DIG 메시지 async 처리
     *
     * @author kyu
     */
    private class asyncProcessDIGMsg extends AsyncTask<Intent, Integer, JSonItem> {
        /**
         * 이곳에 포함된 code는 AsyncTask가 execute 되자 마자 UI 스레드에서 실행됨.
         * 작업 시작을 UI에 표현하거나
         * background 작업을 위한 ProgressBar를 보여 주는 등의 코드를 작성.
         */
        @Override
        protected void onPreExecute() {
            Log.d(PMCType.TAG, "onPreExecute start");
            super.onPreExecute();
        }

        /**
         * UI 스레드에서 AsynchTask객체.execute(...) 명령으로 실행되는 callback
         */
        @Override
        protected JSonItem doInBackground(Intent... params) {
            Log.d(PMCType.TAG, "doInBackground Start");

            Intent intent = params[0];
            int nAck = intent.getIntExtra(PMCType.BNS_PMC_MMS_RECV_ACK, 0);
            String strMsgID = intent.getStringExtra(PMCType.BNS_PMC_MMS_RECV_MSG_ID);
            String strToken = intent.getStringExtra(PMCType.BNS_PMC_MMS_RECV_TOKEN);
            String strSender = intent.getStringExtra(PMCType.BNS_PMC_MMS_RECV_SENDER);
            String strReceiver = intent.getStringExtra(PMCType.BNS_PMC_MMS_RECV_RECEIVER);
            String strContentType = intent.getStringExtra(PMCType.BNS_PMC_MMS_RECV_CONTENT_TYPE);
            String strContent = intent.getStringExtra(PMCType.BNS_PMC_MMS_RECV_CONTENT);
            int nResendCount = intent.getIntExtra(PMCType.BNS_PMC_MMS_RECV_RESEND_COUNT, 0);

            Log.i(PMCType.TAG, "[MsgID]" + strMsgID + " [Token]" + strToken +
                    " [Ack]" + nAck + " [resendCnt]" + nResendCount);
            Log.i(PMCType.TAG, "[Send]" + strSender + " [Recv]" + strReceiver + " [ContentType]"
                    + strContentType);
            Log.i(PMCType.TAG, "[Content]" + strContent);

            JSonItem item = null;
            if (TextUtils.isEmpty(strContent) == false) {
                String strDecode = CommonUtil.decodeBase64ToString(strContent);
                //item = JSonUtil.decodeJSONStringToData(strDecode);
                // 15.03.12 관제 메시지는 JSON 형식으로 안 내려 온다.
                {
                    item = new JSonItem();
                    item.m_strSender = strSender;
                    item.m_strReceiver = strReceiver;
                    item.m_strText = strDecode;
                }

                // Broadcast
                {
                    Log.i(PMCType.TAG, "send Broadcast!!!");

                    Intent sendIntent = new Intent(PMCType.BNS_PMC_PUSH_DIG_ACTION);
                    String[] arr = item.m_strText.split("\\;");
                    for (int i = 0; i < arr.length; i++) {
                        String[] strArr = arr[i].split("\\:");
                        String strName = strArr[0];
                        String strValue = strArr[1];

                        Log.i(PMCType.TAG, strName + " " + strValue);

                        sendIntent.putExtra(strName, strValue);
                    }
                    sendBroadcast(sendIntent);
                }

				/*if (item != null) {
                    String strItemSender = item.m_strSender;
    				String strItemReceiver = item.m_strReceiver;
					
    				Log.i(PMCType.TAG, "JSonItem Sender= " + strItemSender + " Receiver= " + strItemReceiver);
    				
    				// DB용량이 Max 상태이면 제일 처음 DB를 지운다.
    				{
    					int nDBSaveMax = m_configure.getMessageSaveCount();
    					Cursor cursor = getContentResolver().query(DataColumn.CONTENT_URI_LIST_TOTAL_COUNT, null, null, null, null);
    					int nDBCount = -1;
    				    if (cursor.moveToFirst()) {
    				    	nDBCount = cursor.getInt(cursor.getColumnIndex(MessageColumn.DB_COLUMN_COUNT_ALL));
    				    	Log.i(PMCType.TAG, "DBCount= " + nDBCount);
    				    }			 
    					if (nDBCount >= nDBSaveMax) {
    						Log.i(PMCType.TAG, "DBMax= " + nDBSaveMax);
    						getContentResolver().delete(DataColumn.CONTENT_URI_DEL_VALID_FIRST, null, null);					
    					}
    				}
    				// 새로운 메시지를 DB에 등록.
    				{
    					String strItemMsg = item.m_strText;
    					String strItemURL = item.m_strURL;
    					String strItemDataName = item.m_strDataName;
    					
    					if (nResendCount != 0) {
    						strItemMsg = item.m_strText + " [" + m_context.getResources().getString(R.string.resend_msg) + 
    								":+"+ nResendCount + "]";
    						item.m_strText = strItemMsg;
    					}
    					
    					ContentValues values = new ContentValues();
    					values.put(MessageColumn.DB_COLUMN_MSG_ID, strMsgID);
    					values.put(MessageColumn.DB_COLUMN_TOKEN, strToken);    					
    					values.put(MessageColumn.DB_COLUMN_RESEND_COUNT, nResendCount);
    					if (TextUtils.isEmpty(strItemSender))
    						values.put(MessageColumn.DB_COLUMN_NUMBER, 
    								m_context.getResources().getString(R.string.no_sender));
    					else
    						values.put(MessageColumn.DB_COLUMN_NUMBER, strItemSender);
    					values.put(MessageColumn.DB_COLUMN_NUMBER_TYPE, DataColumn.COLUMN_NUMBER_TYPE_CONTROL);
    					values.put(MessageColumn.DB_COLUMN_MSG, strItemMsg);
    					values.put(MessageColumn.DB_COLUMN_URL, strItemURL);
    					
    					if (TextUtils.isEmpty(strItemDataName) == false) {
    						// 메시지에 Data가 들어 있는 경우.
    						int nDataType = DataColumn.COLUMN_DATA_TYPE_NONE;
    						String strItemDataFormat = item.m_strDataFormat;
    						
    						if (strItemDataFormat.equalsIgnoreCase("jpg"))
    							nDataType = DataColumn.COLUMN_DATA_TYPE_JPG;
    						else if (strItemDataFormat.equalsIgnoreCase("bmp"))
    							nDataType = DataColumn.COLUMN_DATA_TYPE_BMP;
    						else if (strItemDataFormat.equalsIgnoreCase("png"))
    							nDataType = DataColumn.COLUMN_DATA_TYPE_PNG;
    						else if (strItemDataFormat.equalsIgnoreCase("mp3"))
    							nDataType = DataColumn.COLUMN_DATA_TYPE_MP3;
    						else
    							nDataType = DataColumn.COLUMN_DATA_TYPE_NONE;
    							
    						values.put(MessageColumn.DB_COLUMN_DATA_TYPE, nDataType);
    						values.put(MessageColumn.DB_COLUMN_DATA, item.m_strDataByteArr);
    					}
    					values.put(MessageColumn.DB_COLUMN_CREATETIME, System.currentTimeMillis());
    					
    					if (TalkActivity.s_strCurrNumber != null && 
    							(TalkActivity.s_strCurrNumber.compareTo(strItemSender) == 0 || 
    							TalkActivity.s_strCurrNumber.compareTo(strItemReceiver) == 0)) {
    						// 현재 들어온 메시지가 사용자가 보고 있는 방에 들어오는 메시지 이면,
    						// 읽은 표시를 해 줘야 함.
    						values.put(MessageColumn.DB_COLUMN_DONT_READ, DataColumn.COLUMN_DONT_READ_READ);
    						Log.i(PMCType.TAG, "Open Talk Room, Read!!");
    						
    						// Ack가 필요한 경우 Ack 전송.
    						if (nAck == DataColumn.COLUMN_ACK_ING) {
    							boolean bRetAck = processAck(strMsgID, strToken);
    							
    							if (bRetAck)
    								values.put(MessageColumn.DB_COLUMN_ACK, DataColumn.COLUMN_ACK_SEND);
    							else 
    								values.put(MessageColumn.DB_COLUMN_ACK, DataColumn.COLUMN_ACK_ING);
    						}
    							
    					} else {
    						values.put(MessageColumn.DB_COLUMN_ACK, nAck);
    					}
    					
    					getContentResolver().insert(DataColumn.CONTENT_URI_INSERT_MSG, values);    					
    				}
				} */
            } else {
                Log.d(PMCType.TAG, "Contents is Empty");
            }

            // onProgressUpdate callback을 호출 해
            // background작업의 실행경과를 UI에 표현함
            // publishProgress((int)(((i+1)/(float)numberOfParams)*100));
            return item;
        }

        /**
         * onInBackground(...)에서 publishProgress(...)를 사용하면,
         * 자동 호출되는 callback으로
         * 이곳에서 ProgressBar를 증가 시키고, text 정보를 update하는 등의
         * background 작업 진행 상황을 UI에 표현함.
         */
        @Override
        protected void onProgressUpdate(Integer... progress) {
            //progressBar.setProgress(progress[0]);
        }

        /**
         * onInBackground(...)가 완료되면 자동으로 실행되는 callback
         * 이곳에서 onInBackground가 리턴한 정보를 UI위젯에 표시 하는 등의 작업을 수행함.
         */
        @Override
        protected void onPostExecute(JSonItem result) {
            Log.d(PMCType.TAG, "onPostExecute");

    		/*if (result != null) {
                String strItemSender = result.m_strSender;
    			//String strItemReceiver = result.m_strReceiver;
    			String strItemMsg = result.m_strText;
    			
    			String strNotiTitle = null;
    			if (TextUtils.isEmpty(strItemSender))
    				strNotiTitle = m_context.getResources().getString(R.string.no_sender);
    			else
    				strNotiTitle = strItemSender;    			
    			
    			boolean bPopupNoti = m_configure.getPopupNoti();
    			Log.i(PMCType.TAG, "Popup Alam State= " + bPopupNoti);
    			
    			if (bPopupNoti == true) {
    				// Popup noti가 True 상태일 때.
    				
    				if (CommonUtil.checkTopClass(m_context, PMCType.BNS_PMC_ACTIVITY_PACKAGE_MAIN) == false && 
    						CommonUtil.checkTopClass(m_context, PMCType.BNS_PMC_ACTIVITY_PACKAGE_NEW) == false &&
    						CommonUtil.checkTopClass(m_context, PMCType.BNS_PMC_ACTIVITY_PACKAGE_TALK) == false &&
    						CommonUtil.checkTopClass(m_context, PMCType.BNS_PMC_ACTIVITY_PACKAGE_SET) == false) {
    					// 앱이 떠 있는 상태가 아니라면 팝업이 발생한다.
    					Log.i(PMCType.TAG, "Popup!!");
    					
    					Intent intent = new Intent();
    					intent.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_TITLE, strNotiTitle);
    					intent.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_SENDER, strItemSender);
    					intent.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_CONTENT, strItemMsg);
    					intent.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_CONTROL, true);
    					intent.setClass(m_context, NotiPopupActivity.class);
    					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    					
    					PendingIntent pendingIntent = PendingIntent.getActivity(m_context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
    					try {
							pendingIntent.send();
						} catch (CanceledException e) {
							Log.e(PMCType.TAG, "Pending Error= " + e);
							e.printStackTrace();
						}
    				}
    			}
    			if (TextUtils.equals(TalkActivity.s_strCurrNumber, strItemSender) == false) {
    				// 현재 대화방이 떠 있는 경우,
        			// 새로운 메시지가 현재 대화방 상대가 아니면 Notification을 호출한다.
    				
    				Log.i(PMCType.TAG, "Noti!!");
    				createNotification(strNotiTitle, strItemSender, strItemMsg, true);
    			}
    		}*/
        }

        /**
         * AsyncTask.cancel(boolean) 메소드가 true 인자로 실행되면 호출되는 콜백.
         * background 작업이 취소될때 꼭 해야될 작업은  여기에 구현.
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    /**
     * PMCService BroadcastReceiver Class
     */
    private BroadcastReceiver m_PMCServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("PMCService", "BroadcastReceiver시작(context=" + context + ", intent=" + intent + ")");
            String action = intent.getAction();

            if (action.equals(PMCType.BNS_PMC_PTT_CALL_START_END_MODE_ACTION)) {
                Log.d(PMCType.TAG, "Curr state Ptt Call " + m_bPttCall);
                boolean b = intent.getBooleanExtra(PMCType.BNS_PMC_PTT_CALL_START_END_MODE_EXTRA_VALUE, false);
                Log.d(PMCType.TAG, "Change state Ptt Call " + b);

                m_bPttCall = b;
            } else if (action.equals(PMCType.UNI_PMC_PTT_SET_PTT_NUMBER_ACTION)) {
                Intent i = new Intent(intent);
                processReceivedPttNumberAction(i);
            } else if (action.equals(PMCType.BNS_PMC_PTT_REG_STATE_ACTION_LOGOUT)) {
                Intent i = new Intent(intent);
                processReceivedPttLogInOutAction(i);
            } else if (action.equals(PMCType.BNS_PMC_KTP_PUSH_USERMSG)) {
                // 메신져 앱 간의 메시지 수신
                Log.i(PMCType.TAG, "== User Msg == ");
                Intent i = new Intent(intent);
                processReceivedUserMsg(i);
            } else if (action.equals(PMCType.BNS_PMC_KTP_PUSH_MMS)) {
                // 관제 시스템 메시지 수신
                Log.i(PMCType.TAG, "== Control Msg == ");
                Intent i = new Intent(intent);
                processReceivedControlMsg(i);
            } else if (action.equals(PMCType.BNS_PMC_KTP_PUSH_FWUPDATE_INFO)) {
                // FW 메시지 수신
                Log.i(PMCType.TAG, "== FW Msg == ");
                Intent i = new Intent(intent);
                processReceivedFWMsg(i);
            } else if (action.equals(PMCType.BNS_PMC_KTP_PUSH_CONN_STATE)) {
                // MQTT 세션 수신
                Log.i(PMCType.TAG, "== MQTT Msg == ");
                Intent i = new Intent(intent);
                processReceivedMQTTMsg(i);
            } else if (action.equals(PMCType.BNS_PMC_KTP_PUSH_DIG)) {
                Log.i(PMCType.TAG, "== DIG Msg == ");
                // DIG 수신
                Intent i = new Intent(intent);
                processReceivedDIGMsg(i);
            }
            Log.d("PMCService", "BroadcastReceiver종료()");
        }
    };

    /**
     * 서비스의 인터페이스와 상호작용하는 Class
     */
    private final ServiceConnection m_Connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(PMCType.TAG, "[onServiceConnected]");
            m_Binder = IPushService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            m_Binder = null;
            Log.d(PMCType.TAG, "[onServiceDisconnected]");
        }
    };
    
/*    private final Handler mHandler = new Handler() { //핸들러를 통해 UI스레드에 접근한다.

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
            textView.setText(msg.what + "");
        }
        
    };*/
}
