package com.bns.pmc.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bns.pmc.R;
import com.bns.pmc.provider.DataColumn;
import com.bns.pmc.provider.DataColumn.MessageColumn;
import com.bns.pmc.util.CommonUtil;
import com.bns.pmc.util.Configure;
import com.bns.pmc.util.Log;
import com.bns.pmc.util.PMCType;

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

        v.setTag(holder);

        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor c) {
        Log.i(PMCType.TAG, "bindView시작(view=" + view + ", context=" + context + ", cursor=" + c);
        int nNumberType = c.getInt(c.getColumnIndex(MessageColumn.DB_COLUMN_NUMBER_TYPE));
        int nRecv = c.getInt(c.getColumnIndex(MessageColumn.DB_COLUMN_RECV));
        String strGroupMember = c.getString(c.getColumnIndex(MessageColumn.DB_COLUMN_GROUP_MEMBER));
        String strMsg = c.getString(c.getColumnIndex(MessageColumn.DB_COLUMN_MSG));
        byte[] byteData = c.getBlob(c.getColumnIndex(MessageColumn.DB_COLUMN_DATA));
        int nDataType = c.getInt(c.getColumnIndex(MessageColumn.DB_COLUMN_DATA_TYPE));
        int nState = c.getInt(c.getColumnIndex(MessageColumn.DB_COLUMN_STATE));

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

        Log.i(PMCType.TAG, "nDataType=" + nDataType);
        // Data
        if (nDataType == DataColumn.COLUMN_DATA_TYPE_JPG) {
            Log.i(PMCType.TAG, "byteData=" + byteData);
            //testCode
            byteData = new byte[10];
            //testEnd
            if (byteData != null) {
//                Bitmap Image = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
//                Bitmap resizeImg = createThumbnail(Image);
//                Drawable drawable = (Drawable) (new BitmapDrawable(m_context.getResources(), resizeImg));
//                tvMsg.setCompoundDrawablePadding(10);
//                tvMsg.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
                //testCode
                Bitmap bitmap = BitmapFactory.decodeResource(m_context.getResources(), R.raw.android);
                //ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                //Bitmap resizeImg = createThumbnail(bitmap);
                Drawable drawable = (Drawable) (new BitmapDrawable(m_context.getResources(), bitmap));
                tvMsg.setCompoundDrawablePadding(10);
                tvMsg.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
                //testEnd
            }
        } else if (nDataType == DataColumn.COLUMN_DATA_TYPE_MP3) {
            // 구현필요
        }
        // create Time
        long createTimel = c.getLong(c.getColumnIndex(MessageColumn.DB_COLUMN_CREATETIME));
        String strTime = CommonUtil.conv_DateTime(m_context, createTimel);
        tvTime.setText(strTime);
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
}