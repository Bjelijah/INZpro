package com.inz.inzpro

import android.databinding.BindingAdapter
import android.databinding.BindingConversion
import android.databinding.ViewDataBinding
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import com.inz.model.ModelMgr
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

    @BindingAdapter("widthHeightRatio")
    @JvmStatic
    fun setWidthHeightRatio(v:View,ratio:Double){
        Log.e("123","set width~~~~~~~~~~~ ratio=$ratio  ")
        v.viewTreeObserver.addOnGlobalLayoutListener (object :ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                var width = v.width
                if (width>0){
                    var height = (1/ratio * width).toInt()
                    var p = LinearLayout.LayoutParams(width, height)
                    v.layoutParams = p
                }
                v.viewTreeObserver.removeGlobalOnLayoutListener(this)
            }
        })
    }


    @BindingAdapter("onTouchListener")
    @JvmStatic
    fun setOnTouchListener(v:View,b:Boolean){
        v.setOnTouchListener({
            v,event->
            var res = false
            Log.i("123","on touch  ddd")
            when(v.id){
                R.id.ll_play->{
                    Log.i("123","on touch ")
                    res = ModelMgr.getPlayViewModelInstance(ModelMgr.mContext!!).onGlTouch(v,event)
                }
            }
            res
        })
    }




}