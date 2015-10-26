package com.bns.pmc;

import java.util.ArrayList;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bns.pmc.util.CommonUtil;
import com.bns.pmc.util.Log;
import com.bns.pmc.util.PMCType;

public class NotiPopupActivity extends Activity implements OnKeyListener {
	private Context m_context = this;
	private String m_strTitle;
	private String m_strSender;
	private String m_strContext;
	private boolean m_bControl;
	private int m_nLinkPosition = 0;	
	
	AlertDialog m_alertDialog = null;
	TextView m_textview_context;
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.e(PMCType.TAG, "[onNewIntent]");
		
		// intent
		{
			m_strTitle = intent.getStringExtra(PMCType.BNS_PMC_INTENT_EXTRA_TITLE);
			m_strSender = intent.getStringExtra(PMCType.BNS_PMC_INTENT_EXTRA_SENDER);
			m_strContext = intent.getStringExtra(PMCType.BNS_PMC_INTENT_EXTRA_CONTENT);
			m_bControl = intent.getBooleanExtra(PMCType.BNS_PMC_INTENT_EXTRA_CONTROL, false);
		}
		
		// onNewIntent가 호출된다는건 AlertDialog가 객체가 있다는 것!
		m_alertDialog.setTitle(m_strTitle);
		m_textview_context.setText(m_strContext);
		// Pattern
        setPattern();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(PMCType.TAG, "[onCreate]");
		/*getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
    			| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		requestWindowFeature(Window.FEATURE_NO_TITLE);      
	    /*getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);		
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, 
	            WindowManager.LayoutParams.FLAG_BLUR_BEHIND);*/
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);     
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | 
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, 
	            WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		
		/**
		 * intent
		 */
		{
			Intent intent = getIntent();
			
			m_strTitle = intent.getStringExtra(PMCType.BNS_PMC_INTENT_EXTRA_TITLE);
			m_strSender = intent.getStringExtra(PMCType.BNS_PMC_INTENT_EXTRA_SENDER);
			m_strContext = intent.getStringExtra(PMCType.BNS_PMC_INTENT_EXTRA_CONTENT);
			m_bControl = intent.getBooleanExtra(PMCType.BNS_PMC_INTENT_EXTRA_CONTROL, false);
		}
		
		AlertDialog.Builder ab = new AlertDialog.Builder(m_context);
		LayoutInflater inflater = (LayoutInflater) m_context.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.item_popup_layout, null);
		m_textview_context = (TextView) v.findViewById(R.id.textView_popup_context);
		ab.setView(v);
        ab.setTitle(m_strTitle);
        m_textview_context.setText(m_strContext);
        // Pattern
        setPattern();
     		
        m_textview_context.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(PMCType.TAG, "click");
				
				URLSpan[] uriSpan = m_textview_context.getUrls();
				if (uriSpan.length != 0) {
					// 15.03.23 autolink 중복 방지
					ArrayList<String> list = new ArrayList<String>();
					for (int i=0; i< uriSpan.length; i++) {
						String strUrl = uriSpan[i].getURL();
						if (list.contains(strUrl) == false)
							list.add(strUrl);
						
					}
					
					final String[] arr = new String[list.size()];
					for (int i=0; i< list.size(); i++) {
						arr[i] = list.get(i);
					}
					//.
					
					AlertDialog.Builder ab = new AlertDialog.Builder(m_context);
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
									String[] strArr = arr[m_nLinkPosition].split("\\:");
									//String strIdx = strArr[0];
									String strContext = strArr[1];
									
									Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+strContext));
								    startActivity(i);
								    finish();
								    
								    return true;
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
			    	
			    	DialogInterface.OnClickListener dlgClicklistener = new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}			    		
			    	};
			    	ab.setItems(arr, dlgClicklistener).show();
				}
			}
		});
        //ab.setMessage(m_strContext);
         
        ab.setPositiveButton(R.string.noti_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //PMCWakeLockUtil.release();
            	Intent i = new Intent(m_context, MainActivity.class);
            	i.setAction(PMCType.BNS_PMC_PMCSERVICE_NOTIPOPUP);
            	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	i.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_NUMBER, m_strSender);
            	i.putExtra(PMCType.BNS_PMC_INTENT_EXTRA_CONTROL, m_bControl);
            	m_context.startActivity(i);
                finish();
            }
        });
        
        ab.setNegativeButton(R.string.noti_close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	finish();
            }
        });
        
        m_alertDialog = ab.create();
        m_alertDialog.setOnKeyListener(this);
        m_alertDialog.show();
        
        // 폰 설정의 조명시간을 가져와서 해당 시간만큼만 화면을 켠다.
/*        int defTimeOut = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 15000);
                 @Override
                public void run() {
                	 	PMCWakeLockUtil.release();
                }
        };
             
        Timer timer = new Timer();
        timer.schedule(task, defTimeOut);*/
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		if (dialog == m_alertDialog) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
					finish();
					return true;
				} else if (event.getKeyCode() == PMCType.BNS_PMC_KEY_CODE_PTT_CALL && 
						event.getRepeatCount() == 0) {
					CommonUtil.callPtt(m_context, m_strSender);
					finish();
					return true;
				} else if (event.getKeyCode() == KeyEvent.KEYCODE_CALL) {
					if (TextUtils.isDigitsOnly(m_strSender))
						CommonUtil.callNormal(m_context, m_strSender);
					else
						Toast.makeText(m_context, R.string.no_sendcall, Toast.LENGTH_SHORT).show();
					finish();
					return true;
				}
			} 
		}
		return false;
	}
	
	private void setPattern() {
		Resources res = m_context.getResources();
			Pattern patternPtt = Pattern.compile(PMCType.BNS_PMC_REGEX_PTT);
			Pattern patternGroup_Ptt = Pattern.compile(PMCType.BNS_PMC_REGEX_GROUP_PTT);
			Linkify.addLinks(m_textview_context, Linkify.PHONE_NUMBERS | Linkify.WEB_URLS);
			Linkify.addLinks(m_textview_context, patternPtt, res.getString(R.string.pattern_ptt));
		    Linkify.addLinks(m_textview_context, patternGroup_Ptt, res.getString(R.string.pattern_ptt_group));
		    
		    m_textview_context.setMovementMethod(null);
		    m_textview_context.setFocusable(true);
	}
}
