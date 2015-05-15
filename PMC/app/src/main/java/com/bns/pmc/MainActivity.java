package com.bns.pmc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bns.pmc.adapter.MsgAdapter;
import com.bns.pmc.provider.DataColumn;
import com.bns.pmc.provider.DataColumn.MessageColumn;
import com.bns.pmc.receiver.PMCService;
import com.bns.pmc.util.CommonUtil;
import com.bns.pmc.util.Configure;
import com.bns.pmc.util.Log;
import com.bns.pmc.util.PMCType;

public class MainActivity extends Activity implements LoaderCallbacks<Cursor> {
    private Context m_context = this;
    private ListView m_listView;
    private MsgAdapter m_adapter;
    private Configure m_configure;
    private AppEULA eula;

    // The loader's unique id. Loader ids are specific to the Activity
    private static final int LOADER_ID = 1;
    // The callbacks through which we will interact with the LoaderManager.
    private LoaderManager.LoaderCallbacks<Cursor> m_callBacks = this;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(PMCType.TAG, "[onNewIntent]");
        // intent
        {
            if (intent != null) {
                String strAction = intent.getAction();
                Log.i(PMCType.TAG, "Intent Action= " + strAction);
                if (TextUtils.isEmpty(strAction) == false) {
                    if (strAction.equals(PMCType.BNS_PMC_PMCSERVICE_NOTIFICATION) ||
                            strAction.equals(PMCType.BNS_PMC_PMCSERVICE_NOTIPOPUP)) {
                        String strNumber = intent.getStringExtra(PMCType.BNS_PMC_INTENT_EXTRA_NUMBER);

                        if (TextUtils.isEmpty(strNumber) == false) {
                            Intent i = new Intent();
                            i.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_NUMBER, strNumber);
                            i.setClass(m_context, TalkActivity.class);
                            startActivity(i);
                        } else {
                            Log.d(PMCType.TAG, "Number is Null");
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(PMCType.TAG, "[onCreate]");

        m_configure = Configure.getInstance(m_context);


        // Service가 돌지 않으면 서비스를 실행시킴.
        if (CommonUtil.checkServiceRunning(m_context, PMCService.class.getName()) == false) {
            Intent i = new Intent(this, PMCService.class);
            i.setAction(PMCType.BNS_PMC_SERVICE_START_ACTION);
            startService(i);
        }
        // intent
        {
            Intent intent = getIntent();
            if (intent != null) {
                String strAction = intent.getAction();
                Log.i(PMCType.TAG, "Intent Action= " + strAction);
                if (TextUtils.isEmpty(strAction) == false) {
                    if (strAction.equals(PMCType.BNS_PMC_PMCSERVICE_NOTIFICATION) ||
                            strAction.equals(PMCType.BNS_PMC_PMCSERVICE_NOTIPOPUP)) {
                        String strNumber = intent.getStringExtra(PMCType.BNS_PMC_INTENT_EXTRA_NUMBER);

                        if (TextUtils.isEmpty(strNumber) == false) {
                            Intent i = new Intent();
                            i.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_NUMBER,
                                    intent.getStringExtra(PMCType.BNS_PMC_INTENT_EXTRA_NUMBER));
                            i.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_CONTROL,
                                    intent.getBooleanExtra(PMCType.BNS_PMC_INTENT_EXTRA_CONTROL, false));
                            i.setClass(m_context, TalkActivity.class);
                            startActivity(i);
                        } else {
                            Log.d(PMCType.TAG, "Number is Null");
                        }
                    }
                }
            }
        }

        m_listView = (ListView) findViewById(R.id.listView_main_msg);
        TextView tvBtLeft = (TextView) findViewById(R.id.bottom_button_left);
        TextView tvBtRight = (TextView) findViewById(R.id.bottom_button_right);
        tvBtLeft.setText(R.string.new_msg);
        tvBtRight.setText(R.string.back);

        m_adapter = new MsgAdapter(m_context, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        // Associate the (now empty) adapter with the ListView.
        m_listView.setAdapter(m_adapter);
        m_listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Cursor c = (Cursor) parent.getItemAtPosition(position);
                if (c != null) {
                    String strNumber = c.getString(c.getColumnIndex(MessageColumn.DB_COLUMN_NUMBER));
                    int nType = c.getInt(c.getColumnIndex(MessageColumn.DB_COLUMN_NUMBER_TYPE));
                    boolean bControl = false;
                    if (nType == DataColumn.COLUMN_NUMBER_TYPE_CONTROL)
                        bControl = true;

                    Intent i = new Intent(m_context, TalkActivity.class);
                    i.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_NUMBER, strNumber);
                    i.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_CONTROL, bControl);
                    startActivity(i);
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
        lm.initLoader(LOADER_ID, null, m_callBacks);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(PMCType.TAG, "[onStart]");

        // Show EULA
        eula = new AppEULA(this);
        eula.show();

        // 15.04.02 계정 동기화.
        {
            Log.i(PMCType.TAG, "Account Sync");

            Intent i = new Intent();
            i.setAction(PMCType.UNI_PMC_PTT_SET_PTT_NUMBER_INFO_SYNC_ACTION);
            m_context.sendBroadcast(i);
        }

    }

    private PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = this.getPackageManager().getPackageInfo(
                    this.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(PMCType.TAG, "[onDestroy]");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(PMCType.TAG, "[onStop]");
        Log.i(PMCType.TAG, "eula=" + eula);
        if (eula != null) {
            eula.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(PMCType.TAG, "[onResume]");
        m_adapter.notifyDataSetChanged();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d("MainActivity", "dispatchKeyEvent시작(event=" + event + ")");
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_SOFT_LEFT) {
                optionNewMessage();
                return true;
            } else if (event.getKeyCode() == PMCType.BNS_PMC_KEY_CODE_PTT_CALL &&
                    event.getRepeatCount() == 0) {
                Cursor c = (Cursor) m_listView.getSelectedItem();
                if (c != null) {
                    String strNumber = c.getString(c.getColumnIndex(DataColumn.MessageColumn.DB_COLUMN_NUMBER));
                    Log.d("MainActivity", "pttNumber=" + strNumber);
                    CommonUtil.callPtt(m_context, strNumber);
                }
                return true;
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_CALL) {
                Cursor c = (Cursor) m_listView.getSelectedItem();
                if (c != null) {
                    String strNumber = c.getString(c.getColumnIndex(DataColumn.MessageColumn.DB_COLUMN_NUMBER));
                    Log.d("MainActivity", "callNumber=" + strNumber + ")");
                    Log.d("MainActivity", "digit=" + TextUtils.isDigitsOnly(strNumber) + ")");
                    // 15.03.19 Send버튼 정리
                    if (TextUtils.isDigitsOnly(strNumber))
                        CommonUtil.callNormal(m_context, strNumber);
                    else
                        Toast.makeText(m_context, R.string.no_sendcall, Toast.LENGTH_SHORT).show();
                    return true;
                }
                return super.dispatchKeyEvent(event);
            }
        }
        Log.d("MainActivity", "dispatchKeyEvent종료(event=" + event + ")");
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.e(PMCType.TAG, "[onPrepareOptionsMenu]");
        if (m_adapter.getCount() == 0) {
            menu.findItem(R.id.menu_main_reply).setVisible(false);
            menu.findItem(R.id.menu_main_delete).setVisible(false);
        } else {
            Cursor c = (Cursor) m_listView.getSelectedItem();
            if (c != null) {
                int nType = c.getInt(c.getColumnIndex(MessageColumn.DB_COLUMN_NUMBER_TYPE));
                if (nType == DataColumn.COLUMN_NUMBER_TYPE_CONTROL)
                    menu.findItem(R.id.menu_main_reply).setVisible(false);
                else
                    menu.findItem(R.id.menu_main_reply).setVisible(true);

                menu.findItem(R.id.menu_main_delete).setVisible(true);
            }
        }
        menu.findItem(R.id.menu_main_set).setVisible(true);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.menu_main_reply:
                optionReply();
                break;
            case R.id.menu_main_delete:
                optionDelete();
                break;
            case R.id.menu_main_set:
                optionSettings();
                break;
            default:
                break;
        }
        return true;
    }

    // TODO LoaderCallbacks Override 부분
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.e(PMCType.TAG, "[onCreateLoader]");
        // Create a new CursorLoader with the following query parameters.
        return new CursorLoader(m_context, DataColumn.CONTENT_URI_LIST, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.e(PMCType.TAG, "[onLoadFinished]");
        // A switch-case is useful when dealing with multiple Loaders/IDs
        switch (loader.getId()) {
            case LOADER_ID:
                // The asynchronous load is complete and the data
                // is now available for use. Only now can we associate
                // the queried Cursor with the CursorAdapter.
                m_adapter.swapCursor(cursor);
                // list 갯수가 바뀔때마다 설정을 안하면 포커스 문제가 발생함.
                if (m_adapter.getCount() == 0) {
                    TextView tvEmpty = (TextView) findViewById(R.id.textview_main_empty);
                    m_listView.setEmptyView(tvEmpty);
                } else {
                    m_listView.setEmptyView(null);
                }

                if (m_adapter.getCount() != 0) {
                    if (m_listView.getSelectedItemPosition() == -1)
                        m_listView.setSelection(0);
                }

                // onPrepareOptionsMenu가 onLoadFinished보다 먼저 불리는 경우
                // 문제가 있으므로 onLoadFinished가 끝나는 시점에에 invalidateOptionsMenu 호출하여
                // onPrepareOptionsMenu를 다시 부른다.
                invalidateOptionsMenu();
                break;
        }
        // The listview now displays the queried data.
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.e(PMCType.TAG, "[onLoaderReset]");
        // For whatever reason, the Loader's data is now unavailable.
        // Remove any references to the old data by replacing it with
        // a null Cursor.
        m_adapter.swapCursor(null);
    }

    /**
     * Option New Message
     */
    private void optionNewMessage() {
        // 계정 정보 확인.
        if (m_configure.getUFMI() == Configure.UFMI_INIT) {
            Toast.makeText(m_context, R.string.disconnect_init_account, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(m_context, NewActivity.class);
        i.setAction(PMCType.BNS_PMC_ACTION_NEWMSG_NEW);
        startActivity(i);
    }

    /**
     * Option Reply
     */
    private void optionReply() {
        // 계정 정보 확인.
        if (m_configure.getUFMI() == Configure.UFMI_INIT) {
            Toast.makeText(m_context, R.string.disconnect_init_account, Toast.LENGTH_SHORT).show();
            return;
        }
        String strNumber = null;
        // 선택된 Number 가져오기.
        {
            Cursor c = (Cursor) m_listView.getSelectedItem();
            if (c != null)
                strNumber = c.getString(c.getColumnIndex(MessageColumn.DB_COLUMN_NUMBER));
        }
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
            Log.i(PMCType.TAG, "ID= " + nID + " Number=" + strNumber + " Msg=" + strMsg);

            c.close();
            startActivity(i);
        }
    }

    /**
     * Option Delete
     */
    private void optionDelete() {
        AlertDialog.Builder ab = new AlertDialog.Builder(m_context);
        ab.setTitle(R.string.delete_popup_title);
        ab.setMessage(R.string.delete_popup_context_sel);
        ab.setNegativeButton(R.string.cancel, null);
        ab.setPositiveButton(R.string.delete, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Cursor c = (Cursor) m_listView.getSelectedItem();
                if (c != null) {
                    String strNumber = c.getString(c.getColumnIndex(MessageColumn.DB_COLUMN_NUMBER));
                    new asyncDeleteMsg().execute(strNumber);
                }
            }
        });
        ab.show();
    }

    /**
     * Setting 동작 함수
     */
    private void optionSettings() {
        Intent i = new Intent(m_context, SettingsActivity.class);
        startActivity(i);
    }

    /**
     * async로 msg를 Delete
     *
     * @author kyu
     */
    private class asyncDeleteMsg extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Log.i(PMCType.TAG, "doInBackground start");

            int nResult = 0;
            int numberOfParams = params.length;
            for (int i = 0; i < numberOfParams; i++) {
                String strNumber = params[i];
                Uri uri = Uri.withAppendedPath(DataColumn.CONTENT_URI_DEL_BY_NUM, strNumber);
                getContentResolver().delete(uri, null, null);
            }
            return nResult;
        }

        @Override
        protected void onPostExecute(Integer result) {
            Toast.makeText(m_context, R.string.delete_popup_result_true, Toast.LENGTH_SHORT).show();
        }
    }
}

