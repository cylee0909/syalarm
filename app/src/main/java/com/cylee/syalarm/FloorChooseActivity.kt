package com.cylee.syalarm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import cn.csnbgsh.herbarium.bind
import com.cylee.androidlib.base.BaseActivity
import com.cylee.androidlib.net.Net
import com.cylee.androidlib.net.NetError
import com.cylee.lib.widget.dialog.DialogUtil
import com.cylee.syalarm.model.BaseReqModel
import com.cylee.syalarm.model.ExecModel
import com.cylee.syalarm.model.FloorModel

class FloorChooseActivity : BasePushActivity() {
    var listView : ListView? = null
    var adapter : InnerAdapter? = null
    var dataList : MutableList<FloorModel.FloorItem> = mutableListOf()
    var cid = 0;

    companion object {
        val INPUT_CID = "INPUT_CID"
        fun createIntent(context : Context, id : Int): Intent {
            var intent = Intent(context, FloorChooseActivity::class.java);
            intent.putExtra(INPUT_CID, id)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_floor_choose)
        listView = bind(R.id.afc_list)
        adapter = InnerAdapter()
        listView?.adapter = adapter
        cid = intent.getIntExtra(INPUT_CID, 0)
        loadData(cid)
        listView?.setOnItemClickListener { adapterView, view, i, l ->
            var floorItem = dataList[i]
            startActivity(DeviceChooseActivity.createIntent(this, cid, floorItem.fid))
        }
    }

    fun loadData(id: Int) {
        dialogUtil.showWaitingDialog(this, "加载中...")
        Net.post(this, ExecModel.buidInput("floor", BaseReqModel.createReq("cid", id.toString())), object : Net.SuccessListener<ExecModel>() {
            override fun onResponse(response: ExecModel?) {
                dialogUtil.dismissWaitingDialog()
                dataList.clear()
                if (response != null && response.result != null) {
                    var floorModel = FloorModel.fromJson(response.result)
                    if (floorModel != null && floorModel.floors != null) {
                        for (floor in floorModel.floors) {
                            dataList.add(floor)
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
                oldView = View.inflate(this@FloorChooseActivity, R.layout.afc_item, null)
                holder = Holder()
                holder.title = oldView.bind(R.id.afci_name)
                holder.state = oldView.bind(R.id.afci_state)
                oldView.setTag(holder)
            } else{
                oldView = convertView
                holder = oldView.getTag() as Holder
            }
            var data = getItem(position) as FloorModel.FloorItem
            holder.title?.text = data.name
            holder.state?.visibility = if (data.state == 0) View.GONE else View.VISIBLE
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
        var state : ImageView? = null
    }
}
