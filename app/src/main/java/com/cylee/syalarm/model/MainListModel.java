package com.cylee.syalarm.model;

import com.cylee.androidlib.GsonBuilderFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cylee on 2017/3/11.
 */

public class MainListModel {
    public List<MainItem> mainStates = new ArrayList<>();
    public static class MainItem {
        public int cid;
        public int state;
    }

    public static MainListModel fromJson(String json) {
        return GsonBuilderFactory.createBuilder().fromJson(json, MainListModel.class);
    }
}
