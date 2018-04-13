package com.inz.model.download

import com.howellsdk.api.ApiManager
import com.inz.model.ModelMgr
import com.inz.utils.FileUtil

class DownloadMgr {



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
        var path = FileUtil.createNewVideoDirPathName()
        ApiManager.getInstance().apDownLoadServer
                .open(path).start()
    }



}