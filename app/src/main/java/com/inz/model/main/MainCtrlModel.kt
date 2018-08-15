package com.inz.model.main

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.databinding.ObservableField
import android.os.Build
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import com.howellsdk.utils.RxUtil
import com.inz.action.Config
import com.inz.action.CtrlAction
import com.inz.activity.view.DialogView
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.model.ModelMgr
import com.inz.model.net.PtzMgr
import com.inz.model.net.UdpMgr
import com.inz.utils.FileUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@TargetApi(Build.VERSION_CODES.M)
class MainCtrlModel(private var mContext: Context):BaseViewModel {
    var mIsRecording = false
    var mIsAlarming  = false
    var mDialog: Dialog?=null

    override fun onCreate() {
    }

    override fun onDestory() {
    }

    fun setContext(c:Context){
        mContext = c
    }

    val mRecordText           = ObservableField<String>(mContext.getString(R.string.ctrl_record))
    val mRecordTextColor      = ObservableField<Int>(mContext.getColor(R.color.black))
    val mAlarmTextColor       = ObservableField<Int>(mContext.getColor(R.color.black))
    val mCatchBtnVisibility   = ObservableField<Int>(View.GONE)
    val mRecordBtnVisibility  = ObservableField<Int>(View.VISIBLE)
    val mSavePasswordText     = ObservableField<String>(mContext.getString(R.string.ctrl_setting))
    val mShowRemoteControlBtn = ObservableField<Int>(if(Config.SHOW_REMOTE_CONTROL)View.VISIBLE else View.INVISIBLE)
    val mShowNormalCtrl       = ObservableField<Int>(View.VISIBLE)
    val mShowRemoteCtrl       = ObservableField<Int>(View.GONE)
    val mFastTextColor        = ObservableField<Int>(mContext.getColor(R.color.black))
    val mNormalTextColor      = ObservableField<Int>(mContext.getColor(R.color.black))
    val mSlowTextColor        = ObservableField<Int>(mContext.getColor(R.color.black))
    val mCruise90TextColor    = ObservableField<Int>(mContext.getColor(R.color.black))
    val mCruise180TextColor   = ObservableField<Int>(mContext.getColor(R.color.black))
    val mInfraredTextColor    = ObservableField<Int>(mContext.getColor(R.color.black))
    val mZoomInTextColor      = ObservableField<Int>(mContext.getColor(R.color.black))
    val mZoomOutTextColor     = ObservableField<Int>(mContext.getColor(R.color.black))





    val onClickCtrlAlarm       = Action {
        if (mIsAlarming){
            mIsAlarming = false
            mAlarmTextColor.set(mContext.getColor(R.color.black))
            ModelMgr.getPlayViewModelInstance(mContext).setAlarm(false)
        }else{
            mIsAlarming = true
            mAlarmTextColor.set(mContext.getColor(R.color.colorAccent))
            ModelMgr.getPlayViewModelInstance(mContext).setAlarm(true)
        }
    }

    val onClickSetting         = Action {
        Log.i("123","on click setting")
        if(ModelMgr.getMainViewModelInstance(mContext).savePasswordFun()){
            mSavePasswordText.set(mContext.getString(R.string.ctrl_setting_save))
        }else{
            mSavePasswordText.set(mContext.getString(R.string.ctrl_setting))
        }


//        if(mShowSave){
//            mDialog?.dismiss()
//            mSavePasswordText.set(mContext.getString(R.string.ctrl_setting))
//        }else{
//
//            mDialog = DialogView.generate(mContext) {
//                mLayoutId = R.layout.view_dialog_password
//                mViewModel = ModelMgr.getSettingModelInstance(mContext)
//                mTitle = mContext.getString(R.string.setting_title)
//                build()
//            }
//            mDialog?.show()
//            mSavePasswordText.set(mContext.getString(R.string.ctrl_setting_save))
//        }






    }

    val onClickCtrlRecord      = Action{
        if (CtrlAction.isPlayBack){
            Toast.makeText(mContext,mContext.getString(R.string.replay_state_error),Toast.LENGTH_SHORT).show()
            return@Action
        }

        if (mIsRecording){//现在是录像 要停止
            mIsRecording = false
            mRecordText.set(mContext.getString(R.string.ctrl_record))
            mRecordTextColor.set(mContext.getColor(R.color.black))
            ModelMgr.getPlayViewModelInstance(mContext).showRecord(false)
            ModelMgr.getDownloadMgrInstance().stepStop()
        }else{//现在没有录  要录像
            mIsRecording = true
            mRecordText.set(mContext.getString(R.string.ctrl_record_stop))
            mRecordTextColor.set(mContext.getColor(R.color.colorAccent))
            ModelMgr.getPlayViewModelInstance(mContext).showRecord(true)
            ModelMgr.getDownloadMgrInstance().stepTask()
        }

    }

    val onClickCtrlCatch       = Action {

        Observable.create(ObservableOnSubscribe<Boolean> {e->
                FileUtil.deleteLastFileIfSpaceLimit(FileUtil.FILE_PICTURE_PATH,
                        Config.FILE_DIR_PICTURE_SIZE,Config.FILE_DIR_LIMITE)
                e.onNext(true)
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    ModelMgr.getApPlayerInstance().catchPic()
                }


    }

    val onLongClickRemoteCtrl = Action {
        Log.i("123","on long click")
        Observable.timer(1,TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mShowNormalCtrl.set(View.GONE)
                    mShowRemoteCtrl.set(View.VISIBLE)
                    ModelMgr.getMainViewModelInstance(mContext).showPtzCtrl(true)
                },{it.printStackTrace() })
    }



