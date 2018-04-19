package com.inz.model.main

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.databinding.ObservableField
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.View
import android.widget.PopupWindow
import android.widget.Toast
import com.howellsdk.utils.ThreadUtil
import com.inz.activity.BigImagesActivity
import com.inz.activity.view.PopWindowView
import com.inz.adapter.MyPictureAdapter
import com.inz.adapter.MyVideoAdapter
import com.inz.bean.PictureBean
import com.inz.bean.VideoBean
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.model.ModelMgr
import com.inz.utils.FileUtil
import com.inz.utils.Utils

import io.reactivex.functions.Action
import java.io.File

class PlayListModel(private var mContext: Context):BaseViewModel {
    private val SHOW_NONE           = 0x00
    private val SHOW_RECORD_FILE    = 0x01
    private val SHOW_PICTURE_FILE   = 0x02

    private var mShowCode       = SHOW_NONE
    private var mFunPop: PopupWindow?=null
    private var mUriList:ArrayList<Uri> = ArrayList()
//    private var mLocalPlayer:BasePlayer?=null

    var activity:Activity?=null

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
                mPictureListVisibility.set(View.INVISIBLE)
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
                mPictureListVisibility.set(View.INVISIBLE)
            }
        }

    }



    val mShowRecordFile              = ObservableField<Boolean>(false)
    val mShowPictureFile             = ObservableField<Boolean>(false)
    val mRecordListVisibility        = ObservableField<Int>(View.GONE)
    val mPictureListVisibility       = ObservableField<Int>(View.GONE)
    val mUpdatePictureList           = ObservableField<Boolean>(false)
    val mUpdatePictureShare          = ObservableField<Boolean>(false)
    val mUpdateVideoList             = ObservableField<Boolean>(false)
    val mShareBtnVisibility          = ObservableField<Int>(View.GONE)
    val onShareShareClick            = Action {
        if(mUriList.size==0) {
            Toast.makeText(mContext,mContext.getString(R.string.share_no_file_error),Toast.LENGTH_SHORT).show()
            return@Action
        }

        //todo
        var shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND_MULTIPLE
        shareIntent.type = "image/*"
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,mUriList)
        mContext.startActivity(Intent.createChooser(shareIntent,mContext.getString(R.string.share_share)))
        mUriList.clear()
        updatePictureShareState(false)
    }

    val onShareCancelClick           = Action {
        updatePictureShareState(false)
        mUriList.clear()
    }

    fun initPictureList(rv:RecyclerView, width:Int){
        Log.i("123","model  initPictureList")
        var picAdapter = MyPictureAdapter(mContext,object :MyPictureAdapter.OnItemClickListener{
            override fun onItemShareCheck(v: View?, pos: Int, b: PictureBean?, isChecked: Boolean) {
                //TODO
                var path = b?.path
                if (isChecked)mUriList.add(Uri.fromFile(File(path)))
                else mUriList.remove(Uri.fromFile(File(path)))

                Log.i("123","urilist = $mUriList")

            }

            override fun onItemClick(v: View?, list: MutableList<PictureBean>?, pos: Int) {
                var picPaths = ArrayList<String>()
                list?.forEach {
                    picPaths.add(it.path)
                }
                var intent = Intent(mContext,BigImagesActivity::class.java)
                intent.putExtra("position",pos).putStringArrayListExtra("arrayList",picPaths)
//                mContext.startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(activity!!,v,"myImage").toBundle())
                activity!!.startActivityForResult(intent,0,ActivityOptions.makeSceneTransitionAnimation(activity!!,v,"myImage").toBundle())

            }


            override fun onItemLongClick(v:View,pos: Int,bean:PictureBean) {
                Log.i("123","on item long click pos=$pos     path=${bean.path}")
                mFunPop = PopWindowView.generate(mContext,{
                    mLayoutId = R.layout.view_list_fun
                    mViewModel = ModelMgr.getListItemModelInstance(mContext)
                    build()
                })
                ModelMgr.getListItemModelInstance(mContext).mPop  = mFunPop
                ModelMgr.getListItemModelInstance(mContext).mBean = bean
                mFunPop?.showAsDropDown(v)

            }
        })
        picAdapter.setWidth(width)
        rv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rv.adapter = picAdapter
        updatePictureList(rv)
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

    fun updatePictureShare(v:RecyclerView,bShareMode:Boolean){
        if (v.adapter is MyPictureAdapter) {
            (v.adapter as MyPictureAdapter).setShareMode(bShareMode)
        }
    }


    fun updatePictureListState(){
        Log.i("123","update picture list state")
        mUpdatePictureList.set(true)
    }

    fun updatePictureShareState(b:Boolean){
        mShareBtnVisibility.set(if (b)View.VISIBLE else View.GONE)

        mUpdatePictureShare.set(b)
    }


    fun initVideoList(v:RecyclerView){
        var vidAdapter = MyVideoAdapter(mContext,object :MyVideoAdapter.OnItemClickListener{
            override fun onItemClickListener(bean:VideoBean,pos: Int) {
                Log.i("123","video on ItemClick   pos=$pos   name=${bean.name}  path=${bean.path}")
                ThreadUtil.cachedThreadStart({
                    ModelMgr.getPlayViewModelInstance(mContext).stopView()
                    Thread.sleep(500)
                    ModelMgr.getPlayViewModelInstance(mContext).initLocalPlay()
                    ModelMgr.getPlayViewModelInstance(mContext).setUrl(bean.path)
                    ModelMgr.getPlayViewModelInstance(mContext).setVideoPlayCurIndex(pos)
                    ModelMgr.getPlayViewModelInstance(mContext).playView()
                })

            }

            override fun onItemLongClickListener(v:View,bean:VideoBean,pos: Int) {
                mFunPop = PopWindowView.generate(mContext,{
                    mLayoutId = R.layout.view_list_fun
                    mViewModel = ModelMgr.getListItemModelInstance(mContext)
                    build()
                })
                ModelMgr.getListItemModelInstance(mContext).mPop  = mFunPop
                ModelMgr.getListItemModelInstance(mContext).mBean = bean
                mFunPop?.showAsDropDown(v)
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
            if(nameArr[1]=="hw") {
                var nameStr = nameArr[0]
                var name = Utils.getVideoName(nameStr)
                arr.add(VideoBean(s, name))
            }
        }
        (v.adapter as MyVideoAdapter).setData(arr)
        ModelMgr.getPlayViewModelInstance(mContext).setVideoSource(arr)
        mUpdateVideoList.set(false)
    }

    fun upDateVideoListState(){
        Log.i("123","update picture list state")
        mUpdateVideoList.set(true)
    }

}