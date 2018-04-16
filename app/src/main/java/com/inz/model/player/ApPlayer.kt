package com.inz.model.player

import android.util.Log
import com.howellsdk.api.ApiManager
import com.howellsdk.api.HWPlayApi
import com.inz.action.Config
import com.inz.utils.FileUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

class ApPlayer :BasePlayer(){

    override fun init(crypto:Int,uri:String): BasePlayer {
        Observable.create(ObservableOnSubscribe<Boolean> { e->
           ApiManager.getInstance()
                    .getAPcamService(
                            Config.CAM_IP,
                            Config.CAM_SLOT,
                            Config.CAM_Crypto,
                            HWPlayApi.IAPCamCB {  }
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
                    ApiManager.getInstance().aPcamService.play(isSub)
                    sendPlayResult(b)
                },{e->e.printStackTrace()})
        return this
    }

    override fun stop(): BasePlayer {
        Observable.create(ObservableOnSubscribe<Boolean> { e->
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
    override fun catchPic(): BasePlayer {
        FileUtil.getPictureDir()
        var nameDirPath = FileUtil.FILE_PICTURE_DIR + FileUtil.getFileName() + ".jpg"
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
}