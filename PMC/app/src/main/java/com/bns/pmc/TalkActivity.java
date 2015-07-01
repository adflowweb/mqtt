package com.bns.pmc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.Loader;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bns.pmc.adapter.TalkAdapter;
import com.bns.pmc.provider.DataColumn;
import com.bns.pmc.provider.DataColumn.MessageColumn;
import com.bns.pmc.receiver.PMCService;
import com.bns.pmc.util.CommonUtil;
import com.bns.pmc.util.Configure;
import com.bns.pmc.util.IPushUtil;
import com.bns.pmc.util.Log;
import com.bns.pmc.util.PMCType;
import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import kr.co.adflow.push.IPushService;

public class TalkActivity extends Activity implements LoaderCallbacks<Cursor> {
    // Static
    public static String s_strCurrNumber = null;
    public static boolean s_bNew = false;
    // Progress Dialog Object
    private ProgressDialog prgDialog;
    // Progress Dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    private Context m_context = this;
    private Configure m_configure;
    private ListView m_listView;
    private TalkAdapter m_adapter;

    private TextView m_tvNotiNew;

    private String m_strFleetNumber;
    private String m_strTitle;
    private String m_strNumber;
    private boolean m_bControl = false;
    private int m_nLinkPosition = 0;

    private boolean m_bInit = false;

    // The loader's unique id. Loader ids are specific to the Activity
    private static final int LOADER_ID = 1;
    // The callbacks through which we will interact with the LoaderManager.
    private LoaderManager.LoaderCallbacks<Cursor> m_Callbacks = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(PMCType.TAG, "[onCreate]");
        setContentView(R.layout.activity_talk);
        m_configure = Configure.getInstance(m_context);
        m_strFleetNumber = m_configure.getFleetNumber();
        String strUfmi = m_configure.getUFMI();
        // Intent
        {
            Intent intent = getIntent();
            String strTitle = null;
            String strNumber = intent.getStringExtra(PMCType.BNS_PMC_INTENT_EXTRA_NUMBER);
            Log.d(PMCType.TAG, "strNumber=" + strNumber);
            boolean bControl = intent.getBooleanExtra(PMCType.BNS_PMC_INTENT_EXTRA_CONTROL, false);
            Log.d(PMCType.TAG, "bControl=" + bControl);
            String strName = null;
            if (CommonUtil.checkPttGroupNumFormat(strNumber)) {
                strName = CommonUtil.searchName_FromContact_ByPtt(m_context, strNumber, strUfmi);
                Log.d(PMCType.TAG, "strName=" + strName);
            } else if (CommonUtil.checkPttCallNumFormat(strNumber)) {
                String strFleetMemberNum = CommonUtil.conv_UrbanFleetMemberNumToFleetMemberNum(strNumber);
                Log.d(PMCType.TAG, "strFleetMemberNum=" + strFleetMemberNum);
                strName = CommonUtil.searchName_FromContact_ByPtt(m_context, strFleetMemberNum, strUfmi);
                Log.d(PMCType.TAG, "strName=" + strName);
                if (strName == null) {
                    strName = strFleetMemberNum;
                }
            }

            if (TextUtils.isEmpty(strName)) {
                boolean bequals = CommonUtil.comp_FleetNumber(m_strFleetNumber, strNumber);
                Log.d(PMCType.TAG, "bequals=" + bequals);
                if (bequals) {
                    strTitle = CommonUtil.getUserNumber(strNumber);
                    Log.d(PMCType.TAG, "strTitle=" + strTitle);
                } else {
                    strTitle = strNumber;
                    Log.d(PMCType.TAG, "strTitle=" + strTitle);
                }
            } else {
                strTitle = strName;
                Log.d(PMCType.TAG, "strTitle=" + strTitle);
            }
            m_strTitle = strTitle;
            m_strNumber = strNumber;
            s_strCurrNumber = strNumber;
            m_bControl = bControl;
        }
        // Title 설정.
        setTitle(m_strTitle);
        // 진입하는 Talk 방이 Notification에 떠 있는 Talk 방이면 Notification을 삭제.
        processNotification(m_strNumber);
        // Update Read : Talk 방안에 안 읽은 메시지들을 모두 읽은 메시지로 update.
        processUpdateRead(m_strNumber);
        // ack가 필요하면 전송.
        processAck(m_strNumber);

