package com.cylee.syalarm;

import com.cylee.androidlib.util.PreferenceUtils;

/**
 * Created by cylee on 2017/3/11.
 */

public enum AlarmPreference implements PreferenceUtils.DefaultValueInterface {
    LOGIN_TOKEN("");

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }

    private Object defaultValue;

    AlarmPreference(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String getNameSpace() {
        return "AlarmPreference";
    }
}
