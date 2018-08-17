package com.inz.utils

import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation

/**
 * 动画效果工具类
 */
object AnimationHelper {
    fun showView(v: View,duration:Long){
        var a = TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,
                Animation.RELATIVE_TO_SELF,-1f,Animation.RELATIVE_TO_SELF,0f)
        a.duration = duration
        v.startAnimation(a)
        v.visibility = View.VISIBLE
    }

    fun hideView(v:View,duration: Long){
        var a = TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,
                Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,-1f)
        a.duration = duration
        v.startAnimation(a)
        v.visibility = View.GONE
    }




}