    @SuppressLint("NewApi")
    val onClickCtrlTest        = Action{
        FileUtil.deleteLastFileIfSpaceLimit(FileUtil.FILE_VIDEO_PATH,Config.FILE_DIR_VIDEO_SIZE,Config.FILE_DIR_LIMITE)

    }

    /**
     * ptz
     */
    val onClickFast            = Action {
        resetSpeedCtrlColor()
        mFastTextColor.set(mContext.getColor(R.color.colorAccent))
        PtzMgr.getInstance().ptzSpeed(15)
    }

    val onClickNormal          = Action{
        resetSpeedCtrlColor()
        mNormalTextColor.set(mContext.getColor(R.color.colorAccent))
        PtzMgr.getInstance().ptzSpeed(10)
    }

    val onClickSlow            = Action {
        resetSpeedCtrlColor()
        mSlowTextColor.set(mContext.getColor(R.color.colorAccent))
        PtzMgr.getInstance().ptzSpeed(5)
    }

    val onClickCruise90        = Action {

        when(mCruise90TextColor.get()){
            mContext.getColor(R.color.colorAccent)->{
                resetCruiseCtrlColor()
                PtzMgr.getInstance().ptzStop()
            }
            mContext.getColor(R.color.black)->{
                resetCruiseCtrlColor()
                mCruise90TextColor.set(mContext.getColor(R.color.colorAccent))
                PtzMgr.getInstance().ptzCruise90()
            }
        }
    }

    val onClickCruise180       = Action {
        when (mCruise180TextColor.get()){
            mContext.getColor(R.color.colorAccent)->{
                resetCruiseCtrlColor()
                //stop
                PtzMgr.getInstance().ptzStop()
            }
            mContext.getColor(R.color.black)->{
                resetCruiseCtrlColor()
                mCruise180TextColor.set(mContext.getColor(R.color.colorAccent))
                //do
                PtzMgr.getInstance().ptzCruise180()
            }
        }
    }

    val onClickInfrared        = Action {
        when (mInfraredTextColor.get()){
            mContext.getColor(R.color.colorAccent)->{//
                //close
                mInfraredTextColor.set(mContext.getColor(R.color.black))
                PtzMgr.getInstance().ptzLrisClose()
            }
            mContext.getColor(R.color.black)->{
                //open
                mInfraredTextColor.set(mContext.getColor(R.color.colorAccent))
                PtzMgr.getInstance().ptzLrisOpen()
            }
        }



    }

    val onClickZoomIn          = Action {

    }
    val onClickZoomOut         = Action {

    }
    val onTouchZoomInDown      = Action {
        resetZoomCtrlColor()
        mZoomInTextColor.set(mContext.getColor(R.color.colorAccent))
//        Log.i("123","on zoom in down")

        PtzMgr.getInstance().ptzZoomIn()
    }

    val onTouchZoomInUp        = Action {
        Log.i("123","on zoom in up")
        PtzMgr.getInstance().ptzZoomStop()
    }

    val onTouchZoomOutDown     = Action {
        resetZoomCtrlColor()
        mZoomOutTextColor.set(mContext.getColor(R.color.colorAccent))
        Log.i("123","on zoom out down")
        PtzMgr.getInstance().ptzZoomOut()
    }

    val onTouchZoomOutUp       = Action {
        Log.i("123","on zoom out up")
        PtzMgr.getInstance().ptzZoomStop()
    }



    fun stopRecording(){

        if (mIsRecording){
            mIsRecording = false
            mRecordText.set(mContext.getString(R.string.ctrl_record))
            mRecordTextColor.set(mContext.getColor(R.color.black))
            ModelMgr.getPlayViewModelInstance(mContext).showRecord(false)
            ModelMgr.getDownloadMgrInstance().stepStop()

        }
    }

    fun setIsPlayReView(b:Boolean){
//        mCatchBtnVisibility.set(if(b)View.INVISIBLE else View.VISIBLE)
    }
    fun setIsShowCatch(b:Boolean){
        mCatchBtnVisibility.set(if(b)View.VISIBLE else View.GONE)
    }
    fun setIsShowRecord(b:Boolean){
        mRecordBtnVisibility.set(if(b)View.VISIBLE else View.GONE)
    }
    fun dismissDialog(){
        mDialog?.dismiss()
    }

    fun resetSpeedCtrlColor(){
        mFastTextColor.set(mContext.getColor(R.color.black))
        mNormalTextColor.set(mContext.getColor(R.color.black))
        mSlowTextColor.set(mContext.getColor(R.color.black))
    }

    fun resetCruiseCtrlColor(){
        mCruise90TextColor.set(mContext.getColor(R.color.black))
        mCruise180TextColor.set(mContext.getColor(R.color.black))
    }

    fun resetZoomCtrlColor(){
        mZoomInTextColor.set(mContext.getColor(R.color.black))
        mZoomOutTextColor.set(mContext.getColor(R.color.black))
    }


}