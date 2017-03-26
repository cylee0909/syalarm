package com.cylee.syalarm.model;

import com.cylee.androidlib.GsonBuilderFactory;

/**
 * Created by cylee on 17/3/26.
 */

public class TokenModel {
    public String token;

    public static TokenModel fromJson(String json) {
        return GsonBuilderFactory.createBuilder().fromJson(json, TokenModel.class);
    }
}
