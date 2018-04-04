package com.inz.activity.view

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.widget.PopupWindow

class SoundVolView (private val mContext: Context){
    @LayoutRes
    var mLayoutId = 0
    companion object {
        fun generate(context: Context,body:SoundVolView.()->PopupWindow):PopupWindow{
            return with(SoundVolView(context)){body()}
        }
    }


    fun build():PopupWindow{
        val v = LayoutInflater.from(mContext).inflate(mLayoutId,null,false)
        return PopupWindow(v)
    }
}