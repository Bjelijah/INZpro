package com.inz.model.download

import com.howellsdk.api.ApiManager
import com.howellsdk.utils.ThreadUtil
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
    fun start(){
        var path = FileUtil.createNewVideoDirPathName()
        ApiManager.getInstance().apDownLoadServer
                .open(path).start()
    }

    fun stop(){
        ApiManager.getInstance().apDownLoadServer.stop().close()
        //send msg
        ModelMgr.getPlayListModelInstance(ModelMgr.mContext!!).upDateVideoListState()

    }

    fun stepTask(){
        startFlag = true
        timeTaskStart()
        task = ThreadUtil.newCachedThreadStart({
            while (startFlag){
                var path = FileUtil.createNewVideoDirPathName()
                ApiManager.getInstance().apDownLoadServer
                        .open(path).start()
                obj.wait()
                ApiManager.getInstance().apDownLoadServer.stop().close()
                ModelMgr.getPlayListModelInstance(ModelMgr.mContext!!).upDateVideoListState()
            }
        })
    }

    fun stepStop(){
        startFlag = false
        obj.notify()
        ThreadUtil.newCachedThreadShutDown(task)
        timeTaskStop()
    }

    private fun timeTaskStart(){
        time = ThreadUtil.newScheduledThreadStart({
            obj.notify()
        },0,2,TimeUnit.SECONDS)
    }
    private fun timeTaskStop(){
        ThreadUtil.newScheduledThreadShutDown(time)
    }

}