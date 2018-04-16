package com.inz.model.player

abstract class BasePlayer {
    interface PlayStateListener{
        fun onInit(b:Boolean)
        fun onDeinit(b:Boolean)
        fun onPlay(b:Boolean)
        fun onStop(b:Boolean)
        fun onCatchPic(b:Boolean)
    }
    private val mListener:ArrayList<PlayStateListener> = ArrayList()
    fun registPlayStateListener(l:PlayStateListener):BasePlayer{
        mListener.add(l)
        return this
    }

    fun registPlayStateListener(o1: (Boolean) -> Unit, o2:(Boolean)->Unit,o3:(Boolean)->Unit,o4:(Boolean)->Unit,o5:(Boolean)->Unit):BasePlayer{
        mListener.clear() //just regist one
        mListener.add(object :PlayStateListener{
                override fun onInit(b: Boolean)     = o1(b)
                override fun onDeinit(b: Boolean)   = o2(b)
                override fun onPlay(b: Boolean)     = o3(b)
                override fun onStop(b: Boolean)     = o4(b)
                override fun onCatchPic(b: Boolean) = o5(b)
            })
        return this
    }


    fun unregistPlayStateListener(l:PlayStateListener){
        mListener.remove(l)
    }

    fun unregistPlayStateListener(){
        mListener.clear()
    }

    fun sendInitResult(b:Boolean){
        for (l in mListener){
            l.onInit(b)
        }
    }

    fun sendDeinitResult(b:Boolean){
        for (l in mListener){
            l.onDeinit(b)
        }
    }

    fun sendPlayResult(b:Boolean){
        for (l in mListener){
            l.onPlay(b)
        }
    }

    fun sendStopResult(b:Boolean){
        for (l in mListener){
            l.onStop(b)
        }
    }

    fun sendCatchResult(b:Boolean){
        for (l in mListener){
            l.onCatchPic(b)
        }
    }


    open fun setUrl(uri:String){}
    abstract fun init(crypto:Int,uri:String):BasePlayer
    abstract fun deinit():BasePlayer
    abstract fun play(isSub:Boolean):BasePlayer
    abstract fun stop():BasePlayer
    abstract fun catchPic():BasePlayer
}