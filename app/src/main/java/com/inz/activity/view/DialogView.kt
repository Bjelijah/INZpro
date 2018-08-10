package com.inz.activity.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.support.annotation.LayoutRes
import com.inz.inzpro.BaseViewModel

class DialogView(val mContext: Context):BaseView() {

    @LayoutRes
    var mLayoutId = 0
    lateinit var mViewModel:BaseViewModel
    var mTitle:String ?=null


    companion object {
        fun generate(context: Context,body:DialogView.()->Dialog):Dialog{
            return with(DialogView(context)){body()}
        }
    }

    fun build():Dialog{
        val v = bindView(mContext)
        return AlertDialog.Builder(mContext)
                .setView(v)
//                .setTitle(mTitle)
                .setCancelable(true)
                .setOnDismissListener {
                    //cancel
                    unBindView()
                }
                .create()
    }

    override fun getLayout():Int = mLayoutId

    override fun getViewmodel(): BaseViewModel = mViewModel
}