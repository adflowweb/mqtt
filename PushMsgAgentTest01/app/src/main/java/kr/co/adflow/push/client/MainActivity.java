package kr.co.adflow.push.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import kr.co.adflow.push.IPushService;


public class MainActivity extends ActionBarActivity {

    // TAG for debug
    public static final String TAG = "MainActivity";
    public IPushService mBinder;

    public static MainActivity mainActivity;

    public MainActivity() {
        mainActivity = this;
    }

    private TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate시작(bundle=" + savedInstanceState + ")");

        txtView = (TextView) findViewById(R.id.textView);
        txtView.setText("PMA 테스트클라이언트");

        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Log.d(TAG, "푸시서비스연결시작");
                        try {
                            Intent explicitIntent = null;

                            // Package 설치여부 확인
                            PackageManager pm = getPackageManager();
                            ApplicationInfo appInfo = pm.getApplicationInfo("kr.co.adflow.push", PackageManager.GET_META_DATA);
                            Log.d(TAG, "appInfo=" + appInfo);

                            Intent implicitIntent = new Intent("kr.co.adflow.push.service.PushService");
                            List<ResolveInfo> resolveInfos = pm.queryIntentServices(implicitIntent, 0);

                            Log.d(TAG, "resolveInfos=" + resolveInfos);

                            ResolveInfo resolveInfo = resolveInfos.get(0);
                            String packageName = resolveInfo.serviceInfo.packageName;
                            Log.d(TAG, "packageName=" + packageName);
                            String className = resolveInfo.serviceInfo.name;
                            Log.d(TAG, "className=" + className);
                            ComponentName component = new ComponentName(packageName, className);
                            explicitIntent = new Intent();
                            explicitIntent.setComponent(component);
                            Log.d(TAG, "explicitIntent=" + explicitIntent);
                            bindService(explicitIntent, mConnection, Context.BIND_AUTO_CREATE);
                            Log.d(TAG, "PushService바인드");
                            Log.d(TAG, "푸시서비스연결종료");
                        } catch (Exception e) {
                            e.printStackTrace();
                            txtView.setText("에러발생=" + e.toString());
                        }
                    }
                }
        );

        Button button2 = (Button) findViewById(R.id.button2);

        button2.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Log.d(TAG, "푸시서비스해제시작");
                        if (mBinder != null) {
                            try {
                                unbindService(mConnection);
                                Log.d(TAG, "PushService언바인드");
                            } catch (Exception e) {
                                e.printStackTrace();
                                txtView.setText("에러발생=" + e.toString());
                            } finally {
                                mBinder = null;
                            }
                        } else {
                            Log.d(TAG, "푸시서비스와연결되어있지않습니다.");
                            txtView.setText("푸시서비스와연결되어있지않습니다.");
                        }
                        Log.d(TAG, "푸시서비스해제종료");
                        txtView.setText("푸시서비스해제종료");
                    }
                }
        );

