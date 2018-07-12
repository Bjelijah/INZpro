package com.inz.model.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.databinding.ObservableField
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.howell.jni.JniUtil
import com.howellsdk.api.ApiManager
import com.inz.action.CtrlAction
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.model.ModelMgr
import com.inz.utils.DebugLog
import com.inz.utils.FileUtil
import com.inz.utils.NetUtil
import io.reactivex.functions.Action


class MainViewModel(private var mContext:Context) : BaseViewModel{
    var mIsPlayBack = false
    var mIsRecording = false
    fun setContext(c:Context){
        mContext = c
    }

     fun setFullScreen(b: Boolean,isShowReplayCtrl:Boolean) {
        if(b){
            Log.i("123","main view model set full")
            mCtrlVisibility.set(View.GONE)//主控按钮
            mReplayListVisibility.set(View.GONE)//列表
            mReplayCtrlVisibility.set(View.GONE)
//            mPlayViewWidth.set(ViewGroup.LayoutParams.MATCH_PARENT)
//            mPlayViewHeight.set(ViewGroup.LayoutParams.MATCH_PARENT)
        }else{
            Log.i("123","main view model set small  isShow=$isShowReplayCtrl")
            mReplayListVisibility.set(View.VISIBLE)
            mCtrlVisibility.set(View.VISIBLE)
            mReplayCtrlVisibility.set(if (isShowReplayCtrl)View.VISIBLE else View.GONE)
//            mPlayViewWidth.set(0)
//            mPlayViewHeight.set(ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    fun setIsPlayReview(b:Boolean){
        mIsPlayBack = !b

    }

    fun showReplayCtrl(b:Boolean){
        mReplayCtrlVisibility.set(if (b)View.VISIBLE else View.GONE)
    }

    override fun onCreate() {
        //do jni
        FileUtil.initFileDir(mContext)
        ApiManager.getInstance().setJNILogEnable(true)
        //permission



    }

    override fun onDestory() {
    }
//    val mPlayViewWidth        = ObservableField<Int>(0)
//    val mPlayViewHeight       = ObservableField<Int>(ViewGroup.LayoutParams.WRAP_CONTENT)
    val mPlayViewFull         = ObservableField<Boolean>(false)

    val mCtrlVisibility       = ObservableField<Int>(View.VISIBLE)
    val mReplayListVisibility = ObservableField<Int>(View.VISIBLE)
    val mReplayCtrlVisibility = ObservableField<Int>(View.GONE)
    val mRecordText           = ObservableField<String>(mContext.getString(R.string.ctrl_record))









}