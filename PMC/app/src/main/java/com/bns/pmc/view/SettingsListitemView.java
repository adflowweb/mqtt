package com.bns.pmc.view;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bns.pmc.R;
import com.bns.pmc.util.CommonUtil;

public class SettingsListitemView extends RelativeLayout {

    public static class MenuItem {
    	public static final int MENU_FONTSIZE = 0;
    	public static final int MENU_SOUND_NOTI = 1;
    	public static final int MENU_ALARM = 2;
    	public static final int MENU_VIBRATION = 3;
    	public static final int MENU_POPUP = 4;
    	public static final int MENU_MSG_SAVECOUNT = 5;
    	public static final int MENU_MSG_DELETE_ALL = 6;
    	public static final int MENU_PTT_NUMBER = 7;
    	public static final int MENU_PTT_GROUP = 8;
    	
        public static final int TYPE_CATEGORY = 0;
        public static final int TYPE_NORMAL = 1;
        public static final int TYPE_SEEK_BAR = 2;
        public static final int TYPE_CHECK_BOX = 3;
        public static final int TYPE_TITLE = 4;
        public static final int TYPE_COUNT = TYPE_TITLE + 1;

        private int m_nType;
        private int m_nMenu;
        private String m_strTitle;
        private String m_strText;
        private boolean m_bCheck;
        private int m_nProgress;

        public MenuItem(Context context, int type, int menu) {
            m_nType = type;
            m_nMenu = menu;
            
            switch (m_nMenu) {
            	case MENU_FONTSIZE:            		
            		m_strTitle = context.getResources().getString(R.string.settings_menu_fontsize);
            		break;
            	case MENU_SOUND_NOTI:
            		m_strTitle = context.getResources().getString(R.string.settings_menu_sound_noti);
            		break;
            	case MENU_ALARM:
            		m_strTitle = context.getResources().getString(R.string.settings_menu_alam);
            		break;
            	case MENU_VIBRATION:
            		m_strTitle = context.getResources().getString(R.string.settings_menu_vibration);
            		break;
            	case MENU_POPUP:
            		m_strTitle = context.getResources().getString(R.string.settings_menu_popup);
            		break;
            	case MENU_MSG_SAVECOUNT:
            		m_strTitle = context.getResources().getString(R.string.settings_menu_msg_save_count);
            		break;
            	case MENU_MSG_DELETE_ALL:
            		m_strTitle = context.getResources().getString(R.string.settings_menu_msg_delete_all);
            		break;
            	case MENU_PTT_NUMBER :
            		m_strTitle = "Ptt: ";//context.getResources().getString(R.string.settings_menu_ptt_number);
            		break;
            	case MENU_PTT_GROUP :
            		m_strTitle = "Grp: ";//context.getResources().getString(R.string.settings_menu_ptt_group);
            		break;
            	default:
            		break;
            }
        }

        public int getType() {
            return m_nType;
        }
        
        public int getMenu() {
            return m_nMenu;
        }

        public String getTitle() {
            return m_strTitle;
        }
        
        public String getText() {
        	return m_strText;
        }
        
        public void setText(String text) {
        	m_strText = text;
        }
        
        public boolean getCheck() {
        	return m_bCheck;
        }
        
        public void setCheck(boolean value) {
        	m_bCheck = value;
        }
        
        public int getProgress() {
        	return m_nProgress;
        }
        
        public void setProgress(int progress) {
        	m_nProgress = progress;
        }
    }
    
    private Context m_context;
    private RelativeLayout m_layNormal;
    private SeekBar m_seekBar_fontsize;
    private TextView m_separate_text;
    private TextView m_textview_title;
    private TextView m_textview_sub;
    private CheckBox m_checkbox_set;

    public SettingsListitemView(Context context) {
        super(context);
        m_context = context;
        init();
    }

