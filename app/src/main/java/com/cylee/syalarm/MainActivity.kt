package com.cylee.syalarm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import cn.csnbgsh.herbarium.bind

class MainActivity : AppCompatActivity() {
    var listView : ListView? = null
    var dataList = arrayOf(EntryItem("烟雾传感器", R.drawable.smoke, 0),
            EntryItem("水流传感器", R.drawable.water, 0),
            EntryItem("可燃气传感器", R.drawable.gas, 0),
            EntryItem("电机传感器", R.drawable.electric, 0),
            EntryItem("温度传感器", R.drawable.temperature, 0)
    )
    var adapter : InnerAdapter? = null
    companion object {
        fun createIntent(context : Context): Intent {
            return Intent(context, MainActivity::class.java);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = bind(R.id.am_list)
        adapter = InnerAdapter()
        listView?.adapter = adapter
    }

    data class EntryItem(var title:String, var iconId : Int, var state: Int)

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
