package com.inz.model.main

import android.app.AlertDialog
import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.PopupWindow
import com.howell.jni.JniUtil
import com.howell.jni.JniUtil.hwFile2mp4File
import com.howellsdk.utils.ThreadUtil
import com.inz.bean.BaseBean
import com.inz.bean.PictureBean
import com.inz.bean.VideoBean
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.model.ModelMgr
import com.inz.utils.FileUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import java.io.File

class ListItemModel(private var mContext: Context) :BaseViewModel  {

    var mPop: PopupWindow ?=null
    var mBean: BaseBean ?=null

    override fun onCreate() {
        Log.e("123"," ListItemModel onCreate ")

    }

    override fun onDestory() {
        Log.e("123"," ListItemModel onDestory ")
    }

    fun setContext(c:Context){
        mContext = c
    }

    val onClickShare = Action{
        Log.i("123","onClick share   path=${mBean?.path}")
        mPop?.dismiss()
        if (mBean is PictureBean){
            ModelMgr.getPlayListModelInstance(mContext).updatePictureShareState(true)
        }else if(mBean is VideoBean){
            //do share here
            Log.i("123","mbean is Video bean  ")
            var path = mBean?.path
            var dialog = ProgressDialog(mContext,R.style.waitDialog)
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            dialog.setMessage(mContext.getString(R.string.share_convert_wait))
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.show()
            Observable.create(ObservableOnSubscribe<Boolean> {e->
                e.onNext(true)
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({it->
                        dialog.dismiss()
                        if (it){
                            var uri = Uri.fromFile(File(path))
                            var shareIntent = Intent()
                            shareIntent.action = Intent.ACTION_SEND
                            shareIntent.putExtra(Intent.EXTRA_STREAM,uri)
                            shareIntent.type = "video/*"
                            mContext.startActivity(Intent.createChooser(shareIntent,mContext.getString(R.string.share_share)))

                        }

                    },{e->e.printStackTrace()})



        }

    }

    /*fixme  */
    val onClickDelect = Action {
        Log.i("123","onclick del    path=${mBean?.path}")
        mPop?.dismiss()
        var dialog = AlertDialog.Builder(mContext)
                .setTitle(mContext.getString(R.string.item_del))
                .setMessage(
                        when (mBean) {
                            is PictureBean -> mContext.getString(R.string.item_del_msg_picture)
                            is VideoBean -> mContext.getString(R.string.item_del_msg_video)
                            else -> ""
                        }
                )
                .setIcon(R.drawable.ic_warning_white_36dp)
                .setPositiveButton(mContext.getString(R.string.ok)
                ) { _, _ ->
                    try {
                        FileUtil.deleteFile(File(mBean?.path))
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                    if(mBean is PictureBean){
                        ModelMgr.getPlayListModelInstance(mContext).updatePictureListState()
                    }else if(mBean is VideoBean){
                        ModelMgr.getPlayListModelInstance(mContext).upDateVideoListState()
                    }
                }
                .setNegativeButton(mContext.getString(R.string.cancel)){_,_->{}}
                .create()
                .show()





    }

    val onClickConvert = Action {
        mPop?.dismiss()
        ThreadUtil.cachedThreadStart({
            var path = mBean?.path
            var hwPath = path!!
            var p = path?.split(".")
            var h264Path = p!![0] + ".h264"
            var mp4Path  = p!![0] + ".mp4"
            Log.i("123","h264path=$hwPath       mp4path=$h264Path")
            Log.i("123","convert hw->h264 start")
            JniUtil.hwFile2H264File(hwPath,h264Path)
            Log.i("123","convert hw->h264 end")
            FileUtil.h264File2mp4File(h264Path,mp4Path)

            hwFile2mp4File(hwPath,mp4Path)

        })

    }



}