    public SettingsListitemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_context = context;
        init();
    }

    public SettingsListitemView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        m_context = context;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) m_context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_settings_layout, this, true);

        m_layNormal = (RelativeLayout) findViewById(R.id.relativelayout_settings_normal);

        m_separate_text = (TextView) findViewById(R.id.separate_settings_text);
        m_textview_title = (TextView) findViewById(R.id.textView_settings_title);

        m_textview_sub = (TextView) findViewById(R.id.textView_settings_normal_sub);
        m_seekBar_fontsize = (SeekBar) findViewById(R.id.seekBar_settings_fontSize);
        m_checkbox_set = (CheckBox) findViewById(R.id.checkBox_settings);
    }
    
    public void setFontPercent(int percent) {
    	float px_titleSize = getResources().getDimension(R.dimen.settings_title_text_size);
    	float px_subSize = getResources().getDimension(R.dimen.settings_title_text_size);
    	
    	m_textview_title.setTextSize(CommonUtil.conv_Size(px_titleSize, percent));
    	m_textview_sub.setTextSize(CommonUtil.conv_Size(px_subSize, percent));
    	//m_checkbox_set.setTextSize(size);
    }
    
    public int getProgress() {
        return m_seekBar_fontsize.getProgress();
    }

    public int getProgress_Max() {
        return m_seekBar_fontsize.getMax();
    }

    public void setType(int type) {
        switch (type) {
            case MenuItem.TYPE_CATEGORY:
                m_layNormal.setVisibility(GONE);
                m_separate_text.setVisibility(VISIBLE);
                m_textview_sub.setVisibility(GONE);
                m_seekBar_fontsize.setVisibility(GONE);
                m_checkbox_set.setVisibility(GONE);
                break;
            case MenuItem.TYPE_NORMAL:
            	m_layNormal.setVisibility(VISIBLE);
                m_separate_text.setVisibility(GONE);
                m_textview_sub.setVisibility(VISIBLE);
                m_seekBar_fontsize.setVisibility(GONE);
                m_checkbox_set.setVisibility(GONE);
                break;
            case MenuItem.TYPE_SEEK_BAR:
                m_layNormal.setVisibility(VISIBLE);
                m_separate_text.setVisibility(GONE);
                m_textview_sub.setVisibility(VISIBLE);
                m_seekBar_fontsize.setVisibility(VISIBLE);
                m_checkbox_set.setVisibility(GONE);
                break;
            case MenuItem.TYPE_CHECK_BOX:
                m_layNormal.setVisibility(VISIBLE);
                m_separate_text.setVisibility(GONE);
                m_textview_sub.setVisibility(GONE);
                m_seekBar_fontsize.setVisibility(GONE);
                m_checkbox_set.setVisibility(VISIBLE);
                break;
            case MenuItem.TYPE_TITLE:
            	m_layNormal.setVisibility(VISIBLE);
                m_separate_text.setVisibility(GONE);
                m_textview_sub.setVisibility(GONE);
                m_seekBar_fontsize.setVisibility(GONE);
                m_checkbox_set.setVisibility(GONE);
            	break;
            default:
                m_layNormal.setVisibility(VISIBLE);
                m_separate_text.setVisibility(GONE);
                m_textview_sub.setVisibility(VISIBLE);
                m_seekBar_fontsize.setVisibility(GONE);
                m_checkbox_set.setVisibility(GONE);
                break;
        }
    }

    public void setItem(MenuItem item) {
        int type = item.getType();
        setType(type);
       
        switch (type) {
            case MenuItem.TYPE_CATEGORY:
            	{
            		m_separate_text.setText(item.getTitle());
            	}
                break;
            case MenuItem.TYPE_SEEK_BAR:
            	{
	            	m_textview_title.setText(item.getTitle());
	            	//m_textview_title.getLayoutParams().height = (int) getResources().getDimension(R.dimen.Settings_title_height);
	            	m_textview_sub.setText(item.getText() + "%");
	            	//m_textview_sub.getLayoutParams().height = (int) getResources().getDimension(R.dimen.Settings_sub_height);
	            	m_seekBar_fontsize.setProgress(item.getProgress());
	            	//m_seekBar_fontsize.getLayoutParams().height = (int) getResources().getDimension(R.dimen.Settings_seekbar_height);
            	}
            	break;
            case MenuItem.TYPE_CHECK_BOX:
            	{
            		RelativeLayout.LayoutParams params;
	            	m_textview_title.setText(item.getTitle());
	            	params = (LayoutParams) m_textview_title.getLayoutParams();
	            	params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
	            	
	            	m_checkbox_set.setChecked(item.getCheck());
            	}
            	break;
            case MenuItem.TYPE_NORMAL:
            	{
	            	m_textview_title.setText(item.getTitle());
	            	
	            	if (item.getMenu() == MenuItem.MENU_ALARM) {
	            		RelativeLayout.LayoutParams params = (LayoutParams) m_textview_title.getLayoutParams();
	            		params.leftMargin = 30;
	            	} 

	            	m_textview_sub.setText(item.getText());
            	}
            	break;
            default:
            	{
            		m_textview_title.setText(item.getTitle());
            	}
                break;
        }
        setFontPercent((item.getProgress()+10)*10);
    }
}
