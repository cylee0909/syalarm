package com.cylee.syalarm

import android.os.Bundle
import android.text.TextUtils
import com.cylee.androidlib.thread.Worker
import com.cylee.androidlib.util.Log
import com.cylee.androidlib.util.PreferenceUtils
import com.cylee.androidlib.util.TaskUtils
import com.umeng.message.PushAgent
import com.umeng.message.UTrack

/**
 * Created by cylee on 16/9/20.
 */
class SplashActivity : BasePushActivity() {
    var startWork = object: Worker() {
        override fun work() {
            if (TextUtils.isEmpty(PreferenceUtils.getString(AlarmPreference.LOGIN_TOKEN))) {
                startActivity(LoginActivity.createIntent(this@SplashActivity))
            } else{
                startActivity(MainActivity.createIntent(this@SplashActivity))
            }
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TaskUtils.postOnMain(startWork, 2000)
//        if (BuildConfig.DEBUG) {
//            PushAgent.getInstance(this@SplashActivity).addAlias("cylee", "SELF_ALIAS", object : UTrack.ICallBack {
//                override fun onMessage(isSuccess: Boolean, message: String) {
//                    Log.d("cylee", "add "+message+" "+isSuccess)
//                }
//            })
//        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        TaskUtils.removePostedWork(startWork)
    }
}