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
    @Volatile var mIsUser = false
    val MIN_CLICK_DELAY_TIME = 1000
    private var mLastClickTime = 0L
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
    val mReplayName  = ObservableField<String>("H264")
    val mReplaySpeed = ObservableField<String>("0Kbps")
    val mProcess     = ObservableField<Int>(0)
    val mProcessMax  = ObservableField<Int>(0)
    val mVisibility  = ObservableField<Int>(View.GONE)
    val mCatchVisibility = ObservableField<Int>(View.INVISIBLE)

    val onClickReplaySound = View.OnClickListener { v ->
        DebugLog.LogE("onClickReplaySound   "+mSoundVol+"  "+mSoundVol?.isShowing)
        if (mSoundVol?.isShowing==true){
            Log.i("123","dismaiss")
            mSoundVol?.dismiss()
            mSoundVol = null
            return@OnClickListener
        }
        mSoundVol = PopWindowView.generate(mContext) {
            mLayoutId = R.layout.view_sound_vol
            mViewModel = ModelMgr.getSoundVolModelInstance(mContext)
            build()
        }
        DebugLog.LogI("before isshow "+mSoundVol?.isShowing)
        var h = mContext.resources.getDimension(R.dimen.sound_vol_height) + v.height
        mSoundVol?.showAsDropDown(v,0,-h.toInt())

//        mSoundVol?.showAtLocation(v,Gravity.NO_GRAVITY,0,-h.toInt())
        DebugLog.LogI("isshow "+mSoundVol?.isShowing)
    }



    val onClickReplayStop         = Action { if(isFastClick())return@Action;stopClick() }
    val onClickReplayFastRewind   = Action { if(isFastClick())return@Action;ModelMgr.getPlayViewModelInstance(mContext).onSlowClick() }
    val onClickReplaySkipLast     = Action { pauseView();ModelMgr.getPlayViewModelInstance(mContext).onPlayLastClick() }
    val onClickReplayPauseAndPlay = Action { if(isFastClick())return@Action;pauseAndPlayClick() }
    val onClickReplaySkipNext     = Action { pauseView();ModelMgr.getPlayViewModelInstance(mContext).onPlayNextClick() }
    val onClickReplayFastForward  = Action { if(isFastClick())return@Action;ModelMgr.getPlayViewModelInstance(mContext).onFastClick() }
    val onClickReplayCatch        = Action { if(isFastClick())return@Action;ModelMgr.getApPlayerInstance().catchPic() }
    val onClickReplayZoom         = Action { CtrlAction.setViewFullOrNot(mContext,ModelMgr.getPlayViewModelInstance(mContext).nowPlayState) }

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
        mIsUser = true
        //stop
        ModelMgr.getPlayViewModelInstance(mContext).setScheduledFlag(false,0)
        //pause
        ModelMgr.getPlayViewModelInstance(mContext).pause()
    }

    val onStopTrackingTouch = SeekBarBindingAdapter.OnStopTrackingTouch{sb->
        Log.i("123","on stop track touch")
//        ModelMgr.getPlayViewModelInstance(mContext).set2LocalFrame(sb.progress)
        ModelMgr.getPlayViewModelInstance(mContext).pause()
        var pos =if (sb.max!=0)sb.progress *100 / sb.max else 0
        Log.e("123","set pos pos=$pos     progress=${sb.progress}   max=${sb.max} ")
        ModelMgr.getPlayViewModelInstance(mContext).set2LocalPos(pos,sb.progress)
        ModelMgr.getPlayViewModelInstance(mContext).setScheduledFlag(true,400)

        mIsUser = false
    }

    fun getPos():Int{
        var p:Int = mProcess.get()?:0
        var m:Int = mProcessMax.get()?:1
        var ret = p *100/m
        return if(ret>100) 100 else ret
    }


    fun initUi(){
        mReplayBeg.set("00:00:00")
        mReplayEnd.set("00:00:00")
        mReplaySpeed.set("0Kbps")
        mProcess.set(0)
        mProcessMax.set(0)

        mPlayState = PLAY_STATE_PLAYING
        mReplaySrc.set(R.drawable.replay_ctrl_pause)

        mIsUser = false
    }

    fun getProcessMax():Int = mProcessMax.get()?:0

    fun setTotalMsec(msec:Long){
        mTotalMsec = msec
    }

    fun setSBMax(max:Int){
        mProcessMax.set(max)
    }

    fun setSBProgress(progress:Int){
        if (!mIsUser) {
            mProcess.set(progress)
        }
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

    fun setIsPlayReView(b:Boolean){
        mVisibility.set(if(b)View.GONE else View.VISIBLE)
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

    fun pauseView(){
        mPlayState = PLAY_STATE_PAUSE
        mReplaySrc.set(R.drawable.replay_ctrl_play)
    }



    fun isFastClick():Boolean{
        val curClickTime = System.currentTimeMillis()
        var flag = true
        if (curClickTime - mLastClickTime >= MIN_CLICK_DELAY_TIME){
            flag = false
        }
        mLastClickTime = curClickTime
        return flag
    }

    fun setIsCatchShow(b:Boolean){
        mCatchVisibility.set(if (b) View.VISIBLE else View.INVISIBLE)
    }
}