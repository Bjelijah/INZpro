package com.inz.model.main

import android.content.Context
import android.util.Log
import android.widget.PopupWindow
import com.inz.bean.BaseBean
import com.inz.bean.PictureBean
import com.inz.bean.VideoBean
import com.inz.inzpro.BaseViewModel
import com.inz.model.ModelMgr
import com.inz.utils.FileUtil
import io.reactivex.functions.Action
import java.io.File

class ListItemModel(private var mContext: Context) :BaseViewModel  {

    var mPop: PopupWindow ?=null
    var mBean: BaseBean ?=null

    override fun onCreate() {
    }

    override fun onDestory() {
    }

    val onClickShare = Action{
        Log.i("123","onClick share   path=${mBean?.path}")

        mPop?.dismiss()
    }

    val onClickDelect = Action {
        Log.i("123","onclick del    path=${mBean?.path}")
        mPop?.dismiss()
        try {
            FileUtil.deleteFile(File(mBean?.path))
        }catch (e:Exception){
            e.printStackTrace()
        }
        if(mBean is PictureBean){
            ModelMgr.getPlayListModelInstance(mContext).upDatePictureListState()
        }else if(mBean is VideoBean){
            ModelMgr.getPlayListModelInstance(mContext).upDateVideoListState()
        }


    }


}