package com.inz.model.main

import android.content.Context
import android.databinding.ObservableField
import android.databinding.adapters.SeekBarBindingAdapter
import android.util.Log
import android.view.View
import android.widget.PopupWindow
import com.inz.action.CtrlAction
import com.inz.activity.view.PopWindowView
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.model.ModelMgr
import com.inz.utils.DebugLog
import com.inz.utils.Utils
import io.reactivex.functions.Action

class ReplayCtrlModel(private var mContext: Context):BaseViewModel {

    val PLAY_STATE_PLAYING = 0x00
    val PLAY_STATE_STOP    = 0x01
    val PLAY_STATE_PAUSE   = 0x02

    fun setContext(c:Context){
        mContext = c
    }
    override fun onCreate() {
    }

    override fun onDestory() {
    }

    /**
     * replay ctrl
     */

    private var mPlayState = PLAY_STATE_PLAYING
    private var mSoundVol:PopupWindow?=null
    private var mTotalMsec = 0L
    var mReplaySrc   = ObservableField<Int>(R.drawable.replay_ctrl_pause)
    val mReplayBeg   = ObservableField<String>("00:00:00")
    val mReplayEnd   = ObservableField<String>("00:00:00")
    val mReplayName  = ObservableField<String>("H.264")
    val mReplaySpeed = ObservableField<String>("0 Kbps")
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
        mSoundVol = PopWindowView.generate(mContext,{
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



    val onClickReplayStop         = Action { stopClick() }
    val onClickReplayFastRewind   = Action { ModelMgr.getPlayViewModelInstance(mContext).onSlowClick() }
    val onClickReplaySkipLast     = Action { ModelMgr.getPlayViewModelInstance(mContext).onPlayLastClick() }
    val onClickReplayPauseAndPlay = Action { pauseAndPlayClick() }
    val onClickReplaySkipNext     = Action { ModelMgr.getPlayViewModelInstance(mContext).onPlayNextClick() }
    val onClickReplayFastForward  = Action { ModelMgr.getPlayViewModelInstance(mContext).onFastClick() }
    val onClickReplayCatch        = Action { ModelMgr.getApPlayerInstance().catchPic() }
    val onClickReplayZoom         = Action { CtrlAction.setFullOrNot(mContext) }

    val onProgressChanged         = SeekBarBindingAdapter.OnProgressChanged{
        seekBar, progress, fromUser ->

//        Log.i("123",progress.toString()+"  isUser="+fromUser+  "mprogress="+mProcess.get())
//        mProcess.set(progress)
        if (fromUser && seekBar.max!=0){
            var nowMsec = progress * mTotalMsec / seekBar.max.toFloat()
            setBegTime(Utils.formatMsec(nowMsec.toLong()))
        }


    }

    val onStartTrackingTouch = SeekBarBindingAdapter.OnStartTrackingTouch{sb->
        Log.i("123","on start track touch")
        //stop
        ModelMgr.getPlayViewModelInstance(mContext).setScheduledFlag(false,0)
        //pause
        ModelMgr.getPlayViewModelInstance(mContext).pauseView()
    }

    val onStopTrackingTouch = SeekBarBindingAdapter.OnStopTrackingTouch{sb->
        Log.i("123","on stop track touch")
//        ModelMgr.getPlayViewModelInstance(mContext).set2LocalFrame(sb.progress)

        var pos =if (sb.max!=0)sb.progress *100 / sb.max else 0
        Log.e("123","set pos pos=$pos")
        ModelMgr.getPlayViewModelInstance(mContext).set2LocalPos(pos)
        ModelMgr.getPlayViewModelInstance(mContext).pauseView()
        ModelMgr.getPlayViewModelInstance(mContext).setScheduledFlag(true,400)


    }

    fun initUi(){
        mReplayBeg.set("00:00:00")
        mReplayEnd.set("00:00:00")
        mReplaySpeed.set("0 Kbps")
        mProcess.set(0)
        mProcessMax.set(100)
    }

    fun setTotalMsec(msec:Long){
        mTotalMsec = msec
    }

    fun setSBMax(max:Int){
        mProcessMax.set(max)
    }

    fun setSBProgress(progress:Int){
        mProcess.set(progress)
    }


    fun setBegTime(s:String){
        mReplayBeg.set(s)
    }

    fun setEndTime(s:String){
        mReplayEnd.set(s)
    }

    fun setSpeed(s:String){
        mReplaySpeed.set(s)
    }

    fun setName(s:String){
        mReplayName.set(s)
    }

    private fun stopClick(){
        ModelMgr.getPlayViewModelInstance(mContext).onStopClick()
        initUi()
        mReplaySrc.set(R.drawable.replay_ctrl_play)
        mPlayState = PLAY_STATE_STOP
    }

    private fun pauseAndPlayClick(){
        when(mPlayState){
            PLAY_STATE_STOP->{
                //play
                mPlayState = PLAY_STATE_PLAYING
                ModelMgr.getPlayViewModelInstance(mContext).onPlayClick()
                mReplaySrc.set(R.drawable.replay_ctrl_pause)

            }
            PLAY_STATE_PAUSE->{
                //play
                mPlayState = PLAY_STATE_PLAYING
                ModelMgr.getPlayViewModelInstance(mContext).onPauseClick()
                mReplaySrc.set(R.drawable.replay_ctrl_pause)
            }
            PLAY_STATE_PLAYING->{
                //pause
                mPlayState = PLAY_STATE_PAUSE
                ModelMgr.getPlayViewModelInstance(mContext).onPauseClick()
                mReplaySrc.set(R.drawable.replay_ctrl_play)
            }
            else->{}
        }
    }
}