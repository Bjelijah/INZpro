package com.inz.model.main

import android.content.Context
import android.databinding.ObservableField
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.inz.inzpro.BaseViewModel
import com.inz.utils.DebugLog
import io.reactivex.functions.Action

class MainViewModel(private var mContext:Context) : BaseViewModel{
    override fun setFullScreen(b: Boolean) {
        if(b){
            Log.i("123","main view model set full")
            mCtrlVisibility.set(View.GONE)
            mReplayListVisibility.set(View.GONE)
            mReplayCtrlVisibility.set(View.GONE)
            mPlayViewWidth.set(ViewGroup.LayoutParams.MATCH_PARENT)
            mPlayViewHeight.set(ViewGroup.LayoutParams.MATCH_PARENT)
        }else{
            mCtrlVisibility.set(View.VISIBLE)
            mReplayListVisibility.set(View.VISIBLE)
            mReplayCtrlVisibility.set(View.VISIBLE)
            mPlayViewWidth.set(0)
            mPlayViewHeight.set(ViewGroup.LayoutParams.WRAP_CONTENT)
        }
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

    }
    val onClickCtrlReplay     = Action {  DebugLog.LogI("onclick ctrl replay") }
    val onClickCtrlAlarm      = Action {  DebugLog.LogI("onclick ctrl alarm") }
    val onClickCtrlRecord     = Action {  DebugLog.LogI("onclick ctrl record") }
    val onClickCtrlCatch      = Action {  DebugLog.LogI("onclick ctrl catch") }

    val onClickTest  = Action {
        Log.i("123"," test click")
    }




}