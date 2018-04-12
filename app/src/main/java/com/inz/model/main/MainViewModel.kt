package com.inz.model.main

import android.content.Context
import android.databinding.ObservableField
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.inz.action.CtrlAction
import com.inz.inzpro.BaseViewModel
import com.inz.model.ModelMgr
import com.inz.utils.DebugLog
import io.reactivex.functions.Action

class MainViewModel(private var mContext:Context) : BaseViewModel{
     var mIsPlayBack = false

     fun setFullScreen(b: Boolean) {
        if(b){
            Log.i("123","main view model set full")
            mCtrlVisibility.set(View.GONE)
            mReplayListVisibility.set(View.GONE)
            mReplayCtrlVisibility.set(View.GONE)
            mPlayViewWidth.set(ViewGroup.LayoutParams.MATCH_PARENT)
            mPlayViewHeight.set(ViewGroup.LayoutParams.MATCH_PARENT)
        }else{
            if (mIsPlayBack) {
                mReplayListVisibility.set(View.VISIBLE)
                mReplayCtrlVisibility.set(View.VISIBLE)
            }else{
                mReplayListVisibility.set(View.INVISIBLE)
                mReplayCtrlVisibility.set(View.GONE)
            }
            mCtrlVisibility.set(View.VISIBLE)
            mPlayViewWidth.set(0)
            mPlayViewHeight.set(ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    fun setIsPlayReview(b:Boolean){
        if (b){
            mIsPlayBack = false
            mReplayListVisibility.set(View.INVISIBLE)
            mReplayCtrlVisibility.set(View.GONE)
        }else{
            mIsPlayBack = true
            mReplayListVisibility.set(View.VISIBLE)
            mReplayCtrlVisibility.set(View.VISIBLE)
        }
    }

    override fun onCreate() {

    }

    override fun onDestory() {
    }
    val mPlayViewWidth        = ObservableField<Int>(0)
    val mPlayViewHeight       = ObservableField<Int>(ViewGroup.LayoutParams.WRAP_CONTENT)
    val mPlayViewFull         = ObservableField<Boolean>(false)

    val mCtrlVisibility       = ObservableField<Int>(View.VISIBLE)
    val mReplayListVisibility = ObservableField<Int>(View.VISIBLE)
    val mReplayCtrlVisibility = ObservableField<Int>(View.VISIBLE)
    val onClickCtrlBack       = Action {
        DebugLog.LogI("onclick ctrl back")
        CtrlAction.setPlayReview(mContext)
    }
    val onClickCtrlReplay     = Action {
        DebugLog.LogI("onclick ctrl replay")
        CtrlAction.setPlayPlayback(mContext)
    }
    val onClickCtrlAlarm      = Action {  DebugLog.LogI("onclick ctrl alarm")

    }
    val onClickCtrlRecord     = Action {  DebugLog.LogI("onclick ctrl record") }
    val onClickCtrlCatch      = Action {
        DebugLog.LogI("onclick ctrl catch")
        ModelMgr.getApPlayerInstance().catchPic()

    }









}