package com.inz.model.download

import com.howellsdk.api.ApiManager
import com.inz.utils.FileUtil

class DownloadMgr {



    fun start(){

    }

    fun stop(){

    }

    fun stepTask(){
        var path = FileUtil.createNewVideoDirPathName()
        ApiManager.getInstance().apDownLoadServer
                .open(path).start()
    }



}