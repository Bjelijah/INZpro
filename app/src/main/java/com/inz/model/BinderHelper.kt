package com.inz.inzpro

import android.databinding.BindingConversion
import android.databinding.ViewDataBinding
import android.view.View
import com.inz.bean.ClickAction
import com.inz.utils.DebugLog
import io.reactivex.functions.Action

object BinderHelper {
    val defaultBinder = object:ViewModelBinder {


        override fun bind(bind: ViewDataBinding, vm: BaseViewModel?) {
            bind.setVariable(BR.vm,vm)//vm name in  layout file
        }
    }

    @BindingConversion
    @JvmStatic
    fun toOnClickListen(listener:Action?) = View.OnClickListener {
        if (listener==null)DebugLog.LogE("listener==null")
//        listener?.v = it
        listener?.run()
    }




}