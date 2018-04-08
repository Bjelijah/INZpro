package com.inz.model.main

import android.content.Context
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





}