package com.inz.model.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.databinding.ObservableField
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.howell.jni.JniUtil
import com.howellsdk.api.ApiManager
import com.howellsdk.utils.RxUtil
import com.inz.action.Config
import com.inz.action.CtrlAction
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.model.ModelMgr
import com.inz.model.net.UdpMgr
import com.inz.utils.*
import io.reactivex.functions.Action


class MainViewModel(private var mContext:Context) : BaseViewModel{
    val UDP_CMD_NONE       = 0x00
    val UDP_CMD_CHANGE_PWD = 0x01
    val UDP_CMD_REBOOT     = 0x02

    var mIsPlayBack = false
    var mIsRecording = false
    var mUdpMgr = UdpMgr.getInstance()
    var mNewPassword:String  ?=null
    var mUdpCmdType = UDP_CMD_NONE
    var mEnable               = true

    fun setContext(c:Context){
        mContext = c
    }

     fun setFullScreen(b: Boolean,isShowReplayCtrl:Boolean) {
        if(!mEnable)return
        if(b){
            Log.i("123","main view model set full")
//            mCtrlVisibility.set(View.GONE)//主控按钮
            mCtrlVisibilityShow.set(false)
            mViewVisibilityShow.set(true)
//            mReplayListVisibility.set(View.GONE)//列表

            mReplayCtrlVisibility.set(View.GONE)
//            mPlayViewWidth.set(ViewGroup.LayoutParams.MATCH_PARENT)
//            mPlayViewHeight.set(ViewGroup.LayoutParams.MATCH_PARENT)
        }else{
            Log.i("123","main view model set small  isShow=$isShowReplayCtrl")
//            mReplayListVisibility.set(View.VISIBLE)
//            mCtrlVisibility.set(View.VISIBLE)
            mCtrlVisibilityShow.set(true)
            mViewVisibilityShow.set(false)
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

    fun showPtzCtrl(b:Boolean){
        mPtzVisiblilty.set(if(b)View.VISIBLE else View.GONE)
    }


    fun savePasswordFun():Boolean{//true 显示view   false隐藏view 保存
        if(mSavePasswordShow.get()==View.GONE){
            //get info
            mSavePasswordOriginal.set(SpConifgUtil.getWIFIPwd(mContext))
            mSavePasswordShow.set(View.VISIBLE)
            return true
        }else{
            mSavePasswordShow.set(View.GONE)
            //save info
            var newStr = mSavePasswordNew.get()!!
            var confirmStr = mSavePasswordConfirm.get()!!
            Log.i("547","new=$newStr   confirm=$confirmStr ")
            if (newStr.isNotEmpty()&& confirmStr.isNotEmpty() && newStr.equals(confirmStr) && newStr.length>=8){
                Log.i("123","we save")
                mNewPassword = newStr
                //send
                RxUtil.doInIOTthread(object :RxUtil.RxSimpleTask<Void>(){
                    override fun doTask() {
                        mUdpCmdType = UDP_CMD_CHANGE_PWD
                        sendUdpMsg(UDPCmdHelper.setPassword(newStr))
                    }
                })

            }else{
                Toast.makeText(mContext,mContext.getString(R.string.password_error),Toast.LENGTH_LONG).show()
                mNewPassword = null
            }



            return false
        }
    }

    fun sendUdpMsg(msg:ByteArray){
        when(mUdpCmdType){
            UDP_CMD_CHANGE_PWD->{
               mUdpMgr.sendMsg(msg,Config.CAM_GATEWAY,6234,{_,_->//onRes
                   mUdpCmdType = UDP_CMD_REBOOT
                   sendUdpMsg(UDPCmdHelper.reboot())
               },{//onTimeout
                   Toast.makeText(mContext,mContext.getString(R.string.password_error),Toast.LENGTH_LONG).show()
                   mNewPassword=null
               },{//onError
                   Toast.makeText(mContext,mContext.getString(R.string.password_error),Toast.LENGTH_LONG).show()
                   mNewPassword=null
               })
            }
            UDP_CMD_REBOOT->{
                mUdpMgr.sendMsg(msg,Config.CAM_GATEWAY,6234,{_,_->//onRes
                    mUdpCmdType = UDP_CMD_NONE
                    if(mNewPassword!=null) SpConifgUtil.setPassword(mContext,mNewPassword!!)
                    Toast.makeText(mContext,mContext.getString(R.string.password_save_ok),Toast.LENGTH_LONG).show()
                },{//onTimeout
                    mUdpCmdType = UDP_CMD_NONE
                    if(mNewPassword!=null) SpConifgUtil.setPassword(mContext,mNewPassword!!)
                    Toast.makeText(mContext,mContext.getString(R.string.password_save_ok),Toast.LENGTH_LONG).show()
                },{//onError
                    Toast.makeText(mContext,mContext.getString(R.string.password_error),Toast.LENGTH_LONG).show()
                    mNewPassword=null
                })

            }
            UDP_CMD_NONE->{}
            else->{}
        }
    }

    fun setFullEnable(b:Boolean){
        if(!b){
            setFullScreen(false,false)
        }
        mEnable = b
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
    val mPtzVisiblilty        = ObservableField<Int>(View.GONE)
    val mRecordText           = ObservableField<String>(mContext.getString(R.string.ctrl_record))


    val mCtrlVisibilityShow   = ObservableField<Boolean>(true)

    val mViewVisibilityShow   = ObservableField<Boolean>(false)
    val mSavePasswordShow     = ObservableField<Int>(View.GONE)
    val mSavePasswordOriginal = ObservableField<String>("")
    val mSavePasswordNew      = ObservableField<String>("")
    val mSavePasswordConfirm  = ObservableField<String>("")



}