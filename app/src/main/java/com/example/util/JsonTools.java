package com.example.util;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonTools {
    /**
     * �õ�һ��json���͵��ַ�������
     * @param key
     * @param value
     * @return
     */
    public static String getJsonString(String key, Object value) {
        JSONObject jsonObject = new JSONObject();
        //put��element������JSONObject�����з��� key/value ��
        try {
            jsonObject.put(key, value);
            //jsonObject.element(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    
    /**
     * �õ�һ��json����
     * @param key
     * @param value
     * @return
     */
    public static JSONObject getJsonObject(String key, Object value) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    
}
