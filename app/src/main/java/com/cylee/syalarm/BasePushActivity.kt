package com.cylee.syalarm

import android.os.Bundle
import com.cylee.androidlib.base.BaseActivity
import com.umeng.message.PushAgent

/**
 * Created by cylee on 17/3/19.
 */
open class BasePushActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PushAgent.getInstance(this).onAppStart();
    }
}