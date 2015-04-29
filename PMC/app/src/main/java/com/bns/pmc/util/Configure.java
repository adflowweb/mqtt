package com.bns.pmc.util;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;

public class Configure {
	
	public static final String UFMI_INIT = "00*00*00";
	public static final String GROUP_LIST_INIT = "noGroupList";
	public static final String BUNCH_INIT = null;
	public static final int VER_INIT = PMCType.PMC_PTT_MODE_NONE;
	
	private final String STR_PREFERENCE_NAME = "config";
	private final String STR_UFMI = "ufmi";
	private final String STR_GROUP_LIST = "grouplist";
	private final String STR_BUNCH_ID = "bunchid";
	private final String STR_VERSION = "version";
	private final String STR_FONT_PERCENT = "fontpercent";
	private final String STR_SOUND_NOTI = "soundnoti";
	private final String STR_VIBRATE_NOTI = "vibratenoti";
	private final String STR_POPUP_NOTI = "popupnoti";
	private final String STR_SOUND_DATA = "sounddata";
	private final String STR_SOUND_TITLE = "soundtitle";
	private final String STR_MSG_SAVE_COUNT ="msgsavecount";
	
	private SharedPreferences m_prefernces;
	private static Configure m_sInstance;
	private Context m_context;
	
	private String m_strUFMI;
	private String m_strGroupList;
	private String m_strBunchID;
	private int m_nVersion;
	private int m_nFontSize;
	private int m_nFontPercent;
	private boolean m_bSoundnoti;
	private boolean m_bVibratenoti;
	private boolean m_bPopupnoti;
	private String m_strSoundData;
	private String m_strSoundTitle;
	private int m_nMsgSaveCount;
	
	private Configure(Context context) {
		m_context = context;
		loadSharedPreferences();
		readSharedPreferences();
	}
	
	public static synchronized Configure getInstance(Context context) {
		if (m_sInstance == null) {
			m_sInstance = new Configure(context);
        }
        return m_sInstance;
	}
	
	public void loadSharedPreferences() {
		m_prefernces = m_context.getSharedPreferences(STR_PREFERENCE_NAME, Service.MODE_PRIVATE);
	}
	
	public void readSharedPreferences() {
		m_strUFMI = m_prefernces.getString(STR_UFMI, UFMI_INIT);
		m_strGroupList = m_prefernces.getString(STR_GROUP_LIST, GROUP_LIST_INIT);
		m_strBunchID = m_prefernces.getString(STR_BUNCH_ID, BUNCH_INIT);
		m_nVersion = m_prefernces.getInt(STR_VERSION, VER_INIT);
		m_nFontPercent = m_prefernces.getInt(STR_FONT_PERCENT, 100);
		m_bSoundnoti = m_prefernces.getBoolean(STR_SOUND_NOTI, true);
		m_bVibratenoti = m_prefernces.getBoolean(STR_VIBRATE_NOTI, false);
		m_bPopupnoti = m_prefernces.getBoolean(STR_POPUP_NOTI, false);
		m_nMsgSaveCount = m_prefernces.getInt(STR_MSG_SAVE_COUNT, 500);
		
		{ 
			// 15.04.06 롬을 새로 깔고 부팅 시 링톤 디비가 생성되기 전에 링톤 디비를 부르면서 생기는 오류가 있음
			// 그러므로 초기에 링톤 디비를 찾아서는 안됨
			// 강제로 Title을 "Pollux"로 지정하고
			// Data는 Title이 Null 값일때 DB에서 찾아오도록 수정.
			
			/*String strData = "", strTitle = "";
			RingtoneManager rm = new RingtoneManager(m_context);
	    	rm.setType(RingtoneManager.TYPE_NOTIFICATION);
	    	Cursor c = rm.getCursor();
	    	
	    	if (c != null) {
	    		while (c.moveToNext()) {
	    			Uri uri = rm.getRingtoneUri(c.getPosition());
	    			strData = uri.toString();
	    			strTitle = c.getString(RingtoneManager.TITLE_COLUMN_INDEX);
	    			
	    			if (strTitle.compareToIgnoreCase("Pollux") == 0)
	    				break;
	    		}
	    	}*/
	    	
	    	/*Uri uri = RingtoneManager.getActualDefaultRingtoneUri(m_Context, RingtoneManager.TYPE_NOTIFICATION);
	    	Cursor cursor = m_Context.getContentResolver().query(uri, null, null, null, null);*/
			
	    	/*if (cursor.moveToFirst()) {
	    		strData = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
	    		strTitle = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE));
	    	}*/
	    	
			//m_strSoundData = m_prefernces.getString(STR_SOUND_DATA, strData);
			//m_strSoundTitle = m_prefernces.getString(STR_SOUND_TITLE, strTitle);
		}
		m_strSoundData = m_prefernces.getString(STR_SOUND_DATA, null);
		m_strSoundTitle = m_prefernces.getString(STR_SOUND_TITLE, "Pollux");
	}
	
