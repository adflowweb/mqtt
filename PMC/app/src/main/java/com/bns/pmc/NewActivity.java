package com.bns.pmc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bns.pmc.provider.DataColumn;
import com.bns.pmc.provider.DataColumn.MessageColumn;
import com.bns.pmc.provider.DataColumn.ValidationColumn;
import com.bns.pmc.receiver.PMCService;
import com.bns.pmc.util.CommonUtil;
import com.bns.pmc.util.CommonUtil.JSonItem;
import com.bns.pmc.util.Configure;
import com.bns.pmc.util.DataUtil;
import com.bns.pmc.util.IPushUtil;
import com.bns.pmc.util.JSonUtil;
import com.bns.pmc.util.Log;
import com.bns.pmc.util.PMCType;
import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;

import kr.co.adflow.push.IPushService;

public class NewActivity extends Activity implements OnFocusChangeListener, TextWatcher, OnClickListener {

    private static final int PICK_IMAGE = 1;
    //public static final String CONTENT_UPLOAD_URL = "http://14.63.224.160:8080/v1/users/";
    //public static final String CONTENT_UPLOAD_URL = "http://172.30.1.18:8080/v1/users/";
    public static final String CONTENT_UPLOAD_URL = "http://192.168.43.25:8080/v1/users/";


    public class ItemNew {
        String strName;
        String strNumber;
        boolean bIsUpdate;

        ItemNew() {
            strName = "";
            strNumber = "";
            bIsUpdate = false;
        }
    }

    private final int DLG_DISMISS_TIME = 1000;
    private final int MENU_CONTACT_ATTACH = 0;
    private final int MENU_TEST_0 = 1;

    private final int GET_CONTACT = 11;
    private final int GET_CONTACT_PPT = 12;
    private final int IDX_ITEM_NEW_MAX = 10;
    private final int IDX_NEW_MSG_EDIT_TEXT = -1;
    private int m_nIdxNumber = 1;
    private int m_nSelectedID = IDX_NEW_MSG_EDIT_TEXT;
    private AlertDialog m_alertDialog;
    private EditText m_etNewMsg;
    private EditText m_etPreFocusEditText;
    private EditText m_etCurrFocusEditText;

    private ImageView imageView;

    private TextView m_tvBtLeft;
    private TextView m_tvBtRight;

    private Context m_context = this;
    private Configure m_configure;
    private ArrayList<RelativeLayout> m_listNumberLayout = new ArrayList<RelativeLayout>();
    private ItemNew m_listItemNew[] = new ItemNew[IDX_ITEM_NEW_MAX];

    private int m_nExtraID = -1;
    private String m_strExtraMsg = null;
    private String m_strExtraNumber = null;

    private boolean m_bSend = false;
    private Intent m_intentResult = new Intent();
    private boolean enablePTT = false; //메시지 전송가능 상태체크용
    private boolean imageSend = false;
    private boolean vibration = false;
    private Bitmap bitmap;
    private String imageFileName;
    private String filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(PMCType.TAG, "[onCreate]");
        setContentView(R.layout.activity_new);

        m_tvBtLeft = (TextView) findViewById(R.id.bottom_button_left);
        m_tvBtRight = (TextView) findViewById(R.id.bottom_button_right);
        m_etNewMsg = (EditText) findViewById(R.id.editText_new_msg);

        imageView = (ImageView) findViewById(R.id.image);


        m_tvBtLeft.setText(R.string.send);
        m_tvBtLeft.setVisibility(View.GONE);
        enablePTT = false;
        m_tvBtRight.setText(R.string.back);

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
        m_tvBtRight.setTextColor(list);
        //tvBtLeft.setClickable(true);
        //tvBtLeft.setFocusableInTouchMode(true);

