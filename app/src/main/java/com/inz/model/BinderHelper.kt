package com.inz.inzpro

import android.databinding.BindingAdapter
import android.databinding.BindingConversion
import android.databinding.ViewDataBinding
import android.util.Log
import android.view.View
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

    @BindingAdapter("widthHeightByRatio")
    @JvmStatic
    fun setWidthHeightByRatio(v:View,width:Int){
        var w = width
        var h = 0

        if(w>0) {
             h = (9.0 / 16.0 * width).toInt()
        }else{
            var p_ = v.layoutParams
            p_.width = w
            v.layoutParams = p_
            w = v.width
            h = (9.0 / 16.0 * width).toInt()
        }
        var p = LinearLayout.LayoutParams(w, h)
        v.layoutParams = p
    }



    @BindingAdapter("onTouchListener")
    @JvmStatic
    fun setOnTouchListener(v:View,b:Boolean){
        v.setOnTouchListener({
            v,event->
            var res = false
            Log.i("123","on touch  ddd")
            when(v.id){
                R.id.play_gl->{
                    res = ModelMgr.getPlayViewModelInstance(ModelMgr.mContext!!).onGlTouch(v,event)
                }
            }
            res
        })
    }

    @BindingAdapter("layout_width")
    @JvmStatic
    fun setMyLayoutWidth(v:View,width:Int){
        Log.e("123","set laylout width  width=$width")
        var p = v.layoutParams
        p.width = width
        v.layoutParams = p
    }

    @BindingAdapter("android:layout_width")
    @JvmStatic
    fun setLayoutWidth(v:View,width:Int){

        var p = v.layoutParams
        p.width = width
        v.layoutParams = p
    }



    @BindingAdapter("layout_height")
    @JvmStatic
    fun setLayoutHeight(v:View,height:Int){
        Log.e("123","set laylout height  height=$height    vh=${v.height}")
        var p = v.layoutParams
        p.height = height
        v.layoutParams = p
        Log.e("123","after set hight  width=${v.width} vh=${v.height}")
        Thread(){
            kotlin.run {
                Thread.sleep(1000)
                Log.e("123","after set hight  width=${v.width} vh=${v.height}")
            }
        }.start()
    }

    @BindingAdapter("layout_width_height")
    @JvmStatic
    fun setLayoutParam(v:View,width:Int,height:Int){
        var p = v.layoutParams
        p.width = width
        p.height = height
        v.layoutParams = p
    }


}