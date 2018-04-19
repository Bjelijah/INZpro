package com.inz.model.download

import android.util.Log
import com.howellsdk.api.ApiManager
import com.howellsdk.utils.ThreadUtil
import com.inz.action.Config
import com.inz.model.ModelMgr
import com.inz.utils.FileUtil
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class DownloadMgr {
    var obj = Object()
    var startFlag = false
    var task : ExecutorService?=null
    var time : ScheduledExecutorService?=null
    var mFilePath:String ?=null
    fun start(){
        var path = FileUtil.createNewVideoDirPathName(0)
        mFilePath = path
        ApiManager.getInstance().getApDownLoadServer(0)
                .open(path).start()
    }

    fun stop(){
        ApiManager.getInstance().apDownLoadServer.stop().close()
        //send msg
        //to do convert
        var h264Path = mFilePath!!
        var p = mFilePath?.split(".")
        var mp4Path = p!![0] + ".mp4"
        Log.i("123","h264path=$h264Path       mp4path=$mp4Path")
//        FileUtil.h264File2mp4File(h264Path,mp4Path)
        Log.i("123","conver over")
        ModelMgr.getPlayListModelInstance(ModelMgr.mContext!!).upDateVideoListState()

    }

    fun stepTask(){
        startFlag = true
        timeTaskStart()
        task = ThreadUtil.newCachedThreadStart({
            while (startFlag){
                var path = FileUtil.createNewVideoDirPathName(if(Config.DOWN_WH_STREAM)0 else 1)
                ApiManager.getInstance().getApDownLoadServer(if(Config.DOWN_WH_STREAM)0 else 1)
                        .open(path).start()
                synchronized(obj){
                    obj.wait()
                }
                ApiManager.getInstance().apDownLoadServer.stop().close()
                ModelMgr.getPlayListModelInstance(ModelMgr.mContext!!).upDateVideoListState()
                Log.e("123","thread new cached stop download")
                Thread.sleep(100)
            }
        })
    }

    fun stepStop(){
        startFlag = false
        synchronized(obj){
            obj.notify()
        }
        ThreadUtil.newCachedThreadShutDown(task)
        timeTaskStop()
        Log.e("123","step stop")
    }

    private fun timeTaskStart(){
        time = ThreadUtil.newScheduledThreadStart({
            synchronized(obj){
                obj.notify()
            }
        },0,2,TimeUnit.MINUTES)
    }
    private fun timeTaskStop(){
        ThreadUtil.newScheduledThreadShutDown(time)
    }

}