package com.inz.model.net

import android.util.Log
import com.googlecode.mp4parser.boxes.apple.AppleAppleIdBox
import com.howellsdk.api.ApiManager
import com.howellsdk.player.turn.bean.PTZ_CMD
import com.howellsdk.utils.RxUtil
import com.howellsdk.utils.ThreadUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * ptz控制功能
 */
class PtzMgr {
    companion object {
        private var mInstance:PtzMgr ?= null
        val PTZ_SPEED_FAST                     = 48
        val PTZ_SPEED_NORMAL                   = 32
        val PTZ_SPEED_SLOW                     = 16
        fun getInstance():PtzMgr{
            if(mInstance == null){
                mInstance = PtzMgr()
            }
            return mInstance!!
        }
    }
    private constructor()

    @Volatile var mCruiseIndex             = 0
    @Volatile var mCruiseEnable            = false
    @Volatile var mThreadIsStart           = false
    @Volatile var mPoints:ArrayList<Int>  ?= null
    @Volatile var mStateTaskIsStart        = false
    @Volatile var mStateTaskEnable         = false
    @Volatile var mPtzMoveEnable           = false
    @Volatile var mPtzMoveIsStart          = false
    @Volatile var mPtzZoomEnable           = false
    @Volatile var mPtzZoomIsStart          = false

