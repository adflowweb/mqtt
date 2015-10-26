package com.bns.pmc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.bns.pmc.util.Configure;
import com.bns.pmc.view.SettingsListitemView;
import com.bns.pmc.view.SettingsListitemView.MenuItem;

import java.util.List;

public class SettingsAdapter extends ArrayAdapter<MenuItem> {

    private Context m_Context;
    private Configure m_Configure;

    private int m_progress_max;
    private int m_fontpercent;
    private boolean m_bSoundNoti;
    private boolean m_bVibrationNoti;
    private boolean m_bPopupNoti;
    private String m_strSoundData;
    private String m_strSoundTitle;
    private int m_nMsgSaveCount;

    public SettingsAdapter(Context context, List<MenuItem> objects) {
        super(context, 0, objects);
        m_Context = context;
        m_Configure = Configure.getInstance(m_Context);
        m_fontpercent = m_Configure.getFontPercent();
        m_bSoundNoti = m_Configure.getSountNoti();
        m_bVibrationNoti = m_Configure.getVibrateNoti();
        m_bPopupNoti = m_Configure.getPopupNoti();
        m_strSoundData = m_Configure.getSoundData();
        m_strSoundTitle = m_Configure.getSoundTitle();
        m_nMsgSaveCount = m_Configure.getMessageSaveCount();
    }

    @Override
    public int getItemViewType(int position) {
        MenuItem item = getItem(position);
        return item.getType();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false; // because of categories
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) != MenuItem.TYPE_CATEGORY;
    }

    @Override
    public int getViewTypeCount() {
        return MenuItem.TYPE_COUNT;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MenuItem item = getItem(position);

        SettingsListitemView view = null;

        if (convertView == null) {
            view = new SettingsListitemView(getContext());
            m_progress_max = (view.getProgress_Max() + 10) * 10;
        } else {
            view = (SettingsListitemView) convertView;
        }

        switch (item.getMenu()) {
            case MenuItem.MENU_FONTSIZE:
                item.setText(Integer.toString(m_fontpercent));
                break;
            case MenuItem.MENU_ALARM:
                item.setText(m_strSoundTitle);
                break;
            case MenuItem.MENU_SOUND_NOTI:
                item.setCheck(m_bSoundNoti);
                break;
            case MenuItem.MENU_VIBRATION:
                item.setCheck(m_bVibrationNoti);
                break;
            case MenuItem.MENU_POPUP:
                item.setCheck(m_bPopupNoti);
                break;
            case MenuItem.MENU_MSG_SAVECOUNT:
                item.setText(Integer.toString(m_nMsgSaveCount));
                break;
            case MenuItem.MENU_PTT_NUMBER:
                item.setText(m_Configure.getUFMI());
                break;
            case MenuItem.MENU_PTT_GROUP:
                item.setText(m_Configure.getGroupList());
                break;

        }
        item.setProgress((m_fontpercent / 10) - 10);
        view.setItem(item, position);
        return view;
    }

    public void setFontPercent(int value) {
        m_fontpercent = value;
        m_Configure.setFontPercent(value);
    }

    public int getFontPercent() {
        return m_fontpercent;
    }

    public int getProgress_Max() {
        return m_progress_max;
    }

    public void changeSoundNoti() {
        m_bSoundNoti = !m_bSoundNoti;
        m_Configure.setSoundNoti(m_bSoundNoti);
    }

    public void changeVibrationNoti() {
        m_bVibrationNoti = !m_bVibrationNoti;
        m_Configure.setVibrateNoti(m_bVibrationNoti);
    }

    public void changePopupNoti() {
        m_bPopupNoti = !m_bPopupNoti;
        m_Configure.setPopupNoti(m_bPopupNoti);
    }

    public String getSoundData() {
        return m_strSoundData;
    }

    public void setSoundData(String value) {
        m_strSoundData = value;
        m_Configure.setSoundData(m_strSoundData);
    }

    public String getSoundTitle() {
        return m_strSoundTitle;
    }

    public void setSoundTitle(String value) {
        m_strSoundTitle = value;
        m_Configure.setSoundtitle(m_strSoundTitle);
    }

    public int getMessageSaveCount() {
        return m_nMsgSaveCount;
    }

    public void setMessageSaveCount(int value) {
        m_nMsgSaveCount = value;
        m_Configure.setMessageSaveCount(m_nMsgSaveCount);
    }

    public void saveConfigure() {
        m_Configure.saveSharedPreferences();
    }
}