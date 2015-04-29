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

public class MsgAdapter extends CursorAdapter {
	
	private Context m_context;
	private Configure m_configure;
	private String m_strFleetNumber;
	
	public MsgAdapter(Context context, Cursor c, int flags) {
    	super(context, c, flags);
            
        m_context = context;
        m_configure = Configure.getInstance(m_context);
        m_strFleetNumber = m_configure.getFleetNumber();
    }
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder) view.getTag();
        
		int nfontpercent = m_configure.getFontPercent();
		int nNumberType = cursor.getInt(cursor.getColumnIndex(MessageColumn.DB_COLUMN_NUMBER_TYPE));
        String strNumber = cursor.getString(cursor.getColumnIndex(MessageColumn.DB_COLUMN_NUMBER));
        String strName = null;
        String strUFMI = m_configure.getUFMI();
        
        if (nNumberType == DataColumn.COLUMN_NUMBER_TYPE_PTT) {
        	String strFleetMemberNum = CommonUtil.conv_UrbanFleetMemberNumToFleetMemberNum(strNumber);
        	strName = CommonUtil.searchName_FromContact_ByPtt(m_context, strFleetMemberNum, strUFMI);
        } else if (nNumberType == DataColumn.COLUMN_NUMBER_TYPE_GROUP) {
        	strName = CommonUtil.searchName_FromContact_ByPtt(m_context, strNumber, strUFMI);
        } 
        	
        int nCntDontRead = cursor.getInt(cursor.getColumnIndex(MessageColumn.DB_COLUMN_SUM_DONT_READ));
        
        Uri uri_save = Uri.withAppendedPath(DataColumn.CONTENT_URI_TALK_SAVE_BY_NUMBER, strNumber);
    	Cursor c = m_context.getContentResolver().query(uri_save, null, null, null, null);
    	if (c != null) {
	    	if (c.moveToFirst()) {
	    		holder.tvRead.setVisibility(View.VISIBLE);
	    		String strText = view.getResources().getString(R.string.tempSave);
	    		holder.tvRead.setText(strText);
	        	float pxReadSizef = view.getResources().getDimension(R.dimen.main_read_text_size);
	        	holder.tvRead.setTextSize(CommonUtil.conv_Size(pxReadSizef, nfontpercent));
	    	} else {
	    	
		        if (nCntDontRead == 0) {
		        	holder.tvRead.setVisibility(View.GONE);
		        } else {
		        	holder.tvRead.setVisibility(View.VISIBLE);
		        	String strText = "(" + Integer.toString(nCntDontRead) + ")";
		        	holder.tvRead.setText(strText);
		        	float pxReadSizef = view.getResources().getDimension(R.dimen.main_read_text_size);
		        	holder.tvRead.setTextSize(CommonUtil.conv_Size(pxReadSizef, nfontpercent));
		        }
	    	}
	    	c.close();
    	}
        
        if (TextUtils.isEmpty(strName)) {
        	String title = null;
        	boolean bequals = CommonUtil.comp_FleetNumber(m_strFleetNumber, strNumber);
        	if (bequals) {
        		title = CommonUtil.getUserNumber(strNumber);
        	} else {
        		if (nNumberType == DataColumn.COLUMN_NUMBER_TYPE_PTT)
        			title = CommonUtil.conv_UrbanFleetMemberNumToFleetMemberNum(strNumber);
        		else
        			title = strNumber;
        	}
        	holder.tvTitle.setText(title);
        }
        else {
        	holder.tvTitle.setText(strName);
        }
        
        float pxTopSizef = view.getResources().getDimension(R.dimen.main_title_text_size);
        holder.tvTitle.setTextSize(CommonUtil.conv_Size(pxTopSizef, nfontpercent));
        
        holder.tvContent.setText(cursor.getString(cursor.getColumnIndex(MessageColumn.DB_COLUMN_MSG)));
        float pxBottomSizef = view.getResources().getDimension(R.dimen.main_content_text_size);
        holder.tvContent.setTextSize(CommonUtil.conv_Size(pxBottomSizef, nfontpercent));
        
		long createTimel = cursor.getLong(cursor.getColumnIndex(MessageColumn.DB_COLUMN_CREATETIME));
		String strText = CommonUtil.conv_DateTime(m_context, createTimel);
		holder.tvTime.setText(strText);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater layInflater = LayoutInflater.from(context);
        View v = layInflater.inflate(R.layout.item_main_layout, parent, false);
        
        ViewHolder holder = new ViewHolder();
        holder.tvTitle = (TextView) v.findViewById(R.id.textView_main_title);
        holder.tvRead = (TextView) v.findViewById(R.id.textView_main_read);
        holder.tvContent = (TextView) v.findViewById(R.id.textView_main_content);
        holder.tvTime = (TextView) v.findViewById(R.id.textView_main_time);
        v.setTag(holder);
        
		return v;
	}
	
	private static class ViewHolder {
		public TextView tvTitle;
		public TextView tvRead;
		public TextView tvContent;
		public TextView tvTime;
	}
}
