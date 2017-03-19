package com.cylee.syalarm

import android.app.Notification
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Environment
import android.widget.RemoteViews
import com.cylee.androidlib.base.BaseApplication
import com.cylee.androidlib.net.Config
import com.cylee.androidlib.util.Log
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.MsgConstant
import com.umeng.message.PushAgent
import com.umeng.message.UmengMessageHandler
import com.umeng.message.entity.UMessage
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
            override fun onSuccess(p0: String?) {
            }
            override fun onFailure(p0: String?, p1: String?) {
            }
        })
        val messageHandler = object : UmengMessageHandler() {
            override fun getNotification(context: Context, msg: UMessage): Notification {
                when (msg.builder_id) {
                    1 -> {
                        val builder = Notification.Builder(context)
                        val myNotificationView = RemoteViews(context.getPackageName(),
                                R.layout.notification_view)
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title)
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text)
                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon,
                                getLargeIcon(context, msg))
                        myNotificationView.setImageViewResource(R.id.notification_small_icon,
                                getSmallIconId(context, msg))
                        builder.setContent(myNotificationView).setSmallIcon(getSmallIconId(context, msg)).setTicker(msg.ticker).setAutoCancel(true)

                        return builder.getNotification()
                    }
                    else ->
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg)
                }
            }
        }
        mPushAgent.messageHandler = messageHandler
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