package com.inz.model.main

import android.content.Context
import android.databinding.ObservableField
import android.databinding.adapters.SeekBarBindingAdapter
import android.util.Log
import android.view.View
import android.widget.PopupWindow
import com.inz.action.CtrlAction
import com.inz.activity.view.SoundVolView
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.model.ModelMgr
import com.inz.utils.DebugLog
import io.reactivex.functions.Action

class ReplayCtrlModel(mContext: Context):BaseViewModel {
    override fun setFullScreen(b: Boolean) {
    }

    override fun onDestory() {
    }

    /**
     * replay ctrl
     */

    private var mSoundVol:PopupWindow?=null

    val mReplayBeg   = ObservableField<String>("00:00:00")
    val mReplayEnd   = ObservableField<String>("00:00:00")
    val mReplayName  = ObservableField<String>("H.265E")
    val mReplaySpeed = ObservableField<String>("524Kbps")
    val mProcess     = ObservableField<Int>(0)
    val mProcessMax  = ObservableField<Int>(100)


    val onClickReplaySound = View.OnClickListener { v ->
        DebugLog.LogE("onClickReplaySound   "+mSoundVol+"  "+mSoundVol?.isShowing)
        if (mSoundVol?.isShowing==true){
            Log.i("123","dismaiss")
            mSoundVol?.dismiss()
            mSoundVol = null
            return@OnClickListener
        }
        mSoundVol = SoundVolView.generate(mContext,{
            mLayoutId = R.layout.view_sound_vol
            mViewModel = ModelMgr.getSoundVolModelInstance(mContext)
            build()
        })
        DebugLog.LogI("before isshow "+mSoundVol?.isShowing)
        var h = mContext.resources.getDimension(R.dimen.sound_vol_height) + v.height
        mSoundVol?.showAsDropDown(v,0,-h.toInt())

//        mSoundVol?.showAtLocation(v,Gravity.NO_GRAVITY,0,-h.toInt())
        DebugLog.LogI("isshow "+mSoundVol?.isShowing)
    }



    val onClickReplayStop         = Action {  }
    val onClickReplayFastRewind   = Action {  }
    val onClickReplaySkipLast     = Action {  }
    val onClickReplayPauseAndPlay = Action {  }
    val onClickReplaySkipNext     = Action {  }
    val onClickReplayFastForward  = Action {  }
    val onClickReplayCatch        = Action {  }
    val onClickReplayZoom         = Action {
        CtrlAction.setFullOrNot(mContext)
    }

    val onProgressChanged         = SeekBarBindingAdapter.OnProgressChanged{
        seekBar, progress, fromUser ->

        Log.i("123",progress.toString()+"  isUser="+fromUser+  "mprogress="+mProcess.get())
        mProcess.set(progress)
    }
}