package com.inz.model.main

import android.content.Context
import android.databinding.ObservableField
import android.graphics.Bitmap
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.View
import com.howellsdk.api.ApiManager
import com.inz.action.Config
import com.inz.adapter.MyPictureAdapter
import com.inz.adapter.MyVideoAdapter
import com.inz.bean.PictureBean
import com.inz.bean.VideoBean
import com.inz.inzpro.BaseViewModel
import com.inz.model.ModelMgr
import com.inz.model.player.BasePlayer
import com.inz.utils.FileUtil
import com.inz.utils.Utils

import io.reactivex.functions.Action

class PlayListModel(private var mContext: Context):BaseViewModel {
    private val SHOW_NONE           = 0x00
    private val SHOW_RECORD_FILE    = 0x01
    private val SHOW_PICTURE_FILE   = 0x02

    private var mShowCode       = SHOW_NONE
//    private var mLocalPlayer:BasePlayer?=null
    override fun onCreate() {

    }

    override fun onDestory() {
    }



    val mPlayListTitleBtnRecordFile = Action {
        Log.i("123","on record file click")
        when(mShowCode){
            SHOW_NONE->{
                mShowCode = SHOW_RECORD_FILE
                mShowRecordFile.set(true)
                mShowPictureFile.set(false)
                mRecordListVisibility.set(View.VISIBLE)
                mPictureListVisibility.set(View.GONE)

            }
            SHOW_RECORD_FILE->{
                mShowCode = SHOW_NONE
                mShowRecordFile.set(false)
                mRecordListVisibility.set(View.GONE)
            }
            SHOW_PICTURE_FILE->{
                mShowCode = SHOW_RECORD_FILE
                mShowPictureFile.set(false)
                mShowRecordFile.set(true)
                mRecordListVisibility.set(View.VISIBLE)
                mPictureListVisibility.set(View.GONE)
            }
        }
    }

    val mPlayListTitleBtnPictureFile = Action {
        Log.i("123","on picture file click")
        when(mShowCode){
            SHOW_NONE->{
                mShowCode = SHOW_PICTURE_FILE
                mShowRecordFile.set(false)
                mShowPictureFile.set(true)
                mRecordListVisibility.set(View.GONE)
                mPictureListVisibility.set(View.VISIBLE)
            }
            SHOW_RECORD_FILE->{
                mShowCode = SHOW_PICTURE_FILE
                mShowRecordFile.set(false)
                mShowPictureFile.set(true)
                mRecordListVisibility.set(View.GONE)
                mPictureListVisibility.set(View.VISIBLE)
            }
            SHOW_PICTURE_FILE->{
                mShowCode = SHOW_NONE
                mShowPictureFile.set(false)
                mPictureListVisibility.set(View.GONE)
            }
        }

    }



    val mShowRecordFile              = ObservableField<Boolean>(false)
    val mShowPictureFile             = ObservableField<Boolean>(false)
    val mRecordListVisibility        = ObservableField<Int>(View.GONE)
    val mPictureListVisibility       = ObservableField<Int>(View.GONE)

    val mUpdatePictureList           = ObservableField<Boolean>(false)
    val mUpdateVideoList             = ObservableField<Boolean>(false)


    fun initPictureList(v:RecyclerView,width:Int){
        Log.i("123","model  initPictureList")
        var picAdapter = MyPictureAdapter(mContext,object :MyPictureAdapter.OnItemClickListener{
            override fun onItemClick(pos: Int) {
                Log.i("123","onItemClickListener pos= $pos")
            }

            override fun onItemLongClick(pos: Int) {
                Log.i("123","onItemLongClickListener pos= $pos")
            }

        })
        picAdapter.setWidth(width)
        v.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        v.adapter = picAdapter
        updatePictureList(v)
    }

    fun updatePictureList(v:RecyclerView){

        var lst = FileUtil.getPictureDirFile()
        Log.i("123","updatePictruelist  ~~~~~ lst=$lst")
        var arr :ArrayList<PictureBean> = ArrayList()
        for (s in lst){
            arr.add(PictureBean(s))
        }
        (v.adapter as MyPictureAdapter) .setData(arr)
        mUpdatePictureList.set(false)
    }

    fun upDatePictureListState(){
        Log.i("123","update picture list state")
        mUpdatePictureList.set(true)
    }

    fun initVideoList(v:RecyclerView){
        var vidAdapter = MyVideoAdapter(mContext,object :MyVideoAdapter.OnItemClickListener{
            override fun onItemClickListener(bean:VideoBean,pos: Int) {
                Log.i("123","video on ItemClick   pos=$pos   name=${bean.name}  path=${bean.path}")
                ModelMgr.getPlayViewModelInstance(mContext).stopView()
                ModelMgr.getPlayViewModelInstance(mContext).initLocalPlay()
                ModelMgr.getPlayViewModelInstance(mContext).setUrl(bean.path)
                ModelMgr.getPlayViewModelInstance(mContext).playView()
            }

            override fun onItemLongClickListener(bean:VideoBean,pos: Int) {

            }

        })

        v.layoutManager = LinearLayoutManager(mContext)
        v.adapter = vidAdapter
        updateVideoList(v)

    }

    fun updateVideoList(v:RecyclerView){
        var lst = FileUtil.getVideoDirFile()
        var arr:ArrayList<VideoBean> = ArrayList()
        for (s in lst){
            var str = s.split("/")
            var nameArr = str[str.lastIndex].split(".")
            var nameStr = nameArr[0]
            var name = Utils.getVideoName(nameStr)
            arr.add(VideoBean(s,name))
        }
        (v.adapter as MyVideoAdapter).setData(arr)
        mUpdateVideoList.set(false)
    }

    fun upDateVideoListState(){
        Log.i("123","update picture list state")
        mUpdateVideoList.set(true)
    }

}