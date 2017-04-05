package com.cylee.syalarm.entity

import com.cylee.syalarm.R

/**
 * Created by cylee on 2017/3/11.
 */
data class EntryItem(var id : Int, var title:String, var iconId : Int, var state: Int) {
    companion object {
        fun create(id: Int, state: Int): EntryItem {
            when(id) {
                0-> return EntryItem(id, "烟雾传感器",  R.drawable.smoke, state)
                1-> return EntryItem(id, "水流传感器",  R.drawable.water, state)
                2-> return EntryItem(id, "可燃气传感器",  R.drawable.gas, state)
                3-> return EntryItem(id, "电机传感器",  R.drawable.electric, state)
                4 -> return EntryItem(id, "温度传感器",  R.drawable.temperature, state)
                5 -> return EntryItem(id, "报警传感器", R.drawable.ic_alarm, state)
                else -> return EntryItem(id, "报警传感器", R.drawable.ic_alarm, state)
            }
        }
    }
}