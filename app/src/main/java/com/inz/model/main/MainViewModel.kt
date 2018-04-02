package com.inz.model.main

import android.content.Context
import android.databinding.ObservableField
import android.databinding.adapters.SeekBarBindingAdapter
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


    /**
     * replay ctrl
     */
    val mReplayBeg   = ObservableField<String>("00:00:00")
    val mReplayEnd   = ObservableField<String>("00:00:00")
    val mReplayName  = ObservableField<String>("H.265E")
    val mReplaySpeed = ObservableField<String>("524Kbps")
    val mProcess     = ObservableField<Int>(0)
    val mProcessMax  = ObservableField<Int>(100)
    val onClickReplaySound   = Action { DebugLog.LogI("onclick replay sound")


    }

    val onClickReplayStop         = Action {  }
    val onClickReplayFastRewind   = Action {  }
    val onClickReplaySkipLast     = Action {  }
    val onClickReplayPauseAndPlay = Action {  }
    val onClickReplaySkipNext     = Action {  }
    val onClickReplayFastForward  = Action {  }
    val onClickReplayCatch        = Action {  }
    val onClickReplayZoom         = Action {  }

    val onProgressChanged         = SeekBarBindingAdapter.OnProgressChanged{
        seekBar, progress, fromUser ->

        Log.i("123",progress.toString()+"  isUser="+fromUser+  "mprogress="+mProcess.get())
        mProcess.set(progress)
    }



}