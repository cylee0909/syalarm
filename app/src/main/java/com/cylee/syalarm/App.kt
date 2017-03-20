package com.cylee.syalarm

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Environment
import com.cylee.androidlib.base.BaseApplication
import com.cylee.androidlib.net.Config
import com.cylee.androidlib.util.Log
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.MsgConstant
import com.umeng.message.PushAgent
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by cylee on 2017/3/11.
 */
class App : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        if (false && BuildConfig.DEBUG) {
            Config.setHost("http://192.168.31.103:8990/")
        } else {
            Config.setHost("http://123.125.127.161:8990/")
        }
        Log.setLogLevel(if (BuildConfig.DEBUG) Log.OFF else Log.OFF)
        redirectLog()
        registerPush()
    }

    fun registerPush() {
        val mPushAgent = PushAgent.getInstance(this)
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(object : IUmengRegisterCallback {
            override fun onSuccess(token: String?) {
                Log.d("cyleetoken", token)
            }
            override fun onFailure(p0: String?, p1: String?) {
            }
        })
        mPushAgent.setDebugMode(BuildConfig.DEBUG)
        mPushAgent.setNoDisturbMode(0, 0, 0, 0)
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER) //声音
        mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SERVER)//呼吸灯
        mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SERVER)//振动
    }

    //测试包或者非release包将日志输出到文件中，方便查问题
    fun redirectLog() {
        if (BuildConfig.DEBUG) {
            val defHandler = Thread.getDefaultUncaughtExceptionHandler()
            Thread.setDefaultUncaughtExceptionHandler { thread, ex ->
                var osw: OutputStreamWriter? = null
                try {
                    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd/HH:mm:ss", Locale.CHINA)
                    val log = File(Environment.getExternalStorageDirectory(), "scan_crash.log")
                    if (!log.exists()) {
                        log.createNewFile()
                    }

                    osw = OutputStreamWriter(FileOutputStream(log, false))
                    var baos = ByteArrayOutputStream()
                    val pw = PrintWriter(baos)
                    pw.println("\n============================================================\n")
                    pw.println(simpleDateFormat.format(Date()))
                    ex.printStackTrace(pw)
                    pw.flush()
                    var content = baos.toString("utf-8")

                    var clipboard = getSystemService(BaseApplication.CLIPBOARD_SERVICE) as ClipboardManager
                    var textCd = ClipData.newPlainText("crash_log",content)
                    clipboard.setPrimaryClip(textCd);

                    osw!!.write(content)
                    osw.flush()
                } catch (e: Exception) {
                } finally {
                    if (osw != null) {
                        try {
                            osw.close()
                        } catch (e: Exception) {
                        }

                    }
                }
                defHandler?.uncaughtException(thread, ex)
            }
        }
    }
}