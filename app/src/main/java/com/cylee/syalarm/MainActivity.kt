package com.cylee.syalarm

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import cn.csnbgsh.herbarium.bind
import com.cylee.androidlib.net.Net
import com.cylee.androidlib.net.NetError
import com.cylee.androidlib.util.Log
import com.cylee.androidlib.util.PreferenceUtils
import com.cylee.lib.widget.dialog.DialogUtil
import com.cylee.syalarm.entity.EntryItem
import com.cylee.syalarm.model.BaseReqModel
import com.cylee.syalarm.model.ExecModel
import com.cylee.syalarm.model.MainListModel
import com.umeng.message.PushAgent
import com.umeng.message.UTrack

class MainActivity : BasePushActivity() {
    var listView : ListView? = null
    var dataList:MutableList<EntryItem> = mutableListOf()
    var adapter : InnerAdapter? = null
    companion object {
        fun createIntent(context : Context): Intent {
            return Intent(context, MainActivity::class.java);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerPushAlias()
        setContentView(R.layout.activity_main)
        listView = bind(R.id.am_list)
        adapter = InnerAdapter()
        listView?.adapter = adapter
        listView?.setOnItemClickListener { adapterView, view, i, l ->
            var entryItem = dataList[i]
            startActivity(FloorChooseActivity.createIntent(this, entryItem.id))
        }
        loadData()
    }

    fun registerPushAlias() {
        var token = PreferenceUtils.getString(AlarmPreference.LOGIN_TOKEN)
        if (!TextUtils.isEmpty(token)) {
            PushAgent.getInstance(this).addAlias(token, "SELF_ALIAS", object : UTrack.ICallBack {
                override fun onMessage(isSuccess: Boolean, message: String) {
                    Log.d("cyleeregisterpush", "success "+isSuccess+" "+message)
                }
            })
        }
    }

    fun loadData() {
        dialogUtil.showWaitingDialog(this, "加载中...")
        Net.post(this, ExecModel.buidInput("mainlist", BaseReqModel.createReq()), object : Net.SuccessListener<ExecModel>() {
            override fun onResponse(response: ExecModel?) {
                dataList.clear()
                dialogUtil.dismissWaitingDialog()
                if (response != null) {
                    var data = MainListModel.fromJson(response.result);
                    if (data != null && data.mainStates != null) {
                        for (a in data.mainStates) {
                            dataList.add(EntryItem.create(a.cid, a.state))
                        }
                    }
                }
                adapter?.notifyDataSetChanged()
            }
        }, object : Net.ErrorListener() {
            override fun onErrorResponse(e: NetError?) {
                dialogUtil.dismissWaitingDialog()
                DialogUtil.showToast("服务器错误，数据加载失败")
            }
        })
    }

    inner class InnerAdapter : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var holder : Holder
            var oldView : View
            if (convertView == null) {
                oldView = View.inflate(this@MainActivity, R.layout.am_item, null)
                holder = Holder()
                holder.icon = oldView.bind(R.id.ai_icon)
                holder.state = oldView.bind(R.id.ai_status)
                holder.title = oldView.bind(R.id.ai_title)
                oldView.setTag(holder)
            } else{
                oldView = convertView
                holder = oldView.getTag() as Holder
            }
            var data = getItem(position) as EntryItem
            holder.icon?.setImageResource(data.iconId)
            holder.title?.text = data.title
            holder.state?.text = if (data.state == 0) "正常" else "异常"
            holder.state?.setTextColor(if (data.state == 0)
                Color.parseColor("#aafea6") else Color.parseColor("#ff8989"))
            return oldView
        }

        override fun getItem(position: Int): Any {
            return dataList.get(position)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return dataList.count()
        }
    }

    class Holder {
        var title : TextView? = null
        var icon : ImageView? = null
        var state : TextView? = null
    }
}