        m_listView = (ListView) findViewById(R.id.listView_talk);

        TextView tvBtLeft = (TextView) findViewById(R.id.bottom_button_left);
        TextView tvBtRight = (TextView) findViewById(R.id.bottom_button_right);
        tvBtLeft.setText(R.string.reply);

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_pressed}, // pressed
                new int[]{android.R.attr.state_focused}, // focused
                new int[]{android.R.attr.state_enabled} // enabled
        };

        int[] colors = new int[]{
                Color.parseColor("#ff000000"),
                Color.parseColor("#999999"),
                Color.parseColor("#ffffffff")
        };

        ColorStateList list = new ColorStateList(states, colors);
        tvBtLeft.setTextColor(list);
        //tvBtLeft.setClickable(true);
        //tvBtLeft.setFocusableInTouchMode(true);

        //testCode
        tvBtLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(PMCType.TAG, "답장버튼이 클릭되었습니다.");
                optionReply();
            }
        });
        //testCodeEnd


        tvBtRight.setText(R.string.back);
        //tvBtRight.setFocusable(false);
        tvBtRight.setTextColor(list);
        //tvBtLeft.setClickable(true);
        //tvBtLeft.setFocusableInTouchMode(true);

        //testCode
        tvBtRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DoIt(v);
                Log.d(PMCType.TAG, "이전버튼이 클릭되었습니다.");
                ((Activity) m_context).onBackPressed();
            }
        });
        //testCodeEnd


        // 관제 메시지일 때 reply가 표시되지 않도록 설정.
        if (m_bControl) {
            tvBtLeft.setFocusable(false);
            tvBtLeft.setEnabled(false);
            tvBtLeft.setText("");
        }

        // notiNew
        m_tvNotiNew = (TextView) findViewById(R.id.textView_notiNewMsg);

        m_adapter = new TalkAdapter(m_context, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        // Associate the (now empty) adapter with the ListView.
        m_listView.setAdapter(m_adapter);
        m_listView.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position == (m_adapter.getCount() - 1)) {
                    if (m_tvNotiNew.isShown()) {
                        m_tvNotiNew.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        m_listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.i(PMCType.TAG, "[Item Click Pos]" + position);

                Cursor cursor = (Cursor) m_adapter.getItem(position);
                int nRecv = cursor.getInt(cursor.getColumnIndex(MessageColumn.DB_COLUMN_RECV));
                TextView textView = null;

                if (nRecv == DataColumn.COLUMN_RECV_SEND) {
                    textView = (TextView) view.findViewById(R.id.textView_talk_mymsg);
                } else {
                    textView = (TextView) view.findViewById(R.id.textView_talk_yourmsg);
                }

                //testCode
                byte[] data = cursor.getBlob((cursor.getColumnIndex(MessageColumn.DB_COLUMN_DATA)));
                if (data != null) {
                    String obj = new String(data);
                    Log.i(PMCType.TAG, "data=" + obj);
                    int dataFormat = cursor.getInt((cursor.getColumnIndex(MessageColumn.DB_COLUMN_DATA_TYPE)));

                    //이미지일경우 뷰어로 보여준다.
                    if (dataFormat == DataColumn.COLUMN_DATA_TYPE_JPG
                            || dataFormat == DataColumn.COLUMN_DATA_TYPE_BMP
                            || dataFormat == DataColumn.COLUMN_DATA_TYPE_PNG) {
                        try {
                            //파일존재유무체크하고
                            String root = Environment.getExternalStorageDirectory().toString();
                            File storageDir = new File(root + "/pmc/images/");
                            JSONObject json = new JSONObject(obj);
                            File file = new File(storageDir, json.getString("name") + "." + json.getString("format"));
                            if (!file.exists()) {
                                Log.d(PMCType.TAG, "로컬에파일존재하지않습니다");
                                //없으면 다운로드후 viewing
                                String token = IPushUtil.getToken(PMCService.m_Binder);
                                Log.d(PMCType.TAG, "token=" + token);
                                setStrictMode();
                                //download
                                JSONObject jsonData = new JSONObject();
                                jsonData.put("url", NewActivity.CONTENT_UPLOAD_URL + json.getString("user") + "/" + json.getString("name") + "." + json.getString("format"));
                                jsonData.put("path", root + "/pmc/images/" + json.getString("name") + "." + json.getString("format"));
                                jsonData.put("file", "file://" + storageDir + "/" + json.getString("name") + "." + json.getString("format"));
                                jsonData.put("format", json.getString("format"));
                                jsonData.put("token", token);
                                new DownloadFromCTS().execute(jsonData);
//                                HttpRequest request = HttpRequest.get(NewActivity.CONTENT_UPLOAD_URL + json.getString("user") + "/" + json.getString("name") + "." + json.getString("format"))
//                                        .header("token", token);
//                                Log.d(PMCType.TAG, "이미지다운로드응답코드=" + request.code());
//
//                                if (request.ok()) {
//                                    File output = new File(root + "/pmc/images/" + json.getString("name") + "." + json.getString("format"));
//                                    request.receive(output);
//                                }
                            } else {
                                //존재하면 바로 viewing
                                Log.d(PMCType.TAG, "로컬에파일존재합니다");
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.parse("file://" + storageDir + "/" + json.getString("name") + "." + json.getString("format")), "image/*");
                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(PMCType.TAG, "에러발생" + e);
                        }
                    } else {
                        //이미지가아니지만 첨부파일이 있는경우
                        //로컬 다운로드
                        try {
                            String root = Environment.getExternalStorageDirectory().toString();
                            File storageDir = new File(root + "/pmc/files/");
                            JSONObject json = new JSONObject(obj);
                            File file = new File(storageDir, json.getString("name") + "." + json.getString("format"));
                            if (!file.exists()) {
                                Log.d(PMCType.TAG, "로컬에파일존재하지않습니다");
                                //없으면 다운로드
                                String token = IPushUtil.getToken(PMCService.m_Binder);
                                Log.d(PMCType.TAG, "token=" + token);
                                setStrictMode();
                                //download
                                JSONObject jsonData = new JSONObject();
                                jsonData.put("url", NewActivity.CONTENT_UPLOAD_URL + json.getString("user") + "/" + json.getString("name") + "." + json.getString("format"));
                                jsonData.put("path", root + "/pmc/files/" + json.getString("name") + "." + json.getString("format"));
                                jsonData.put("file", "file://" + storageDir + "/" + json.getString("name") + "." + json.getString("format"));
                                jsonData.put("format", json.getString("format"));
                                jsonData.put("token", token);
                                new DownloadFromCTS().execute(jsonData);

//                                HttpRequest request = HttpRequest.get(NewActivity.CONTENT_UPLOAD_URL + json.getString("user") + "/" + json.getString("name") + "." + json.getString("format"))
//                                        .header("token", token);
//                                Log.d(PMCType.TAG, "이미지다운로드응답코드=" + request.code());
//
//                                if (request.ok()) {
//                                    File output = new File(root + "/pmc/files/" + json.getString("name") + "." + json.getString("format"));
//                                    request.receive(output);
//                                    m_adapter.notifyDataSetChanged();
//                                }
                            } else {
                                Log.d(PMCType.TAG, "로컬에파일존재합니다");
                                //mp3 파일일 경우 바로 플레이
                                if (json.getString("format").equalsIgnoreCase("mp3")) {
                                    Intent intent = new Intent();
                                    intent.setAction(android.content.Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.fromFile(file), "audio/*");
                                    startActivity(intent);
                                } else if (json.getString("format").equalsIgnoreCase("3gp")) {
                                    Intent intent = new Intent();
                                    intent.setAction(android.content.Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.fromFile(file), "video/3gpp");
                                    startActivity(intent);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


//                        String root = Environment.getExternalStorageDirectory().toString();
//                        File storageDir = new File(root + "/pmc/images/");
//                        JSONObject json = null;
//
//
//                        try {
//                            //Uri uri = Uri.fromFile(file);
//                            json = new JSONObject(obj);
//                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                            intent.setClassName("com.android.htmlviewer", "com.android.htmlviewer.HTMLViewerActivity");
//                            intent.setDataAndType(Uri.parse("file://" + storageDir + "/" + json.getString("name") + "." + json.getString("format")), "text/plain");
//                            startActivity(intent);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }

//                        Intent explicitIntent = null;
//                        // Package 설치여부 확인
//                        PackageManager pm = getPackageManager();
//                        try {
//                            json = new JSONObject(obj);
//                            ApplicationInfo appInfo = pm.getApplicationInfo("com.android.htmlviewer", PackageManager.GET_META_DATA);
//                            Log.d(PMCType.TAG, "appInfo=" + appInfo);
//
//                            Intent implicitIntent = new Intent("com.android.htmlviewer.HTMLViewerActivity");
//                            List<ResolveInfo> resolveInfos = pm.queryIntentServices(implicitIntent, 0);
//
//                            Log.d(PMCType.TAG, "resolveInfos=" + resolveInfos);
//
//                            ResolveInfo resolveInfo = resolveInfos.get(0);
//                            String packageName = resolveInfo.serviceInfo.packageName;
//                            Log.d(PMCType.TAG, "packageName=" + packageName);
//                            String className = resolveInfo.serviceInfo.name;
//                            Log.d(PMCType.TAG, "className=" + className);
//                            ComponentName component = new ComponentName(packageName, className);
//                            explicitIntent = new Intent();
//                            explicitIntent.setComponent(component);
//                            explicitIntent.setAction(Intent.ACTION_VIEW);
//                            explicitIntent.setDataAndType(Uri.parse("file://" + storageDir + "/" + json.getString("name") + "." + json.getString("format")), "text/plain");
//                            Log.d(PMCType.TAG, "explicitIntent=" + explicitIntent);
//                            startActivity(explicitIntent);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    }
                }
                //MessageColumn.DB_COLUMN_DATA, imgJson.toString().getBytes()
                //testCodeEnd

                URLSpan[] uriSpan = textView.getUrls();
                if (uriSpan.length != 0) {
                    // 15.03.23 autolink 중복 방지
                    ArrayList<String> list = new ArrayList<String>();
                    for (int i = 0; i < uriSpan.length; i++) {
                        String strUrl = uriSpan[i].getURL();
                        Log.d(PMCType.TAG, "strUrl=" + strUrl);
                        if (list.contains(strUrl) == false)
                            list.add(strUrl);

                    }

                    final String[] arr = new String[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        arr[i] = list.get(i);
                    }
                    //.

                    AlertDialog.Builder ab = new AlertDialog.Builder(m_context);
                    m_nLinkPosition = 0;
                    ab.setTitle(R.string.link);
                    ab.setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent,
                                                   View view, int position, long id) {
                            m_nLinkPosition = position;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub

                        }
                    });
                    ab.setOnKeyListener(new OnKeyListener() {

                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            Log.i(PMCType.TAG, "link pos=" + m_nLinkPosition);

                            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                if (event.getKeyCode() == PMCType.BNS_PMC_KEY_CODE_PTT_CALL &&
                                        event.getRepeatCount() == 0) {
                                    String[] strArr = arr[m_nLinkPosition].split("\\:");
                                    //String strIdx = strArr[0];
                                    String strContext = strArr[1];

                                    Intent i = new Intent();
                                    i.setAction(PMCType.BNS_PMC_PTT_OUTCOMING_ACTION);
                                    i.putExtra(PMCType.BNS_PMC_PTT_QUICK_NUMBER_EXTRA_VALUE, strContext);
                                    sendBroadcast(i);
                                    finish();

                                    return true;
                                } else if (event.getKeyCode() == KeyEvent.KEYCODE_CALL) {
                                    try {
                                        String[] strArr = arr[m_nLinkPosition].split("\\:");
                                        //String strIdx = strArr[0];
                                        String strContext = strArr[1];
                                        //testCode
                                        Log.d(PMCType.TAG, "callNumber=" + strContext);
                                        //testCodeEnd
                                        Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + strContext));
                                        Log.d(PMCType.TAG, "intent=" + i);
                                        startActivity(i);
                                        finish();

                                        return true;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
                                    String[] strArr = arr[m_nLinkPosition].split("\\:");
                                    //String strIdx = strArr[0];
                                    String strContext = strArr[1];

                                    // URL
                                    Toast.makeText(m_context, strContext, Toast.LENGTH_SHORT).show();
                                    finish();

                                    return true;
                                }
                            }
                            return false;
                        }
                    });
                    ab.setItems(arr, new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    }).show();
                    /*ab.setSingleChoiceItems(arr, 0, new DialogInterface.OnClickListener() {
                        @Override
						public void onClick(DialogInterface dialog, int which) {
							Log.i(PMCType.TAG, "[Dialog Click]" + arr[which].toString());
							
							String[] strArr = arr[which].split("\\:");
							String strIdx = strArr[0];
							String strContext = strArr[1];
							
							if (TextUtils.equals("tel", strIdx)) {
								Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+strContext));
							    startActivity(i);
							} else if (TextUtils.equals("ptt", strIdx) || TextUtils.equals("group", strIdx)) {
								Intent i = new Intent();
								i.setAction(A1Type.BNS_PTT_OUTCOMING_ACTION);
								i.putExtra(A1Type.BNS_PTT_QUICK_NUMBER_EXTRA_VALUE, strContext);
								sendBroadcast(i);
							} else { // URL
								// URL은 보류
								Toast.makeText(m_context, strContext, Toast.LENGTH_SHORT).show();
							}
							dialog.dismiss();
						}
						
					}).show();*/
                }
            }

        });

        // Initialize the Loader with id '1' and callbacks 'mCallbacks'.
        // If the loader doesn't already exist, one is created. Otherwise,
        // the already created Loader is reused. In either case, the
        // LoaderManager will manage the Loader across the Activity/Fragment
        // lifecycle, will receive any new loads once they have completed,
        // and will report this new data back to the 'mCallbacks' object.
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, m_Callbacks);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(PMCType.TAG, "[onDestroy]");
        s_strCurrNumber = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(PMCType.TAG, "[onResume]");
        //m_Adapter.notifyDataSetChanged();
        s_strCurrNumber = m_strNumber;
        m_adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(PMCType.TAG, "[onPause]");
        //m_Adapter.notifyDataSetChanged();
        s_strCurrNumber = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (m_adapter != null) {
            if (m_adapter.getCount() == 0) {
                finish();
            }
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_SOFT_LEFT) {
                // 관제 메시지일때 reply는 안되게..
                if (m_bControl)
                    return true;

                optionReply();
                return true;
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                Intent intent = new Intent();
                intent.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_NUMBER, m_strNumber);
                this.setResult(Activity.RESULT_CANCELED, intent);
                this.finish();
                return true;
            } else if (event.getKeyCode() == PMCType.BNS_PMC_KEY_CODE_PTT_CALL
                    && event.getRepeatCount() == 0) {
                CommonUtil.callPtt(m_context, m_strNumber);
                return true;
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_CALL) {
                // 15.03.19 Send버튼 정리
                if (TextUtils.isDigitsOnly(m_strNumber))
                    CommonUtil.callNormal(m_context, m_strNumber);
                else
                    Toast.makeText(m_context, R.string.no_sendcall, Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    // Show Dialog Box with Progress bar
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                prgDialog = new ProgressDialog(this);
                prgDialog.setMessage("다운로드 중...");
                prgDialog.setIndeterminate(false);
                prgDialog.setMax(100);
                prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                prgDialog.setCancelable(false);
                prgDialog.show();
                return prgDialog;
            default:
                return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.talk, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(PMCType.TAG, "onOptionsItemSelected시작(item=" + item + ")");
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.menu_talk_forwarding:
                optionForwarding();
            /*Intent i = new Intent();
            i.setAction("com.bns.pmc.action.newmsg.contact");
			i.putExtra("name", "하하");
			i.putExtra("number", "50*1234");
			sendBroadcast(i);*/
                break;
            case R.id.menu_talk_delete:
                optionDelete();
                break;
            case R.id.menu_talk_set:
                optionSettings();
                break;
            default:
                break;
        }
        Log.d(PMCType.TAG, "onOptionsItemSelected종료()");
        return true;
    }

    // TODO LoaderCallbacks Override 부분
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.withAppendedPath(DataColumn.CONTENT_URI_TALK_BY_NUMBER, m_strNumber);
        return new CursorLoader(m_context, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // A switch-case is useful when dealing with multiple Loaders/IDs
        switch (loader.getId()) {
            case LOADER_ID:
                // The asynchronous load is complete and the data
                // is now available for use. Only now can we associate
                // the queried Cursor with the CursorAdapter.
                m_adapter.swapCursor(cursor);

                if (m_bInit == false || s_bNew == true) {
                    m_listView.setSelection(m_adapter.getCount() - 1);
                    m_bInit = true;
                    s_bNew = false;
                }

                if (m_listView.getSelectedItemPosition() != m_adapter.getCount() - 1) {
                    Cursor c = (Cursor) m_listView.getItemAtPosition(m_adapter.getCount() - 1);
                    if (c != null) {
                        String msg = c.getString(c.getColumnIndex(MessageColumn.DB_COLUMN_MSG));
                        m_tvNotiNew.setText(msg);
                        m_tvNotiNew.setVisibility(View.VISIBLE);
                    }
                } else {
                    m_tvNotiNew.setVisibility(View.GONE);
                }
                break;
        }
        // The listview now displays the queried data.

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // For whatever reason, the Loader's data is now unavailable.
        // Remove any references to the old data by replacing it with
        // a null Cursor.
        m_adapter.swapCursor(null);
    }

    /**
     * 읽은 Notification 삭제.
     *
     * @param number
     */
    private void processNotification(String number) {
        NotificationManager nm = (NotificationManager) m_context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(number, PMCType.BNS_PMC_NOTI_ID);
    }

    /**
     * 읽은 표시.
     *
     * @param number
     */
    private void processUpdateRead(String number) {
        Uri uri = Uri.withAppendedPath(DataColumn.CONTENT_URI_UPDATE_DONT_READ_BY_NUMBER, number);
        getContentResolver().update(uri, null, null, null);
    }

    /**
     * ack가 필요하면 전송.
     *
     * @param number
     */
    private void processAck(String number) {
        final String strAckNumber = number;

        new Thread(new Runnable() {

            @Override
            public void run() {
                IPushService binder = PMCService.m_Binder;
                // 15.04.02 무조건 Ack 전송.. PMA가 처리하도록 변경.
                /*{
                    boolean bIsConnected = IPushUtil.isConnectedMqttSession(binder);
					if (bIsConnected == false)
						return;
				}*/

                Cursor c = null;
                {
                    Uri uri = Uri.withAppendedPath(DataColumn.CONTENT_URI_TALK_ACK_BY_NUMBER, strAckNumber);
                    c = getContentResolver().query(uri, null, null, null, null);
                }
                if (c != null) {
                    while (c.moveToNext()) {
                        long id = c.getLong(c.getColumnIndex(MessageColumn.DB_COLUMN_ID));
                        String strMsgID = c.getString(c.getColumnIndex(MessageColumn.DB_COLUMN_MSG_ID));
                        String strToken = c.getString(c.getColumnIndex(MessageColumn.DB_COLUMN_TOKEN));
                        // 15.04.02 무조건 Ack 전송.. PMA가 처리하도록 변경.
                        {
                            IPushUtil.ack(binder, strMsgID, strToken);
                            Uri uri = Uri.withAppendedPath(DataColumn.CONTENT_URI_UPDATE_ACK_BY_ID, Long.toString(id));
                            getContentResolver().update(uri, null, null, null);
                        }
                        /*{
                            String strResult = IPushUtil.ack(binder, strMsgID, strToken);
							
							if (TextUtils.isEmpty(strResult) == false) {
								boolean bResultSuccess = JSonUtil.responeAckResultSuccess(strResult);
								
								if (bResultSuccess) {
									Uri uri = Uri.withAppendedPath(DataColumn.CONTENT_URI_UPDATE_ACK_BY_ID, Long.toString(id));
									getContentResolver().update(uri, null, null, null);
								}
							}
						}*/
                    }
                    c.close();
                }
            }
        }).start();
    }

    /**
     * option Reply
     */
    private void optionReply() {
        // 계정 정보 확인.
        if (m_configure.getUFMI() == Configure.UFMI_INIT) {
            Toast.makeText(m_context, R.string.disconnect_init_account, Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(PMCType.TAG, "[pttNumber]" + m_strNumber);
        String strNumber = m_strNumber;
        // 임시저장파일이 존재하면
        Uri uri = Uri.withAppendedPath(DataColumn.CONTENT_URI_TALK_SAVE_BY_NUMBER, strNumber);
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        if (c != null) {
            Intent i = new Intent(m_context, NewActivity.class);
            int nID = -1;
            String strMsg = null;
            if (c.moveToFirst()) {
                nID = c.getInt(c.getColumnIndex(MessageColumn.DB_COLUMN_ID));
                strMsg = c.getString(c.getColumnIndex(MessageColumn.DB_COLUMN_MSG));
            }

            i.setAction(PMCType.BNS_PMC_ACTION_NEWMSG_REPLY);
            i.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_ID, nID);
            i.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_NUMBER, strNumber);
            i.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_CONTENT, strMsg);
            Log.d(PMCType.TAG, "[ID]" + nID + " [PttNumber]" + strNumber + " [msg]" + strMsg);
            try {
                //첨부파일
                int type = c.getInt(c.getColumnIndex(MessageColumn.DB_COLUMN_DATA_TYPE));
                if (type != 0) {
                    byte[] data = c.getBlob(c.getColumnIndex(MessageColumn.DB_COLUMN_DATA));
                    if (data != null) {
                        i.putExtra("data", data);
                        i.putExtra("dataType", type);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                c.close();
            }
            startActivityForResult(i, 100);
        }
    }

    /**
     * option Forwarding
     */
    private void optionForwarding() {
        Log.d(PMCType.TAG, "optionForwarding시작()");
        // 계정 정보 확인.
        if (m_configure.getUFMI() == Configure.UFMI_INIT) {
            Log.d(PMCType.TAG, "계정정보가등록되지않았습니다.");
            Toast.makeText(m_context, R.string.disconnect_init_account, Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor c = (Cursor) m_listView.getSelectedItem();
        Log.d(PMCType.TAG, "커서=" + c);
        if (c != null) {
            String strMsg = c.getString(c.getColumnIndex(MessageColumn.DB_COLUMN_MSG));

            Intent i = new Intent(m_context, NewActivity.class);
            i.setAction(PMCType.BNS_PMC_ACTION_NEWMSG_FORWARDING);
            i.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_CONTENT, strMsg);
            //첨부파일일경우 데이타 넣어줘야함
            byte[] data = c.getBlob(c.getColumnIndex(MessageColumn.DB_COLUMN_DATA));
            if (data != null) {
                i.putExtra("data", data);
                int type = c.getInt(c.getColumnIndex(MessageColumn.DB_COLUMN_DATA_TYPE));
                i.putExtra("dataType", type);
            }
            startActivityForResult(i, 101);
        }
        Log.d(PMCType.TAG, "optionForwarding종료()");
    }

    /**
     * option Delete
     */
    private void optionDelete() {
        AlertDialog.Builder ab = new AlertDialog.Builder(m_context);
        ab.setTitle(R.string.delete_popup_title);
        ab.setMessage(R.string.delete_popup_context_sel);
        ab.setNegativeButton(R.string.cancel, null);
        ab.setPositiveButton(R.string.delete, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strNumber = m_strNumber;
                new asyncDeleteMsg().execute(strNumber);
            }
        });
        ab.show();
    }

    /**
     * option Setting
     */
    private void optionSettings() {
        Intent i = new Intent(m_context, SettingsActivity.class);
        startActivityForResult(i, 102);
    }

    /**
     * asyncDeleteMsg
     *
     * @author kyu
     */
    private class asyncDeleteMsg extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Log.d(PMCType.TAG, "doInBackground start");

            int nResult = 0;
            int numberOfParams = params.length;
            for (int i = 0; i < numberOfParams; i++) {
                String strPttNumber = params[i];
                Uri uri = Uri.withAppendedPath(DataColumn.CONTENT_URI_DEL_BY_NUM, strPttNumber);
                getContentResolver().delete(uri, null, null);
            }
            return nResult;
        }

        @Override
        protected void onPostExecute(Integer result) {
            Toast.makeText(m_context, R.string.delete_popup_result_true, Toast.LENGTH_SHORT).show();
            TalkActivity.this.finish();
        }
    }

    /**
     *
     */
    private void setStrictMode() {
        //android.util.Log.d(TAG, "setStrictMode시작()");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        // android.util.Log.d(TAG, "setStrictMode종료()");
    }

    // Async Task Class
    class DownloadFromCTS extends AsyncTask<JSONObject, String, JSONObject> {

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            showDialog(progress_bar_type);
        }

        // Download Music File from Internet
        @Override
        protected JSONObject doInBackground(JSONObject... data) {

            ProgressBarThread progress = null;
            try {
                Log.d(PMCType.TAG, "doInBackground시작(data=" + data + ")");
                Log.d(PMCType.TAG, "url=" + data[0].getString("url"));
                Log.d(PMCType.TAG, "token=" + data[0].getString("token"));
                Log.d(PMCType.TAG, "path=" + data[0].getString("path"));
                final HttpRequest request = HttpRequest.get(data[0].getString("url")).header("token", data[0].getString("token"));
                request.getConnection().setReadTimeout(BuildConfig.HTTP_READ_TIMEOUT);
                Log.d(PMCType.TAG, "httpReadTimeout=" + request.getConnection().getReadTimeout());
                final File file = new File(data[0].getString("path"));
                //root + "/pmc/images/" + json.getString("name") + "." + json.getString("format"));
                Log.d(PMCType.TAG, "responseCode=" + request.ok());
                if (request.ok()) {
                    //프로그레스바용 쓰레드
                    progress = new ProgressBarThread(request, file);
                    progress.start();
                    request.receive(file);
                    Log.d(PMCType.TAG, "컨텐트다운로드완료");
                }
                //"file://" + storageDir + "/" + json.getString("name") + "." + json.getString("format")
                Log.d(PMCType.TAG, "doInBackground종료(return=" + data[0] + ")");
                return data[0];
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(PMCType.TAG, "컨텐트다운로드실패");
                Toast.makeText(m_context, "컨텐츠다운로드에실패하였습니다", Toast.LENGTH_SHORT);
                try {
                    if (progress != null) {
                        progress.running = false;
                        Log.d(PMCType.TAG, "프로그레스바쓰레드종료");
                    }
                    File failedFile = new File(data[0].getString("path"));
                    failedFile.delete();
                    Log.d(PMCType.TAG, "다운로드파일삭제");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                return null;
            }
        }

        class ProgressBarThread extends Thread {
            HttpRequest request;
            File file;
            public boolean running = true;

            public ProgressBarThread(HttpRequest request, File file) {
                this.request = request;
                this.file = file;
            }

            @Override
            public void run() {
                while (request.getConnection().getContentLength() != file.length() && running) {
                    double totallength = request.getConnection().getContentLength();
                    double downloadLength = file.length();
                    Log.d(PMCType.TAG, "실제파일크기=" + totallength);
                    Log.d(PMCType.TAG, "받은파일크기=" + downloadLength);
                    //Log.d(PMCType.TAG, "다운로드퍼센트=" + Math.round((downloadLength / totallength * 100)));
                    publishProgress("" + Math.round((downloadLength / totallength * 100)));
                    SystemClock.sleep(800);
                }
            }
        }

        // While Downloading Music File
        protected void onProgressUpdate(String... progress) {
            // Set progress percentage
            prgDialog.setProgress(Integer.parseInt(progress[0]));
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(JSONObject data) {
            Log.d(PMCType.TAG, "onPostExecute시작(data=" + data + ")");
            // Dismiss the dialog after the Music file was downloaded
            dismissDialog(progress_bar_type);
            //Toast.makeText(getApplicationContext(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();
            if (data != null) {
                try {
                    if (data.getString("format").equals("jpg")
                            || data.getString("format").equals("bmp")
                            || data.getString("format").equals("png")) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        //"file://" + storageDir + "/" + json.getString("name") + "." + json.getString("format")
                        intent.setDataAndType(Uri.parse(data.getString("file")), "image/*");
                        Log.d(PMCType.TAG, "이미지뷰어호출");
                        startActivity(intent);
                    } else {
                        //각종파일뷰어처리의 시작점..
                    }
                    Log.d(PMCType.TAG, "어댑터노티호출");
                    m_adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.d(PMCType.TAG, "onPostExecute종료()");
        }
    }
}
