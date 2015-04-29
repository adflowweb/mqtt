package com.bns.pmc;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bns.pmc.adapter.SettingsAdapter;
import com.bns.pmc.provider.DataColumn;
import com.bns.pmc.util.Log;
import com.bns.pmc.util.PMCType;
import com.bns.pmc.view.SettingsListitemView.MenuItem;

public class SettingsActivity extends Activity {
	private Context m_context = this;
	private SettingsAdapter m_adapter;

	ListView m_listView;
	TextView m_tvValue;
	
	ArrayList<String> m_listSoundData = new ArrayList<String>();
	ArrayList<String> m_listSoundTitle = new ArrayList<String>();
	private int m_nIdxSoundSel = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		ImageView ivBtCenter = (ImageView) findViewById(R.id.bottom_button_center);
		TextView tvBtRight = (TextView) findViewById(R.id.bottom_button_right);
		ivBtCenter.setVisibility(View.GONE);
		tvBtRight.setText(R.string.back);
		m_listView = (ListView) findViewById(R.id.listView_settings);
		
		//float scale = 0.0f;
		//scale = getResources().getConfiguration().
		
		//Log.i("kyu", "sys " + scale);
		
		//m_listview.setScaleX(1.1f);
		//m_listview.setScaleY(1.1f);
		
/*		List<Header> objects = new ArrayList<PreferenceActivity.Header>();
		objects.add(new Header());
		Header a = new Header();
		a.intent = new Intent();
		objects.add(a);
		objects.add(a);
		objects.add(a);
		objects.add(new Header());
		objects.add(a);
		objects.add(a);
		objects.add(a);
		objects.add(a);
		
		
		Log.i(TAG, "m_listview.isInTouchMode() " + m_listview.isInTouchMode());
		m_listview.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                if (position == 3) {
                    View a = view.focusSearch(View.FOCUS_FORWARD);
                    a.setSelected(true);
                }
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
                
            }
        });*/
		
		m_adapter = new SettingsAdapter(m_context, getMenuObjectList());
		m_listView.setAdapter(m_adapter);
		/*m_Listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i(PMCType.TAG, "onItemClick = " + view);
				optionSaveCount();
			}
		});*/
		
		m_listView.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					Log.i(PMCType.TAG, "onKey = " + keyCode);
					
					if (keyCode == KeyEvent.KEYCODE_MENU)
						return true;
					
					int pos = m_listView.getSelectedItemPosition();
					