        //testCode
        m_tvBtRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = (String) ((TextView) v).getText();
                Log.d(PMCType.TAG, "text=" + txt);
                if (txt.equals("이전")) {
                    Log.d(PMCType.TAG, "이전버튼클릭");
                    ((Activity) m_context).onBackPressed();
                } else if (txt.equals("삭제")) {
                    Log.d(PMCType.TAG, "삭제버튼클릭");
                    ((Activity) m_context).dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                } else {
                    Log.d(PMCType.TAG, "무슨버튼이냥 ");
                }
            }
        });
        //testCodeEnd


        m_etNewMsg.setOnFocusChangeListener(this);
        m_etNewMsg.addTextChangedListener(this);

        m_configure = Configure.getInstance(m_context);

        int nfontpercent = m_configure.getFontPercent();
        float pxReadSizef = getResources().getDimension(R.dimen.main_read_text_size);
        m_etNewMsg.setTextSize(CommonUtil.conv_Size(pxReadSizef, nfontpercent));

        for (int i = 0; i < IDX_ITEM_NEW_MAX; i++)
            m_listItemNew[i] = new ItemNew();

        selTagNumber tag = new selTagNumber();
        tag.nSelIdx = IDX_NEW_MSG_EDIT_TEXT;
        m_etNewMsg.setTag(tag);

        LinearLayout layNumber = (LinearLayout) findViewById(R.id.linearLayout_new_number);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < IDX_ITEM_NEW_MAX; i++) {
            RelativeLayout layNew = (RelativeLayout) inflater.inflate(R.layout.item_new_layout, null);
            layNumber.addView(layNew);
            if (i > 0)
                layNew.setVisibility(View.GONE);
            m_listNumberLayout.add(layNew);

            EditText etNumber = (EditText) layNew.findViewById(R.id.editText_new_number);
            ImageButton imgBtContact = (ImageButton) layNew.findViewById(R.id.imageButton_new_contact);
            imgBtContact.setOnClickListener(this);
            etNumber.setOnFocusChangeListener(this);
            etNumber.addTextChangedListener(this);
            etNumber.setTextSize(CommonUtil.conv_Size(pxReadSizef, nfontpercent));

            selTagNumber tagNumber_Edit = new selTagNumber();
            tagNumber_Edit.nSelIdx = i;
            etNumber.setTag(tagNumber_Edit);
            selTagNumber tagNumber_Bt = new selTagNumber();
            tagNumber_Bt.nSelIdx = i;
            imgBtContact.setTag(tagNumber_Bt);
        }
        // intent
        {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (action == null) {
                Log.d(PMCType.TAG, "New Msg Action is Null");
                finish();
            }

            if (action.equals(PMCType.BNS_PMC_ACTION_NEWMSG_CONTACT)) {
                m_strExtraNumber = intent.getStringExtra(PMCType.BNS_PMC_INTENT_EXTRA_NUMBER);

                RelativeLayout lay = m_listNumberLayout.get(0);
                EditText etNumber = (EditText) lay.findViewById(R.id.editText_new_number);
                etNumber.requestFocus();
                etNumber.setText(m_strExtraNumber);
                etNumber.setSelection(etNumber.length());
            } else {
                m_nExtraID = intent.getIntExtra(PMCType.BNS_PMC_INTENT_EXTRA_ID, -1);
                m_strExtraMsg = intent.getStringExtra(PMCType.BNS_PMC_INTENT_EXTRA_CONTENT);
                String strExtraNumber = intent.getStringExtra(PMCType.BNS_PMC_INTENT_EXTRA_NUMBER);

                if (m_nExtraID != -1) {
                    m_intentResult.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_IDX, 0);
                    NewActivity.this.setResult(Activity.RESULT_CANCELED, m_intentResult);
                }

                if (TextUtils.isEmpty(m_strExtraMsg) == false) {
                    m_etNewMsg.requestFocus();
                    m_etNewMsg.setText(m_strExtraMsg);
                    m_etNewMsg.setSelection(m_etNewMsg.length());
                }

                if (TextUtils.isEmpty(strExtraNumber) == false) {
                    if (CommonUtil.checkPttGroupNumFormat(strExtraNumber)) {
                        m_strExtraNumber = strExtraNumber;
                    } else {
                        //String strFleetMemberNum = CommonUtil.conv_UrbanFleetMemberNumToFleetMemberNum(strExtraNumber);
                        //Log.d(PMCType.TAG, "strFleetMemberNum=" + strFleetMemberNum);
                        //m_strExtraNumber = strFleetMemberNum;

                        m_strExtraNumber = strExtraNumber;
                    }
                }
                Log.d(PMCType.TAG, "m_strExtraNumber=" + m_strExtraNumber);

                RelativeLayout lay = m_listNumberLayout.get(0);
                EditText etNumber = (EditText) lay.findViewById(R.id.editText_new_number);
                etNumber.requestFocus();
                etNumber.setText(m_strExtraNumber);
                etNumber.setSelection(etNumber.length());
            }

            m_etNewMsg.requestFocus();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(PMCType.TAG, "[onResume]");

        // 임시 저장 삭제
        deleteTempData();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(PMCType.TAG, "[onPause]");

        // send 동작일땐 임시저장을 하지 않음
        if (m_bSend)
            return;

        // 메시지가 없는 경우도 임시 저장을 하지 않음
        if (TextUtils.isEmpty(m_etNewMsg.getText().toString()))
            return;

        // 임시저장
        saveTempData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(PMCType.TAG, "[onStop]");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(PMCType.TAG, "[onDestroy]");
        /**
         * 안드로이드 Dialog
         * java.lang.IllegalArgumentException: View not attached to window manager dialog dismiss
         * 오류 해결 방안
         */
        if (m_alertDialog != null) {
            if (m_alertDialog.isShowing())
                m_alertDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(PMCType.TAG, "onActivityResult시작(requestCode=" + requestCode + " resultCode= " + resultCode + ", data=" + data + ")");
        switch (requestCode) {
            case GET_CONTACT_PPT:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        EditText etFocusedEdit = null;
                        String strName = data.getStringExtra(PMCType.BNS_PMC_PTT_QUICK_NAME_EXTRA_VALUE);
                        String strNumber = data.getStringExtra(PMCType.BNS_PMC_PTT_QUICK_NUMBER_EXTRA_VALUE);
                        Log.d(PMCType.TAG, "Name= " + strName + " Number= " + strNumber);

                        try {
                            m_listItemNew[m_nSelectedID].strName = strName;
                            m_listItemNew[m_nSelectedID].strNumber = strNumber;
                            m_listItemNew[m_nSelectedID].bIsUpdate = true;

                            RelativeLayout lay = (RelativeLayout) m_listNumberLayout.get(m_nSelectedID);
                            etFocusedEdit = (EditText) lay.findViewById(R.id.editText_new_number);
                            etFocusedEdit.removeTextChangedListener(this);

                            if (strName != null && TextUtils.isEmpty(strName) == false) {
                                etFocusedEdit.setText(strName);
                                etFocusedEdit.setSelection(etFocusedEdit.length());
                            } else {
                                etFocusedEdit.setText(strNumber);
                                etFocusedEdit.setSelection(etFocusedEdit.length());
                            }

                            etFocusedEdit.addTextChangedListener(this);
                            // 15.03.13 수신대상 연락처 불러온 경우 한줄 추가
                            {
                                EditText etNumber = (EditText) m_listNumberLayout.get(m_nIdxNumber - 1).findViewById(R.id.editText_new_number);
                                if (etFocusedEdit == etNumber) {
                                    visibleNextPhone();
                                }
                            }
                            etFocusedEdit.requestFocus();

                        } catch (Exception e) {
                            Log.e(PMCType.TAG, e.getMessage());
                        }
                    }
                }
                break;
            case GET_CONTACT:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Cursor c = getContentResolver().query(data.getData(),
                                new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                                        ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);

                        String strText = null;
                        if (c != null) {
                            if (c.moveToFirst()) {
                                String strName = c.getString(0);     // 0은 이름을 얻어옵니다.
                                String strNumber = c.getString(1);   // 1은 번호를 받아옵니다.
                                strText = "[" + getResources().getString(R.string.name) + ":" + strName + " " +
                                        getResources().getString(R.string.number) + ":" + strNumber + "]";

                                m_etNewMsg.append(strText);

                            }
                            c.close();
                        }
                    }
                }
                break;
            case PICK_IMAGE:
                if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
                        && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    decodeFile(picturePath);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d(PMCType.TAG, "dispatchKeyEvent시작(event=" + event + ")");
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == PMCType.BNS_PMC_KEY_CODE_PTT_CALL &&
                    event.getRepeatCount() == 0) {
                optionSendMessage();
                return true;
            }
        }
        boolean response = super.dispatchKeyEvent(event);
        Log.d(PMCType.TAG, "dispatchKeyEvent종료(response=" + response + ")");
        return response;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_CONTACT_ATTACH, 0, R.string.contact_attach);
        menu.add(0, 1, 0, "사진촬영");
        menu.add(0, 2, 0, "앨범");
        menu.add(0, 3, 0, "파일");
        //menu.add(0, MENU_TEST_0, 0, "Test 서브스크라이브해제");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case MENU_CONTACT_ATTACH:
                optionContact_Attach();
                break;
            case 1:
                //unsubscribe();
                //selectImageFromGallery();
                break;
            case 2:
                //unsubscribe();
                selectImageFromGallery();
                break;
            case 3:
                //unsubscribe();
                selectFile();
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.d(PMCType.TAG, "onFocusChange시작(v=" + v + ", hasFocus=" + hasFocus + ")");
        EditText etFocusText = (EditText) v;
        if (hasFocus) {
            selTagNumber tagNumber = (selTagNumber) etFocusText.getTag();
            Log.d(PMCType.TAG, "tagNumber=" + tagNumber);
            Log.d(PMCType.TAG, "etFocusText.getText().length()=" + etFocusText.getText().length());

            if (etFocusText.getText().length() != 0) {
                // LeftBottom button 조건시작
                if (tagNumber.nSelIdx == IDX_NEW_MSG_EDIT_TEXT) {
                    boolean bIsEmpty = true;
                    for (int i = 0; i < m_listNumberLayout.size(); i++) {
                        EditText etText = (EditText) m_listNumberLayout.get(i).findViewById(R.id.editText_new_number);
                        if (TextUtils.isEmpty(etText.getText().toString()) == false) {
                            bIsEmpty = false;
                            break;
                        }
                    }

                    if (bIsEmpty == true) {
                        m_tvBtLeft.setVisibility(View.GONE);
                        enablePTT = false;
                    } else {
                        //m_tvBtLeft.setVisibility(View.VISIBLE);
                        enablePTT = true;
                    }
                } else {
                    String strNewMsg = m_etNewMsg.getText().toString();
                    if (TextUtils.isEmpty(strNewMsg)) {
                        m_tvBtLeft.setVisibility(View.GONE);
                        enablePTT = false;
                    } else {
                        //m_tvBtLeft.setVisibility(View.VISIBLE);
                        enablePTT = true;
                    }
                }
                // LeftBottom button 조건종료

                m_tvBtRight.setText(R.string.delete);
            } else {
                // LeftBottom button 조건시작
                Log.d(PMCType.TAG, "tagNumber.nSelIdx===================================" + tagNumber.nSelIdx);
                if (tagNumber.nSelIdx == IDX_NEW_MSG_EDIT_TEXT) {
                    m_tvBtLeft.setVisibility(View.GONE);
                    enablePTT = false;
                } else {
                    boolean bIsEmpty = true;
                    for (int i = 0; i < m_listNumberLayout.size(); i++) {
                        EditText etText = (EditText) m_listNumberLayout.get(i).findViewById(R.id.editText_new_number);
                        if (TextUtils.isEmpty(etText.getText().toString()) == false) {
                            bIsEmpty = false;
                            break;
                        }
                    }
                    String strNewMsg = m_etNewMsg.getText().toString();
                    if (bIsEmpty == true || TextUtils.isEmpty(strNewMsg) == true) {
                        m_tvBtLeft.setVisibility(View.GONE);
                        enablePTT = false;
                    } else {
                        //m_tvBtLeft.setVisibility(View.VISIBLE);
                        enablePTT = true;
                    }
                }
                // LeftBottom button 조건종료

                m_tvBtRight.setText(R.string.back);
            }

            m_nSelectedID = tagNumber.nSelIdx;
            m_etCurrFocusEditText = etFocusText;
        } else {
            if (m_nSelectedID != IDX_NEW_MSG_EDIT_TEXT) {
                if (m_listItemNew[m_nSelectedID].bIsUpdate == true) {
                    if (searchContactInfo((EditText) v, m_nSelectedID) == false) {
                        Log.i(PMCType.TAG, "searchContactInfo is false");
                    }
                }
            }
            m_etPreFocusEditText = etFocusText;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d(PMCType.TAG, "onTextChanged시작(CharSequence=" + s + ", start=" + start + ", before=" + before + ", count=" + count + ")");

        //메시지 카운팅
        Log.d(PMCType.TAG, "에디터포커스=" + m_etNewMsg.isFocused());
        if (m_etNewMsg.isFocused()) {
            Log.d(PMCType.TAG, "문자열=" + s);
            String title = getResources().getString(R.string.title_new_activity);
            String str = s.toString();
            int msgCnt = 0;
            for (int i = 0; i < str.length(); i++) {
                if (Character.getType(str.charAt(i)) == 5) {
                    msgCnt = msgCnt + 2;
                } else {
                    msgCnt++;
                }
            }

            if (msgCnt > 140) {
                this.setTitle(title + "(MMS)");
                //진동
                if (!vibration) {
                    // Java Source Code
                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(500);
                    vibration = true;
                }
            } else if (msgCnt == 0) {
                this.setTitle(title);
                vibration = false;
            } else {
                this.setTitle(title + "(" + msgCnt + "/140)");
                vibration = false;
            }
        }

        EditText etCurrFocusEditText = m_etCurrFocusEditText;
        EditText etNumber = (EditText) m_listNumberLayout.get(m_nIdxNumber - 1).findViewById(R.id.editText_new_number);

        if (etCurrFocusEditText == etNumber) {
            if (s.length() >= 1 && start == 0) {
                visibleNextPhone();
            }
        }

        if (s.length() != 0) {
            // LeftBottom button 조건시작
            if (m_nSelectedID == IDX_NEW_MSG_EDIT_TEXT) {
                boolean bIsEmpty = true;
                for (int i = 0; i < m_listNumberLayout.size(); i++) {
                    EditText etText = (EditText) m_listNumberLayout.get(i).findViewById(R.id.editText_new_number);
                    if (TextUtils.isEmpty(etText.getText().toString()) == false) {
                        bIsEmpty = false;
                        break;
                    }
                }

                if (bIsEmpty == true) {
                    m_tvBtLeft.setVisibility(View.GONE);
                    enablePTT = false;
                } else {
                    // m_tvBtLeft.setVisibility(View.VISIBLE);
                    enablePTT = true;
                }
            } else {
                String strNewMsg = m_etNewMsg.getText().toString();
                if (TextUtils.isEmpty(strNewMsg)) {
                    m_tvBtLeft.setVisibility(View.GONE);
                    enablePTT = false;
                } else {
                    //m_tvBtLeft.setVisibility(View.VISIBLE);
                    enablePTT = true;
                }
            }
            // LeftBottom button 조건종료

            m_tvBtRight.setText(R.string.delete);
        } else {
            // LeftBottom button 조건시작
            if (m_nSelectedID == IDX_NEW_MSG_EDIT_TEXT) {
                m_tvBtLeft.setVisibility(View.GONE);
                enablePTT = false;
            } else {
                boolean bIsEmpty = true;
                for (int i = 0; i < m_listNumberLayout.size(); i++) {
                    EditText etText = (EditText) m_listNumberLayout.get(i).findViewById(R.id.editText_new_number);
                    if (TextUtils.isEmpty(etText.getText().toString()) == false) {
                        bIsEmpty = false;
                        break;
                    }
                }
                String strNewMsg = m_etNewMsg.getText().toString();
                if (bIsEmpty == true || TextUtils.isEmpty(strNewMsg) == true) {
                    m_tvBtLeft.setVisibility(View.GONE);
                    enablePTT = false;
                } else {
                    // m_tvBtLeft.setVisibility(View.VISIBLE);
                    enablePTT = true;
                }
            }
            // LeftBottom button 조건종료

            m_tvBtRight.setText(R.string.back);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (m_nSelectedID != IDX_NEW_MSG_EDIT_TEXT) {
            m_listItemNew[m_nSelectedID].strNumber = s.toString();
            m_listItemNew[m_nSelectedID].bIsUpdate = true;
        }
    }

    @Override
    public void onClick(View v) {
        ImageButton imgBt = (ImageButton) v;
        m_nSelectedID = ((selTagNumber) imgBt.getTag()).nSelIdx;

        Intent i = new Intent();
        i.setAction(Intent.ACTION_PICK);
        i.setData(Uri.parse("content://com.android.contacts/data/ptt"));

        startActivityForResult(i, GET_CONTACT_PPT);
    }

    private void visibleNextPhone() {
        if (m_nIdxNumber < m_listNumberLayout.size()) {
            m_listNumberLayout.get(m_nIdxNumber).setVisibility(View.VISIBLE);
            m_nIdxNumber++;
        }
    }

    /**
     * 개인 번호만 들어있는 번호인 경우 FleetNumber를 자동으로 붙여주는 함수
     * FleetNumber는 자기 자신 FleetNumber로
     */
    private String addFleetNumber_Auto(String str) {
        String strFleet = m_configure.getFleetNumber();
        String strResult = str;
        Log.i(PMCType.TAG, "addFleetNumber_Auto input= " + strResult);

        strResult = CommonUtil.addToFleetNumber(strFleet, str);
        Log.i(PMCType.TAG, "addFleetNumber_Auto result= " + strResult);

        return strResult;
    }

    private boolean searchContactInfo(EditText etfocusedEdit, int index) {
        String strSearch = etfocusedEdit.getText().toString();

        // 검사 로직
        if (TextUtils.isEmpty(strSearch)) {
            // Search할 데이터가 없는 경우
            m_listItemNew[index].strName = "";
            m_listItemNew[index].strNumber = "";
            m_listItemNew[index].bIsUpdate = false;

        } else if (CommonUtil.checkPttCallNumFormat(strSearch) == true || CommonUtil.checkPttGroupNumFormat(strSearch) == true) {
            // search data가 ppt or group ptt Number 형식인 경우
            Log.i(PMCType.TAG, "[Person & Group Num]" + strSearch);

            try {
                ArrayList<String> list = //DataUtil.getContactsUsingNumber(m_context, strSearch);
                        CommonUtil.searchContactInfo(m_context, strSearch, m_configure.getUFMI());
                if (list.isEmpty() == false) {
                    m_listItemNew[index].strName = list.get(0);
                    m_listItemNew[index].strNumber = list.get(1);
                }
                m_listItemNew[index].bIsUpdate = false;

                Log.d(PMCType.TAG, "[Person & Group Num Result]name= " + m_listItemNew[index].strName +
                        " number= " + m_listItemNew[index].strNumber);

                if (m_listItemNew[index].strName != null && TextUtils.isEmpty(m_listItemNew[index].strName) == false) {
                    etfocusedEdit.removeTextChangedListener(this);
                    etfocusedEdit.setText(m_listItemNew[index].strName);
                    etfocusedEdit.setSelection(etfocusedEdit.length());
                    etfocusedEdit.addTextChangedListener(this);
                }

                // UFMI 검색.
                if (validNumber(m_listItemNew[index].strNumber) == false) {
                    m_listItemNew[index].bIsUpdate = true;
                    String str = "[" + m_listItemNew[index].strNumber + "]\n" +
                            getResources().getString(R.string.exist_input);

                    m_alertDialog = PMCAlertDialog.createDialog(m_context, str);
                    m_alertDialog.show();
                    dismissDlg(DLG_DISMISS_TIME, m_alertDialog, true);
                    return false;
                }

            } catch (Exception e) {
                Log.e(PMCType.TAG, e.getMessage());
            }
        } else {
            // Search data가 이름인 경우
            try {
                ArrayList<String> list = DataUtil.getContactsUsingName(m_context, strSearch);
                m_listItemNew[index].strNumber = list.get(1);
                m_listItemNew[index].bIsUpdate = false;

                Log.d(PMCType.TAG, "[Name]name= " + strSearch + " number= " + list.get(1));

                // UFMI 검색.
                // +82형식..
                boolean b = strSearch.startsWith("+");
                String strExist;
                if (b)
                    strExist = strSearch;
                else
                    strExist = list.get(1);

                if (strSearch != null && TextUtils.isEmpty(strSearch) == false) {
                    etfocusedEdit.removeTextChangedListener(this);
                    etfocusedEdit.setText(strSearch);
                    etfocusedEdit.setSelection(etfocusedEdit.length());
                    etfocusedEdit.addTextChangedListener(this);
                }

                if (validNumber(strExist) == false) {
                    m_listItemNew[index].bIsUpdate = true;
                    String str = null;
                    if (TextUtils.isEmpty(strExist))
                        str = "[" + strSearch + "]";
                    else
                        str = "[" + strExist + "]";
                    str += "\n" + getResources().getString(R.string.exist_input);

                    m_alertDialog = PMCAlertDialog.createDialog(m_context, str);
                    m_alertDialog.show();
                    dismissDlg(DLG_DISMISS_TIME, m_alertDialog, true);
                    return false;
                }

                if (TextUtils.isEmpty(m_listItemNew[index].strNumber)) {
                    Log.d(PMCType.TAG, "Number is Null");
                    m_listItemNew[index].strName = "";
                    m_listItemNew[index].strNumber = "";
                    m_listItemNew[index].bIsUpdate = false;

                    return false;
                }
            } catch (Exception e) {
                Log.e(PMCType.TAG, e.getMessage());
            }
        }
        return true;
    }

    private void optionSendMessage() {
        //if (m_tvBtLeft.isShown() == false)
        //    return;

        //ptt 로그아웃시 메시지전송하지 않음
        if (PMCService.loginStatus == false) {
            Toast.makeText(m_context, "PTT로그아웃상태입니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(PMCType.TAG, "enablePTT=" + enablePTT);
        if (enablePTT == false) {

            //이미지 전송일경우
            if (imageSend) {
                boolean bIsEmpty = true;
                for (int i = 0; i < m_listNumberLayout.size(); i++) {
                    EditText etText = (EditText) m_listNumberLayout.get(i).findViewById(R.id.editText_new_number);
                    if (TextUtils.isEmpty(etText.getText().toString()) == false) {
                        bIsEmpty = false;
                        break;
                    }
                }

                if (bIsEmpty == true) {
                    return;
                }
            } else {
                return;
            }
        }

        //modified by nadir
        // validataionCheck
        // exist검사
        for (int i = 0; i < 10; i++) {
            if (TextUtils.isEmpty(m_listItemNew[i].strNumber) == false &&
                    validNumber(m_listItemNew[i].strNumber) == false) {
                String str = "[" + m_listItemNew[i].strNumber + "]\n" +
                        getResources().getString(R.string.exist_input);
                Toast.makeText(m_context, str, Toast.LENGTH_SHORT).show();
//                String str = "[" + m_listItemNew[i].strNumber + "]\n" +
//                        getResources().getString(R.string.exist_input);
//
//                m_alertDialog = PMCAlertDialog.createDialog(m_context, str);
//                m_alertDialog.show();
//                dismissDlg(DLG_DISMISS_TIME, m_alertDialog, true);
                return;
            }
        }

        //메시지 수신자 확인
        int receiversCnt = 0;
        boolean success = true;
        for (int i = 0; i < 10; i++) {
            if (TextUtils.isEmpty(m_listItemNew[i].strNumber) == false) {
                if (CommonUtil.checkPttGroupNumFormat(m_listItemNew[i].strNumber)) {
                    try {
                        // Group Number인 경우
                        String strReceiver = m_listItemNew[i].strNumber;
                        Log.i(PMCType.TAG, "Receiver=" + strReceiver);
                        String strGroup = strReceiver.substring(1);
                        String strUFMI = m_configure.getUFMI();
                        String strBunchID = m_configure.getBunchID();
                        int nVersion = m_configure.getVersion();
                        String topic = CommonUtil.conv_GroupToMMS(strGroup, strUFMI, strBunchID, nVersion);

                        //그룹메시지사용자 수를 표시하고 사용자가 발신여부를 결정한다.
                        //request
                        IPushService binder = PMCService.m_Binder;
                        String result = IPushUtil.getGrpSubscribers(binder, topic);
                        Log.i(PMCType.TAG, "result=" + result);
                        JSONObject jsonObj = null;
                        jsonObj = new JSONObject(result);
                        int subscribers = jsonObj.getJSONObject("result").getInt("data");
                        Log.i(PMCType.TAG, "subscribers=" + subscribers);
                        receiversCnt = receiversCnt + subscribers;
                    } catch (Exception e) {
                        success = false;
                        Log.e(PMCType.TAG, e.getMessage());
                    }
                } else {
                    //일반수신자카운팅
                    receiversCnt++;
                }
            }
        }

        if (success) {
            if (receiversCnt > 1) {
                //총메시지 건수 확인창 뛰우기
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("수신자확인")
                        .setMessage("메시지수신자는\n총" + receiversCnt + "명 입니다.\n메시지를 보내시겠습니까?")
                        .setCancelable(false).setPositiveButton(R.string.accept,
                                new Dialog.OnClickListener() {

                                    @Override
                                    public void onClick(
                                            DialogInterface dialogInterface, int i) {
                                        // Close dialog
                                        dialogInterface.dismiss();
                                        //발송
                                        new asyncSendMsg().execute(m_listItemNew[0], m_listItemNew[1], m_listItemNew[2], m_listItemNew[3], m_listItemNew[4],
                                                m_listItemNew[5], m_listItemNew[6], m_listItemNew[7], m_listItemNew[8], m_listItemNew[9]);
                                    }
                                })
                        .setNegativeButton(R.string.do_not_accect,
                                new Dialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // Close dialog
                                        dialog.dismiss();
                                    }
                                });
                final AlertDialog ad = builder.create();
                ad.setOnKeyListener(new Dialog.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                        Log.d(PMCType.TAG, "onKey시작(dialogInterface=" + dialogInterface + ", keyCode=" + keyCode + ", keyEvent=" + keyEvent + ")");
                        //ppt키를 이용하여 메시지를 발송한다.
                        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                            if (keyEvent.getKeyCode() == PMCType.BNS_PMC_KEY_CODE_PTT_CALL &&
                                    keyEvent.getRepeatCount() == 0) {
                                //만약 현재 포커스가 positive 버튼에 있다면 메시지 전송 아니면 취소
                                Log.d(PMCType.TAG, "focus=" + (Button) ad.getCurrentFocus());
                                Button focusedBtn = (Button) ad.getCurrentFocus();
                                // Close dialog
                                dialogInterface.dismiss();
                                if (focusedBtn == ad.getButton(DialogInterface.BUTTON_POSITIVE)) {
                                    //발송
                                    new asyncSendMsg().execute(m_listItemNew[0], m_listItemNew[1], m_listItemNew[2], m_listItemNew[3], m_listItemNew[4],
                                            m_listItemNew[5], m_listItemNew[6], m_listItemNew[7], m_listItemNew[8], m_listItemNew[9]);
                                }
                                return true;
                            }
                        }
                        return false;
                    }
                });
                ad.show();
            } else {
                //단일메시지 발송
                new asyncSendMsg().execute(m_listItemNew[0], m_listItemNew[1], m_listItemNew[2], m_listItemNew[3], m_listItemNew[4],
                        m_listItemNew[5], m_listItemNew[6], m_listItemNew[7], m_listItemNew[8], m_listItemNew[9]);
            }
        } else {
            //실패시
            //단일메시지 발송 여부 문의바람 !!!!
            new asyncSendMsg().execute(m_listItemNew[0], m_listItemNew[1], m_listItemNew[2], m_listItemNew[3], m_listItemNew[4],
                    m_listItemNew[5], m_listItemNew[6], m_listItemNew[7], m_listItemNew[8], m_listItemNew[9]);
        }


        //modifiedEnd
    }

    private void optionContact_Attach() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(i, GET_CONTACT);
    }

    private static class selTagNumber {
        public int nSelIdx;
    }

    /**
     * test
     */
    private void unsubscribe() {
        Log.d(PMCType.TAG, "서브스크라이브해제시작");
        if (PMCService.m_Binder != null) {
            String strUFMI = m_configure.getUFMI();
            int nVer = m_configure.getVersion();
            String strConvUFMI = CommonUtil.conv_UFMIToMMS(strUFMI, nVer);
            try {
                long start = System.currentTimeMillis();
                String strResult = PMCService.m_Binder.unsubscribe(strConvUFMI);
                long stop = System.currentTimeMillis();
                Log.d(PMCType.TAG, "result=" + strResult + ", 걸린시간=" + (stop - start) + "ms");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(m_context, R.string.disconnect_push_service, Toast.LENGTH_SHORT).show();
        }
        Log.d(PMCType.TAG, "서브스크라이브해제종료");
    }

    /**
     * DB에 저장되어 있는지 확인.
     *
     * @param strNumber
     * @return boolean
     */
    private boolean validNumberByDB(String strNumber) {
        Cursor c = getContentResolver().query(DataColumn.CONTENT_URI_SELECT_VALID_ALL, null, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                String strNum = c.getString(c.getColumnIndex(ValidationColumn.DB_COLUMN_NUMBER));
                if (strNumber.compareToIgnoreCase(strNum) == 0) {
                    Log.d(PMCType.TAG, "Valid Number");
                    return true;
                }
            }
            c.close();
        }

        Log.d(PMCType.TAG, "Not Valid Number");
        return false;
    }

    private boolean validNumber(String strNumber) {
        // UFMI 검색.
        if (CommonUtil.checkPttGroupNumFormat(strNumber) == false) {
            boolean bNormal = false;
            String strExistNumber = null;
            // 일반번호 구현 필요.
            /*if (CommonUtil.checkNormalNumFormat(strNumber)) {
                // 일반번호.
				bNormal = true;
				boolean bFull = strNumber.startsWith("+");
				if (bFull == false) {
					boolean b = strNumber.startsWith("010");
					if (b)
						strExistNumber = "+82" + strNumber.substring(1);
					else
						strExistNumber = "+8210" + strNumber;
				} else {
					strExistNumber = strNumber;
				}
			} else 
			*/
            {
                // Ptt 번호.
                // 개인 PTT 번호만 넣은경우 FleetNumber를 자동으로 붙여준다.
                String strFleetUser = addFleetNumber_Auto(strNumber);
                String strUrban = m_configure.getUrbanNumber();
                strExistNumber = CommonUtil.addToUrbanNumber(strUrban, strFleetUser);
            }
            Log.i(PMCType.TAG, "existNumber= " + strExistNumber);

            boolean bValidDB = validNumberByDB(strExistNumber);
            if (bValidDB)
                return true;

            if (TextUtils.isEmpty(strExistNumber) == false) {
                String strResult = null;
                if (bNormal) {
                    strResult = IPushUtil.existPMAByUserID(PMCService.m_Binder, strExistNumber);
                } else {
                    strResult = IPushUtil.existPMAByUFMI(PMCService.m_Binder, strExistNumber);
                }
                boolean bResult_Success = JSonUtil.responeExistPMAResultSuccess(strResult);
                if (bResult_Success) {
                    boolean bResult_DataVali = JSonUtil.responeExistPMAResultDataValidation(strResult);
                    if (bResult_DataVali == false) {
                        return false;
                    } else {
                        // save
                        if (validNumberByDB(strExistNumber) == false) {
                            // DB용량이 Max 상태이면 제일 처음 DB를 지운다.
                            {
                                int nDBSaveMax = 200;
                                int nDBCount = -1;
                                Cursor c = getContentResolver().
                                        query(DataColumn.CONTENT_URI_SELECT_VALID_TOTAL_COUNT, null, null, null, null);

                                if (c != null) {
                                    if (c.moveToFirst()) {
                                        nDBCount = c.getInt(c.getColumnIndex(ValidationColumn.DB_COLUMN_VALID_COUNT_ALL));
                                        Log.i(PMCType.TAG, "DBCount= " + nDBCount);
                                    }
                                    if (nDBCount >= nDBSaveMax) {
                                        Log.i(PMCType.TAG, "DBMax= " + nDBSaveMax);
                                        getContentResolver().delete(DataColumn.CONTENT_URI_DEL_VALID_FIRST, null, null);
                                    }
                                    c.close();
                                }
                            }
                            //  DB에 등록.
                            {
                                ContentValues values = new ContentValues();
                                values.put(ValidationColumn.DB_COLUMN_NUMBER, strExistNumber);
                                values.put(ValidationColumn.DB_COLUMN_CREATETIME, System.currentTimeMillis());
                                getContentResolver().insert(DataColumn.CONTENT_URI_INSERT_VALID, values);
                            }
                        }
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            String strGroupList = m_configure.getGroupList();
            if (CommonUtil.isJoinGroup(strNumber, strGroupList) == false) {
                return false;
            }
        }

        return true;
    }

    /**
     * 임시 저장.
     */
    private void saveTempData() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < m_listItemNew.length; i++) {
                    String strNumber = m_listItemNew[i].strNumber;

                    if (TextUtils.isEmpty(strNumber) == false) {
                        String strSaveNumber = null;
                        boolean bGroup = false;

                        if (CommonUtil.checkPttGroupNumFormat(strNumber)) {
                            // Group Number인 경우
                            bGroup = true;
                            strSaveNumber = strNumber;

                        } /*else if (CommonUtil.checkNormalNumFormat(strNumber)) {
                            // 일반번호.
						}*/ else {
                            // 개인 PTT 번호만 넣은경우 FleetNumber를 자동으로 붙여준다.
                            String strFleetUser = addFleetNumber_Auto(strNumber);
                            // Urban번호 추가
                            String strUrban = m_configure.getUrbanNumber();
                            strSaveNumber = CommonUtil.addToUrbanNumber(strUrban, strFleetUser);
                        }

                        TalkActivity.s_bNew = true;

                        // 선택된 pttNumber에 임시저장파일이 있으면 삭제한다.
                        Uri uri = Uri.withAppendedPath(DataColumn.CONTENT_URI_DEL_SAVE_BY_NUMBER, strSaveNumber);
                        int nRet = getContentResolver().delete(uri, null, null);
                        Log.i(PMCType.TAG, "[onPause]Delete Temp PttNumber= " + strSaveNumber + " Result= " + nRet);

                        Log.i(PMCType.TAG, "[onPause]Insert Temp PttNumber=" + strSaveNumber);

                        ContentValues values = new ContentValues();
                        if (bGroup) {
                            values.put(MessageColumn.DB_COLUMN_GROUP_MEMBER, m_configure.getUFMI());
                            values.put(MessageColumn.DB_COLUMN_NUMBER, strSaveNumber);
                            values.put(MessageColumn.DB_COLUMN_NUMBER_TYPE, DataColumn.COLUMN_NUMBER_TYPE_GROUP);
                        } else {
                            values.put(MessageColumn.DB_COLUMN_NUMBER, strSaveNumber);
                        }

                        values.put(MessageColumn.DB_COLUMN_MSG, m_etNewMsg.getText().toString());
                        values.put(MessageColumn.DB_COLUMN_STATE, DataColumn.COLUMN_STATE_SAVE);
                        values.put(MessageColumn.DB_COLUMN_RECV, DataColumn.COLUMN_RECV_SEND);
                        values.put(MessageColumn.DB_COLUMN_DONT_READ, DataColumn.COLUMN_DONT_READ_READ);
                        values.put(MessageColumn.DB_COLUMN_CREATETIME, System.currentTimeMillis());
                        getContentResolver().insert(DataColumn.CONTENT_URI_INSERT_MSG, values);
                    }
                }
            }
        }).start();
    }

    /**
     * 임시저장 삭제.
     */
    private void deleteTempData() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < m_listItemNew.length; i++) {
                    String strNumber = m_listItemNew[i].strNumber;
                    if (TextUtils.isEmpty(strNumber) == false) {
                        String strSaveNumber = null;
                        if (CommonUtil.checkPttGroupNumFormat(strNumber)) {
                            // Group Number인 경우

                            strSaveNumber = strNumber;

                        }/* else if (CommonUtil.checkNormalNumFormat(strNumber)) {
                            // 일반번호.
						}*/ else {
                            // 개인 PTT 번호만 넣은경우 FleetNumber를 자동으로 붙여준다.
                            String strFleetUser = addFleetNumber_Auto(strNumber);
                            // Urban번호 추가
                            String strUrban = m_configure.getUrbanNumber();
                            strSaveNumber = CommonUtil.addToUrbanNumber(strUrban, strFleetUser);
                        }

                        // 선택된 pttNumber에 임시저장파일이 있으면 삭제한다.
                        Uri uri = Uri.withAppendedPath(DataColumn.CONTENT_URI_DEL_SAVE_BY_NUMBER, strSaveNumber);
                        int nRet = getContentResolver().delete(uri, null, null);
                        Log.i(PMCType.TAG, "[onPause]Delete Temp PttNumber= " + strSaveNumber + " Result= " + nRet);
                    }
                }

            }
        }).start();
    }

    private void dismissDlg(long time, final AlertDialog d, boolean bpreFocus) {
        final boolean bFocus = bpreFocus;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (d.isShowing()) {
                    if (bFocus)
                        m_etPreFocusEditText.requestFocus();
                    d.dismiss();
                }
            }
        }, time);
    }

    private void dismissSendResultDlg(long time, final AlertDialog d, final boolean success) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (d.isShowing()) {
                    TalkActivity.s_bNew = true;
                    finish();
                }
            }
        }, time);
    }

    /**
     * asyncSendMsg
     *
     * @author kyu
     */
    private class asyncSendMsg extends AsyncTask<ItemNew, Integer, Integer> {
        private final int RESULT_MSG_INIT = -1;
        private final int RESULT_MSG_NO_BINDER = -2;
        private final int RESULT_MSG_NO_CONNECT = -3;
        private final int RESULT_MSG_NO_UFMI = -4;
        private final int RESULT_MSG_NO_EXIST = -5;
        private final int RESULT_FAIL_SENDING_IMAGE = -6;

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(PMCType.TAG, "onPreExecute start");

            progressDialog = ProgressDialog
                    .show(m_context, m_context.getResources().getString(R.string.send_popup_title),
                            m_context.getResources().getString(R.string.send_popup_context_progress));
            progressDialog.setOnKeyListener(new OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return true;
                }
            });

            //progressDialog.setTitle("메시지");
            //progressDialog.setMessage("메시지 전송중");
            //progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            //progressDialog.show();
        }

        @Override
        protected Integer doInBackground(ItemNew... params) {
            Log.d(PMCType.TAG, "doInBackground start");

            IPushService binder = PMCService.m_Binder;
            int nResultFail = RESULT_MSG_INIT;
            m_bSend = true;

            String strUFMI = m_configure.getUFMI();
            String strBunchID = m_configure.getBunchID();
            int nVersion = m_configure.getVersion();

            // Binder 연결 상태 확인.
            if (binder == null)
                return RESULT_MSG_NO_BINDER;

            // MqttSession 연결상태 확인.
            boolean bIsConnected = IPushUtil.isConnectedMqttSession(binder);
            if (bIsConnected == false)
                return RESULT_MSG_NO_CONNECT;

            // UFMI가 초기 상태인지 확인.
            String strSender = strUFMI;
            if (TextUtils.equals(strSender, Configure.UFMI_INIT))
                return RESULT_MSG_NO_UFMI;


            //이미지 전송이면 컨텐츠서버에 이미지 전송
            if (imageSend) {
                try {
                    TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String phoneNum = tMgr.getLine1Number();
                    Log.d(PMCType.TAG, "전화번호=" + phoneNum);

                    //ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    //byte[] data = bos.toByteArray();
                    //String file = Base64.encodeBytes(data, 0, data.length);
                    //entity.addPart("uploaded", new StringBody(file));
                    File imgFile = new File(filePath);

//                    MessageDigest md = null;
//                    StringBuffer sb = new StringBuffer();
//
//                    md = MessageDigest.getInstance("MD5");
//                    InputStream is = new FileInputStream(imgFile);
//                    DigestInputStream dis = new DigestInputStream(is, md);
//                    byte[] digest = md.digest();
//                    //convert the byte to hex format method 1
//                    for (int i = 0; i < digest.length; i++) {
//                        sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
//                    }

                    String md5 = checkSum(filePath);
                    Log.d(PMCType.TAG, "md5=" + md5);

                    String token = IPushUtil.getToken(binder);
                    Log.d(PMCType.TAG, "token=" + token);


                    //create thumbnail
                    final int THUMBSIZE = 32;

                    Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(filePath),
                            THUMBSIZE, THUMBSIZE);
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
                        Log.d(PMCType.TAG, "썸네일싸이즈=" + ThumbImage.getRowBytes() * ThumbImage.getHeight());
                    } else {
                        Log.d(PMCType.TAG, "썸네일싸이즈=" + ThumbImage.getByteCount());
                    }

                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream("/mnt/sdcard/test.png");
                        ThumbImage.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                        // PNG is a lossless format, the compression factor (100) is ignored
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //end


                    int responseCode = HttpRequest.head(CONTENT_UPLOAD_URL + phoneNum)
                            .header("md5", md5)
                            .header("token", token)
                            .header("file", imageFileName)
                            .code();
                    Log.d(PMCType.TAG, "이미지존재유무응답코드=" + responseCode);
                    if (responseCode == 409) {
                        //이미업로드됨
                        Log.d(PMCType.TAG, "이미지가컨텐츠서버에이미존재합니다");
                    } else if (responseCode == 404) {
                        responseCode = HttpRequest.post(CONTENT_UPLOAD_URL + phoneNum)
                                //.trustAllCerts() //Accept all certificates
                                //.trustAllHosts() //Accept all hostnames
                                .header("md5", md5)
                                .header("token", token)
                                .part("file", imageFileName, imgFile)
                                .code();
                        Log.d(PMCType.TAG, "이미지업로드응답코드=" + responseCode);
                        if (responseCode != 200 && responseCode != 409/*이미업로드됨*/) {
                            return RESULT_FAIL_SENDING_IMAGE;
                        } else {
                            Log.d(PMCType.TAG, "이미지전송에성공하였습니다.");
                        }
                    } else {
                        return RESULT_FAIL_SENDING_IMAGE;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return RESULT_FAIL_SENDING_IMAGE;
                }
            }

            // start send.
            nResultFail = 0;
            String strSenderMMS = CommonUtil.conv_UFMIToMMS(strSender, nVersion);
            String strMsg = m_etNewMsg.getText().toString();

            int numberOfParams = params.length;
            Log.d(PMCType.TAG, "numberOfParams=" + params.length);


