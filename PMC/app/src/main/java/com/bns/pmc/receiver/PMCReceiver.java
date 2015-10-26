package com.bns.pmc.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bns.pmc.NewActivity;
import com.bns.pmc.util.Log;
import com.bns.pmc.util.PMCType;

public class PMCReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
		if (action.equals("android.intent.action.BOOT_COMPLETED")) {
	        Log.e(PMCType.TAG, "Boot Complete");
	        Intent i = new Intent(context, PMCService.class);
	        i.setAction(PMCType.BNS_PMC_SERVICE_START_ACTION);
	        context.startService(i);
		} else if (action.equals("com.bns.pmc.action.newmsg.contact")) {
			Log.e(PMCType.TAG, "New Msg Start");
			
			String strName = intent.getStringExtra("name");
            String strNumber = intent.getStringExtra("number");
            
            Log.i(PMCType.TAG, "contact msg name= " + strName + " number=" + strNumber);
            
			Intent i = new Intent(context, NewActivity.class);
			i.setAction(action);
			i.putExtra("name", strName);
			i.putExtra("number", strNumber);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(i);
		}
	}
}
