package com.inz.model.main

import android.content.Context
import android.databinding.ObservableField
import android.support.design.widget.Snackbar
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
import com.inz.inzpro.BaseViewModel
import com.inz.model.ModelMgr

import com.inz.model.player.ApPlayer
import com.inz.model.player.BasePlayer
import com.inz.utils.MessageHelp
import java.util.concurrent.TimeUnit

class PlayViewModel(private var mContext:Context):BaseViewModel {
    var mIsPlayback = false
    var mIsFull = false
    val F_TIME = 1L//刷新率  s
    var mWaiteNum = 0
    var mPlayer:BasePlayer?=null
    var nowPlayState = 0//0:playAp  1:playLocal



    override fun onCreate() {

        Log.e("123","onCreate!!!!")
        initLocalPlay()
        initApPlay() //now is ap
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
                        ModelMgr.getPlayListModelInstance(mContext).upDatePictureListState()
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
                    stopTimeTask()
                    startTimeTask(ApiManager.getInstance().localService)
                },{
                    stopTimeTask()
                },{})
                .init(Config.CAM_Crypto,"whatever")
    }

    fun setUrl(url:String){
        mPlayer?.setUrl(url)
    }

    fun playView(){
        mPlayer?.play(Config.CAM_IS_SUB)

    }

    fun stopView(){
        mPlayer?.stop()
    }

    fun change2AP(){
        if (nowPlayState==0)return
        ThreadUtil.cachedThreadStart({
            stopView()
            Thread.sleep(500)
            initApPlay()
        })

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
    val mPlayViewFull           = ObservableField<Boolean>(false)

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
        if (b){
            mReplayCtrlVisibility.set(View.GONE)
        }
    }




    fun onTime(speed:Int,timestamp: Long,firstTimeStamp:Long,bWait:Boolean){
        RxUtil.doInUIThread(object : RxUtil.RxSimpleTask<Boolean>(){
            override fun doTask() {
                if (bWait){

                    mProcessVisibility.set(View.VISIBLE)
                }else{
                    mProcessVisibility.set(View.GONE)
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

}