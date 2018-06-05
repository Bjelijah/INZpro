package com.inz.model.player

import android.media.RingtoneManager
import android.util.Log
import com.howell.jni.JniUtil
import com.howellsdk.api.ApiManager
import com.howellsdk.api.HWPlayApi
import com.howellsdk.player.ap.bean.ReplayFile
import com.inz.action.Config
import com.inz.bean.AlarmBean
import com.inz.utils.FileUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.io.File
import java.util.ArrayList
import android.media.RingtoneManager.getRingtone
import android.media.RingtoneManager.TYPE_NOTIFICATION
import android.media.RingtoneManager.getDefaultUri
import android.widget.Toast
import com.howellsdk.utils.RxUtil
import com.inz.bean.BaseBean
import com.inz.bean.RemoteBean
import com.inz.inzpro.R
import com.inz.model.ModelMgr
import kotlinx.android.synthetic.main.fragment_play_view.view.*
import java.util.concurrent.TimeUnit


class ApPlayer :BasePlayer(){
    var mIsAlarmEnable = false
    var mIsSub = false
    @Volatile private var mIsReLinking   = false
    override fun pause(): BasePlayer {
        ApiManager.getInstance().aPcamService
                .playPause()
        return this
    }

    override fun isPause(): Boolean = ApiManager.getInstance().aPcamService.isPause

    override fun setAlarm(b: Boolean) {
        mIsAlarmEnable = b
        if (b) {
            JniUtil.netRegistAlarm()
        }else{
            JniUtil.netUnregistAlarm()
        }

    }

    override fun init(crypto:Int,uri:String): BasePlayer {
        Observable.create(ObservableOnSubscribe<Boolean> { e->
           ApiManager.getInstance()
                    .getAPcamService(
                            Config.CAM_IP,
                            Config.CAM_SLOT,
                            Config.CAM_Crypto,
                            object :HWPlayApi.IAPCamCB{
                                override fun onRecordFileList(files: ArrayList<ReplayFile>?) {
                                    val l = ArrayList<BaseBean>()
//                                    Log.i("123","files=$files")
                                    files?.forEach {it ->
                                        var begStr = String.format("%04d-%02d-%02d %02d:%02d:%02d",
                                                it.begYear,it.begMonth,it.begDay,it.begHour,it.begMin,it.begSec)

                                        var endStr = String.format("%04d-%02d-%02d %02d:%02d:%02d",
                                                it.endYear,it.endMonth,it.endDay,it.endHour,it.endMin,it.endSec)
                                        var name = begStr
                                        l.add(RemoteBean(name,begStr,endStr))
                                    }
                                    sendRecordFileListResult(l)
                                }

                                override fun onAlarm(type: Int, msg: String?) {
                                    if (type==8)return //fixme  心跳
                                    Log.i("123","onAlarm  type=$type  msg=$msg")
                                    var json = JSONObject(msg)
                                    Log.i("123","${AlarmBean(msg?:"").toString()}")
                                    val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                                    val r = RingtoneManager.getRingtone(ModelMgr.mContext, notification)
                                    r.play()

                                    Observable.timer(1,TimeUnit.SECONDS)
                                            .subscribe({r.stop()})
                                    RxUtil.doInUIThread(object :RxUtil.RxSimpleTask<Void>(){
                                        override fun doTask() {
                                            Toast.makeText(ModelMgr.mContext,ModelMgr.mContext?.getString(R.string.alarm_msg_text)?:"收到报警！",Toast.LENGTH_SHORT).show()
                                        }
                                    })


                                }

                            }
                    ).bindCam()
            e.onNext(true)
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ isSuccess->
                    sendInitResult(isSuccess)

                },{e-> e.printStackTrace()
                })
        return this
    }

