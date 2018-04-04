package com.inz.model.main

import android.content.Context
import android.databinding.ObservableField
import android.databinding.adapters.SeekBarBindingAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import com.inz.activity.view.SoundVolView
import com.inz.bean.ClickAction
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.utils.DebugLog
import io.reactivex.functions.Action
import java.util.*

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
//    val onClickReplaySound   = ClickAction { DebugLog.LogI("onclick replay sound")
//        var popWindow = SoundVolView.generate(mContext,{
//            mLayoutId = R.layout.layout_sound_vol
//            build()
//        })
//        popWindow.showAsDropDown()
//    }

    val onClickReplaySound = View.OnClickListener { v ->
        DebugLog.LogE("onClickReplaySound")
//        var popWindow = SoundVolView.generate(mContext, {
//            mLayoutId = R.layout.layout_sound_vol
//            build() })
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_sound_vol,null,false)
        var popWindow = PopupWindow(mContext)
        popWindow.contentView = view
        popWindow.showAsDropDown(v)

        DebugLog.LogI("isshow "+popWindow.isShowing)
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