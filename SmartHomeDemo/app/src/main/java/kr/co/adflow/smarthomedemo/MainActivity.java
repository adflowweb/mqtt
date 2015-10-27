/*
 * Copyright (C) ADFlow, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by nadir93 <typark@adflow.co.kr>, October 2015
 */

package kr.co.adflow.smarthomedemo;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

import kr.co.adflow.push.client.mqttv3.binder.PushLocalBinder;
import kr.co.adflow.push.client.mqttv3.service.PushService;
import kr.co.adflow.push.client.mqttv3.service.impl.PushServiceImpl;

public class MainActivity extends AppCompatActivity {

    // Logger for this class.
    private static final String TAG = "MainActivity";
    private TextView connectionText;
    private TextView contentTextView;
    PushService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "ADFPush 데모", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //텍스트뷰 초기화
        initTextView();

        //버튼 초기화
        addListenerOnButton();
    }

    private void initTextView() {

        connectionText = (TextView) findViewById(R.id.connectionTextView);
        contentTextView = (TextView) findViewById(R.id.contentTextView);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent 시작(intent=" + intent + ")");

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Log.v(TAG, key + " : " + bundle.get(key));
            }
        }

        // bind service
        Intent i = new Intent(this, PushServiceImpl.class);
        Log.d(TAG, "로컬 푸시서비스를 연결합니다");
        this.bindService(i, mConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "로컬 푸시서비스 연결상태=" + (mBound ? "바인드" : "언바인드"));

        if (mBound) {

//            V  sender : sktsmarthome
//            V  receiver : testTopic
//            V  contentType : text/plain
//            V  msgId : 2015102a6ae422f87243f3ba54c3dee22bf8f0
//            V  token : 0f463d7ccffb1034ab9716a
//            V  content : 테스트 케이스 메시지 전송 !!

            mService.ack(bundle.getString("msgId"), new Date());
        }


        // Unbind from the service
        if (mBound) {
            Log.d(TAG, "로컬 푸시서비스 연결을 종료합니다");
            this.unbindService(mConnection);
            mBound = false;
            Log.d(TAG, "로컬 푸시서비스가 연결 종료되었습니다");
        }
        Log.d(TAG, "onNewIntent 종료()");
    }

    public void addListenerOnButton() {

        /**
         * 푸시 서비스 시작
         */
        Log.d(TAG, "startService=" + findViewById(R.id.startService));

        Button startService = (Button) findViewById(R.id.startService);

        startService.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, PushServiceImpl.class);
                i.setAction("kr.co.adflow.push.service.START");
                i.putExtra("token", "0f463d7ccffb1034ab9716a");
                i.putExtra("server", "112.223.76.75");
                i.putExtra("port", 11883);
                i.putExtra("ssl", false);
                i.putExtra("cleanSession", false);
                i.putExtra("keepAlive", 240);
                Log.d(TAG, "PushService가 시작됩니다");
                startService(i);
            }

        });

        /**
         * 푸시 서비스 종료
         */
        Button stopService = (Button) findViewById(R.id.stopService);

        stopService.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, PushServiceImpl.class);
                Log.d(TAG, "PushService가 종료됩니다");
                stopService(i);
            }

        });

        /**
         * 로컬 서비스 바인드
         */
        Button bindService = (Button) findViewById(R.id.bindService);

        bindService.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, PushServiceImpl.class);
                Log.d(TAG, "로컬 푸시서비스를 연결합니다");
                bindService(i, mConnection, Context.BIND_AUTO_CREATE);
            }

        });

        /**
         * 로컬 서비스 언바인드
         */
        Button unbindService = (Button) findViewById(R.id.unbindService);

        unbindService.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Log.d(TAG, "로컬 푸시서비스 연결상태=" + (mBound ? "바인드" : "언바인드"));
                // Unbind from the service
                if (mBound) {
                    Log.d(TAG, "로컬 푸시서비스 연결을 종료합니다");
                    unbindService(mConnection);
                    mBound = false;
                    contentTextView.setText("로컬 푸시서비스가 연결 종료되었습니다");
                }
            }

        });

        /**
         * 토픽 구독
         */
        Button subscribe = (Button) findViewById(R.id.subscribe);

        subscribe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, "로컬 푸시 서비스 연결상태=" + (mBound ? "바인드" : "언바인드"));
                if (mBound) {
                    new SubscribeTask().execute();
                } else {
                    contentTextView.setText("로컬 푸시서비스가 연결 되어있지 않습니다");
                }
            }

        });

        /**
         * 토픽 구독 취소
         */
        Button unsubscribe = (Button) findViewById(R.id.unsubscribe);

        unsubscribe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, "로컬 푸시 서비스 연결상태=" + (mBound ? "바인드" : "언바인드"));
                if (mBound) {
                    new UnsubscribeTask().execute();
                } else {
                    contentTextView.setText("로컬 푸시 서비스가 연결 되어있지 않습니다");
                }
            }

        });

        /**
         * 토큰 등록하기
         */
        Button registerToken = (Button) findViewById(R.id.registerToken);

        registerToken.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, "로컬 푸시 서비스 연결상태=" + (mBound ? "바인드" : "언바인드"));
                if (mBound) {
                    Log.d(TAG, "토큰을 등록합니다");
                    String response = mService.registerToken("01234567890", false);
                    contentTextView.setText(response);
                } else {
                    contentTextView.setText("로컬 푸시 서비스가 연결 되어있지 않습니다");
                }
            }
        });

        /**
         * 토큰 가져오기
         */
        Button getToken = (Button) findViewById(R.id.getToken);

        getToken.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, "로컬 푸시 서비스 연결상태=" + (mBound ? "바인드" : "언바인드"));
                if (mBound) {
                    Log.d(TAG, "토큰정보를 가져옵니다");
                    String response = mService.getToken();
                    contentTextView.setText(response);
                } else {
                    contentTextView.setText("로컬 푸시 서비스가 연결 되어있지 않습니다");
                }
            }
        });

        /**
         * 세션정보 가져오기
         */
        Button getSession = (Button) findViewById(R.id.getSession);

        getSession.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, "로컬 푸시 서비스 연결상태=" + (mBound ? "바인드" : "언바인드"));
                if (mBound) {
                    Log.d(TAG, "세션정보를 가져옵니다");
                    String response = mService.getSession();
                    contentTextView.setText(response);
                } else {
                    contentTextView.setText("로컬 푸시 서비스가 연결 되어있지 않습니다");
                }
            }
        });

        /**
         * 구독정보 가져오기
         */
        Button getSubscriptions = (Button) findViewById(R.id.getSubscriptions);

        getSubscriptions.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, "로컬 푸시 서비스 연결상태=" + (mBound ? "바인드" : "언바인드"));
                if (mBound) {
                    new SubscriptionsTask().execute();
                } else {
                    contentTextView.setText("로컬 푸시 서비스가 연결 되어있지 않습니다");
                }
            }
        });

        /**
         * PING
         */
        Button ping = (Button) findViewById(R.id.ping);

        ping.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, "로컬 푸시 서비스 연결상태=" + (mBound ? "바인드" : "언바인드"));
                if (mBound) {
                    new PingTask().execute();
                } else {
                    contentTextView.setText("로컬 푸시 서비스가 연결 되어있지 않습니다");
                }
            }
        });

        Button smsSend = (Button) findViewById(R.id.sendSMS);

        smsSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, " sms를 보냅니다");
                ContentValues values = new ContentValues();
                values.put("address", "123456789");
                values.put("body", "[Web발신]\n씨티카드(3*2*)\n박*영님\n10/26 14:12\n일시불 11,200원\n누계 1,489,109원\n에이디플로우");
                getContentResolver().insert(Uri.parse("content://sms/sent"), values);

                getContentResolver().notifyChange(Uri.parse("content://sms/inbox"), null);

                Intent intent = new Intent("android.provider.Telephony.SMS_RECEIVED");
                byte[] b = (byte[])(SmsMessage.getSubmitPdu("5551", "5552", "some_text", false).encodedMessage);
                Object[] vrs = {b};
                intent.putExtra("pdus", vrs);
                sendBroadcast(intent);
            }
        });
    }

    /**
     * ping  asyncTask
     */
    private class PingTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            Log.d(TAG, "send Ping");
            return mService.ping();
        }

        @Override
        protected void onPostExecute(String response) {
            contentTextView.setText(response);
        }
    }

    /**
     * 토픽 구독  asyncTask
     */
    private class SubscribeTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            Log.d(TAG, "토픽을 구독합니다");
            return mService.subscribe("testTopic", 2);
        }

        @Override
        protected void onPostExecute(String response) {
            contentTextView.setText(response);
        }
    }

    /**
     * 토픽 구독 취소 asyncTask
     */
    private class UnsubscribeTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            Log.d(TAG, "토픽 구독을 취소합니다");
            return mService.unsubscribe("testTopic");
        }

        @Override
        protected void onPostExecute(String response) {
            contentTextView.setText(response);
        }
    }

    /**
     * 구독 정보 가져오기 asyncTask
     */
    private class SubscriptionsTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            Log.d(TAG, "구독정보를 가져옵니다");
            return mService.getSubscriptions();
        }

        @Override
        protected void onPostExecute(String response) {
            contentTextView.setText(response);
        }
    }

    /**
     * 수신확인 asyncTask
     */
    private class AckTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "수신확인");
            return mService.ack(params[0], new Date());
        }

        @Override
        protected void onPostExecute(String response) {
            contentTextView.setText(response);
        }
    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume 시작()");

        Log.d(TAG, "브로드캐스트 리시버를 등록합니다");
        /**
         * 리시버 등록
         */
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("kr.co.adflow.push.status"));

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("kr.co.skt.push.mms"));
        super.onResume();
        Log.d(TAG, "onResume 종료()");
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause 시작()");

        Log.d(TAG, "브로드캐스트 리시버를 제거합니다");
        /**
         * 리시버 등록 해제
         */
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
        super.onPause();
        Log.d(TAG, "onPause 종료()");
    }

    /**
     * 브로드 캐스트 리시버
     */
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive 시작(context=" + context + ", intent=" + intent + ")");

            //for debugging
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Log.v(TAG, key + " : " + bundle.get(key));
                }
            }

            if (intent.getAction().equals("kr.co.adflow.push.status")) {
                int eventCode = intent.getIntExtra("code", 0);

                if (eventCode == 112100) {
                    connectionText.setText("푸시 서버 연결됨");
                }

                if (eventCode == 112101) {
                    connectionText.setText("푸시 서버 연결 끊김");
                }

                contentTextView.setText(intent.getStringExtra("message"));
            } else if (intent.getAction().equals("kr.co.skt.push.mms")) {

                Log.d(TAG, "로컬 푸시 서비스 연결상태=" + (mBound ? "바인드" : "언바인드"));
                if (mBound) {
                    new AckTask().execute(intent.getStringExtra("msgId"));
                } else {
                    contentTextView.setText("로컬 푸시서비스가 연결 되어있지 않습니다");
                }
                contentTextView.setText(intent.getStringExtra("content"));
            }
            Log.d(TAG, "onReceive 종료()");
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            PushLocalBinder binder = (PushLocalBinder) service;
            mService = (PushService) binder.getService();
            mBound = true;
            contentTextView.setText("로컬 푸시 서비스가 연결되었습니다");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

            mBound = false;
            contentTextView.setText("로컬 푸시 서비스가 연결 종료되었습니다");
        }
    };
}
