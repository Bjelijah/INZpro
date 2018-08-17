package com.inz.model.main

import android.content.Context
import android.databinding.ObservableField
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.inz.inzpro.BaseViewModel
import com.inz.model.ModelMgr
import com.inz.model.net.PtzMgr
import io.reactivex.functions.Action

/**
 * 右下ptz控制ViewModel
 */
class PtzCtrlModel(private var mContext:Context):BaseViewModel {
    override fun onCreate() {
    }

    override fun onDestory() {
    }

    fun setCOntext(c:Context){
        mContext = c
    }


    val onTouchUpDown   = Action {
        Log.i("123","up  touch down")
        ModelMgr.getMainCtrlModelInstance(mContext).resetCruiseCtrlColor()
        PtzMgr.getInstance().ptzUp()
    }

    val onTouchUpUp     = Action {
        Log.i("123","up touch up")
        PtzMgr.getInstance().ptzStop()
    }

    val onTouchLeftDown = Action {
        Log.i("123","left  touch down")
        ModelMgr.getMainCtrlModelInstance(mContext).resetCruiseCtrlColor()
        PtzMgr.getInstance().ptzLeft()
    }
    val onTouchLeftUp   = Action {
        Log.i("123","left touch up")
        PtzMgr.getInstance().ptzStop()
    }

    val onTouchDownDown = Action {
        Log.i("123","down  touch down")
        ModelMgr.getMainCtrlModelInstance(mContext).resetCruiseCtrlColor()
        PtzMgr.getInstance().ptzDown()
    }
    val onTouchDownUp   = Action {
        Log.i("123","down touch up")
        PtzMgr.getInstance().ptzStop()
    }

    val onTouchRightDown = Action {
        Log.i("123","right  touch down")
        ModelMgr.getMainCtrlModelInstance(mContext).resetCruiseCtrlColor()
        PtzMgr.getInstance().ptzRight()
    }
    val onTouchRightUp   = Action {
        Log.i("123","right touch up")
        PtzMgr.getInstance().ptzStop()
    }





}