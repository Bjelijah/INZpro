package com.inz.model.main

import android.content.Context
import android.databinding.ObservableField
import android.databinding.adapters.SeekBarBindingAdapter
import android.util.Log
import com.howellsdk.audio.AudioAction
import com.inz.inzpro.BaseViewModel

class SoundVolModel (var mContext:Context):BaseViewModel{

    fun setContext(c:Context){
        mContext = c
    }

    override fun onCreate() {
        var curVol = AudioAction.getInstance().getStreamVolum(mContext)
        mProcess.set(curVol)
        mProcessStr.set(curVol.toString())
    }
    override fun onDestory() {
    }

    val mProcessStr     = ObservableField<String>("0")
    val mProcess        = ObservableField<Int>(0)
    val onProgressChanged         = SeekBarBindingAdapter.OnProgressChanged{
        seekBar, progress, fromUser ->
        if (fromUser) {
            mProcessStr.set(progress.toString())
            AudioAction.getInstance().setStreamVolum(mContext, progress)
        }
    }

}