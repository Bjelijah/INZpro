package com.inz.action

import android.content.Context
import com.inz.model.ModelMgr

object CtrlAction {
    var isFull:Boolean = false
    var isPlayBack:Boolean = false

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

    fun setPlayReview(c:Context){
        isPlayBack = false
        ModelMgr.getMainViewModelInstance(c).setIsPlayReview(true)
        ModelMgr.getPlayViewModelInstance(c).setIsPlayReview(true)
        ModelMgr.getReplayCtrlModelInstance(c).setIsPlayReView(true)
    }
    fun setPlayPlayback(c:Context){
        isPlayBack = true
        ModelMgr.getMainViewModelInstance(c).setIsPlayReview(false)
        ModelMgr.getPlayViewModelInstance(c).setIsPlayReview(false)
        ModelMgr.getReplayCtrlModelInstance(c).setIsPlayReView(false)
    }


}