//        //mqtt연결
//        Button button3 = (Button) findViewById(R.id.button3);
//
//        button3.setOnClickListener(
//                new Button.OnClickListener() {
//                    public void onClick(View v) {
//                        if (mBinder != null) {
//                            try {
//                                String result = mBinder.connect("testUser", "nexus5", "200*8003");
//                                Log.d(TAG, "result=" + result);
//                            } catch (RemoteException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//        );

        //subscribe
        Button button4 = (Button) findViewById(R.id.button4);

        button4.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Log.d(TAG, "서브스크라이브시작");
                        if (mBinder != null) {
                            try {
                                long start = System.currentTimeMillis();
                                String result = mBinder.subscribe("users", 2);
                                long stop = System.currentTimeMillis();
                                txtView.setText("result=" + result + ", 걸린시간=" + (stop - start) + "ms");
                                Log.d(TAG, "result=" + result);
                            } catch (Exception e) {
                                e.printStackTrace();
                                txtView.setText("에러발생=" + e.toString());
                            }
                        } else {
                            Log.d(TAG, "푸시서비스와연결되어있지않습니다.");
                            txtView.setText("푸시서비스와연결되어있지않습니다.");
                        }
                        Log.d(TAG, "서브스크라이브종료");
                    }
                }
        );

        //unsubscribe
        Button button5 = (Button) findViewById(R.id.button5);

        button5.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Log.d(TAG, "서브스크라이브해제시작");
                        if (mBinder != null) {
                            try {
                                long start = System.currentTimeMillis();
                                String result = mBinder.unsubscribe("users");
                                long stop = System.currentTimeMillis();
                                txtView.setText("result=" + result + ", 걸린시간=" + (stop - start) + "ms");
                                Log.d(TAG, "result=" + result);
                            } catch (Exception e) {
                                e.printStackTrace();
                                txtView.setText("에러발생=" + e.toString());
                            }
                        } else {
                            Log.d(TAG, "푸시서비스와연결되어있지않습니다.");
                            txtView.setText("푸시서비스와연결되어있지않습니다.");
                        }
                        Log.d(TAG, "서브스크라이브해제종료");
                    }
                }
        );

        //preCheck
        Button button6 = (Button) findViewById(R.id.button6);

        button6.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Log.d(TAG, "프리첵시작");
                        if (mBinder != null) {
                            try {
                                long start = System.currentTimeMillis();
                                String result = mBinder.preCheck("testUser", "users");
                                long stop = System.currentTimeMillis();
                                txtView.setText("result=" + result + ", 걸린시간=" + (stop - start) + "ms");
                                Log.d(TAG, "result=" + result);
                            } catch (Exception e) {
                                e.printStackTrace();
                                txtView.setText("에러발생=" + e.toString());
                            }
                        } else {
                            Log.d(TAG, "푸시서비스와연결되어있지않습니다.");
                            txtView.setText("푸시서비스와연결되어있지않습니다.");
                        }
                        Log.d(TAG, "프리첵종료");
                    }
                }
        );

        //subscriptions
        Button button7 = (Button) findViewById(R.id.button7);

        button7.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Log.d(TAG, "서브스크립션가져오기시작");
                        if (mBinder != null) {
                            try {
                                long start = System.currentTimeMillis();
                                String result = mBinder.getSubscriptions();
                                long stop = System.currentTimeMillis();
                                txtView.setText("result=" + result + ", 걸린시간=" + (stop - start) + "ms");
                                Log.d(TAG, "result=" + result);
                            } catch (Exception e) {
                                e.printStackTrace();
                                txtView.setText("에러발생=" + e.toString());
                            }
                        } else {
                            Log.d(TAG, "푸시서비스와연결되어있지않습니다.");
                            txtView.setText("푸시서비스와연결되어있지않습니다.");
                        }
                        Log.d(TAG, "서브스크립션가져오기종료");
                    }
                }
        );

        //isConnected
        Button button8 = (Button) findViewById(R.id.button8);

        button8.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Log.d(TAG, "mqttSession연결상태가져오기시작");
                        if (mBinder != null) {
                            try {
                                long start = System.currentTimeMillis();
                                boolean result = mBinder.isConnected();
                                long stop = System.currentTimeMillis();
                                txtView.setText("연결상태=" + result + ", 걸린시간=" + (stop - start) + "ms");
                                Log.d(TAG, "연결상태=" + result);
                            } catch (Exception e) {
                                e.printStackTrace();
                                txtView.setText("에러발생=" + e.toString());
                            }
                        } else {
                            Log.d(TAG, "푸시서비스와연결되어있지않습니다.");
                            txtView.setText("푸시서비스와연결되어있지않습니다.");
                        }
                        Log.d(TAG, "mqttSession연결상태가져오기종료");
                    }
                }
        );

        //existPMAByUFMI
        Button button9 = (Button) findViewById(R.id.button9);

        button9.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Log.d(TAG, "existPMAByUFMI시작");
                        if (mBinder != null) {
                            try {
                                long start = System.currentTimeMillis();
                                String result = mBinder.existPMAByUFMI("200*800");
                                long stop = System.currentTimeMillis();
                                txtView.setText("체크결과=" + result + ", 걸린시간=" + (stop - start) + "ms");
                                Log.d(TAG, "체크결과=" + result);
                            } catch (Exception e) {
                                e.printStackTrace();
                                txtView.setText("에러발생=" + e.toString());
                            }
                        } else {
                            Log.d(TAG, "푸시서비스와연결되어있지않습니다.");
                            txtView.setText("푸시서비스와연결되어있지않습니다.");
                        }
                        Log.d(TAG, "existPMAByUFMI종료");
                    }
                }
        );

        //existPMAByUserID
        Button button10 = (Button) findViewById(R.id.button10);
        button10.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Log.d(TAG, "existPMAByUserID시작");
                        if (mBinder != null) {
                            try {
                                long start = System.currentTimeMillis();
                                String result = mBinder.existPMAByUserID("testUser");
                                long stop = System.currentTimeMillis();
                                txtView.setText("체크결과=" + result + ", 걸린시간=" + (stop - start) + "ms");
                                Log.d(TAG, "체크결과=" + result);
                            } catch (Exception e) {
                                e.printStackTrace();
                                txtView.setText("에러발생=" + e.toString());
                            }
                        } else {
                            Log.d(TAG, "푸시서비스와연결되어있지않습니다.");
                            txtView.setText("푸시서비스와연결되어있지않습니다.");
                        }
                        Log.d(TAG, "existPMAByUserID종료");
                    }
                }
        );

        //sendMsg
        Button button11 = (Button) findViewById(R.id.button11);

        button11.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Log.d(TAG, "sendMsg시작");
                        Log.d(TAG, "mBinder=" + mBinder);
                        if (mBinder != null) {
                            try {
                                long start = System.currentTimeMillis();
                                String result = mBinder.sendMsg("testUser", "users", 2, "application/json", "{\"key\":\"value\"}", 60000);
                                long stop = System.currentTimeMillis();
                                txtView.setText("체크결과=" + result + ", 걸린시간=" + (stop - start) + "ms");
                                Log.d(TAG, "체크결과=" + result);
                            } catch (Exception e) {
                                e.printStackTrace();
                                txtView.setText("에러발생=" + e.toString());
                            }
                        } else {
                            Log.d(TAG, "푸시서비스와연결되어있지않습니다.");
                            txtView.setText("푸시서비스와연결되어있지않습니다.");
                        }
                        Log.d(TAG, "sendMsg종료");
                    }
                }
        );

        //펌웨어업데이트
