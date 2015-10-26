package com.bns.pmc.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;

public class CommonUtil {
    public static class JSonItem {
        public String m_strSender = null;
        public String m_strReceiver = null;
        public String m_strText = null;
        public String m_strURL = null;
        public String m_strDataName = null;
        public String m_strDataFormat = null;
        public byte[] m_strDataByteArr;

        //added by nadir
        public String fileName = null;
        public String fileFormat = null;
        public String issueId = null;
        //end

        @Override
        public String toString() {
            return "JSonItem{" +
                    "m_strSender='" + m_strSender + '\'' +
                    ", m_strReceiver='" + m_strReceiver + '\'' +
                    ", m_strText='" + m_strText + '\'' +
                    ", m_strURL='" + m_strURL + '\'' +
                    ", m_strDataName='" + m_strDataName + '\'' +
                    ", m_strDataFormat='" + m_strDataFormat + '\'' +
                    ", m_strDataByteArr=" + Arrays.toString(m_strDataByteArr) +
                    ", fileName='" + fileName + '\'' +
                    ", fileFormat='" + fileFormat + '\'' +
                    ", issueId='" + issueId + '\'' +
                    '}';
        }
    }

    /**
     * 최상단 Process 확인.
     *
     * @param context
     * @param packageName
     * @return boolean
     */
    public static boolean checkTopRunningProcess(Context context, String packageName) {
        boolean bChecked = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> info;
        info = activityManager.getRunningTasks(1);
        for (Iterator<RunningTaskInfo> iterator = info.iterator(); iterator.hasNext(); ) {
            RunningTaskInfo runningTaskInfo = (RunningTaskInfo) iterator.next();
            if (runningTaskInfo.topActivity.getPackageName().equals(packageName)) {
                bChecked = true;
                Log.d(PMCType.TAG, "com.bns.pushmsg is running");
            }
        }
        return bChecked;
    }

    /**
     * 최 상단 Class 확인.
     *
     * @param context
     * @param className
     * @return boolean
     */
    public static boolean checkTopClass(Context context, String className) {
        boolean bChecked = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> info;
        info = activityManager.getRunningTasks(1);
        for (Iterator<RunningTaskInfo> iterator = info.iterator(); iterator.hasNext(); ) {
            RunningTaskInfo runningTaskInfo = (RunningTaskInfo) iterator.next();
            Log.i(PMCType.TAG, "top " + runningTaskInfo.topActivity.getClassName() + " " + className);
            if (runningTaskInfo.topActivity.getClassName().equals(className)) {
                Log.i(PMCType.TAG, "Activity true");
                bChecked = true;
            }
        }
        return bChecked;
    }

