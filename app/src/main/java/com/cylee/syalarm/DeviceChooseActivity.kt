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
import com.cylee.syalarm.model.DeviceModel
import com.cylee.syalarm.model.ExecModel
import com.cylee.syalarm.model.FloorModel

class DeviceChooseActivity : BaseActivity() {
    var listView : ListView? = null
    var adapter : InnerAdapter? = null
    var dataList : MutableList<DeviceModel.DeviceItem> = mutableListOf()
    var cid = 0
    var fid = 0

    companion object {
        val INPUT_CID = "INPUT_CID"
        val INPUT_FID = "INPUT_FID"
        fun createIntent(context : Context, id : Int, fid : Int): Intent {
            var intent = Intent(context, FloorChooseActivity::class.java);
            intent.putExtra(INPUT_CID, id)
            intent.putExtra(INPUT_FID, fid)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_choose)
        listView = bind(R.id.adc_list)
        adapter = InnerAdapter()
        listView?.adapter = adapter
        cid = intent.getIntExtra(INPUT_CID, 0)
        fid = intent.getIntExtra(INPUT_FID, 0)
        loadData(cid, fid)

        listView?.setOnItemLongClickListener { adapterView, view, i, l ->
            var device = dataList[i]
            if (device.state > 0 && device.state == 1) {
                dialogUtil.showDialog(this, "取消", "确定", object : DialogUtil.ButtonClickListener {
                    override fun OnLeftButtonClick() {
                    }

                    override fun OnRightButtonClick() {
                        Net.post(this@DeviceChooseActivity, ExecModel.buidInput("close", BaseReqModel.createReq("cid", cid.toString(), "fid", fid.toString(), "did", device.did.toString())),
                                object : Net.SuccessListener<ExecModel>() {
                                    override fun onResponse(response: ExecModel?) {

                                    }
                                }, null)
                    }
                }, "关闭当前设备？")
                return@setOnItemLongClickListener true
            }
            return@setOnItemLongClickListener false
        }
    }

    fun loadData(id: Int, fid : Int) {
        dialogUtil.showWaitingDialog(this, "加载中...")
        Net.post(this, ExecModel.buidInput("device", BaseReqModel.createReq("cid", id.toString(), "fid", fid.toString())), object : Net.SuccessListener<ExecModel>() {
            override fun onResponse(response: ExecModel?) {
                dialogUtil.dismissWaitingDialog()
                dataList.clear()
                if (response != null && response.result != null) {
                    var floorModel = DeviceModel.fromJson(response.result)
                    if (floorModel != null && floorModel.devices != null) {
                        for (floor in floorModel.devices) {
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
                oldView = View.inflate(this@DeviceChooseActivity, R.layout.adc_item, null)
                holder = Holder()
                holder.title = oldView.bind(R.id.adci_name)
                holder.state = oldView.bind(R.id.adci_state)
                oldView.setTag(holder)
            } else{
                oldView = convertView
                holder = oldView.getTag() as Holder
            }
            var data = getItem(position) as FloorModel.FloorItem
            holder.title?.text = data.name
            holder.state?.visibility = if (data.state == 0) View.VISIBLE else View.GONE
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
