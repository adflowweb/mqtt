package kr.co.adflow.push;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Map;

import kr.co.adflow.push.util.DebugLog;

public class TRLogger extends Activity {

    private WebView mWebView;
    private Activity activity;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DebugLog.d("onCreate 시작()");
        setContentView(R.layout.activity_main);
        activity = this;
        setLayout();
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        mWebView.getSettings().setJavaScriptEnabled(true);

        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNum = tMgr.getLine1Number();
        DebugLog.d("전화번호 = " + phoneNum);

        Map<String, String> extraHeaders = new HashMap<String, String>();

        //Add condition only if you want to do on over than Android 4.4
        if (Build.VERSION.SDK_INT >= 4.4) {
            extraHeaders.put("X-Requested-With", phoneNum.substring(5));
        }

        mWebView.loadUrl(url, extraHeaders);
        mWebView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String data) {
                // do your stuff here
                DebugLog.d("onPageFinished url = " + data);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DebugLog.d("I am the UI thread");
                        activity.finish();
                    }
                }, 5000);
//                activity.finish();
//                TRLogger.this.mWebView.post(new Runnable() {
//                    public void run() {
//                        //Log.d("UI thread", "I am the UI thread");
//                        DebugLog.d("I am the UI thread");
//                        activity.finish();
//                    }
//                });
            }
        });

        DebugLog.d("onCreate 종료()");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    /*
     * Layout
     */
    private void setLayout() {
        mWebView = (WebView) findViewById(R.id.webview);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DebugLog.d("onStart 시작()");
        DebugLog.d("onStart 종료()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        DebugLog.d("onPause 시작()");
        DebugLog.d("onPause 종료()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        DebugLog.d("onStop 시작()");
        DebugLog.d("onStop 종료()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DebugLog.d("onDestroy 시작()");
        DebugLog.d("onDestroy 종료()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        DebugLog.d("onResume 시작()");
        DebugLog.d("onResume 종료()");
    }
}
