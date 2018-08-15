package com.inz.model.player

import com.inz.bean.BaseBean
import com.inz.bean.RemoteBean

abstract class BasePlayer {
    interface PlayStateListener{
        fun onInit(b:Boolean)
        fun onDeinit(b:Boolean)
        fun onPlay(b:Boolean)
        fun onStop(b:Boolean)
        fun onCatchPic(b:Boolean)
        fun onRecordFileList(files:ArrayList<BaseBean>)
    }
    private var mListener:PlayStateListener ?=null
    fun registPlayStateListener(l:PlayStateListener):BasePlayer{
        mListener = l
        return this
    }

    fun registPlayStateListener(o1: (Boolean) -> Unit, o2:(Boolean)->Unit,o3:(Boolean)->Unit,o4:(Boolean)->Unit,o5:(Boolean)->Unit,o6:(ArrayList<BaseBean>)->Unit):BasePlayer{
        mListener = object :PlayStateListener{

                override fun onInit(b: Boolean)     = o1(b)
                override fun onDeinit(b: Boolean)   = o2(b)
                override fun onPlay(b: Boolean)     = o3(b)
                override fun onStop(b: Boolean)     = o4(b)
                override fun onCatchPic(b: Boolean) = o5(b)
                override fun onRecordFileList(files: ArrayList<BaseBean>) = o6(files)
            }
        return this
    }


    fun unregistPlayStateListener(l:PlayStateListener){
        mListener = null
    }

    fun unregistPlayStateListener(){
        mListener = null
    }

    fun sendInitResult(b:Boolean) = mListener?.onInit(b)

    fun sendDeinitResult(b:Boolean) = mListener?.onDeinit(b)

    fun sendPlayResult(b:Boolean) = mListener?.onPlay(b)

    fun sendStopResult(b:Boolean) = mListener?.onStop(b)

    fun sendCatchResult(b:Boolean) = mListener?.onCatchPic(b)

    fun sendRecordFileListResult(l: ArrayList<BaseBean>) = mListener?.onRecordFileList(l)

    open fun setUrl(uri:String){}
    open fun setAlarm(b:Boolean){}
    open fun searchRemoteFile(beg:String,end:String,curPage:Int?,pageSize:Int?){}
    abstract fun init(crypto:Int,uri:String):BasePlayer
    abstract fun deinit():BasePlayer
    abstract fun play(isSub:Boolean):BasePlayer
    abstract fun playback(isSub:Boolean,beg:String,end:String):BasePlayer
    abstract fun rePlay():BasePlayer
    abstract fun pause():BasePlayer
    abstract fun isPause():Boolean
    abstract fun stop():BasePlayer
    abstract fun stepNext():Boolean
    abstract fun stepLast():Boolean
    abstract fun catchPic():BasePlayer


    open fun getTotalFrame():Int = 0
    open fun getCurFrame():Int = 0
    open fun setCurFrame(curFrame:Int){}
    open fun getTotalMsec():Int = 0
    open fun getPlayedMsec():Int = 0
    open fun getPos():Int = 0
    open fun setPos(pos:Int){}
    open fun setPlaySpeed(speed:Float){}
    open fun stopAndPlayAnother(url:String){}



}
