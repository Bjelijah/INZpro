package com.inz.model.main

import android.content.Context
import android.databinding.ObservableField
import android.util.Log
import com.inz.inzpro.BaseViewModel
import com.inz.utils.DebugLog
import io.reactivex.functions.Action

class MainViewModel(private var mContext:Context) : BaseViewModel{
    override fun onDestory() {
    }

    val onClickCtrlBack       = Action {  DebugLog.LogI("onclick ctrl back") }
    val onClickCtrlReplay     = Action {  DebugLog.LogI("onclick ctrl replay") }
    val onClickCtrlAlarm      = Action {  DebugLog.LogI("onclick ctrl alarm") }
    val onClickCtrlRecord     = Action {  DebugLog.LogI("onclick ctrl record") }
    val onClickCtrlCatch      = Action {  DebugLog.LogI("onclick ctrl catch") }



    val mReplayBeg  = ObservableField<String>("00:00:01")
    val mReplayEnd  = ObservableField<String>("00:00:02")
    val onClickReplaySound   = Action { DebugLog.LogI("onclick replay sound")
        mReplayBeg.set("00:11:11")
    }

}