//            // exist검사
//            for (int i = 0; i < numberOfParams; i++) {
//                if (params[i].bIsUpdate == true) {
//                    if (TextUtils.isEmpty(params[i].strNumber) == false &&
//                            validNumber(params[i].strNumber) == false) {
//                        return RESULT_MSG_NO_EXIST;
//                    }
//                }
//            }

            for (int i = 0; i < numberOfParams; i++) {
                long start = System.currentTimeMillis();
                String strNumber = params[i].strNumber;
                Log.d(PMCType.TAG, "입력무전번호=" + strNumber);

                int nNumberType = DataColumn.COLUMN_NUMBER_TYPE_PTT;
                if (TextUtils.isEmpty(strNumber) == false) {
                    // Receiver UFMI가 있는 경우.
                    String strReceiverMMS = null;
                    String strReceiver = null;

                    if (CommonUtil.checkPttGroupNumFormat(strNumber)) {
                        // Group Number인 경우
                        nNumberType = DataColumn.COLUMN_NUMBER_TYPE_GROUP;
                        strReceiver = strNumber;
                        Log.i(PMCType.TAG, "Receiver=" + strReceiver);

                        String strGroup = strReceiver.substring(1);
                        strReceiverMMS = CommonUtil.conv_GroupToMMS(strGroup, strUFMI, strBunchID, nVersion);

                    } /*else if (CommonUtil.checkNormalNumFormat(strNumber)) {
                        // 일반번호.
					}*/ else {
                        nNumberType = DataColumn.COLUMN_NUMBER_TYPE_PTT;
                        // 개인 PTT 번호만 넣은경우 FleetNumber를 자동으로 붙여준다.
                        String strFleetUser = addFleetNumber_Auto(strNumber);
                        // Urban번호 추가
                        String strUrban = m_configure.getUrbanNumber();
                        strReceiver = CommonUtil.addToUrbanNumber(strUrban, strFleetUser);
                        Log.i(PMCType.TAG, "Receiver=" + strReceiver);


                        //82*로시작할경우 무전기버전은 P1 아니면 P2
                        if (strReceiver.startsWith("82*")) {
                            strReceiverMMS = CommonUtil.conv_UFMIToMMS(strReceiver, 1);
                        } else {
                            strReceiverMMS = CommonUtil.conv_UFMIToMMS(strReceiver, 2);
                        }
                    }

                    Log.i(PMCType.TAG, "Receiver MMS= " + strReceiverMMS);

                    // convert JSON
                    JSonItem item = new JSonItem();
                    {
                        item.m_strSender = strSender;
                        item.m_strReceiver = strReceiver;
                        item.m_strText = strMsg;
                        // test
                        item.m_strDataName = "test001.jpg";
                        item.m_strDataFormat = "jpg";
                        Bitmap bitmap = BitmapFactory.decodeResource(m_context.getResources(), R.raw.android);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        item.m_strDataByteArr = stream.toByteArray();
                        //testEnd

                    }
                    String strJson = JSonUtil.encodeDataToJSONString(item);
                    Log.d(PMCType.TAG, "strJson=" + strJson);
                    String strContent = CommonUtil.encodeStringToBase64(strJson);
                    // Send Msg.
                    boolean bResultSuccess = false;
                    String contentType = "application/base64";

                    //testCode
                    int contentLength = 0;
                    for (int j = 0; j < strMsg.length(); j++) {
                        if (Character.getType(strMsg.charAt(j)) == 5) {
                            contentLength = contentLength + 2;
                        } else {
                            contentLength++;
                        }
                    }
                    Log.d(PMCType.TAG, "입력문자크기=" + contentLength + "문자");
                    //testCodeEnd

                    String strResult = IPushUtil.sendMsg(binder, strSenderMMS, strReceiverMMS, contentType, strContent, contentLength);
                    if (TextUtils.isEmpty(strResult) == false) {
                        bResultSuccess = JSonUtil.responeSendMessageResultSuccess(strResult);
                    }
                    // insert DB
                    {
                        ContentValues values = new ContentValues();
                        switch (nNumberType) {
                            case DataColumn.COLUMN_NUMBER_TYPE_PTT: {
                                values.put(MessageColumn.DB_COLUMN_NUMBER, strReceiver);
                            }
                            break;
                            case DataColumn.COLUMN_NUMBER_TYPE_GROUP: {
                                values.put(MessageColumn.DB_COLUMN_NUMBER, strReceiver);
                                values.put(MessageColumn.DB_COLUMN_GROUP_MEMBER, strSender);
                            }
                            break;
                            case DataColumn.COLUMN_NUMBER_TYPE_NORMAL: {

                            }
                            break;
                            default:
                                break;
                        }

                        values.put(MessageColumn.DB_COLUMN_NUMBER_TYPE, nNumberType);
                        values.put(MessageColumn.DB_COLUMN_MSG, strMsg);
                        values.put(MessageColumn.DB_COLUMN_CREATETIME, System.currentTimeMillis());
                        values.put(MessageColumn.DB_COLUMN_RECV, DataColumn.COLUMN_RECV_SEND);
                        values.put(MessageColumn.DB_COLUMN_DONT_READ, DataColumn.COLUMN_DONT_READ_READ);
                        if (bResultSuccess)
                            values.put(MessageColumn.DB_COLUMN_STATE, DataColumn.COLUMN_STATE_SUCCESS);
                        else
                            values.put(MessageColumn.DB_COLUMN_STATE, DataColumn.COLUMN_STATE_FAIL);

                        // testCode
                         {
                             Bitmap bitmap = BitmapFactory.decodeResource(m_context.getResources(), R.raw.android);
		 					ByteArrayOutputStream stream = new ByteArrayOutputStream(); 
		 					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		 					byte[] imageInByte = stream.toByteArray();
		 					values.put(MessageColumn.DB_COLUMN_DATA, imageInByte);
		 					values.put(MessageColumn.DB_COLUMN_DATA_TYPE, DataColumn.DB_COLUMN_DATA_TYPE_JPG);
		 				}
                        //testEnd
                        getContentResolver().insert(DataColumn.CONTENT_URI_INSERT_MSG, values);
                    }

                    if (bResultSuccess == false)
                        nResultFail++;

                    long stop = System.currentTimeMillis();
                    Log.d(PMCType.TAG, "[Send Success]" + bResultSuccess + " [time]" + (stop - start) + "ms");
                }
            }

            Log.d(PMCType.TAG, "doInBackground end");
            return nResultFail;
        }

        @Override
        protected void onPostExecute(Integer result) {
            progressDialog.dismiss();

            if (result > 0) {
                //final AlertDialog alertDialog;
                AlertDialog.Builder ab = new AlertDialog.Builder(m_context);
                ab.setTitle(R.string.send_popup_title);
                ab.setMessage(result + m_context.getResources().getString(R.string.send_popup_context_fail));

                m_alertDialog = ab.create();
                m_alertDialog.setOnKeyListener(new OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        return true;
                    }
                });
                m_alertDialog.show();

                dismissSendResultDlg(DLG_DISMISS_TIME, m_alertDialog, false);
            } else if (result == 0) {
                //final AlertDialog alertDialog;
                AlertDialog.Builder ab = new AlertDialog.Builder(m_context);
                ab.setTitle(R.string.send_popup_title);
                ab.setMessage(R.string.send_popup_context_success);

                m_alertDialog = ab.create();
                m_alertDialog.setOnKeyListener(new OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        return true;
                    }
                });
                m_alertDialog.show();

                dismissSendResultDlg(DLG_DISMISS_TIME, m_alertDialog, true);
            } else if (result == RESULT_MSG_NO_BINDER) {
                Toast.makeText(m_context, R.string.disconnect_push_service, Toast.LENGTH_LONG).show();
            } else if (result == RESULT_MSG_NO_CONNECT) {
                Toast.makeText(m_context, R.string.disconnect_push_service, Toast.LENGTH_LONG).show();
            } else if (result == RESULT_MSG_NO_UFMI) {
                Toast.makeText(m_context, R.string.disconnect_init_account, Toast.LENGTH_LONG).show();
            } else if (result == RESULT_FAIL_SENDING_IMAGE) {
                Toast.makeText(m_context, "이미지전송에 실패하였습니다.", Toast.LENGTH_LONG).show();
            } else {
                m_alertDialog = PMCAlertDialog.createDialog(m_context, getString(R.string.exist_input_ptt));
                m_alertDialog.show();
                dismissDlg(DLG_DISMISS_TIME, m_alertDialog, false);
            }
        }
    }

    /**
     * Opens dialog picker, so the user can select image from the gallery. The
     * result is returned in the method <code>onActivityResult()</code>
     */
    public void selectImageFromGallery() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);

        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


        startActivityForResult(Intent.createChooser(i, "Select Picture"),
                PICK_IMAGE);
    }

    private void selectFile() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, 2);

        //com.android.qrdfileexplorer


