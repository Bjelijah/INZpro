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
import com.howellsdk.utils.RxUtil
import com.howellsdk.utils.ThreadUtil
import com.inz.action.CtrlAction
import com.inz.inzpro.BaseViewModel
import com.inz.model.ModelMgr

import com.inz.model.player.ApPlayer
import com.inz.model.player.BasePlayer
import com.inz.utils.MessageHelp
import java.util.concurrent.TimeUnit

class PlayViewModel(mContext:Context):BaseViewModel {
    var mIsPlayback = false
    var mIsFull = false
    val F_TIME = 1L//刷新率  s
    var mWaiteNum = 0
    var mPlayer:BasePlayer?=null

    var mC = mContext


    override fun onCreate() {

        Log.e("123","onCreate!!!!")
        mPlayer = ModelMgr.getApPlayerInstance()
                .registPlayStateListener({
                    //init
                },{
                    //deinit
                },{ //play
                    startTimeTask()
                },{//stop
                    stopTimeTask()
                },{//catchPic
                    Toast.makeText(mC,MessageHelp.msgCatchPic(mC),Toast.LENGTH_LONG).show()
                })
                .init()
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






    private fun startTimeTask(){
        ThreadUtil.scheduledSingleThreadStart({
            var bWait = true
            var streamLen = ApiManager.getInstance().aPcamService.streamLen
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
            var timestamp = ApiManager.getInstance().aPcamService.timestamp
            var firstTime = ApiManager.getInstance().aPcamService.firstTimestamp
            onTime(speed,timestamp,firstTime,bWait)
        },0,F_TIME, TimeUnit.SECONDS)
    }

    private fun stopTimeTask(){
        ThreadUtil.scheduledSingleThreadShutDown()
    }

}