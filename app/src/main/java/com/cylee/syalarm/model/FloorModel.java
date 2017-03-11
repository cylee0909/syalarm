package com.cylee.syalarm.model;

import com.cylee.androidlib.GsonBuilderFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cylee on 2017/3/11.
 */

public class FloorModel {
    public List<FloorItem> floors = new ArrayList<>();
    public static class FloorItem {
        public String name;
        public int state;
        public int fid;
    }

    public static FloorModel fromJson(String json) {
        return GsonBuilderFactory.createBuilder().fromJson(json, FloorModel.class);
    }

}