    override fun deinit(): BasePlayer {
        Observable.create(ObservableOnSubscribe<Boolean> { e->
            ApiManager.getInstance().aPcamService.unBindCam()
            e.onNext(true)
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({b->
                    sendDeinitResult(b)
                },{e->e.printStackTrace()})
        return this
    }

    override fun play(isSub:Boolean): BasePlayer {
        Observable.create(ObservableOnSubscribe<Boolean> { e->
            var ret = ApiManager.getInstance().aPcamService.connect()
            e.onNext(ret)
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({b->
                    //todo
                    //校时&报警
                    JniUtil.netSetSystemTimeNow()
                    if (mIsAlarmEnable)JniUtil.netRegistAlarm()
                    mIsSub = isSub
                    ApiManager.getInstance().aPcamService.play(isSub)
                    sendPlayResult(b)
                },{e->e.printStackTrace()})
        return this
    }

    override fun playback(isSub: Boolean,beg:String,end:String): BasePlayer {
        Observable.create(ObservableOnSubscribe<Boolean> {
            it.onNext(ApiManager.getInstance().aPcamService.connect())
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mIsSub = isSub
                    ApiManager.getInstance().aPcamService.playback(isSub,beg,end)
                    sendPlayResult(it)
                },{e->e.printStackTrace()})
        return this
    }

    override fun rePlay(): BasePlayer {
        if (mIsReLinking){Log.i("123","isRelinging  return");return this}
        Observable.create(ObservableOnSubscribe<Boolean> {it->
            Log.e("123","relink..........")
            mIsReLinking = true
            JniUtil.netUnregistAlarm()
//            ApiManager.getInstance().aPcamService.stop()
//            ApiManager.getInstance().aPcamService.disconnect()
//            if(!ApiManager.getInstance().aPcamService.connect()){
//                it.onNext(false)
//                return@ObservableOnSubscribe
//            }
//            if (mIsAlarmEnable)JniUtil.netRegistAlarm()
//            ApiManager.getInstance().aPcamService.play(mIsSub)
            ApiManager.getInstance().aPcamService.reLink(mIsSub,"","")
            if (mIsAlarmEnable)JniUtil.netRegistAlarm()

            it.onNext(true)
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({b->
                    mIsReLinking = false
//                    sendPlayResult(b)
                },{e->e.printStackTrace()})


        return this
    }


    override fun stop(): BasePlayer {
        Observable.create(ObservableOnSubscribe<Boolean> { e->
            JniUtil.netUnregistAlarm()
            ApiManager.getInstance().aPcamService.stop()
            ApiManager.getInstance().aPcamService.disconnect()
            e.onNext(true)
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({b->
                    sendStopResult(b)
                },{e->e.printStackTrace()})
        return this
    }

    override fun stepNext(): Boolean = ApiManager.getInstance().aPcamService.stepNext()

    override fun stepLast(): Boolean = ApiManager.getInstance().aPcamService.stepLast()


    override fun catchPic(): BasePlayer {
        FileUtil.getPictureDir()
        var nameDirPath = FileUtil.FILE_PICTURE_PATH + FileUtil.getFileName() + ".jpg"
        Log.i("123","save pic=$nameDirPath")

//        var testfile = FileUtil.FILE_TF_DIR+"/"+"Android/data/com.androidcycle.sdcardscanner/a.txt"
//        Log.i("123","testfile = $testfile")
//        var f = File(testfile)
//        Log.i("123","create new file = ${f.createNewFile()}")

        Observable.create(ObservableOnSubscribe<Boolean> { e->
            ApiManager.getInstance().aPcamService.catchPic(nameDirPath)
            var i = 10;
            var ret = false
            while (i>0){
                Thread.sleep(100)
                i--
                if (FileUtil.isExists(nameDirPath)){
                    ret = true
                    break
                }
            }
            e.onNext(ret)
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({b->
                   sendCatchResult(b)
                },{e->e.printStackTrace()})
        return this
    }

    override fun searchRemoteFile(beg: String, end: String, curPage: Int?, pageSize: Int?) {
        var needLogin = false
        Observable.create(ObservableOnSubscribe<Boolean> {
            if (!JniUtil.isLogin()){
                needLogin = true
                ApiManager.getInstance().aPcamService.connect()
            }
            it.onNext(ApiManager.getInstance().aPcamService.getRecordedFiles(beg,end,curPage,pageSize))
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (needLogin) ApiManager.getInstance().aPcamService.disconnect()
                    Log.i("123","get recordedFiles=$it")
                },{e->e.printStackTrace()})
    }


    override fun setPlaySpeed(speed: Float) {
        ApiManager.getInstance().aPcamService.setSpeed(speed)
    }

    override fun stopAndPlayAnother(url: String) {
        var strs = url.split("&")
        var beg = strs[0]
        var end = strs[1]
        Log.i("123","beg=$beg   end=$end")
        Observable.create(ObservableOnSubscribe<Boolean> {
            ApiManager.getInstance().aPcamService.stop()
            ApiManager.getInstance().aPcamService.playback(Config.CAM_IS_SUB,beg,end)
            it.onNext(true)
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    sendPlayResult(it)
                },{e->e.printStackTrace()})
    }
}