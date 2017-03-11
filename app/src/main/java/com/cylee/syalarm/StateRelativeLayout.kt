package com.cylee.syalarm

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 * Created by cylee on 17/3/11.
 */
class StateRelativeLayout : RelativeLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun setPressed(pressed: Boolean) {
        super.setPressed(pressed)
        if (pressed) {
            alpha = 0.6f
        } else{
            alpha = 1f
        }
    }
}