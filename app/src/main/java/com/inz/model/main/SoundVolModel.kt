package com.inz.model.main

import android.content.Context
import android.databinding.ObservableField
import android.databinding.adapters.SeekBarBindingAdapter
import android.util.Log
import com.inz.inzpro.BaseViewModel

class SoundVolModel (mContext:Context):BaseViewModel{


    override fun onDestory() {
    }

    val mProcess     = ObservableField<String>("0")
    val onProgressChanged         = SeekBarBindingAdapter.OnProgressChanged{
        seekBar, progress, fromUser ->

        Log.i("123",progress.toString()+"  isUser="+fromUser+  "mprogress="+mProcess.get())
        mProcess.set(progress.toString())
    }
}