					switch (pos) {
						case MenuItem.MENU_FONTSIZE:
							int size = m_adapter.getFontPercent();
								
							if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
								size+=10;
								if (size <= m_adapter.getProgress_Max()) {
									m_adapter.setFontPercent(size);
									m_adapter.notifyDataSetChanged();
								}
							} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
								size-=10;
								if (size >= 100) {
									m_adapter.setFontPercent(size);
									m_adapter.notifyDataSetChanged();
								}
							}
							break;
						case MenuItem.MENU_ALARM :
							if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
								optionRingTone();
							}
							break;
						case MenuItem.MENU_SOUND_NOTI:
							if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			            		m_adapter.changeSoundNoti();
			            		m_adapter.notifyDataSetChanged();
							}
		            		break;
						case MenuItem.MENU_VIBRATION:
							if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			            		m_adapter.changeVibrationNoti();
			            		m_adapter.notifyDataSetChanged();
							}
		            		break;
						case MenuItem.MENU_POPUP:
							if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			            		m_adapter.changePopupNoti();
			            		m_adapter.notifyDataSetChanged();
							}
		            		break;
						case MenuItem.MENU_MSG_SAVECOUNT:
							if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
								optionSaveCount();
							}
							break;
						case MenuItem.MENU_MSG_DELETE_ALL:
							if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
								optionDelete();
							}
							break;
						default:
							break;
					}
				}
				return false;
			}
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		m_adapter.saveConfigure();
	}
	
    private List<MenuItem> getMenuObjectList() {
        List<MenuItem> list = new ArrayList<MenuItem>();
        list.add(new MenuItem(m_context, MenuItem.TYPE_SEEK_BAR, MenuItem.MENU_FONTSIZE));
        list.add(new MenuItem(m_context, MenuItem.TYPE_CHECK_BOX, MenuItem.MENU_SOUND_NOTI));
        list.add(new MenuItem(m_context, MenuItem.TYPE_NORMAL, MenuItem.MENU_ALARM));
        list.add(new MenuItem(m_context, MenuItem.TYPE_CHECK_BOX, MenuItem.MENU_VIBRATION));
        list.add(new MenuItem(m_context, MenuItem.TYPE_CHECK_BOX, MenuItem.MENU_POPUP));
        list.add(new MenuItem(m_context, MenuItem.TYPE_NORMAL, MenuItem.MENU_MSG_SAVECOUNT));
        list.add(new MenuItem(m_context, MenuItem.TYPE_TITLE, MenuItem.MENU_MSG_DELETE_ALL));
        //list.add(new MenuItem(m_context, MenuItem.TYPE_NORMAL, MenuItem.MENU_PTT_NUMBER));
        //list.add(new MenuItem(m_context, MenuItem.TYPE_NORMAL, MenuItem.MENU_PTT_GROUP));
        return list;
    }

    private void optionRingTone() {
//    	String strSoundData = m_adapter.getSoundData();
        String strSoundTitle = m_adapter.getSoundTitle();
    	m_listSoundData.clear();
    	m_listSoundTitle.clear();
    	m_nIdxSoundSel = -1;
    	
//    	Uri uri = RingtoneManager.getActualDefaultRingtoneUri(m_context, RingtoneManager.TYPE_NOTIFICATION);
//    	Cursor c = getContentResolver().query(uri, null, null, null, null);
    	
    	RingtoneManager rm = new RingtoneManager(m_context);
    	rm.setType(RingtoneManager.TYPE_NOTIFICATION);
    	Cursor c = rm.getCursor();
    	
    	if (c != null) {
    	    int nCount = c.getCount();
            Log.i(PMCType.TAG, "count " + nCount);
        	while (c.moveToNext()) {
//        		String strData = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
//        		String strTitle = c.getString(c.getColumnIndex(MediaStore.MediaColumns.TITLE));
//        		Log.i(PMCType.TAG, "uri =  " + strData);
//                m_listSoundData.add(strData);
//                m_listSoundTitle.add(strTitle);
        		
        	    Uri uri = rm.getRingtoneUri(c.getPosition());
        		m_listSoundData.add(uri.toString());
        		m_listSoundTitle.add(c.getString(RingtoneManager.TITLE_COLUMN_INDEX));
        	}
    	}
    	
//    	{
//    		String customUri = "android.resource://" + getPackageName() + "/"+R.raw.piss_call;
//    		Log.i(PMCType.TAG, "custom uri " + customUri);
//    		m_listSoundData.add(customUri);
//    		m_listSoundTitle.add("Piss Call");
//    	}
//    	
    	for (int i=0; i <m_listSoundData.size(); i++) {
//    		if (TextUtils.equals(m_listSoundData.get(i), strSoundData)) {
    	    if (TextUtils.equals(m_listSoundTitle.get(i), strSoundTitle)) {
    			m_nIdxSoundSel = i;
    			break;
    		}
    	}
    	
    	String[] arr = new String[m_listSoundTitle.size()];
    	for (int i=0; i<m_listSoundTitle.size(); i++) {
    		arr[i] = m_listSoundTitle.get(i);
    	}
	
    	AlertDialog.Builder ab = new AlertDialog.Builder(m_context);
    	ab.setTitle(R.string.alam);
    	ab.setSingleChoiceItems(arr, m_nIdxSoundSel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				m_nIdxSoundSel = which;
				String strData = m_listSoundData.get(which);
				Uri uri = Uri.parse(strData);
				new asyncMediaPlay().execute(uri);
			}
			
		}).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (m_nIdxSoundSel != -1) {
					String strData = m_listSoundData.get(m_nIdxSoundSel);
					String strTitle = m_listSoundTitle.get(m_nIdxSoundSel);
					Log.i(PMCType.TAG, "title" + strTitle);
					m_adapter.setSoundData(strData);
					m_adapter.setSoundTitle(strTitle);
					m_adapter.notifyDataSetChanged();
				}
				dialog.dismiss();
				
			}
		}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
    }
    
    private void optionSaveCount() {
    	int nSaveCnt = m_adapter.getMessageSaveCount();
    	int nValue = (nSaveCnt-100) / 10;
    	Log.i(PMCType.TAG, "[Value idx]" + nValue);
    	
    	final Dialog d = new Dialog(m_context);
    	d.setTitle(R.string.settings_menu_msg_save_count);
    	d.setContentView(R.layout.item_picker_layout);
    	final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
    	String[] displayedValues = new String[41];
    	for (int i=0; i<41; i++)
    	{
    		displayedValues[i] = Integer.toString(i*10 + 100);
    	}
        np.setMaxValue(displayedValues.length-1);
        np.setMinValue(0);
        np.setDisplayedValues(displayedValues);
        np.setValue(nValue);
        np.setFocusableInTouchMode(true);
        //np.setFocusable(true);
        np.setWrapSelectorWheel(false);
        np.setLongClickable(true);
       
        final EditText input = findInput(np);
        //input.setFocusable(true);
/*       input.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				Log.i(PMCType.TAG, "afterTextChanged " + s.toString());
				
                if (s.toString().length() != 0) {
                    Integer value = Integer.parseInt(s.toString());
                    if (value >= np.getMinValue()) {
                        np.setValue(value);
                    }
                }
				
			}
		});*/
		
        //np.setLongClickable(true);
       // np.setFocusableInTouchMode(true);
        np.setOnValueChangedListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				Log.i(PMCType.TAG, "chane " + oldVal + " " +newVal);
				
								//picker.setFocusable(true);
				//d.findViewById(R.id.numberPicker).requestFocus();
				
			}
		});


