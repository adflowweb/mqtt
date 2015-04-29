package com.bns.pmc.util;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

public class DataUtil {
	public static ArrayList<String> getContactsUsingNumber(Context context, String number) { 
		String contact_raw_id = "";
		String name = "";
		ArrayList<String> contactsContractList = new ArrayList<String>();  
		String [] projection = {ContactsContract.Data.RAW_CONTACT_ID, ContactsContract.Data.DISPLAY_NAME};
		String where = ContactsContract.Data.HAS_PHONE_NUMBER + "=1" + " AND " + ContactsContract.Data.DATA1 + " ='" + number + "'"
				+ " AND " + ContactsContract.CommonDataKinds.Phone.TYPE + " ='" + PMCType.BNS_PMC_CONTACT_PTT_TYPE + "'";
		Cursor cursor = context.getContentResolver().query( ContactsContract.Data.CONTENT_URI, projection, where , null, null );
		try {
			while(cursor.moveToNext() ) {
				//Log.w(TAG, "firstCursor.1 = " + cursor.getString(0));
				//Log.w(TAG, "firstCursor.1 = " + cursor.getString(1));
				contact_raw_id = cursor.getString(0);
				name = cursor.getString(1);
			}
			contactsContractList.add(contact_raw_id);
			contactsContractList.add(name);
			//Log.d(TAG, "Search name = " + name);
		} finally {
			cursor.close();
		}
		
		return contactsContractList;
	}
	
	public static ArrayList<String> getContactsUsingName(Context context, String name) { 
		String contact_raw_id = "";
		String number = "";
		ArrayList<String> contactsContractList = new ArrayList<String>();  
		String [] projection = {ContactsContract.Data.RAW_CONTACT_ID, ContactsContract.Data.DATA1};
		String where = ContactsContract.Data.HAS_PHONE_NUMBER + "=1" + " AND " + ContactsContract.Data.DISPLAY_NAME + " ='" + name + "'"
				+ " AND " + ContactsContract.CommonDataKinds.Phone.TYPE + " ='" + PMCType.BNS_PMC_CONTACT_PTT_TYPE + "'";
		Cursor cursor = context.getContentResolver().query( ContactsContract.Data.CONTENT_URI, projection, where , null, null );
		try {
			while(cursor.moveToNext() ) {
				//Log.w(TAG, "firstCursor.1 = " + cursor.getString(0));
				//Log.w(TAG, "firstCursor.1 = " + cursor.getString(1));
				contact_raw_id = cursor.getString(0);
				number = cursor.getString(1);
			}
			contactsContractList.add(contact_raw_id);
			contactsContractList.add(number);
			//Log.d(TAG, "Search number = " + number);
		} finally {
			cursor.close();
		}
		
		return contactsContractList;
	}
}