//        Intent i = this.getPackageManager().getLaunchIntentForPackage("com.android.qrdfileexplorer");
//        i.setType("file/*");
//        i.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(i, 2);

    }

//    /**
//     * Retrives the result returned from selecting image, by invoking the method
//     * <code>selectImageFromGallery()</code>
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        android.util.Log.d("imageUpload", "onActivityResult시작(requestCode=" + requestCode + ", resultCode=" + resultCode + ", data=" + data + ")");
//
//
//        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
//                && null != data) {
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//
//            decodeFile(picturePath);
//
//        }
//    }

    /**
     * The method decodes the image file to avoid out of memory issues. Sets the
     * selected image in to the ImageView.
     *
     * @param filePath
     */
    public void decodeFile(String filePath) {
        Log.d(PMCType.TAG, "decodeFile시작(filePath=" + filePath + ")");

        this.filePath = filePath;
        imageFileName = filePath.substring(filePath.lastIndexOf("/") + 1);

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 350;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);

        imageView.setImageBitmap(bitmap);
        imageView.bringToFront();
        m_etNewMsg.setVisibility(View.GONE);
        imageSend = true;
    }

    /**
     * @param path
     * @return
     */
    public static String checkSum(String path) throws Exception {
        String checksum = null;
        FileInputStream fis = new FileInputStream(path);
        MessageDigest md = MessageDigest.getInstance("MD5");

        //Using MessageDigest update() method to provide input
        byte[] buffer = new byte[8192];
        int numOfBytesRead;
        while ((numOfBytesRead = fis.read(buffer)) > 0) {
            md.update(buffer, 0, numOfBytesRead);
        }
        byte[] hash = md.digest();
        checksum = new BigInteger(1, hash).toString(16); //don't use this, truncates leading zero
        return checksum;
    }

}
