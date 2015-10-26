package com.bns.pmc.util;

import android.text.TextUtils;

import com.bns.pmc.util.CommonUtil.JSonItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSonUtil {

    /**
     * Subscribe Result Success.
     *
     * @param jsonResult
     * @return boolean
     */
    public static boolean responeSubscribeResultSuccess(String jsonResult) {
        return responeResultSuccess(jsonResult);
    }

    /**
     * UnSubscribe Result Success.
     *
     * @param jsonResult
     * @return boolean
     */
    public static boolean responeUnSubscribeResultSuccess(String jsonResult) {
        return responeResultSuccess(jsonResult);
    }

    /**
     * Subscriptions Result Success.
     *
     * @param jsonResult
     * @return boolean
     */
    public static boolean responeSubscriptionsResultSuccess(String jsonResult) {
        return responeResultSuccess(jsonResult);
    }

    /**
     * Subscription Result Data.
     *
     * @param jsonResult
     * @return ArrayList<String>
     */
    public static ArrayList<String> responeSubscriptionsResultData(String jsonResult) {
        if (TextUtils.isEmpty(jsonResult)) {
            Log.d(PMCType.TAG, "Json is null");
            return null;
        }

        String strData = responeResultData(jsonResult);
        if (TextUtils.isEmpty(strData) == false) {

            try {
                ArrayList<String> list = new ArrayList<String>();
                JSONArray array = new JSONArray(strData);

                for (int i = 0; i < array.length(); i++)
                    list.add(array.getString(i));

                return list;
            } catch (JSONException e) {
                Log.e(PMCType.TAG, "Error= " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Log.d(PMCType.TAG, "Data is null");
            return null;
        }

        return null;
    }

    /**
     * responeExistPMA Result Success.
     *
     * @param jsonResult
     * @return
     */
    public static boolean responeExistPMAResultSuccess(String jsonResult) {
        return responeResultSuccess(jsonResult);
    }

    /**
     * ExistPMA Result DataValidation.
     *
     * @param jsonResult
     * @return boolean
     */
    public static boolean responeExistPMAResultDataValidation(String jsonResult) {
        if (TextUtils.isEmpty(jsonResult)) {
            Log.d(PMCType.TAG, "Json is null");
            return false;
        }

        String strData = responeResultData(jsonResult);
        if (TextUtils.isEmpty(strData) == false) {
            return responeDataValidation(strData);
        } else {
            Log.d(PMCType.TAG, "Data is null");
        }

        return false;
    }

    /**
     * SendMsg Result Success.
     *
     * @param jsonResult
     * @return boolean
     */
    public static boolean responeSendMessageResultSuccess(String jsonResult) {
        return responeResultSuccess(jsonResult);
    }

    /**
     * Ack Result Success.
     *
     * @param jsonResult
     * @return boolean
     */
    public static boolean responeAckResultSuccess(String jsonResult) {
        return responeResultSuccess(jsonResult);
    }

    /**
     * check Subscriptions
     *
     * @param resultData
     * @param subscribeNum
     * @return boolean
     */
    public static boolean isSubscriptions(ArrayList<String> resultData, String subscribeNum) {
        if (resultData == null) {
            Log.d(PMCType.TAG, "resultData is null");
            return false;
        }

        if (TextUtils.isEmpty(subscribeNum)) {
            Log.d(PMCType.TAG, "ufmi is null");
            return false;
        }

        for (int i = 0; i < resultData.size(); i++) {
            String strDataItem = resultData.get(i);
            if (subscribeNum.compareTo(strDataItem) == 0) {
                Log.d(PMCType.TAG, "PttNumber was already registered= " + subscribeNum);
                return true;
            }
        }

        return false;
    }

    /**
     * JSonItem을 JSonString으로 Encode
     *
     * @param jsonItem
     * @return String
     */
    public static String encodeDataToJSONString(JSonItem jsonItem) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(PMCType.BNS_PMC_MMS_SND, jsonItem.m_strSender);
            obj.put(PMCType.BNS_PMC_MMS_RCV, jsonItem.m_strReceiver);
            obj.put(PMCType.BNS_PMC_MMS_STR, jsonItem.m_strText);
            // DataName이 있는 경우 Data가 들어 있음
            if (jsonItem.m_strDataName != null) {
                //if (jsonItem.m_strDataFormat.compareToIgnoreCase("jpg") == 0 ||
                //        jsonItem.m_strDataFormat.compareToIgnoreCase("bmp") == 0 ||
                //        jsonItem.m_strDataFormat.compareToIgnoreCase("png") == 0) {
                JSONObject objData = new JSONObject();
                objData.put(PMCType.BNS_PMC_MMS_NAME, jsonItem.m_strDataName);
                objData.put(PMCType.BNS_PMC_MMS_FORMAT, jsonItem.m_strDataFormat);

                JSONArray objArr = new JSONArray();
                for (int i = 0; i < jsonItem.m_strDataByteArr.length; i++) {
                    objArr.put(jsonItem.m_strDataByteArr[i]);
                }

                objData.put(PMCType.BNS_PMC_MMS_DATA, objArr);
                obj.put(PMCType.BNS_PMC_MMS_IMG, objData);
                //  }
            }
        } catch (JSONException e) {
            Log.e(PMCType.TAG, "Error= " + e.getMessage());
            e.printStackTrace();
        }
        return obj.toString();
    }

    /**
     * JSonString을 JSonItem 형식으로 Decode
     *
     * @param json
     * @return JSonItem
     */
    public static JSonItem decodeJSONStringToData(String json) {
        JSonItem item = new JSonItem();
        try {
            JSONObject obj = new JSONObject(json);
            if (obj.isNull(PMCType.BNS_PMC_MMS_SND) == false)
                item.m_strSender = obj.getString(PMCType.BNS_PMC_MMS_SND);
            if (obj.isNull(PMCType.BNS_PMC_MMS_RCV) == false)
                item.m_strReceiver = obj.getString(PMCType.BNS_PMC_MMS_RCV);
            if (obj.isNull(PMCType.BNS_PMC_MMS_STR) == false)
                item.m_strText = obj.getString(PMCType.BNS_PMC_MMS_STR);
            if (obj.isNull(PMCType.BNS_PMC_MMS_URL) == false)
                item.m_strURL = obj.getString(PMCType.BNS_PMC_MMS_URL);

            if (obj.isNull("fileName") == false)
                item.fileName = obj.getString("fileName");
            if (obj.isNull("fileFormat") == false)
                item.fileFormat = obj.getString("fileFormat");
            if (obj.isNull("issueId") == false)
                item.issueId = obj.getString("issueId");

            if (obj.isNull(PMCType.BNS_PMC_MMS_IMG) == false) {
                String strImg = obj.getString(PMCType.BNS_PMC_MMS_IMG);
                if (strImg != null) {
                    JSONObject objImg = new JSONObject(strImg);
                    //Log.i(PMCType.TAG, "objImg=" + objImg);
                    if (objImg.isNull(PMCType.BNS_PMC_MMS_NAME) == false)
                        item.m_strDataName = objImg.getString(PMCType.BNS_PMC_MMS_NAME);
                    if (objImg.isNull(PMCType.BNS_PMC_MMS_FORMAT) == false)
                        item.m_strDataFormat = objImg.getString(PMCType.BNS_PMC_MMS_FORMAT);
                    if (objImg.isNull(PMCType.BNS_PMC_MMS_DATA) == false) {
                        String strData = objImg.getString(PMCType.BNS_PMC_MMS_DATA);

                        JSONArray array = new JSONArray(strData);
                        item.m_strDataByteArr = new byte[array.length()];
                        for (int i = 0; i < array.length(); i++)
                            item.m_strDataByteArr[i] = Byte.parseByte(array.getString(i));
                    }
                    Log.i(PMCType.TAG, "m_strDataName=" + item.m_strDataName);
                    Log.i(PMCType.TAG, "m_strDataFormat=" + item.m_strDataFormat);
                }
            }
        } catch (JSONException e) {
            Log.e(PMCType.TAG, "Error= " + e.getMessage());
            e.printStackTrace();
        }
        return item;
    }

    /**
     * Json형식 result.
     *
     * @param json
     * @return String
     */
    private static String responeResult(String json) {
        if (TextUtils.isEmpty(json)) {
            Log.d(PMCType.TAG, "Json is null");
            return null;
        }

        try {
            JSONObject obj = new JSONObject(json);
            if (obj.isNull(PMCType.BNS_PMC_PMA_RESULT_RESULT) == false) {

                String strResult = obj.getString(PMCType.BNS_PMC_PMA_RESULT_RESULT);
                if (TextUtils.isEmpty(strResult) == false) {
                    return strResult;
                } else {
                    Log.d(PMCType.TAG, "Result is null");
                    return null;
                }
            } else {
                Log.d(PMCType.TAG, "No Result Key");
                return null;
            }
        } catch (JSONException e) {
            Log.e(PMCType.TAG, "Error= " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Json 형식 result에 대한 Success값.
     *
     * @param json
     * @return boolean
     */
    private static boolean responeResultSuccess(String json) {
        if (TextUtils.isEmpty(json)) {
            Log.d(PMCType.TAG, "Json is null");
            return false;
        }

        String strResult = responeResult(json);
        if (TextUtils.isEmpty(strResult) == false) {
            try {
                JSONObject obj = new JSONObject(strResult);
                if (obj.isNull(PMCType.BNS_PMC_PMA_RESULT_SUCCESS) == false) {
                    String strSuccess = obj.getString(PMCType.BNS_PMC_PMA_RESULT_SUCCESS);

                    if (TextUtils.isEmpty(strSuccess) == false) {
                        return Boolean.parseBoolean(strSuccess);
                    } else {
                        Log.d(PMCType.TAG, "Success is null");
                        return false;
                    }
                } else {
                    Log.d(PMCType.TAG, "No Success Key");
                    return false;
                }
            } catch (JSONException e) {
                Log.e(PMCType.TAG, "Error= " + e.getMessage());
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * Json 형식 result에 대한 Data.
     *
     * @param json
     * @return String
     */
    private static String responeResultData(String json) {
        if (TextUtils.isEmpty(json)) {
            Log.d(PMCType.TAG, "Json is null");
            return null;
        }

        String strResult = responeResult(json);
        if (TextUtils.isEmpty(strResult) == false) {
            try {
                JSONObject obj = new JSONObject(strResult);
                if (obj.isNull(PMCType.BNS_PMC_PMA_RESULT_DATA) == false) {
                    String strData = obj.getString(PMCType.BNS_PMC_PMA_RESULT_DATA);

                    if (TextUtils.isEmpty(strData) == false) {
                        return strData;
                    } else {
                        Log.d(PMCType.TAG, "Data is null");
                        return null;
                    }
                } else {
                    Log.d(PMCType.TAG, "No Data Key");
                    return null;
                }
            } catch (JSONException e) {
                Log.e(PMCType.TAG, "Error= " + e.getMessage());
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * JSon 형식 DataValidation 값.
     *
     * @param jsonData
     * @return boolean
     */
    private static boolean responeDataValidation(String jsonData) {
        if (TextUtils.isEmpty(jsonData)) {
            Log.d(PMCType.TAG, "Json is null");
            return false;
        }

        try {
            JSONObject obj = new JSONObject(jsonData);
            if (obj.isNull(PMCType.BNS_PMC_PMA_RESULT_VALIDATION) == false) {
                return obj.getBoolean(PMCType.BNS_PMC_PMA_RESULT_VALIDATION);
            } else {
                Log.d(PMCType.TAG, "No Validation Key");
                return false;
            }
        } catch (JSONException e) {
            Log.e(PMCType.TAG, "Error= " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
