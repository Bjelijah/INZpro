package com.inz.action

import android.content.Context
import com.inz.model.ModelMgr

object CtrlAction {
    var isFull:Boolean = false


    fun setFullOrNot(c:Context){
        if (isFull){
            isFull = false
            ModelMgr.getMainViewModelInstance(c).setFullScreen(false)
            ModelMgr.getPlayViewModelInstance(c).setFullScreen(false)

        }else{
            isFull = true
            ModelMgr.getMainViewModelInstance(c).setFullScreen(true)
            ModelMgr.getPlayViewModelInstance(c).setFullScreen(true)
        }
    }



}