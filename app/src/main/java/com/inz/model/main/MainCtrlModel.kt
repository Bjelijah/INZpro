package com.inz.model.main

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.databinding.ObservableField
import android.os.Build
import android.os.storage.StorageManager
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.View
import android.widget.Toast
import com.inz.action.Config
import com.inz.action.CtrlAction
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.model.ModelMgr
import com.inz.utils.FileUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.io.File

@TargetApi(Build.VERSION_CODES.M)
class MainCtrlModel(private var mContext: Context):BaseViewModel {
    var mIsRecording = false
    var mIsAlarming  = false
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
    val mCatchBtnVisibility    = ObservableField<Int>(View.INVISIBLE)
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

    @SuppressLint("NewApi")
    val onClickCtrlTest        = Action{
        FileUtil.deleteLastFileIfSpaceLimit(FileUtil.FILE_VIDEO_PATH,Config.FILE_DIR_VIDEO_SIZE,Config.FILE_DIR_LIMITE)

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
        mCatchBtnVisibility.set(if(b)View.VISIBLE else View.INVISIBLE)
    }

}