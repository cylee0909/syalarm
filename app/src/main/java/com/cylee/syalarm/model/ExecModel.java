package com.cylee.syalarm.model;

import com.android.volley.Request;
import com.cylee.androidlib.net.InputBase;
import com.cylee.androidlib.util.Rc4Util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cylee on 16/12/18.
 */

public class ExecModel {
    public String result;
    public int code;
    public static class Input extends InputBase {
        private String command;
        private String params;
        private Input(String command, String params){
            this.url = "/syalarm/exec";
            this.method = Request.Method.POST;
            this.aClass = ExecModel.class;
            this.command = command;
            this.params = params;
        }
        @Override
        public Map<String, Object> getParams() {
            Map<String, Object> params = new HashMap<>();
            params.put("command", command);
            long current = System.currentTimeMillis();
            params.put("t", current);
            params.put("s", Rc4Util.quickEncry(String.valueOf(current)));
            params.put("params", this.params);
            return params;
        }
    }

    public static InputBase buidInput(String command, String params) {
        return new Input(command, params);
    }
}