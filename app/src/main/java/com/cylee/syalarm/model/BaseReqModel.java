package com.cylee.syalarm.model;

import com.cylee.androidlib.util.PreferenceUtils;
import com.cylee.syalarm.AlarmPreference;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cylee on 2017/3/11.
 */

public class BaseReqModel {
    public static String createReq(String... params) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", PreferenceUtils.getString(AlarmPreference.LOGIN_TOKEN));
            if (params != null) {
                if ((params.length & 1) > 0) {
                    throw new RuntimeException("params length error!");
                }
                for (int i = 0; i < params.length; i+=2) {
                    String key = params[i];
                    String value = params[i + 1];
                    jsonObject.put(key, value);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