    fun ptzSpeed(speed:Int){

        Observable.create(ObservableOnSubscribe<Boolean> {
            it.onNext(ApiManager.getInstance().aPcamService
                    .ptzSetSpeed(speed))
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { }
    }



    fun ptzUp(){
        ptzStopCruis()
//        ptzMove(PTZ_CMD.ptz_up)
        Observable.create(ObservableOnSubscribe<Boolean> {
            ApiManager.getInstance().aPcamService.ptzControl(PTZ_CMD.ptz_stop,0,0)
            it.onNext(ApiManager.getInstance().aPcamService
                    .ptzControl(PTZ_CMD.ptz_up,0,0))
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.i("123","ptz up  res=$it")
                }
    }

    fun ptzDown(){
        ptzStopCruis()
        ptzMove(PTZ_CMD.ptz_down)
    }

    fun ptzLeft(){
        ptzStopCruis()
        ptzMove(PTZ_CMD.ptz_left)
    }

    fun ptzRight(){
        ptzStopCruis()
        ptzMove(PTZ_CMD.ptz_right)
    }

    fun ptzStop(){
        ptzStopCruis()
        mPtzMoveEnable = false
        Observable.create(ObservableOnSubscribe<Boolean> {
            it.onNext(ApiManager.getInstance().aPcamService
                    .ptzControl(PTZ_CMD.ptz_stop,0,0))
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.i("123","ptz stop  res=$it")
                }
    }
//    private fun ptzMove( cmd:PTZ_CMD){
//        mPtzMoveEnable = true
//        if (!mPtzMoveIsStart){
//            ThreadUtil.cachedThreadStart {
//                mPtzMoveIsStart = true
//                while (mPtzMoveEnable){
//                    var res = ApiManager.getInstance().aPcamService
//                            .ptzControl(cmd,0,0)
//                    Log.i("123","ptz move res=$res")
//                    Thread.sleep(333)
//                }
//                mPtzMoveIsStart = false
//                ptzStop()
//            }
//        }
//    }

    private fun ptzMove(cmd:PTZ_CMD){
        Observable.create(ObservableOnSubscribe<Boolean> {
            ApiManager.getInstance().aPcamService.ptzControl(PTZ_CMD.ptz_stop,0,0)
            it.onNext(ApiManager.getInstance().aPcamService
                    .ptzControl(cmd,0,0))
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.i("123","ptz up  res=$it")
                }
    }

    fun ptzLrisOpen(){
        Observable.create(ObservableOnSubscribe<Boolean> {
            it.onNext(ApiManager.getInstance().aPcamService
                    .ptzControl(PTZ_CMD.ptz_lrisOpen,0,0))
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.i("123","lris open  res=$it")
                }
    }

    fun ptzLrisClose(){
        Observable.create(ObservableOnSubscribe<Boolean> {
            it.onNext(ApiManager.getInstance().aPcamService
                    .ptzControl(PTZ_CMD.ptz_lrisClose,0,0))
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.i("123","lris close  res=$it")
                }
    }

    fun ptzZoomIn(){
        ptzZoom(PTZ_CMD.ptz_zoomWide)
    }

    fun ptzZoomOut(){
        ptzZoom(PTZ_CMD.ptz_zoomTele)
    }

    fun ptzZoomStop(){
        mPtzZoomEnable = false
        Observable.create(ObservableOnSubscribe<Boolean> {
            it.onNext(ApiManager.getInstance().aPcamService
                    .ptzControl(PTZ_CMD.ptz_zoomStop,0,0))
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.i("123","zoom stop  res=$it")
                }
    }

//    fun ptzZoom(cmd:PTZ_CMD){
//        mPtzZoomEnable = true
//        if(!mPtzZoomIsStart){
//            ThreadUtil.cachedThreadStart {
//                mPtzZoomIsStart = true
//                while (mPtzZoomEnable){
//                    var res = ApiManager.getInstance().aPcamService
//                            .ptzControl(cmd,0,0)
//                    Log.i("123","ptz zoom res=$res")
//                    Thread.sleep(333)
//                }
//                mPtzZoomIsStart = false
//                ptzZoomStop()
//            }
//        }
//    }

    fun ptzZoom(cmd:PTZ_CMD){
                Observable.create(ObservableOnSubscribe<Boolean> {
            it.onNext(ApiManager.getInstance().aPcamService
                    .ptzControl(cmd,0,0))
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.i("123","zoom out  res=$it")
                }
    }

    fun ptzSetCruis(ps:ArrayList<Int>):PtzMgr{
        mPoints = ps
        return this
    }

    /**
     * go preset 100
     */
    fun ptzCruise90(){//we set point 100
        Observable.create(ObservableOnSubscribe<Boolean> {
            it.onNext(ApiManager.getInstance().aPcamService
                    .ptzGoPreset(100))
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.i("123","set preset  100 $it")
                }
    }

    /**
     * go preset 101
     */
    fun ptzCruise180(){
        Observable.create(ObservableOnSubscribe<Boolean> {
            it.onNext(ApiManager.getInstance().aPcamService
                    .ptzGoPreset(101))
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.i("123","set preset 101 $it")
                }
    }


    fun ptzStartCruise(){
        mCruiseIndex = 0
        mCruiseEnable = true
        if(!mThreadIsStart) {
            ThreadUtil.cachedThreadStart {
                mThreadIsStart = true
                while (mCruiseEnable) {
                    if (mPoints==null)break
                    var p = mPoints!![mCruiseIndex]

                    var res =ApiManager.getInstance().aPcamService.ptzGoPreset(p)
                    Log.i("123", "i=$mCruiseIndex     p=$p  res=$res")
                    mCruiseIndex++
                    if (mCruiseIndex == mPoints!!.size) mCruiseIndex = 0
                    //do

                    Thread.sleep(1000)
                }
                mThreadIsStart = false
            }
        }
    }



    fun ptzStopCruis(){
        mCruiseEnable = false
    }

    fun ptzStateRegist(){
        Observable.create(ObservableOnSubscribe<Boolean> {
            it.onNext(ApiManager.getInstance().aPcamService
                    .ptzStateCmd(1))
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {  }
    }

    fun ptzStateUnregist(){
        Observable.create(ObservableOnSubscribe<Boolean> {
            it.onNext(ApiManager.getInstance().aPcamService
                    .ptzStateCmd(2))
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {  }
    }


    fun ptzGetStateTaskStart(onStateRes:(Boolean)->Unit){
        mStateTaskEnable = true
        if (!mStateTaskIsStart){
            ThreadUtil.cachedThreadStart {
                mStateTaskIsStart=true
                while(mStateTaskEnable){
                    Thread.sleep(2000)
                    var res = ApiManager.getInstance().aPcamService.ptzStateCmd(3)
                    RxUtil.doInUIThread(object :RxUtil.RxSimpleTask<Void>(){
                        override fun doTask() {

                            Log.i("123","ptz state res=$res")

                            onStateRes(res)
                        }
                    })
                    Thread.sleep(3000)
                }
                mStateTaskIsStart=false
            }
        }
    }
    fun ptzGetStateTaskStop(){
        mStateTaskEnable = false
    }


}