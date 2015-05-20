package com.bns.pmc.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.bns.pmc.R;
import com.bns.pmc.provider.DataColumn;
import com.bns.pmc.provider.DataColumn.MessageColumn;
import com.bns.pmc.util.CommonUtil;
import com.bns.pmc.util.Configure;
import com.bns.pmc.util.Log;
import com.bns.pmc.util.PMCType;

public class MsgAdapter extends CursorAdapter {

    private Context m_context;
    private Configure m_configure;
    private String m_strFleetNumber;

    public MsgAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        Log.d(PMCType.TAG, "MsgAdapter생성자시작(context=" + context + ", cursor=" + c + ", flags=" + flags + ")");

        m_context = context;
        m_configure = Configure.getInstance(m_context);
        m_strFleetNumber = m_configure.getFleetNumber();
        Log.d(PMCType.TAG, "MsgAdapter생성자종료()");
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.d(PMCType.TAG, "bindView시작(view=" + view + ", context=" + context + ", cursor=" + cursor + ")");
        ViewHolder holder = (ViewHolder) view.getTag();

        int nfontpercent = m_configure.getFontPercent();
        int nNumberType = cursor.getInt(cursor.getColumnIndex(MessageColumn.DB_COLUMN_NUMBER_TYPE));
        String number = cursor.getString(cursor.getColumnIndex(MessageColumn.DB_COLUMN_NUMBER));
        String name = null;
        String strUFMI = m_configure.getUFMI();

        if (nNumberType == DataColumn.COLUMN_NUMBER_TYPE_PTT) {
            String strFleetMemberNum = CommonUtil.conv_UrbanFleetMemberNumToFleetMemberNum(number);
            name = CommonUtil.searchName_FromContact_ByPtt(m_context, strFleetMemberNum, strUFMI);
        } else if (nNumberType == DataColumn.COLUMN_NUMBER_TYPE_GROUP) {
            name = CommonUtil.searchName_FromContact_ByPtt(m_context, number, strUFMI);
        }

        int nCntDontRead = cursor.getInt(cursor.getColumnIndex(MessageColumn.DB_COLUMN_SUM_DONT_READ));

        Uri uri_save = Uri.withAppendedPath(DataColumn.CONTENT_URI_TALK_SAVE_BY_NUMBER, number);
        Cursor c = m_context.getContentResolver().query(uri_save, null, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                holder.tvRead.setVisibility(View.VISIBLE);
                String tmp = view.getResources().getString(R.string.tempSave);
                holder.tvRead.setText(tmp);
                float pxReadSizef = view.getResources().getDimension(R.dimen.main_read_text_size);
                holder.tvRead.setTextSize(CommonUtil.conv_Size(pxReadSizef, nfontpercent));
            } else {
                if (nCntDontRead == 0) {
                    holder.tvRead.setVisibility(View.GONE);
                } else {
                    holder.tvRead.setVisibility(View.VISIBLE);
                    String unreadCnt = "(" + Integer.toString(nCntDontRead) + ")";
                    Log.d(PMCType.TAG, "안읽은메시지수=" + unreadCnt);
                    holder.tvRead.setText(unreadCnt);
                    float pxReadSizef = view.getResources().getDimension(R.dimen.main_read_text_size);
                    holder.tvRead.setTextSize(CommonUtil.conv_Size(pxReadSizef, nfontpercent));
                }
            }
            c.close();
        }

        if (TextUtils.isEmpty(name)) {
            String title = null;
            boolean bequals = CommonUtil.comp_FleetNumber(m_strFleetNumber, number);
            if (bequals) {
                title = CommonUtil.getUserNumber(number);
            } else {
                if (nNumberType == DataColumn.COLUMN_NUMBER_TYPE_PTT)
                    title = CommonUtil.conv_UrbanFleetMemberNumToFleetMemberNum(number);
                else if (nNumberType == DataColumn.COLUMN_NUMBER_TYPE_CONTROL) {
                    //관제타입일경우
                    title = number + "(관제)";
                } else
                    title = number;
            }
            holder.tvTitle.setText(title);
            Log.d(PMCType.TAG, "제목=" + title);
        } else {
            holder.tvTitle.setText(name);
        }

        float pxTopSizef = view.getResources().getDimension(R.dimen.main_title_text_size);
        holder.tvTitle.setTextSize(CommonUtil.conv_Size(pxTopSizef, nfontpercent));
        String content = cursor.getString(cursor.getColumnIndex(MessageColumn.DB_COLUMN_MSG));
        Log.d(PMCType.TAG, "메시지=" + content);
        holder.tvContent.setText(content);
        float pxBottomSizef = view.getResources().getDimension(R.dimen.main_content_text_size);
        holder.tvContent.setTextSize(CommonUtil.conv_Size(pxBottomSizef, nfontpercent));

        long createTimel = cursor.getLong(cursor.getColumnIndex(MessageColumn.DB_COLUMN_CREATETIME));
        String createtime = CommonUtil.conv_DateTime(m_context, createTimel);
        Log.d(PMCType.TAG, "생성시간=" + createtime);
        holder.tvTime.setText(createtime);
        Log.d(PMCType.TAG, "bindView종료()");
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.d(PMCType.TAG, "newView시작(context=" + context + ", cursor=" + cursor + ", parent=" + parent + ")");
        LayoutInflater layInflater = LayoutInflater.from(context);
        View v = layInflater.inflate(R.layout.item_main_layout, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.tvTitle = (TextView) v.findViewById(R.id.textView_main_title);
        holder.tvRead = (TextView) v.findViewById(R.id.textView_main_read);
        holder.tvContent = (TextView) v.findViewById(R.id.textView_main_content);
        holder.tvTime = (TextView) v.findViewById(R.id.textView_main_time);
        v.setTag(holder);

        Log.d(PMCType.TAG, "newView종료(view=" + v + ")");
        return v;
    }

    private static class ViewHolder {
        public TextView tvTitle;
        public TextView tvRead;
        public TextView tvContent;
        public TextView tvTime;
    }
}
