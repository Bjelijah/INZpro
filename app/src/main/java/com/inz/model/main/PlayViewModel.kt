package com.inz.model.main

import android.content.Context
import android.databinding.ObservableField
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Toast
import com.howellsdk.api.ApiManager
import com.howellsdk.api.HWPlayApi
import com.howellsdk.utils.RxUtil
import com.howellsdk.utils.ThreadUtil
import com.inz.action.Config
import com.inz.action.CtrlAction
import com.inz.activity.view.PlayGLTextureView
import com.inz.bean.BaseBean
import com.inz.bean.RemoteBean
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.model.ModelMgr

import com.inz.model.player.BasePlayer
import com.inz.utils.FileUtil
import com.inz.utils.MessageHelp
import com.inz.utils.Utils
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class PlayViewModel(private var mContext:Context):BaseViewModel {
    val PLAY_SPEED         = doubleArrayOf(0.25,0.5,1.0,2.0,4.0)
    var mPlaySpeedIndex = 2
    var mIsPlayback = false
    var mIsFull = false
    val F_TIME = 1L//刷新率  s
    var mWaiteNum = 0
    var mPlayer:BasePlayer?=null
    var nowPlayState = 0//0:playAp  1:playLocal  2:playRemote
    var mCurFrame = 0
    var mCurMsec = 0
    var mScheduledFlag = false
    var mVideoSourceArr:ArrayList<BaseBean> ?=null
    var mVideoIndex = 0

    var mRemoteBeg=""
    var mRemoteEnd=""
    var mRemoteBegTime = 0L
    var mRemoteOffset = 0
    override fun onCreate() {

        Log.e("123","onCreate!!!!")
        initLocalPlay()
        initApPlay() //now is ap
        mPlaySpeedIndex = 2
    }
    fun setContext(c:Context){
        mContext = c
    }
    fun initApPlay(){

        mPlayer = ModelMgr.getApPlayerInstance()
                .registPlayStateListener({ isSuccess->//init
                    Log.i("123","init ap  init = $isSuccess")
                    if (isSuccess) playView()
                    nowPlayState = CtrlAction.PLAY_MODE_SUB_VIEW
                },{
                    //deinit
                },{ //play

                    nowPlayState = CtrlAction.PLAY_MODE_SUB_VIEW
                    initInfoAp()
                    stopTimeTask()
                    startTimeTask(ApiManager.getInstance().aPcamService)
                },{//stop
                    stopTimeTask()
                },{b->//catchPic
                    if (b)  {
                        ModelMgr.getPlayListModelInstance(mContext).updatePictureListState()
                        Toast.makeText(mContext,MessageHelp.msgCatchPic(mContext),Toast.LENGTH_LONG).show()
                    }
                    else  Toast.makeText(mContext,MessageHelp.msgCatchError(mContext),Toast.LENGTH_LONG).show()
                },{files->//todo 远程回放列表
                    mVideoSourceArr = files
                    mVideoIndex = 0
                    ModelMgr.getPlayListModelInstance(mContext).onUpDateRemoteListState(files as ArrayList<RemoteBean>)
                })
                .init(Config.CAM_Crypto,Config.CAM_IP)
    }

    fun initRemotePlay(beg:String,end:String){

        mPlayer = ModelMgr.getApPlayerInstance()
                .registPlayStateListener({//init
                    if(it)playBack(beg,end)
                    nowPlayState = CtrlAction.PLAY_MODE_REMOTE_REPLAY
                    mRemoteBeg = beg
                    mRemoteEnd = end
                },{//deinit
                },{//play
                    nowPlayState = CtrlAction.PLAY_MODE_REMOTE_REPLAY
                    Log.i("123","play ok")
                    initInfoRemote(beg,end)
                    stopTimeTask()
                    startTimeTask(ApiManager.getInstance().aPcamService)
                    mPlaySpeedIndex = 2
                },{//stop
                    stopNewTask()
                    stopTimeTask()
                },{b->//catchPicture
                    if (b)  {
                        ModelMgr.getPlayListModelInstance(mContext).updatePictureListState()
                        Toast.makeText(mContext,MessageHelp.msgCatchPic(mContext),Toast.LENGTH_LONG).show()
                    }
                    else  Toast.makeText(mContext,MessageHelp.msgCatchError(mContext),Toast.LENGTH_LONG).show()
                },{files->//search record
                    mVideoSourceArr = files
                    mVideoIndex = 0
                    ModelMgr.getPlayListModelInstance(mContext).onUpDateRemoteListState(files as ArrayList<RemoteBean>)
                })
                .init(Config.CAM_Crypto,Config.CAM_IP)
    }

    fun initLocalPlay(){


        mPlayer = ModelMgr.getLoaclPlayerInstance()
                .registPlayStateListener({
                    nowPlayState = CtrlAction.PLAY_MODE_LOCAL_REPLAY
                },{},{
                    //init seekbar
                    nowPlayState = CtrlAction.PLAY_MODE_LOCAL_REPLAY
                    initInfoLocal()
                    stopTimeTask()
                    startTimeTask(ApiManager.getInstance().localService)
                    mPlaySpeedIndex = 2
                },{
                    stopNewTask()
                    stopTimeTask()
                },{},{})
                .init(Config.CAM_Crypto,"whatever")
    }

    fun initInfoRemote(beg:String,end:String){
        if (nowPlayState != CtrlAction.PLAY_MODE_REMOTE_REPLAY)return
        ModelMgr.getMainViewModelInstance(mContext).showReplayCtrl(true)
        showReplayCtrl(false)
        RxUtil.doRxTask(object :RxUtil.CommonTask<Void>(){
            var mTotaltime = 0L

            var mName = ""
            var mEndTime = ""
            override fun doInIOThread() {
                var smf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                var begDate = smf.parse(beg)
                var endDate = smf.parse(end)
                mRemoteBegTime = begDate.time
                mTotaltime = endDate.time - begDate.time
                Log.i("123","beg=$beg,$begDate,${begDate.time}  end=$end,$endDate,${endDate.time}                time len = $mTotaltime")
                mName = when(Config.CAM_Crypto){
                    0->"H264"
                    1->"H265"
                    2->"H264 C"
                    3->"H265 C"
                    else->"H264"
                }
                mEndTime = Utils.formatMsec(mTotaltime)
                mRemoteOffset = 0
            }

            override fun doInUIThread() {
                Log.i("123","  total=$mTotaltime     set max totaltime=${mTotaltime.toInt()}")

                ModelMgr.getReplayCtrlModelInstance(mContext).initUi()
                ModelMgr.getReplayCtrlModelInstance(mContext).setTotalMsec(mTotaltime)
                ModelMgr.getReplayCtrlModelInstance(mContext).setSBMax(mTotaltime.toInt())
                ModelMgr.getReplayCtrlModelInstance(mContext).setName(mName)
                ModelMgr.getReplayCtrlModelInstance(mContext).setEndTime(mEndTime)
                stopNewTask()
                setScheduledFlag(true,0)
                newTimeTask(ApiManager.getInstance().aPcamService)
            }
        })
    }

    fun initInfoAp(){
        ModelMgr.getMainViewModelInstance(mContext).showReplayCtrl(false)
        showReplayCtrl(false)
    }

    fun initInfoLocal(){
        if (nowPlayState != CtrlAction.PLAY_MODE_LOCAL_REPLAY)return
        ModelMgr.getMainViewModelInstance(mContext).showReplayCtrl(false)
        showReplayCtrl(true)
        RxUtil.doRxTask(object :RxUtil.CommonTask<Long>(1000){

            var mTotalFrame =0
            var mName = ""
            var mEndTime = ""
            override fun doInIOThread() {
                Log.i("123","~~~~~~~~~start sleep")
                Thread.sleep(t)
                mTotalFrame = mPlayer?.getTotalFrame()?:100
                mName = when(Config.CAM_Crypto){
                    0->"H264"
                    1->"H265"
                    2->"H264 C"
                    3->"H265 C"
                    else->"H264"
                }
                var msec = mPlayer?.getTotalMsec()?.toLong()?:0L
                mEndTime = Utils.formatMsec(msec)
                ModelMgr.getReplayCtrlModelInstance(mContext).setTotalMsec(msec)
            }

            override fun doInUIThread() {
                Log.i("123","after sleep")

                ModelMgr.getReplayCtrlModelInstance(mContext).initUi()
                ModelMgr.getReplayCtrlModelInstance(mContext).setSBMax(mTotalFrame)
                ModelMgr.getReplayCtrlModelInstance(mContext).setName(mName)
                ModelMgr.getReplayCtrlModelInstance(mContext).setEndTime(mEndTime)
                stopNewTask()
                setScheduledFlag(true,0)
                newTimeTask(ApiManager.getInstance().localService)
            }

        })
    }

    fun set2SeekFrame(cur:Int){
        ModelMgr.getReplayCtrlModelInstance(mContext).setSBProgress(cur)
    }

    fun set2LocalFrame(cur:Int){
        mPlayer?.setCurFrame(cur)
    }

    fun setCurTime(cur:String){
        ModelMgr.getReplayCtrlModelInstance(mContext).setBegTime(cur)
    }

    fun set2LocalPos(pos:Int,progress:Int){

        RxUtil.doInIOTthread(object :RxUtil.RxSimpleTask<Void>(){
            override fun doTask() {
                if(nowPlayState==CtrlAction.PLAY_MODE_LOCAL_REPLAY) {
                    mPlayer?.setPos(pos)
                }else if(nowPlayState == CtrlAction.PLAY_MODE_REMOTE_REPLAY){
                    Log.i("123","set 2 LocalPos=$progress")
                    mRemoteOffset = progress
                    var setBegDate = Date(mRemoteBegTime + mRemoteOffset)
                    var smf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    var str = smf.format(setBegDate)
                    Log.i("123","str = $str")
                    ApiManager.getInstance().aPcamService.reLink(Config.CAM_IS_SUB,str,mRemoteEnd)
                }
            }
        })
    }



    fun setSpeed(speed:Long){
//        if (nowPlayState==0)return

//        ModelMgr.getReplayCtrlModelInstance(mContext).setSpeed(String.format("%d Kbps",speed/1024))
        ModelMgr.getReplayCtrlModelInstance(mContext).setSpeed(FileUtil.fmtSpeed(speed))
    }

    fun setUrl(url:String){
        mPlayer?.setUrl(url)
    }

    fun setAlarm(b:Boolean){
        mPlayer?.setAlarm(b)
    }

    fun playView(){
        mPlayer?.play(true)//play view sub
    }

    fun playBack(beg:String,end:String){
        mPlayer?.playback(false,beg,end)// remote main
    }

    fun reLinkPlayView(){
        mPlayer?.rePlay()
    }

    fun reLinkRemoteView(){
        var offset = ApiManager.getInstance().aPcamService.timestamp - ApiManager.getInstance().aPcamService.firstTimestamp+mRemoteOffset
        set2LocalPos(0,offset.toInt())
    }

    fun pauseView(){
        mPlayer?.pause()
    }

    fun remotePause(){
        if (mPlayer?.isPause()==false){
            mPlayer?.pause()
        }else{
            mPlayer?.pause()
            reLinkRemoteView()
        }
    }



    fun pause(){
        mPlayer?.pause()

    }

    fun stopView(){
        mPlayer?.setPlaySpeed(1.0f)
        mPlayer?.stop()
    }

    fun change2AP(){
        if (nowPlayState==CtrlAction.PLAY_MODE_SUB_VIEW){Log.e("123","change 2 ap now is playview state=0 return");return}
        mProcessVisibility.set(View.VISIBLE)
        Log.i("123","chande2AP")
        stopNewTask()
        stopTimeTask()
        ThreadUtil.cachedThreadStart {
            stopView()
            Thread.sleep(500)
            initApPlay()
        }
    }

    fun showRecord(b:Boolean){
        if (b){
            mRecordVisibility.set(View.VISIBLE)
        }else{
            mRecordVisibility.set(View.GONE)
        }
    }

    fun showRecordText(num:Int){
        mRecordTimeText.set(String.format("%02d:%02d",num/60,num%60))
    }

    override fun onDestory() {

        mPlayer?.unregistPlayStateListener()
        mPlayer?.stop()

    }

    val mGestureDetector = GestureDetector(mContext,object :GestureDetector.SimpleOnGestureListener(){
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            Log.i("123","onSingleTapUp")
            if (mIsPlayback && mIsFull){
                if(mReplayCtrlVisibility.get()==View.GONE){
                    mReplayCtrlVisibility.set(View.VISIBLE)
                }else{
                    mReplayCtrlVisibility.set(View.GONE)
                }

            }
            return super.onSingleTapUp(e)
        }
        override fun onDoubleTap(e: MotionEvent?): Boolean {
            Log.e("123","onDoubleTap        nowPlayState=$nowPlayState")
//            CtrlAction.setFullOrNot(mContext)
            CtrlAction.setViewFullOrNot(mContext,nowPlayState)

//            var scal = ScaleAnimation(1.0f,4.0f / 3.0f,1.0f,10f/9f,Animation.RELATIVE_TO_SELF,0f,
//                    Animation.RELATIVE_TO_SELF,0f)
//            scal.duration = 100
//            scal.fillAfter = true
//            mv?.startAnimation(scal)
            return super.onDoubleTap(e)
        }
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }
    })
    var mv:View?=null
    fun onGlTouch(v:View,event:MotionEvent):Boolean{
        mv = v
        return mGestureDetector.onTouchEvent(event)
    }
    val mProcessVisibility      = ObservableField<Int>(View.VISIBLE)
    val mReplayCtrlVisibility   = ObservableField<Int>(View.GONE)//视频内控制条
    val mRecordVisibility       = ObservableField<Int>(View.GONE)
    val mPlayViewFull           = ObservableField<Boolean>(false)
    val mRecordTimeText         = ObservableField<String>("00:00")

    fun showReplayCtrl(b:Boolean){
        mReplayCtrlVisibility.set(if(b) View.VISIBLE else View.GONE)
    }


    fun setFullScreen(b: Boolean,isShowReplayCtrl :Boolean) {
        if (b){
            Log.i("123","PlayViewModel set full")
            mReplayCtrlVisibility.set(if (isShowReplayCtrl)View.VISIBLE else View.GONE)
            mIsFull = true
            mPlayViewFull.set(true)
            //
        }else{
            mReplayCtrlVisibility.set(if (isShowReplayCtrl)View.VISIBLE else View.GONE)
            mIsFull = false
            mPlayViewFull.set(false)
        }
    }
    fun setIsPlayReview(b:Boolean){
        mIsPlayback = !b
    }

    fun setVideoSource(arr:ArrayList<BaseBean>){
        mVideoSourceArr = arr
    }

    fun setVideoPlayCurIndex(index:Int){
        mVideoIndex = index
    }

    fun setRemotePlayCurIndex(index:Int){
        mVideoIndex = index
    }

    fun onTime(speed:Long,bWait:Boolean){
        RxUtil.doInUIThread(object : RxUtil.RxSimpleTask<Boolean>(){
            override fun doTask() {
                if (bWait){
                    if (!mIsPlayback) {
                        mProcessVisibility.set(View.VISIBLE)
                        //todo we need relink

                    }
                }else{
                    mProcessVisibility.set(View.GONE)
                }
                //set speed
                setSpeed(speed)
            }

        })
    }

    fun onScheduled(curFrame:Int,curTime:String){
        RxUtil.doInUIThread(object :RxUtil.RxSimpleTask<Boolean>(){
            override fun doTask() {
                if (nowPlayState==CtrlAction.PLAY_MODE_LOCAL_REPLAY){
                    set2SeekFrame(curFrame)
                    setCurTime(curTime)
                }
            }
        })
    }
    fun onScheduled(firstTimeStamp: Long,timestamp: Long){
        var tPos = (timestamp - firstTimeStamp)
//        Log.i("123","tPos=$tPos     timestamp=$timestamp    firstTimeStamp=$firstTimeStamp")
        if (tPos<0) tPos = 0
        if (tPos>ModelMgr.getReplayCtrlModelInstance(mContext).getProcessMax()) tPos = 0
        RxUtil.doInUIThread(object :RxUtil.RxSimpleTask<Boolean>(){
            override fun doTask() {
                if(nowPlayState==CtrlAction.PLAY_MODE_REMOTE_REPLAY) {
                    var p = mRemoteOffset+tPos
                    set2SeekFrame(p.toInt())
                    setCurTime(Utils.formatMsec(p))
                }
            }
        })

    }




    fun startTimeTask(server: HWPlayApi){
        ThreadUtil.scheduledSingleThreadStart({
            var bWait = true
            var streamLen = server.streamLen
            if(streamLen!=0){
                bWait = false
                mWaiteNum = 0
            }else{
                mWaiteNum++
                if (mWaiteNum==3){
                    bWait = true
                    if (!mIsPlayback) {
                        reLinkPlayView()
                    }else if(nowPlayState==CtrlAction.PLAY_MODE_REMOTE_REPLAY && mPlayer?.isPause()==false){
                        reLinkRemoteView()
                    }
                    mWaiteNum = 0
                }
            }

//            var speed:Int = (streamLen*8/1024/F_TIME).toInt()
            var speed = (streamLen*8/F_TIME)

            onTime(speed,bWait)


        },0,F_TIME, TimeUnit.SECONDS)
    }

    fun stopTimeTask(){
        ThreadUtil.scheduledSingleThreadShutDown()
    }

    fun setScheduledFlag(flag:Boolean,delay:Int){
        ThreadUtil.cachedThreadStart {
            Thread.sleep(delay.toLong())
            mScheduledFlag = flag
        }
    }

    fun newTimeTask(server:HWPlayApi){
        Log.i("123","new time task")
        ThreadUtil.scheduledThreadStart({
            //            Log.i("123","new  scheduled task")
            if (mScheduledFlag){
                when (nowPlayState){
                    CtrlAction.PLAY_MODE_LOCAL_REPLAY -> onScheduled(server.curFrame, Utils.formatMsec(server.playedMsec.toLong()))
                    CtrlAction.PLAY_MODE_REMOTE_REPLAY-> onScheduled(server.firstTimestamp,server.timestamp)
                }
            }
        },
                0,200,TimeUnit.MILLISECONDS)
    }

    fun stopNewTask(){
        ThreadUtil.scheduledThreadShutDown()
    }


    fun onStopClick(){

        stopTimeTask()
        stopNewTask()
        stopView()

    }

    fun onPlayClick(){
        if(nowPlayState!=CtrlAction.PLAY_MODE_REMOTE_REPLAY) {
            playView()
        }else{
            playBack(mRemoteBeg,mRemoteEnd)
        }
    }

    fun onPauseClick(){
        if (nowPlayState!=CtrlAction.PLAY_MODE_REMOTE_REPLAY) {
            pauseView()
        }else{
            remotePause()
        }
    }

    fun onSlowClick(){
        if (mPlaySpeedIndex > 0){
            mPlaySpeedIndex--
        }
        var speed  = PLAY_SPEED[mPlaySpeedIndex]
        mPlayer?.setPlaySpeed(speed.toFloat())
        Toast.makeText(mContext,"${mContext.getString(R.string.play_speed)} $speed",Toast.LENGTH_SHORT).show()
    }

    fun onFastClick(){
        if (mPlaySpeedIndex < PLAY_SPEED.size-1){
            mPlaySpeedIndex++
        }
        var speed = PLAY_SPEED[mPlaySpeedIndex]
        mPlayer?.setPlaySpeed(speed.toFloat())
        Toast.makeText(mContext,"${mContext.getString(R.string.play_speed)} $speed",Toast.LENGTH_SHORT).show()
    }



    fun onPlayNextClick(){
        if(mPlayer?.isPause()==false)mPlayer?.pause()
        if(mPlayer?.stepNext()==false){
            Toast.makeText(mContext,mContext.getString(R.string.step_next_error),Toast.LENGTH_SHORT).show()
        }
    }

    fun onPlayLastClick(){
        if (mPlayer?.isPause()==false)mPlayer?.pause()
        if(mPlayer?.stepLast()==false){
            Toast.makeText(mContext,mContext.getString(R.string.step_last_error),Toast.LENGTH_SHORT).show()
        }
    }

}