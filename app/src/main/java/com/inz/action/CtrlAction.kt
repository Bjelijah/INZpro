package com.inz.action

import android.content.Context
import android.view.Display
import com.inz.model.ModelMgr

/**
 * 全局控制工具类
 */
object CtrlAction {
    val PLAY_MODE_MAIN_VIEW     = 0x00  //no use
    val PLAY_MODE_SUB_VIEW      = 0x01
    val PLAY_MODE_LOCAL_REPLAY  = 0x02
    val PLAY_MODE_REMOTE_REPLAY = 0x03

    var isFull:Boolean = false
    var isPlayBack:Boolean = false


    fun setViewFullOrNot(c:Context,curPlayMode:Int){
        if(isFull){
            isFull = false

            when(curPlayMode){
                PLAY_MODE_MAIN_VIEW->{}
                PLAY_MODE_SUB_VIEW->{
                    ModelMgr.getMainViewModelInstance(c).setFullScreen(false,false)
                    ModelMgr.getPlayViewModelInstance(c).setFullScreen(false,false)
                }
                PLAY_MODE_LOCAL_REPLAY->{
                    ModelMgr.getMainViewModelInstance(c).setFullScreen(false,false)
                    ModelMgr.getPlayViewModelInstance(c).setFullScreen(false,true)
                }
                PLAY_MODE_REMOTE_REPLAY->{
                    ModelMgr.getMainViewModelInstance(c).setFullScreen(false,true)
                    ModelMgr.getPlayViewModelInstance(c).setFullScreen(false,false)
                }
                else ->{}
            }
        }else {
            isFull = true

            when(curPlayMode){
                PLAY_MODE_MAIN_VIEW->{}
                PLAY_MODE_SUB_VIEW->{
                    ModelMgr.getMainViewModelInstance(c).setFullScreen(true,false)
                    ModelMgr.getPlayViewModelInstance(c).setFullScreen(true,false)
                }
                PLAY_MODE_LOCAL_REPLAY->{
                    ModelMgr.getMainViewModelInstance(c).setFullScreen(true,false)
                    ModelMgr.getPlayViewModelInstance(c).setFullScreen(true,true)
                }
                PLAY_MODE_REMOTE_REPLAY->{
                    ModelMgr.getMainViewModelInstance(c).setFullScreen(true,false)
                    ModelMgr.getPlayViewModelInstance(c).setFullScreen(true,true)
                }
                else ->{}
            }
        }
    }

    fun setFullOrNot(c:Context){
        if (isFull){
            isFull = false
            ModelMgr.getMainViewModelInstance(c).setFullScreen(false,true)
            ModelMgr.getPlayViewModelInstance(c).setFullScreen(false,true)

        }else{
            isFull = true
            ModelMgr.getMainViewModelInstance(c).setFullScreen(true,true)
            ModelMgr.getPlayViewModelInstance(c).setFullScreen(true,true)
        }
    }

    fun setPlayReview(c:Context){
        isPlayBack = false
        ModelMgr.getMainViewModelInstance(c).setIsPlayReview(true)
        ModelMgr.getPlayViewModelInstance(c).setIsPlayReview(true)
        ModelMgr.getReplayCtrlModelInstance(c).setIsPlayReView(true)
        ModelMgr.getMainCtrlModelInstance(c).setIsPlayReView(true)
    }
    fun setPlayPlayback(c:Context){
        isPlayBack = true
        ModelMgr.getMainViewModelInstance(c).setIsPlayReview(false)
        ModelMgr.getPlayViewModelInstance(c).setIsPlayReview(false)
        ModelMgr.getReplayCtrlModelInstance(c).setIsPlayReView(false)
        ModelMgr.getMainCtrlModelInstance(c).setIsPlayReView(false)
    }


}