/*        np.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					Log.i(PMCType.TAG, "event " +" " +v + " " + event.getAction()+ " "+ keyCode);
					if (event.getKeyCode() >= KeyEvent.KEYCODE_0 && event.getKeyCode() <= KeyEvent.KEYCODE_9) {
						Log.i(PMCType.TAG, "haha ");
						//return true;
					}
					else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
						//np.setFocusable(true);
					}
				}
				return false;
			}
		});*/
        
        DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				Log.i(PMCType.TAG, "key event " +" " +dialog.toString() + " " + event.getAction()+ " "+ keyCode);
				
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER || event.getKeyCode() == KeyEvent.KEYCODE_SOFT_LEFT) {
						Log.i(PMCType.TAG, "text " + input.getText().toString());
						int nVal = Integer.parseInt(input.getText().toString());
						m_adapter.setMessageSaveCount(nVal);
						m_adapter.notifyDataSetChanged();
						d.dismiss();
						return false;
					} /*else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
						//np.setFocusable(true);
						Log.i(PMCType.TAG, "down up");
						//np.clearFocus();
						return np.dispatchKeyEvent(event);
					}*/
				}/* else {
					input.setFocusable(true);
					return input.dispatchKeyEvent(event);
				}*/
/*				Log.i(PMCType.TAG, "dd1 " + input.isFocusable());
				Log.i(PMCType.TAG, "ddd " + np.isFocusable());*/
				return false;
			}
		};
		
		d.setOnKeyListener(keylistener);
        d.show();   
    }
    
    private EditText findInput(ViewGroup np) {
        int count = np.getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = np.getChildAt(i);
            if (child instanceof ViewGroup) {
                findInput((ViewGroup) child);
            } else if (child instanceof EditText) {
                return (EditText) child;
            }
        }
        return null;
    }
    
    /**
	 * Delete 동작 함수
	 */
	private void optionDelete() {
		AlertDialog.Builder ab = new AlertDialog.Builder(m_context);
		ab.setTitle(R.string.delete_popup_title);
		ab.setMessage(R.string.delete_popup_context);
		ab.setNegativeButton(R.string.cancel, null);
		ab.setPositiveButton(R.string.delete, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				new asyncDeleteMsg().execute(1);
			}
		});
		ab.show();
	}
	
	/**
	 * asyncDeleteMsg 함수
	 * @author kyu
	 *
	 */
	private class asyncDeleteMsg extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {
			Log.d(PMCType.TAG, "doInBackground start");
			
			int nResult = 0;
		    getContentResolver().delete(DataColumn.CONTENT_URI_DEL_ALL, null, null);
    		
			return nResult;
		}
		
		@Override
    	protected void onPostExecute(Integer result) {
			Toast.makeText(m_context, R.string.delete_popup_result_true, Toast.LENGTH_SHORT).show();
		}
	}
	
	private class asyncMediaPlay extends AsyncTask<Uri, Integer, Integer> {

		@Override
		protected Integer doInBackground(Uri... params) {
			Log.d(PMCType.TAG, "doInBackground start");
			Uri uri = params[0];
			MediaPlayer mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			try {
				mediaPlayer.setDataSource(m_context, uri);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				mediaPlayer.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mediaPlayer.start();
			return null;
		}
	}
}
