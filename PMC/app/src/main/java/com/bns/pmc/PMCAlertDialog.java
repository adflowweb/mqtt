package com.bns.pmc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

public class PMCAlertDialog extends AlertDialog{

	protected PMCAlertDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public static AlertDialog createDialog(Context context, String msg) {
		  AlertDialog.Builder mPttDialog = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_DARK);
//		  mPttDialog.setTitle(" ");
		  if (msg.isEmpty()) {
			  msg = "no message";
		  }
		  mPttDialog.setMessage(msg);
		  /*mPttDialog.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if (event.getAction() == KeyEvent.ACTION_DOWN) {
						switch (keyCode) {
						case KeyEvent.KEYCODE_DPAD_CENTER: {
							dialog.dismiss();
							break;
						}
	
						default:
							break;
						}
					}
					return false;
				}
		  });*/
		return mPttDialog.create();
	    }
	  
}
