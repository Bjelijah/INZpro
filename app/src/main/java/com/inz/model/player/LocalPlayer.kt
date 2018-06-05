package com.inz.model.player

import android.graphics.Bitmap
import android.util.Log
import com.howellsdk.api.ApiManager
import com.inz.action.Config
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LocalPlayer :BasePlayer() {
    override fun pause(): BasePlayer {
        ApiManager.getInstance().localService
                .playPause()
        return this
    }

    override fun isPause(): Boolean = ApiManager.getInstance().localService.isPause

    override fun init(crypto:Int,uri:String): BasePlayer {
        Observable.create(ObservableOnSubscribe<Boolean> {e->
            ApiManager.getInstance()
                    .getLocalService(crypto,uri)
                    .bindCam()
            e.onNext(true)
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({isSuccess->
                    sendInitResult(isSuccess)
                },{e->e.printStackTrace()})
        return this
    }

    override fun deinit(): BasePlayer {
        Observable.create(ObservableOnSubscribe<Boolean> { e->
            ApiManager.getInstance().localService
                    .unBindCam()
            e.onNext(true)
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({b->
                    sendDeinitResult(b)
                },{e->e.printStackTrace()})
        return this
    }

    override fun setUrl(uri: String) {
        ApiManager.getInstance().localService
                .setUri(uri)
    }

    override fun play(isSub: Boolean): BasePlayer {
        Observable.create(ObservableOnSubscribe<Boolean> {e->
            ApiManager.getInstance().localService
                    .play(false)
            e.onNext(true)
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({b->
                    sendPlayResult(b)
                    Log.i("123","send play result $b")
                },{e->e.printStackTrace()})
        return this
    }

    override fun playback(isSub: Boolean,beg:String,end:String): BasePlayer {
        return this
    }
    override fun rePlay(): BasePlayer {
        return this
    }

    override fun stop(): BasePlayer {
        Observable.create(ObservableOnSubscribe<Boolean> {  e->
            ApiManager.getInstance().localService
                    .stop()
            e.onNext(true)
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({b->
                    sendStopResult(b)
                },{e->e.printStackTrace()})
        return this
    }

    override fun stepNext(): Boolean  = ApiManager.getInstance().localService.stepNext()


    override fun stepLast(): Boolean
        = ApiManager.getInstance().localService.stepLast()

    override fun catchPic(): BasePlayer {
        return this
    }

    override fun getTotalFrame(): Int  =  ApiManager.getInstance().localService.totalFrame

    override fun getCurFrame(): Int = ApiManager.getInstance().localService.curFrame

    override fun setCurFrame(curFrame: Int) {
        ApiManager.getInstance().localService.clearStreamBuf()
        ApiManager.getInstance().localService.curFrame = curFrame
    }

    override fun getTotalMsec(): Int = ApiManager.getInstance().localService.totalMsec

    override fun getPlayedMsec(): Int = ApiManager.getInstance().localService.playedMsec

    override fun setPos(pos: Int) {ApiManager.getInstance().localService.pos = pos}

    override fun getPos(): Int = ApiManager.getInstance().localService.pos

    override fun setPlaySpeed(speed: Float) {
        ApiManager.getInstance().localService.setSpeed(speed)
    }

    override fun stopAndPlayAnother(url: String) {
        Observable.create(ObservableOnSubscribe<Boolean> {e->
            ApiManager.getInstance().localService.stop()
            ApiManager.getInstance().localService.setUri(url)
            ApiManager.getInstance().localService.play(Config.CAM_IS_SUB)
            e.onNext(true)
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    sendPlayResult(it)
                },{e->e.printStackTrace()})
    }
}