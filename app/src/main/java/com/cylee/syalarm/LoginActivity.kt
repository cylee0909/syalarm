package com.cylee.syalarm

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import cn.csnbgsh.herbarium.bind

class LoginActivity : AppCompatActivity() {
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

        }
    }
}
