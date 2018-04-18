package com.inz.activity.view

import android.content.Context
import android.support.annotation.LayoutRes
import android.widget.PopupWindow
import com.inz.inzpro.BaseViewModel

class PopWindowView (val mContext: Context):BaseView(){


    @LayoutRes
    var mLayoutId = 0
    lateinit var mViewModel:BaseViewModel

    companion object {
        fun generate(context: Context,body:PopWindowView.()->PopupWindow):PopupWindow{
            return with(PopWindowView(context)){body()}
        }
    }


    fun build():PopupWindow{
        val v = bindView(mContext)
        val pw = PopupWindow(mContext)
        pw.contentView = v
        pw.isOutsideTouchable = true
        pw.setOnDismissListener { unBindView() }
        return pw
    }

    override fun getLayout(): Int = mLayoutId

    override fun getViewmodel(): BaseViewModel = mViewModel!!

}