	public void saveSharedPreferences() {
		SharedPreferences.Editor e = m_prefernces.edit();
		e.putString(STR_UFMI, m_strUFMI);
		e.putString(STR_GROUP_LIST, m_strGroupList);
		e.putString(STR_BUNCH_ID, m_strBunchID);
		e.putInt(STR_VERSION, m_nVersion);
		e.putInt(STR_FONT_PERCENT, m_nFontPercent);
		e.putBoolean(STR_SOUND_NOTI, m_bSoundnoti);
		e.putBoolean(STR_VIBRATE_NOTI, m_bVibratenoti);
		e.putBoolean(STR_POPUP_NOTI, m_bPopupnoti);
		e.putString(STR_SOUND_DATA, m_strSoundData);
		e.putString(STR_SOUND_TITLE, m_strSoundTitle);
		e.putInt(STR_MSG_SAVE_COUNT, m_nMsgSaveCount);
		e.commit();
	}
	
	public void setUFMI(String ufmi) {
		m_strUFMI = ufmi;;
	}
	
	public String getUFMI() {
		return m_strUFMI;
	}
	
	public void setGroupList(String group) {
		m_strGroupList = group;
	}
	
	public String getGroupList() {
		return m_strGroupList;
	}
	
	public void setBunchID(String id) {
		m_strBunchID = id;
	}
	
	public String getBunchID() {
		return m_strBunchID;
	}
	
	public void setVersion(int ver) {
		m_nVersion = ver;
	}
	
	public int getVersion() {
		return m_nVersion;
	}
	
	public String getUrbanNumber() {
		String str = CommonUtil.getUrbanNumber(m_strUFMI);
		return str;
	}
	
	public String getFleetNumber() {
		String str = CommonUtil.getFleetNumber(m_strUFMI);
		return str;
	}
	
	public int getFontSize() {
		return m_nFontSize;
	}
	
	public void setFontSize(int size) {
		m_nFontSize = size;
	}
	
	public int getFontPercent() {
		return m_nFontPercent;
	}
	
	public void setFontPercent(int value) {
		m_nFontPercent = value;
	}
	
	public boolean getSountNoti() {
		return m_bSoundnoti; 
	}
	
	public void setSoundNoti(boolean value) {
		m_bSoundnoti = value;
	}
	
	public boolean getVibrateNoti() {
		return m_bVibratenoti;
	}
	
	public void setVibrateNoti(boolean value) {
		m_bVibratenoti = value;
	}
	
	public boolean getPopupNoti() {
		return m_bPopupnoti;
	}
	
	public void setPopupNoti(boolean value) {
		m_bPopupnoti = value;
	}
	
	public String getSoundData() {
		if (m_strSoundData == null) {
			RingtoneManager rm = new RingtoneManager(m_context);
	    	rm.setType(RingtoneManager.TYPE_NOTIFICATION);
	    	Cursor c = rm.getCursor();
	    	
	    	if (c != null) {
	    		while (c.moveToNext()) {
	    			Uri uri = rm.getRingtoneUri(c.getPosition());
	    			String strTitle = c.getString(RingtoneManager.TITLE_COLUMN_INDEX);
	    			
	    			if (strTitle.compareToIgnoreCase(m_strSoundTitle) == 0) {
	    				m_strSoundData = uri.toString();
	    				return m_strSoundData;
	    			}
	    		}
	    	}
		} else {
			return m_strSoundData;
		}
		
		return m_strSoundData;
	}
	
	public void setSoundData(String value) {
		m_strSoundData = value;
	}
	
	public String getSoundTitle() {
		return m_strSoundTitle;
	}
	
	public void setSoundtitle(String value) {
		m_strSoundTitle = value;
	}
	
	public int getMessageSaveCount() {
		return m_nMsgSaveCount;
	}
	
	public void setMessageSaveCount(int value) {
		m_nMsgSaveCount = value;
	}
}