//        Button button12 = (Button) findViewById(R.id.button12);
//
//        button12.setOnClickListener(
//                new Button.OnClickListener() {
//                    public void onClick(View v) {
//                        Intent explicitIntent = null;
//                        // Package 설치여부 확인
//                        PackageManager pm = getPackageManager();
//                        try {
//                            ApplicationInfo appInfo = pm.getApplicationInfo("kr.co.adflow.push", PackageManager.GET_META_DATA);
//                            Log.d(TAG, "appInfo=" + appInfo);
//
//                            Intent implicitIntent = new Intent("kr.co.adflow.push.service.PushService");
//                            List<ResolveInfo> resolveInfos = pm.queryIntentServices(implicitIntent, 0);
//
//                            Log.d(TAG, "resolveInfos=" + resolveInfos);
//
//                            ResolveInfo resolveInfo = resolveInfos.get(0);
//                            String packageName = resolveInfo.serviceInfo.packageName;
//                            Log.d(TAG, "packageName=" + packageName);
//                            String className = resolveInfo.serviceInfo.name;
//                            Log.d(TAG, "className=" + className);
//                            ComponentName component = new ComponentName(packageName, className);
//                            explicitIntent = new Intent();
//                            explicitIntent.setComponent(component);
//                            explicitIntent.setAction("kr.co.adflow.push.service.FWUPDATE");
//                            explicitIntent.putExtra("Message", "테스트");
//                            Log.d(TAG, "explicitIntent=" + explicitIntent);
//
//                        } catch (PackageManager.NameNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                        startService(explicitIntent);
//                    }
//                }
//        );
    }


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


    // 서비스의 인터페이스와 상호작용하는 클래스
    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            txtView.setText("푸시서비스가연결되었습니다.");
            mBinder = IPushService.Stub.asInterface(service);
//            LocalBinder binder = (LocalBinder) service;
//            Log.d(TAG, "bindedService=" + binder.getService());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinder = null;
            Log.d(TAG, "onServiceDisconnected");
            txtView.setText("푸시서비스가해제되었습니다.");
        }
    };


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        if (mBinder != null) {
            try {
                unbindService(mConnection);
                Log.d(TAG, "PushService언바인드");
            } catch (Exception e) {
                e.printStackTrace();
                txtView.setText("에러발생=" + e.toString());
            } finally {
                mBinder = null;
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "onPostResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }
}
