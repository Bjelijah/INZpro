package com.inz.model.download

import android.util.Log
import com.howellsdk.api.ApiManager
import com.howellsdk.utils.ThreadUtil
import com.inz.action.Config
import com.inz.model.ModelMgr
import com.inz.utils.FileUtil
import java.io.FileNotFoundException
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * 下载功能模块
 */
class DownloadMgr {
    var obj = Object()
    var startFlag = false
    var task : ExecutorService?=null
    var time : ScheduledExecutorService?=null
    var mFilePath:String ?=null
    var secNum = 0
    /**
     * 手动开始录像
     */
    fun start(){
        var path = FileUtil.createNewVideoDirPathName(0)
        mFilePath = path
        try {
            ApiManager.getInstance().getApDownLoadServer(0)
                    .open(path).start()
        }catch (e:FileNotFoundException){
            e.printStackTrace()
        } catch (e:Exception){
            e.printStackTrace()
        }
    }

    /**
     * 手动停止录像
     */
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

    /**
     * 顺序录像任务
     * @param onError 错误回调
     */
    fun stepTask(onError:()->Unit) {

        startFlag = true
        timeTaskStart()

        task = ThreadUtil.newCachedThreadStart {
            while (startFlag){

                try {
                    FileUtil.deleteLastFileIfSpaceLimit(FileUtil.FILE_VIDEO_PATH,Config.FILE_DIR_VIDEO_SIZE,Config.FILE_DIR_LIMITE)
                    var path = FileUtil.createNewVideoDirPathName(if(Config.DOWN_WH_STREAM)0 else 1)
                    ApiManager.getInstance().getApDownLoadServer(if (Config.DOWN_WH_STREAM) 0 else 1)
                            .open(path).start()
                }catch (e:FileNotFoundException){
                    e.printStackTrace()
                    startFlag = false
                    timeTaskStop()
                    onError()
                    return@newCachedThreadStart
                }catch (e:Exception){
                    e.printStackTrace()
                    startFlag = false
                    timeTaskStop()
                    onError()
                    return@newCachedThreadStart
                }
                secNum = 0
                synchronized(obj){
                    obj.wait()
                }
                ApiManager.getInstance().apDownLoadServer.stop().close()
                ModelMgr.getPlayListModelInstance(ModelMgr.mContext!!).upDateVideoListState()
                Log.e("123","thread new cached stop download")
                Thread.sleep(100)
            }
        }
    }

    /**
     * 停止顺序录像
     */
    fun stepStop(){
        if(!startFlag)return
        startFlag = false
        synchronized(obj){
            obj.notify()
        }
        ThreadUtil.newCachedThreadShutDown(task)
        timeTaskStop()
        Log.e("123","step stop")
    }

    /**
     * 时间计时器开始
     * 每隔60s发送信号量 启停录像任务
     */
    private fun timeTaskStart(){
        secNum = 0
        time = ThreadUtil.newScheduledThreadStart({
            ModelMgr.getPlayViewModelInstance(ModelMgr.mContext!!).showRecordText(secNum)
            if (secNum>=60) {
                synchronized(obj) {
                    obj.notify()
                }
                secNum = 0
            }
            secNum ++

        },0,1,TimeUnit.SECONDS)
    }

    /**
     * 时间计时器停止
     */
    private fun timeTaskStop(){
        ThreadUtil.newScheduledThreadShutDown(time)
        secNum = 0
    }

}