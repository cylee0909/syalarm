package com.cylee.syalarm.model;

import com.cylee.androidlib.GsonBuilderFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cylee on 2017/3/11.
 */

public class DeviceModel {
    public List<DeviceItem> devices = new ArrayList<>();
    public static class DeviceItem {
        public String name;
        public int state;
        public int did;
        /**
         * 操作的状态
         */
        public int astate;
    }

    public static DeviceModel fromJson(String json) {
        return GsonBuilderFactory.createBuilder().fromJson(json, DeviceModel.class);
    }

}
