package com.inz.model.main

import android.content.Context
import android.databinding.ObservableField
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.inz.action.CtrlAction
import com.inz.inzpro.BaseViewModel

class PlayViewModel(mContext:Context):BaseViewModel {
     var mIsPlayback = false
     var mIsFull = false
     fun setFullScreen(b: Boolean) {
        if (b){
            Log.i("123","PlayViewModel set full")
            if (mIsPlayback)mReplayCtrlVisibility.set(View.VISIBLE)
            else mReplayCtrlVisibility.set(View.GONE)
            mIsFull = true
            mPlayViewFull.set(true)
            //
        }else{
            mReplayCtrlVisibility.set(View.GONE)
            mIsFull = false
            mPlayViewFull.set(false)
        }
    }
    fun setIsPlayReview(b:Boolean){
        mIsPlayback = !b
        if (b){
            mReplayCtrlVisibility.set(View.GONE)
        }
    }


    override fun onDestory() {
    }

    val mGestureDetector = GestureDetector(mContext,object :GestureDetector.SimpleOnGestureListener(){
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            Log.i("123","onSingleTapUp")
            if (mIsPlayback && mIsFull){
                if(mReplayCtrlVisibility.get()==View.GONE){
                    mReplayCtrlVisibility.set(View.VISIBLE)
                }else{
                    mReplayCtrlVisibility.set(View.GONE)
                }

            }
            return super.onSingleTapUp(e)
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            Log.e("123","onDoubleTap")
            CtrlAction.setFullOrNot(mContext)
            return super.onDoubleTap(e)
        }

//        override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
//            Log.e("123","onDouble tap event")
//            return super.onDoubleTapEvent(e)
//        }

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }
    })

    fun onGlTouch(v:View,event:MotionEvent):Boolean{
        return mGestureDetector.onTouchEvent(event)
    }
    val mProcessVisibility      = ObservableField<Int>(View.VISIBLE)
    val mReplayCtrlVisibility   = ObservableField<Int>(View.GONE)
    val mPlayViewFull           = ObservableField<Boolean>(false)









}