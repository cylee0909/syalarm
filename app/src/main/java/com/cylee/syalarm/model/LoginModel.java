package com.cylee.syalarm.model;

import com.cylee.androidlib.GsonBuilderFactory;

/**
 * Created by cylee on 2017/3/11.
 */

public class LoginModel {
    public String name;
    public String passwd;
    public static String create(String name, String passwd) {
        LoginModel model = new LoginModel();
        model.name = name;
        model.passwd = passwd;
        return GsonBuilderFactory.createBuilder().toJson(model);
    }
}
