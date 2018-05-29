package com.inz.model.main

import android.content.Context
import android.databinding.ObservableField
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.howellsdk.api.ApiManager
import com.howellsdk.api.HWPlayApi
import com.howellsdk.utils.RxUtil
import com.howellsdk.utils.ThreadUtil
import com.inz.action.Config
import com.inz.action.CtrlAction
import com.inz.bean.VideoBean
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.model.ModelMgr

import com.inz.model.player.BasePlayer
import com.inz.utils.MessageHelp
import com.inz.utils.Utils
import java.util.concurrent.TimeUnit

class PlayViewModel(private var mContext:Context):BaseViewModel {
    val PLAY_SPEED         = doubleArrayOf(0.25,0.5,1.0,2.0,4.0)
    var mPlaySpeedIndex = 2
    var mIsPlayback = false
    var mIsFull = false
    val F_TIME = 1L//刷新率  s
    var mWaiteNum = 0
    var mPlayer:BasePlayer?=null
    var nowPlayState = 0//0:playAp  1:playLocal
    var mCurFrame = 0
    var mCurMsec = 0
    var mScheduledFlag = false
    var mVideoSourceArr:ArrayList<VideoBean> ?=null
    var mVideoIndex = 0

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
                    if (isSuccess) playView()

                    nowPlayState = 0
                },{
                    //deinit
                },{ //play
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
                })
                .init(Config.CAM_Crypto,Config.CAM_IP)
    }

    fun initLocalPlay(){
        mPlayer = ModelMgr.getLoaclPlayerInstance()
                .registPlayStateListener({
                    nowPlayState = 1
                },{},{
                    Log.i("123","")
                    //init seekbar
                    initInfo()
                    stopTimeTask()
                    startTimeTask(ApiManager.getInstance().localService)
                    mPlaySpeedIndex = 2
                },{
                    stopTimeTask()
                },{})
                .init(Config.CAM_Crypto,"whatever")
    }

    fun initInfo(){
        if (nowPlayState == 0)return
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
                    2->"H264C"
                    3->"H265C"
                    else->"H264"
                }
                var msec = mPlayer?.getTotalMsec()?.toLong()?:0L
                mEndTime = Utils.formatMsec(msec)
                ModelMgr.getReplayCtrlModelInstance(mContext).setTotalMsec(msec)
            }

            override fun doInUIThread() {
                Log.i("123","after sleep")
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

    fun set2LocalPos(pos:Int){
        mPlayer?.setPos(pos)
    }

    fun setCurTime(cur:String){
        ModelMgr.getReplayCtrlModelInstance(mContext).setBegTime(cur)
    }


    fun setSpeed(speed:Int){
        if (nowPlayState==0)return
        ModelMgr.getReplayCtrlModelInstance(mContext).setSpeed(String.format("%d Kbps",speed/1024))
    }

    fun setUrl(url:String){
        mPlayer?.setUrl(url)
    }

    fun setAlarm(b:Boolean){
        mPlayer?.setAlarm(b)
    }

    fun playView(){
        mPlayer?.play(Config.CAM_IS_SUB)
    }

    fun reLinkPlayView(){
        mPlayer?.rePlay()
    }

    fun pauseView(){
        mPlayer?.pause()
    }

    fun stopView(){
        mPlayer?.setPlaySpeed(1.0f)
        mPlayer?.stop()
    }

    fun change2AP(){
        if (nowPlayState==0)return
        mProcessVisibility.set(View.VISIBLE)
        Log.i("123","chande2AP")
        stopNewTask()
        stopTimeTask()
        ThreadUtil.cachedThreadStart({
            stopView()
            Thread.sleep(500)
            initApPlay()
        })
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
            Log.e("123","onDoubleTap")
            CtrlAction.setFullOrNot(mContext)
            return super.onDoubleTap(e)
        }
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }
    })

    fun onGlTouch(v:View,event:MotionEvent):Boolean{
        return mGestureDetector.onTouchEvent(event)
    }
    val mProcessVisibility      = ObservableField<Int>(View.VISIBLE)
    val mReplayCtrlVisibility   = ObservableField<Int>(View.GONE)
    val mRecordVisibility       = ObservableField<Int>(View.GONE)
    val mPlayViewFull           = ObservableField<Boolean>(false)
    val mRecordTimeText         = ObservableField<String>("00:00")

    fun setFullScreen(b: Boolean) {
        if (b){
            Log.i("123","PlayViewModel set full")
            if (mIsPlayback)mReplayCtrlVisibility.set(View.VISIBLE)
            else mReplayCtrlVisibility.set(View.GONE)
            mIsFull = true
            mPlayViewFull.set(true)
            //
        }else{
            mReplayCtrlVisibility.set(View.GONE)
            mIsFull = false
            mPlayViewFull.set(false)
        }
    }
    fun setIsPlayReview(b:Boolean){
        mIsPlayback = !b
    }

    fun setVideoSource(arr:ArrayList<VideoBean>){
        mVideoSourceArr = arr
    }

    fun setVideoPlayCurIndex(index:Int){
        mVideoIndex = index
    }


    fun onTime(speed:Int,timestamp: Long,firstTimeStamp:Long,bWait:Boolean){
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
                if (nowPlayState==1){
                    set2SeekFrame(curFrame)
                    setCurTime(curTime)
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
                    }
                    mWaiteNum = 0
                }
            }

            var speed:Int = (streamLen*8/1024/F_TIME).toInt()
            var timestamp = server.timestamp
            var firstTime = server.firstTimestamp
            onTime(speed,timestamp,firstTime,bWait)


        },0,F_TIME, TimeUnit.SECONDS)
    }

    fun stopTimeTask(){
        ThreadUtil.scheduledSingleThreadShutDown()
    }

    fun setScheduledFlag(flag:Boolean,delay:Int){
        ThreadUtil.cachedThreadStart({
            Thread.sleep(delay.toLong())
            mScheduledFlag = flag

        })
    }

    fun newTimeTask(server:HWPlayApi){
        Log.i("123","new time task")
        ThreadUtil.scheduledThreadStart({
//            Log.i("123","new  scheduled task")
            if (mScheduledFlag){


                onScheduled(server.curFrame,Utils.formatMsec(server.playedMsec.toLong()))
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
        playView()
    }

    fun onPauseClick(){
        pauseView()
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
        var len = mVideoSourceArr?.size?:0
        if( len==0)return
        if (mVideoIndex == len -1)mVideoIndex=0
        else mVideoIndex++
        var url = mVideoSourceArr!![mVideoIndex].path
        mPlayer?.stopAndPlayAnother(url)
    }

    fun onPlayLastClick(){
        var len = mVideoSourceArr?.size?:0
        if (len ==0)return
        if (mVideoIndex == 0)mVideoIndex = len -1
        else mVideoIndex--
        var url = mVideoSourceArr!![mVideoIndex].path
        mPlayer?.stopAndPlayAnother(url)

    }

}