    /**
     * 현재 돌아가는 서비스 확인
     *
     * @param context
     * @param className
     * @return boolean
     */
    public static boolean checkServiceRunning(Context context, String className) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (className.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ptt Number 인지 확인.
     *
     * @param pttNum
     * @return boolean
     */
    public static boolean checkPttCallNumFormat(String pttNum) {
        if (pttNum == null || pttNum.isEmpty()) {
            return false;
        }

        String[] arr = pttNum.split("\\*");

        //Log.d(TAG, "[checkCallNumFormat] arr.length : " + arr.length);

        if (arr.length < 4) {
            for (int i = 0; i < arr.length; i++) {
                if (!TextUtils.isDigitsOnly(arr[i].toString())) {
                    return false;
                }
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * Ptt Group Number 인지 확인.
     *
     * @param pttGroup
     * @return boolean
     */
    public static boolean checkPttGroupNumFormat(String pttGroup) {
        //Log.d(TAG, "[checkGroupNumFormat] number : " + number);
        if (pttGroup == null) {
            return false;
        }

        boolean isGroup = pttGroup.startsWith("#");
        //Log.d(TAG, "[checkGroupNumFormat] isGroup : " + isGroup);
        if (isGroup) {
            try {
                String realNum = pttGroup.substring(1);
                //Log.d(TAG, "[checkGroupNumFormat] realNum : " + realNum);
                if (TextUtils.isDigitsOnly(realNum)) {
                    int num = Integer.valueOf(realNum);
                    if (num > 0 && num <= 255) {
                        return true;
                    }
                }
            } catch (Exception ex) {

            }
        }
        return false;
    }

    public static boolean checkNormalNumFormat(String number) {
        if (number.startsWith("#") == false && number.length() > 5) {
            if (number.startsWith("+")) {
                String strTemp = number.substring(1);
                if (TextUtils.isDigitsOnly(strTemp)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (TextUtils.isDigitsOnly(number))
                    return true;
                else
                    return false;
            }
        }
        return false;
    }

    /*
     * return value
     * get(0) = name
     * get(1) = number
     */
    public static ArrayList<String> searchContactInfo(Context context, String number, String myUFMI) {
        ArrayList<String> retInfo = new ArrayList<String>();
        String myNum = myUFMI;
        String whereNumber = "";
        String[] arr = number.split("\\*");
        boolean isGroup = number.startsWith("#");

        //Log.d(PMCType.TAG, "[searchContactInfo] arr.length : " + arr.length + ", number : " + number);

        if (myNum.isEmpty() || isGroup) {
            whereNumber = ContactsContract.Data.DATA1 + " ='" + number + "'";
        } else {
            String[] arrMy = myNum.split("\\*");

//            if (PTTGlobals.getInstance().getPttMode() == A1Type.PTT_MODE_3PAPI) {
            switch (arr.length) {
                case 1: {    //No Urban, No Fleet
                    whereNumber = ContactsContract.Data.DATA1 + " ='" + number + "'"
                            + " OR " + ContactsContract.Data.DATA1 + " ='" + arrMy[1] + "*" + number + "'"
                            + " OR " + ContactsContract.Data.DATA1 + " ='" + arrMy[0] + "*" + arrMy[1] + "*" + number + "'";
                    break;
                }
                case 2: {    //No Urban
                    int myFleet = Integer.valueOf(arrMy[1]);
                    int diffFleet = Integer.valueOf(arr[0]);
                    if (diffFleet == myFleet) {
                        whereNumber = ContactsContract.Data.DATA1 + " ='" + arr[1] + "'"
                                + " OR " + ContactsContract.Data.DATA1 + " ='" + arr[0] + "*" + arr[1] + "'"
                                + " OR " + ContactsContract.Data.DATA1 + " ='" + arrMy[0] + "*" + arr[0] + "*" + arr[1] + "'";
                    } else {
                        whereNumber = ContactsContract.Data.DATA1 + " ='" + arr[0] + "*" + arr[1] + "'"
                                + " OR " + ContactsContract.Data.DATA1 + " ='" + arrMy[0] + "*" + arr[0] + "*" + arr[1] + "'";
                    }
                    break;
                }
                case 3: {
                    int myFleet = Integer.valueOf(arrMy[1]);
                    int diffFleet = Integer.valueOf(arr[1]);
                    if (diffFleet == myFleet) {
                        whereNumber = ContactsContract.Data.DATA1 + " ='" + arr[2] + "'"
                                + " OR " + ContactsContract.Data.DATA1 + " ='" + arr[1] + "*" + arr[2] + "'"
                                + " OR " + ContactsContract.Data.DATA1 + " ='" + arr[0] + "*" + arr[1] + "*" + arr[2] + "'";
                    } else {
                        whereNumber = ContactsContract.Data.DATA1 + " ='" + arr[1] + "*" + arr[2] + "'"
                                + " OR " + ContactsContract.Data.DATA1 + " ='" + arr[0] + "*" + arr[1] + "*" + arr[2] + "'";
                    }
                    break;
                }
            }
//            } else {
//                whereNumber = ContactsContract.Data.DATA1 + " ='" + number + "'";
//            }
        }

        String[] projection = {ContactsContract.Data.DISPLAY_NAME, ContactsContract.Data.DATA1};
        String where = ContactsContract.Data.HAS_PHONE_NUMBER + "=1" + " AND " + ContactsContract.CommonDataKinds.Phone.TYPE + " ='" +
                PMCType.BNS_PMC_CONTACT_PTT_TYPE + "'"
                + " AND (" + whereNumber + ")";
        //Log.d(PMCType.TAG, "[searchContactInfo] where : " + where);

        Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, projection, where, null, null);

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    retInfo.add(cursor.getString(0));
                    retInfo.add(cursor.getString(1));
                }
            } finally {
                cursor.close();
            }
        }

        /*if (retInfo != null && retInfo.size() > 0) {
            Log.d(PMCType.TAG, "[searchContactInfo] retInfo(0) = " + retInfo.get(0) + ", retInfo(1) = " + retInfo.get(1));
        } else {
            Log.d(PMCType.TAG, "[searchContactInfo] Search fail");
        }*/

        return retInfo;
    }

    /**
     * Contact 으로부터 PttNumber또는 GroupNumber를 검색해서 이름을 찾음.
     *
     * @param context
     * @param ptt
     * @return String
     */
    public static String searchName_FromContact_ByPtt(Context context, String ptt, String myUFMI) {
        String strRetVal = null;
        if (CommonUtil.checkPttCallNumFormat(ptt) == true ||
                CommonUtil.checkPttGroupNumFormat(ptt) == true) {
            try {
                ArrayList<String> list = //DataUtil.getContactsUsingNumber(context, ptt);
                        searchContactInfo(context, ptt, myUFMI);
                if (list != null && list.size() > 0) {
                    //strRetVal= list.get(1);
                    strRetVal = list.get(0);
                } else {
                    strRetVal = null;
                }
            } catch (Exception e) {
                Log.e(PMCType.TAG, e.getMessage());
            }
        }
        return strRetVal;
    }

    /**
     * 등록된 그룹 리스트인지 확인.
     *
     * @param Group
     * @param GroupList
     * @return boolean
     */
    public static boolean isJoinGroup(String Group, String GroupList) {
        String[] list = conv_GroupListToGroup(GroupList);
        boolean isGroup = Group.startsWith("#");
        String str = null;
        if (isGroup)
            str = Group.substring(1);
        else
            str = Group;

        for (int i = 0; i < list.length; i++) {
            String strItem = list[i];
            if (strItem.compareToIgnoreCase(str) == 0)
                return true;
        }

        return false;
    }

    /**
     * UrbanNumber 찾아서 Return.
     *
     * @param ufmi
     * @return String
     */
    public static String getUrbanNumber(String ufmi) {
        // UrbanNumber * FleetNumber * UserNumber
        String strRetVal = null;
        if (ufmi == null)
            return strRetVal;

        String[] arr = ufmi.split("\\*");

        if (arr.length == 3) {
            // ufmi가 UrbanNumber * FleetNumber * UserNumber형식일 경우.
            strRetVal = arr[0];
        } else {
            // 그 외는 검증하지 않음.
            strRetVal = null;
        }
        return strRetVal;
    }

    /**
     * fleetNumber를 찾아서 Return.
     *
     * @param ufmi
     * @return String
     */
    public static String getFleetNumber(String ufmi) {
        // UrbanNumber * FleetNumber * UserNumber
        String strRetVal = null;
        if (ufmi == null)
            return strRetVal;

        String[] arr = ufmi.split("\\*");

        if (arr.length == 3) {
            // ufmi가 UrbanNumber * FleetNumber * UserNumber형식일 경우.
            strRetVal = arr[1];
        } else if (arr.length == 2) {
            // ufmi가 FleetNumber * UserNumber형식일 경우.
            strRetVal = arr[0];
        } else {
            // 그 외는 검증하지 않음.
            strRetVal = null;
        }
        return strRetVal;
    }

    /**
     * UserNumber만 찾아서 Return
     *
     * @param ufmi
     * @return String
     */
    public static String getUserNumber(String ufmi) {
        // UrbanNumber * FleetNumber * UserNumber
        String strRetVal = null;
        if (ufmi == null)
            return strRetVal;

        String[] arr = ufmi.split("\\*");

        if (arr.length == 3) {
            // ufmi가 UrbanNumber * FleetNumber * UserNumber형식일 경우.
            strRetVal = arr[2];
        } else if (arr.length == 2) {
            // ufmi가 FleetNumber * UserNumber형식일 경우.
            strRetVal = arr[1];
        } else {
            // 그 외는 검증하지 않음.
            strRetVal = ufmi;
        }
        return strRetVal;
    }

    /**
     * 나의 FleetNumber와 같은지 비교.
     *
     * @param myFleetNum
     * @param ufmi
     * @return boolean
     */
    public static boolean comp_FleetNumber(String myFleetNum, String ufmi) {
        boolean bRet = false;
        if (myFleetNum == null || ufmi == null)
            return bRet;

        String[] arr = ufmi.split("\\*");
        if (arr.length == 3) {
            String fleet = arr[1];
            if (TextUtils.equals(myFleetNum, fleet))
                bRet = true;
        }

        return bRet;
    }

    /**
     * compare_GroupList
     *
     * @param groupList
     * @param newGroupList
     * @return boolean
     */
    public static boolean comp_GroupList(String groupList, String newGroupList) {
        if (groupList.compareToIgnoreCase(newGroupList) == 0)
            return true;

        ArrayList<String> list = new ArrayList<String>();
        {
            String[] arr = CommonUtil.conv_GroupListToGroup(groupList);
            if (arr.length == 0)
                return false;

            for (int i = 0; i < arr.length; i++) {
                list.add(arr[i]);
            }
        }

        {
            String[] arr = CommonUtil.conv_GroupListToGroup(newGroupList);
            if (arr.length == 0)
                return false;

            for (int i = 0; i < arr.length; i++) {
                if (list.contains(arr[i]) == false)
                    return false;
            }
        }

        return true;
    }

    /**
     * font size "px".
     *
     * @param fontsize
     * @param percent
     * @return float
     */
    public static float conv_Size(float fontsize, int percent) {
        float val = (float) (fontsize * (percent / 100.0));

        return val;
    }

    /**
     * long형식의 시간을 현재 System 시간과 비교하여 알맞게 표현.
     *
     * @param context
     * @param time
     * @return String
     */
    public static String conv_DateTime(Context context, long time) {
        String str = null;
        SimpleDateFormat sdf = null;

        Date date = new Date(time);
        Calendar createCal = Calendar.getInstance();
        Calendar currCal = Calendar.getInstance();

        createCal.setTime(date);

        if (createCal.get(Calendar.YEAR) != currCal.get(Calendar.YEAR)) {
            sdf = new SimpleDateFormat("yy.MM.dd");
            str = sdf.format(date);
            return str;
        }

        if (createCal.get(Calendar.MONTH) != currCal.get(Calendar.MONTH)) {
            sdf = new SimpleDateFormat("MM.dd");
            str = sdf.format(date);
            return str;
        }

        if (createCal.get(Calendar.DATE) != currCal.get(Calendar.DATE)) {
            sdf = new SimpleDateFormat("MM.dd");
            str = sdf.format(date);
            return str;
        }

        str = DateFormat.getTimeFormat(context).format(date);
        return str;
    }

    /**
     * Build Model을 MMS보내는 형식으로 변환.
     *
     * @param modelName
     * @param ver
     * @return String
     */
    public static String conv_BuildModelToMMS(String modelName, int ver) {
        Log.d(PMCType.TAG, "BuildModelToMMS " + modelName + " ver: " + ver);

        if (modelName.isEmpty() || ver == PMCType.PMC_PTT_MODE_NONE)
            return null;

        String strVer = null;
        if (ver == PMCType.PMC_PTT_MODE_3PAPI) {
            strVer = "P1";
        } else {
            strVer = "P2";
        }

        String convert = "mms";
        convert += "/";
        convert += strVer;
        convert += "/";
        convert += modelName;

        Log.d(PMCType.TAG, "convert BuildModelToMMS " + convert);

        return convert;
    }

    /**
     * Phone Number를 MMS보내는 형식으로 변환.
     *
     * @param phoneNum
     * @param ver
     * @return String
     */
    public static String conv_PhoneNumberToMMS(String phoneNum) {
        Log.d(PMCType.TAG, "PhoneNumberToMMS " + phoneNum);

        //testCode
        if (phoneNum == null || phoneNum.isEmpty())
            return null;
        //testCodeEnd

        String convert = "mms";
        String[] myArr = phoneNum.split("\\+");
        convert += "/";
        convert += myArr[1];

        Log.d(PMCType.TAG, "convert PhoneNumberToMMS " + convert);

        return convert;
    }

    /**
     * Urban을 MMS보내는 형식으로 변환.
     *
     * @param ufmi
     * @param ver
     * @return String
     */
    public static String conv_UrbanToMMS(String ufmi, int ver) {
        Log.i(PMCType.TAG, "UrbanToMMS Number= " + ufmi + " Ver= " + ver);

        if (ufmi.isEmpty() || ver == PMCType.PMC_PTT_MODE_NONE)
            return null;

        String convert = "mms";
        String[] myArr = ufmi.split("\\*");

        if (ver == PMCType.PMC_PTT_MODE_3PAPI) {
            convert += "/P1/";
            convert += myArr[0];
        } else {
            convert += "/P2/";
            convert += myArr[0];
        }

        Log.d(PMCType.TAG, "convert UrbanToMMS " + convert);

        return convert;
    }

    /**
     * UFMI를 MMS보내는 형식으로 변환.
     *
     * @param ufmi
     * @return String
     */
    public static String conv_UFMIToMMS(String ufmi, int ver) {
        Log.i(PMCType.TAG, "UFMIToMMS Number= " + ufmi + " Ver= " + ver);

        if (ufmi.isEmpty() || ver == PMCType.PMC_PTT_MODE_NONE)
            return null;

        String convert = "mms";
        String[] myArr = ufmi.split("\\*");

        if (ver == PMCType.PMC_PTT_MODE_3PAPI) {
            convert += "/P1/";
            convert += myArr[0];
            convert += "/";
            convert += myArr[1];
            convert += "/";
            convert += "p";
            convert += myArr[2];
        } else {
            convert += "/P2/";
            convert += myArr[0];
            convert += "/";
            convert += myArr[1];
            convert += "/";
            convert += "p";
            convert += myArr[2];
        }

        Log.d(PMCType.TAG, "convert UFMIToMMS " + convert);

        return convert;
    }

    /**
     * Group을 MMS보내는 형식으로 변환.
     *
     * @param group
     * @param ufmi
     * @param bunch
     * @param ver
     * @return String
     */
    public static String conv_GroupToMMS(String group, String ufmi, String bunch, int ver) {
        Log.d(PMCType.TAG, "GroupToMMS Number: " + group + " Ufmi: " + ufmi + " ver: " + ver);

        if (group.isEmpty() || ufmi.isEmpty() || ver == PMCType.PMC_PTT_MODE_NONE)
            return null;

        // PTalk 2.0일때 Bunch ID 필수.
        if (ver == PMCType.PMC_PTT_MODE_PTALK) {
            if (bunch.compareToIgnoreCase("noBunchID") == 0)
                return null;
        }

        String convert = "mms";
        String[] myArr = ufmi.split("\\*");

        if (ver == PMCType.PMC_PTT_MODE_3PAPI) {
            convert += "/P1/";
            convert += myArr[0];
            convert += "/";
            convert += myArr[1];
            convert += "/";
            convert += "g";
            convert += group;
        } else {
            convert += "/P2/";
            convert += myArr[0];
            convert += "/";
            convert += "b";
            convert += bunch;
            convert += "/";
            convert += "g";
            convert += group;
        }

        Log.d(PMCType.TAG, "convert GroupToMMS " + convert);

        return convert;
    }

    /**
     * Urban*Fleet*Member -> Fleet*Member 변환.
     *
     * @param ufmi
     * @return String
     */
    public static String conv_UrbanFleetMemberNumToFleetMemberNum(String ufmi) {
        String convert = "";

        if (ufmi != null) {
            String[] myArr = ufmi.split("\\*");

            if (myArr.length == 3) {
                convert = myArr[1] + "*" + myArr[2];
            }
        }

        return convert;
    }

    /**
     * Group List String -> Group 개별로 split.
     *
     * @param group
     * @return String[]
     */
    public static String[] conv_GroupListToGroup(String group) {
        if (group.isEmpty())
            return null;

        String[] myArr = group.split("\\|");

        return myArr;
    }

    /**
     * fleetNumber와 user가 입력한 String값을 fleet*user 번호 형식으로 바꾼다.
     * String값이 number가 아니면 입력된 String 값으로 Return.
     *
     * @param myfleetNum
     * @param num
     * @return String
     */
    public static String addToFleetNumber(String myfleetNum, String num) {
        String strResult = null;
        // String 값이 Number 형식이고 null값이 아니면 Convert.
        if (TextUtils.isDigitsOnly(num) == true && TextUtils.isEmpty(num) == false) {
            String[] arr = num.split("\\*");
            if (arr.length == 1) {
                // Number에 *이 포함되어 있지 않으면 UserNumber로 간주하고,
                // 나의 FleetNumber를 붙여준다.
                strResult = myfleetNum + "*" + num;
            } else {
                // 조건에 포함되지 않으므로 Number를 그대로 Return 한다.
                strResult = num;
            }
        } else {
            strResult = num;
        }

        return strResult;
    }

    public static String addToFleetNumberForGroup(String myfleetNum, String num) {
        String strResult = null;
        // String 값이 Number 형식이고 null값이 아니면 Convert.
        if (TextUtils.isDigitsOnly(num) == true && TextUtils.isEmpty(num) == false) {

            boolean isGroup = num.startsWith("#");
            //Log.d(TAG, "[checkGroupNumFormat] isGroup : " + isGroup);
            if (isGroup) {
                String realNum = num.substring(1);
                strResult = myfleetNum + "*" + realNum;
            }

            String[] arr = num.split("\\#");
            if (arr.length == 1) {
                // Number에 *이 포함되어 있지 않으면 UserNumber로 간주하고,
                // 나의 FleetNumber를 붙여준다.
                strResult = myfleetNum + "*" + num;
            } else {
                // 조건에 포함되지 않으므로 Number를 그대로 Return 한다.
                strResult = num;
            }
        } else {
            strResult = num;
        }

        return strResult;
    }

    /**
     * String Number 값을 UrbanNumber를 붙여줌.
     *
     * @param num
     * @return String
     */
    public static String addToUrbanNumber(String myUrbanNum, String num) {
        String strResult = null;
        if (checkPttCallNumFormat(num) == true && TextUtils.isEmpty(num) == false) {
            String[] arr = num.split("\\*");
            if (arr.length == 2) {
                // Fleet*User 로 간주하고 Urban Number를 붙여준다.
                strResult = myUrbanNum + "*" + num;
            } else {
                // 조건에 포함되지 않으므로 Number를 그대로 Return 한다.
                strResult = num;
            }
        } else {
            strResult = num;
        }

        return strResult;
    }

    /**
     * String을 Base64형식으로 Encode
     *
     * @param content
     * @return String
     */
    public static String encodeStringToBase64(String content) {
        return Base64.encodeToString(content.getBytes(), 0);
    }

    /**
     * Base64 String을 Decode
     *
     * @param content
     * @return String
     */
    public static String decodeBase64ToString(String content) {
        return new String(Base64.decode(content, 0));
    }

    /**
     * Call Ptt
     *
     * @param context
     * @param number
     */
    public static void callPtt(Context context, String number) {
        Log.i(PMCType.TAG, "Ptt Call Number=" + number);

        Intent i = new Intent();
        i.setAction(PMCType.BNS_PMC_PTT_OUTCOMING_ACTION);
        i.putExtra(PMCType.BNS_PMC_PTT_QUICK_NUMBER_EXTRA_VALUE, number);
        context.sendBroadcast(i);
    }

    /**
     * Call Normal
     *
     * @param context
     * @param number
     */
    public static void callNormal(Context context, String number) {
        Log.i(PMCType.TAG, "Call Number=" + number);

        Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        context.startActivity(i);
    }
}
