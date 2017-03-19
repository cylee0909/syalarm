package com.cylee.syalarm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import cn.csnbgsh.herbarium.bind
import com.cylee.androidlib.base.BaseActivity
import com.cylee.androidlib.net.Net
import com.cylee.androidlib.net.NetError
import com.cylee.androidlib.thread.Worker
import com.cylee.androidlib.util.PreferenceUtils
import com.cylee.androidlib.util.Rc4Util
import com.cylee.androidlib.util.TaskUtils
import com.cylee.lib.widget.dialog.DialogUtil
import com.cylee.syalarm.model.ExecModel
import com.cylee.syalarm.model.LoginModel

class LoginActivity : BasePushActivity() {
    var nameEdit : EditText? = null
    var passwdEdit : EditText? = null
    var loginBn : TextView? = null

    companion object {
        fun createIntent(context : Context): Intent {
            return Intent(context, LoginActivity::class.java);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        nameEdit = bind(R.id.al_name)
        passwdEdit = bind(R.id.al_passwd)
        loginBn = bind(R.id.al_confirm)
        loginBn?.setOnClickListener {
            dialogUtil.showWaitingDialog(this@LoginActivity, "操作中...")
            var name = nameEdit?.text.toString()
            var passwd = passwdEdit?.text.toString()
            passwd = Rc4Util.encry(passwd)
            Net.post(this@LoginActivity, ExecModel.buidInput("login", LoginModel.create(name, passwd)), object : Net.SuccessListener<ExecModel>() {
                override fun onResponse(response: ExecModel?) {
                    dialogUtil.dismissWaitingDialog()
                    if (response != null) {
                        PreferenceUtils.setString(AlarmPreference.LOGIN_TOKEN, response.result)
                        TaskUtils.postOnMain(object : Worker() {
                            override fun work() {
                                startActivity(MainActivity.createIntent(this@LoginActivity))
                                finish()
                            }
                        }, 300)
                    } else{
                        DialogUtil.showToast("登陆失败")
                    }
                }
            }, object : Net.ErrorListener() {
                override fun onErrorResponse(e: NetError?) {
                    dialogUtil.dismissWaitingDialog()
                    DialogUtil.showToast("登陆失败")
                }
            })
        }
    }
}
