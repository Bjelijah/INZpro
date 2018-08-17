package com.inz.inzpro

import android.app.Dialog
import android.databinding.BindingAdapter
import android.databinding.BindingConversion
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import com.inz.model.ModelMgr
import com.inz.utils.DebugLog
import io.reactivex.functions.Action

/**
 * dataBinder helper类
 */
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

    @BindingConversion
    @JvmStatic
    fun toOnLongClickListen(listener:Action?) = View.OnLongClickListener {
        listener?.run()
        true
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




    @BindingAdapter("onTouchUp","onTouchDown",requireAll = true)
    @JvmStatic
    fun setOnTouchListener(v:View,up:Action?,down:Action?){
        v.setOnTouchListener{v,event->
            when(event.action){
                MotionEvent.ACTION_DOWN->down?.run()
                MotionEvent.ACTION_UP->up?.run()
            }
            false
        }
    }


    @BindingAdapter("onTouchListener")
    @JvmStatic
    fun setOnTouchListener(v:View,b:Boolean){
        v.setOnTouchListener {
            v,event->
            var res = false
            when(v.id){
                R.id.play_gl->{
                    res = ModelMgr.getPlayViewModelInstance(ModelMgr.mContext!!).onGlTouch(v,event)
                }
            }
            res
        }
    }

    @BindingAdapter("layout_width")
    @JvmStatic
    fun setLayoutWidth(v:View,width:Int){
        Log.e("123","set laylout width  width=$width")
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

    @BindingAdapter("adjust_layout")
    @JvmStatic
    fun setAdjustLayout(v:View,bFull:Boolean){
        v.viewTreeObserver.addOnGlobalLayoutListener (object :ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                if (bFull){
                    var lp = v.layoutParams
                    lp.width = -1
                    lp.height = -1
                    v.layoutParams = lp
                }else{
                    var width = v.width
                    if (width>0){
                        var height = (9.0/16.0 * width).toInt()
                        var p = LinearLayout.LayoutParams(width, height)
                        v.layoutParams = p
                    }
                }
                v.viewTreeObserver.removeGlobalOnLayoutListener(this)
            }
        })
    }

    @BindingAdapter("play_list_title_src")
    @JvmStatic
    fun setPlayListTitleSrc(v:View,bExpand:Boolean){
        if (bExpand){
            v.setBackgroundResource(R.drawable.ic_expand_less_white)
        }else{
            v.setBackgroundResource(R.drawable.ic_expand_more_white)
        }
    }



    @BindingAdapter("update_picture_list")
    @JvmStatic
    fun onPictureListUpdate(v:View,b:Boolean){
        Log.i("123","onPictureListUpdate  b=$b")
        if(!b)return
        Log.i("123","update  picture list v as Rec")
        ModelMgr.getPlayListModelInstance(ModelMgr.mContext!!).updatePictureList(v as RecyclerView)
    }

    @BindingAdapter("update_picture_cmd")
    @JvmStatic
    fun onPictureCmdUpdate(v:View,b:Boolean){
        ModelMgr.getPlayListModelInstance(ModelMgr.mContext!!).updatePictureCmd(v as RecyclerView,b)
    }


    @BindingAdapter("update_video_list")
    @JvmStatic
    fun onVideoListUpdate(v:View,b:Boolean){
        if (!b)return
        ModelMgr.getPlayListModelInstance(ModelMgr.mContext!!).updateVideoList(v as RecyclerView)
    }

    @BindingAdapter("update_remote_list")
    @JvmStatic
    fun onRemoteListUpdate(v:View,b:Boolean){
        if(!b)return
        ModelMgr.getPlayListModelInstance(ModelMgr.mContext!!).updataRemoteList(v as RecyclerView)
    }

    @BindingAdapter("android:src")
    @JvmStatic
    fun setSrc(v:ImageView,resId:Int){
        v.setImageResource(resId)
    }

    @BindingAdapter("visible_action_down","duration_action_down",requireAll = true)
    @JvmStatic
    fun setVisibleActionDown(v:View,bShow:Boolean,va:Long){
        var action :TranslateAnimation?=null
        if(bShow){
            action = TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f,
                    Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0.0f)
            action.duration = va
            v.startAnimation(action)
            v.visibility = View.VISIBLE
        }else{
            action = TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f,
                    Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,1.0f)
            action.duration = va
            v.startAnimation(action)
            v.visibility = View.GONE
        }

    }

    @BindingAdapter( "visible_action_right","duration_action_right",requireAll = true)
    @JvmStatic
    fun setVisibleActionRight(v:View,bShow:Boolean,va:Long){
        Log.i("123","va=$va")
        var action :TranslateAnimation?=null
        if(bShow){
            action = TranslateAnimation(Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0.0f,
                    Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f)
            action.duration = va
            v.startAnimation(action)
            v.visibility = View.VISIBLE
        }else{
            action = TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,1.0f,
                    Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f)
            action.duration = va
            v.startAnimation(action)
            v.visibility = View.GONE
        }

    }

    @BindingAdapter("visible_action_big","duration_action_big",requireAll = false)
    @JvmStatic
    fun setVisibleActionBig(v:View,bShow:Boolean,va:Long){
        var action:ScaleAnimation?=null
        if (bShow){
            Log.i("123","show big")
            action = ScaleAnimation(1f,4/3f,1f,10/9f,0f,0f)
            action.duration = va
            v.startAnimation(action)
        }else{
            Log.i("123","show small")
            action = ScaleAnimation(1f,3/4f,1f,9/10f,0f,0f)
            action.duration = va
            v.startAnimation(action)
        }
    }



}