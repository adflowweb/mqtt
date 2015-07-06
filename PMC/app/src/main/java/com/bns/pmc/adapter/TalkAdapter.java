package com.bns.pmc.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bns.pmc.NewActivity;
import com.bns.pmc.R;
import com.bns.pmc.provider.DataColumn;
import com.bns.pmc.provider.DataColumn.MessageColumn;
import com.bns.pmc.receiver.PMCService;
import com.bns.pmc.util.CommonUtil;
import com.bns.pmc.util.Configure;
import com.bns.pmc.util.IPushUtil;
import com.bns.pmc.util.Log;
import com.bns.pmc.util.PMCType;
import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONObject;

import java.io.File;
import java.util.regex.Pattern;

public class TalkAdapter extends CursorAdapter {
    private Context m_context;
    private Configure m_configure;
    private String m_strFleetNumber;

    public TalkAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

        m_context = context;
        m_configure = Configure.getInstance(m_context);
        String strtUfmi = m_configure.getUFMI();
        m_strFleetNumber = CommonUtil.getFleetNumber(strtUfmi);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.i(PMCType.TAG, "newView시작(context=" + context + ", cursor=" + cursor + ", parent=" + parent);
        LayoutInflater layInflater = LayoutInflater.from(context);
        View v = layInflater.inflate(R.layout.item_talk_layout, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.lyout_mymsg = (RelativeLayout) v.findViewById(R.id.relativelayout_talk_mymsg);
        holder.lyout_yourmsg = (RelativeLayout) v.findViewById(R.id.relativelayout_talk_yourmsg);

        holder.tvMyname = (TextView) v.findViewById(R.id.textView_talk_myname);
        holder.tvYourname = (TextView) v.findViewById(R.id.textView_talk_yourname);
        holder.tvMymsg = (TextView) v.findViewById(R.id.textView_talk_mymsg);
        holder.tvYourmsg = (TextView) v.findViewById(R.id.textView_talk_yourmsg);
        holder.tvMytime = (TextView) v.findViewById(R.id.textView_talk_mytime);
        holder.tvYourtime = (TextView) v.findViewById(R.id.textView_talk_yourtime);

        //testCode
//        int nRecv = cursor.getInt(cursor.getColumnIndex(MessageColumn.DB_COLUMN_RECV));
//        Log.i(PMCType.TAG, "nRecv=" + nRecv);
//
//        TextView tvName, tvMsg, tvTime;
//        if (nRecv == DataColumn.COLUMN_RECV_SEND) {
//            holder.lyout_mymsg.setVisibility(View.VISIBLE);
//            holder.lyout_yourmsg.setVisibility(View.GONE);
//            tvName = holder.tvMyname;
//            tvMsg = holder.tvMymsg;
//            tvTime = holder.tvMytime;
//        } else {
//            holder.lyout_mymsg.setVisibility(View.GONE);
//            holder.lyout_yourmsg.setVisibility(View.VISIBLE);
//            tvName = holder.tvYourname;
//            tvMsg = holder.tvYourmsg;
//            tvTime = holder.tvYourtime;
//
//        }
//
//        String message = cursor.getString(cursor.getColumnIndex(MessageColumn.DB_COLUMN_MSG));
//        tvMsg.setText(message);
        //testCodeEnd

        v.setTag(holder);
        Log.i(PMCType.TAG, "newView종료(view=" + v + ")");
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor c) {
        Log.i(PMCType.TAG, "bindView시작(view=" + view + ", context=" + context + ", cursor=" + c + ")");
        int nNumberType = c.getInt(c.getColumnIndex(MessageColumn.DB_COLUMN_NUMBER_TYPE));
        Log.i(PMCType.TAG, "nNumberType=" + nNumberType);
        int nRecv = c.getInt(c.getColumnIndex(MessageColumn.DB_COLUMN_RECV));
        Log.i(PMCType.TAG, "nRecv=" + nRecv);
        String strGroupMember = c.getString(c.getColumnIndex(MessageColumn.DB_COLUMN_GROUP_MEMBER));
        Log.i(PMCType.TAG, "strGroupMember=" + strGroupMember);
        String strMsg = c.getString(c.getColumnIndex(MessageColumn.DB_COLUMN_MSG));
        Log.i(PMCType.TAG, "strMsg=" + strMsg);
        byte[] byteData = c.getBlob(c.getColumnIndex(MessageColumn.DB_COLUMN_DATA));
        Log.i(PMCType.TAG, "byteData=" + byteData);
        int nDataType = c.getInt(c.getColumnIndex(MessageColumn.DB_COLUMN_DATA_TYPE));
        Log.i(PMCType.TAG, "nDataType=" + nDataType);
        int nState = c.getInt(c.getColumnIndex(MessageColumn.DB_COLUMN_STATE));
        Log.i(PMCType.TAG, "nState=" + nState);

        ViewHolder holder = (ViewHolder) view.getTag();

        TextView tvName, tvMsg, tvTime;
        int nFontpercent = m_configure.getFontPercent();
        float pxTextSizef = view.getResources().getDimension(R.dimen.talk_msg_text_size);

        if (nRecv == DataColumn.COLUMN_RECV_SEND) {
            holder.lyout_mymsg.setVisibility(View.VISIBLE);
            holder.lyout_yourmsg.setVisibility(View.GONE);
            tvName = holder.tvMyname;
            tvMsg = holder.tvMymsg;
            tvTime = holder.tvMytime;
        } else {
            holder.lyout_mymsg.setVisibility(View.GONE);
            holder.lyout_yourmsg.setVisibility(View.VISIBLE);
            tvName = holder.tvYourname;
            tvMsg = holder.tvYourmsg;
            tvTime = holder.tvYourtime;

        }
        // Name
        if (nNumberType == DataColumn.COLUMN_NUMBER_TYPE_GROUP) {
            String strUFMI = m_configure.getUFMI();
            String strName = CommonUtil.searchName_FromContact_ByPtt(m_context, strGroupMember, strUFMI);
            if (TextUtils.isEmpty(strName) == false) {
                tvName.setText(strName);
            } else {
                String strTitle = null;
                boolean bequals = CommonUtil.comp_FleetNumber(m_strFleetNumber, strGroupMember);
                if (bequals) {
                    strTitle = CommonUtil.getUserNumber(strGroupMember);
                } else {
                    strTitle = CommonUtil.conv_UrbanFleetMemberNumToFleetMemberNum(strGroupMember);
                }
                tvName.setText(strTitle);
            }
        } else {
            tvName.setVisibility(View.GONE);
        }
        tvName.setTextSize(CommonUtil.conv_Size(pxTextSizef, nFontpercent));

        // Message
        if (nState != DataColumn.COLUMN_STATE_SUCCESS)
            tvMsg.setTextColor(m_context.getResources().getColor(R.color.color_talk_msg_temp));
        else
            tvMsg.setTextColor(m_context.getResources().getColor(R.color.color_talk_msg));

        tvMsg.setText(strMsg);
        Log.i(PMCType.TAG, "메시지=" + strMsg);
        tvMsg.setTextSize(CommonUtil.conv_Size(pxTextSizef, nFontpercent));
        // Message Pattern
        {
            Resources res = m_context.getResources();
            Pattern patternPtt = Pattern.compile(PMCType.BNS_PMC_REGEX_PTT);
            Pattern patternGroup_Ptt = Pattern.compile(PMCType.BNS_PMC_REGEX_GROUP_PTT);
            Linkify.addLinks(tvMsg, Linkify.PHONE_NUMBERS | Linkify.WEB_URLS);
            Linkify.addLinks(tvMsg, patternPtt, res.getString(R.string.pattern_ptt));
            Linkify.addLinks(tvMsg, patternGroup_Ptt, res.getString(R.string.pattern_ptt_group));
            tvMsg.setMovementMethod(null);
        }

        Log.i(PMCType.TAG, "데이터타입=" + nDataType);

        // 테스트뷰 리셋
        tvMsg.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        /**
         * 바이너리데이타가 존재하는경우 처리
         */
        try {
            if (byteData != null) {
                Log.i(PMCType.TAG, "데이타=" + new String(byteData));
                if (nDataType == DataColumn.COLUMN_DATA_TYPE_JPG
                        || nDataType == DataColumn.COLUMN_DATA_TYPE_PNG
                        || nDataType == DataColumn.COLUMN_DATA_TYPE_BMP) {
                    //이미지인경우
                    String root = Environment.getExternalStorageDirectory().toString();
                    File storageDir = new File(root + "/pmc/images/thumb/");
                    //create storage directories, if they don't exist
                    storageDir.mkdirs();
                    JSONObject obj = null;
                    String fileName = null;
                    String user = null;

                    obj = new JSONObject(new String(byteData));
                    fileName = obj.getString("name");
                    user = obj.getString("user");

                    File file = new File(storageDir, fileName + ".png"); //썸네일은 PNG로 고정
                    if (file.exists()) {
                        Log.d(PMCType.TAG, "로컬에파일이존재합니다");
                        Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
                        Drawable drawable = (Drawable) (new BitmapDrawable(m_context.getResources(), bitmap));
                        tvMsg.setCompoundDrawablePadding(10);
                        tvMsg.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
                    } else {
                        Log.d(PMCType.TAG, "파일이존재하지않아컨텐츠서버에서다운로드합니다");
                        //썸네일이 없으므로 컨텐츠서버에서 다운로드함
                        String token = IPushUtil.getToken(PMCService.m_Binder);
                        Log.d(PMCType.TAG, "token=" + token);
                        setStrictMode();
                        //download
                        HttpRequest request = HttpRequest.get(NewActivity.CONTENT_UPLOAD_URL + user + "/thumb/" + fileName + ".png")
                                .header("token", token);
                        Log.d(PMCType.TAG, "이미지다운로드응답코드=" + request.code());

                        if (request.ok()) {
                            File output = new File(root + "/pmc/images/thumb/" + fileName + ".png");
                            request.receive(output);
                            Bitmap bitmap = BitmapFactory.decodeFile(output.toString());
                            Drawable drawable = (Drawable) (new BitmapDrawable(m_context.getResources(), bitmap));
                            tvMsg.setCompoundDrawablePadding(10);
                            tvMsg.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
                        }
                    }
                } else {
                    //이미지가아닌경우
                    //로컬파일이 존재하면 파일아이콘으로
                    //아니면 다운로드 아이콘으로
                    String root = Environment.getExternalStorageDirectory().toString();
                    File storageDir = new File(root + "/pmc/files/");
                    //create storage directories, if they don't exist
                    storageDir.mkdirs();

                    JSONObject obj = null;
                    String fileName = null;
                    String format = null;
                    obj = new JSONObject(new String(byteData));
                    fileName = obj.getString("name");
                    format = obj.getString("format");

                    File file = new File(storageDir, fileName + "." + format);
                    String buildModel = Build.MODEL;
                    Log.i(PMCType.TAG, "buildModel=" + buildModel);
                    if (file.exists()) {
                        //파일이 존재하면 파일아이콘 표시
                        if (format.equalsIgnoreCase("mp3") || format.equalsIgnoreCase("amr") || format.equalsIgnoreCase("wav") || format.equalsIgnoreCase("mid") || format.equalsIgnoreCase("midi")) {
                            tvMsg.setCompoundDrawablePadding(10);
                            if (buildModel.equals("DH-A101K")) {
                                tvMsg.setCompoundDrawablesWithIntrinsicBounds(null, null, null, m_context.getResources().getDrawable(R.drawable.ic_library_music_black_48dp));
                            } else {
                                tvMsg.setCompoundDrawablesWithIntrinsicBounds(null, null, null, m_context.getResources().getDrawable(R.drawable.ic_library_music_white_48dp));
                            }
                        } else if (format.equalsIgnoreCase("3gp") || format.equalsIgnoreCase("mpeg")) {
                            tvMsg.setCompoundDrawablePadding(10);
                            if (buildModel.equals("DH-A101K")) {
                                tvMsg.setCompoundDrawablesWithIntrinsicBounds(null, null, null, m_context.getResources().getDrawable(R.drawable.ic_video_library_black_48dp));
                            } else {
                                tvMsg.setCompoundDrawablesWithIntrinsicBounds(null, null, null, m_context.getResources().getDrawable(R.drawable.ic_video_library_white_48dp));
                            }
                        } else {
                            tvMsg.setCompoundDrawablePadding(10);
                            if (buildModel.equals("DH-A101K")) {
                                tvMsg.setCompoundDrawablesWithIntrinsicBounds(null, null, null, m_context.getResources().getDrawable(R.drawable.ic_description_black_48dp));
                            } else {
                                tvMsg.setCompoundDrawablesWithIntrinsicBounds(null, null, null, m_context.getResources().getDrawable(R.drawable.ic_description_white_48dp));
                            }
                        }
                    } else {
                        //파일이 로컬에 없으면 다운로드 아이콘으로표시
                        tvMsg.setCompoundDrawablePadding(10);
                        if (buildModel.equals("DH-A101K")) {
                            tvMsg.setCompoundDrawablesWithIntrinsicBounds(null, null, null, m_context.getResources().getDrawable(R.drawable.ic_get_app_black_48dp));
                        } else {
                            tvMsg.setCompoundDrawablesWithIntrinsicBounds(null, null, null, m_context.getResources().getDrawable(R.drawable.ic_get_app_white_48dp));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(PMCType.TAG, "에러발생=" + e);
        }

        // create Time
        long createTimel = c.getLong(c.getColumnIndex(MessageColumn.DB_COLUMN_CREATETIME));
        String strTime = CommonUtil.conv_DateTime(m_context, createTimel);
        tvTime.setText(strTime);
        Log.i(PMCType.TAG, "bindView종료()");
    }

    public Bitmap createThumbnail(Bitmap bitmap) {
        Bitmap img;
        final int nScale = 150;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        img = Bitmap.createScaledBitmap(bitmap, nScale, height / (width / nScale), true);

        return img;
    }

    private static class ViewHolder {
        public RelativeLayout lyout_mymsg;
        public RelativeLayout lyout_yourmsg;
        public TextView tvMyname;
        public TextView tvYourname;
        public TextView tvMymsg;
        public TextView tvYourmsg;
        public TextView tvMytime;
        public TextView tvYourtime;
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
}