package com.inz.model.main

import android.app.Activity
import android.app.ActivityOptions
import android.app.AlertDialog
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
import com.howellsdk.utils.Util
import com.inz.action.CtrlAction
import com.inz.activity.BigImagesActivity
import com.inz.activity.view.PopWindowView
import com.inz.adapter.MyPictureAdapter
import com.inz.adapter.MyRemoteAdapter
import com.inz.adapter.MyVideoAdapter
import com.inz.bean.BaseBean
import com.inz.bean.PictureBean
import com.inz.bean.RemoteBean
import com.inz.bean.VideoBean
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.model.ModelMgr
import com.inz.utils.FileUtil
import com.inz.utils.Utils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers

import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.net.URI
import java.util.*
import kotlin.collections.ArrayList

class PlayListModel(private var mContext: Context):BaseViewModel {
    companion object {
        val CMD_SHARE = 0x00
        val CMD_DEL   = 0x01
    }
    val MIN_CLICK_DELAY_TIME = 1000
    private val SHOW_NONE           = 0x00
    private val SHOW_RECORD_FILE    = 0x01
    private val SHOW_PICTURE_FILE   = 0x02
    private val SHOW_REMOTE_FILE    = 0x03

    private var mShowCode       = SHOW_NONE
    private var mFunPop: PopupWindow?=null
    private var mUriList:ArrayList<Uri> = ArrayList()
    private var mRemoteList:ArrayList<RemoteBean>?=null
//    private var mLocalPlayer:BasePlayer?=null

    var activity:Activity?=null
    var mCmd = CMD_SHARE
    private var mLastClickTime = 0L
    fun setContext(c:Context){
        mContext = c
    }


    override fun onCreate() {

    }

    override fun onDestory() {
    }



    val mPlayListTitleBtnRecordFile = Action {
        Log.i("123","on record file click")
        //todo back click
        if (isFastClick())return@Action


        CtrlAction.setPlayReview(mContext)
        //play view
        ModelMgr.getReplayCtrlModelInstance(mContext).initUi()
        ModelMgr.getPlayViewModelInstance(mContext).change2AP()

        when(mShowCode){
            SHOW_NONE->{
                mShowCode = SHOW_RECORD_FILE
                mShowRecordFile.set(true)
                mShowRemoteFile.set(false)
                mShowPictureFile.set(false)
                mRecordListVisibility.set(View.VISIBLE)
                mRemoteListVisibility.set(View.GONE)
                mPictureListVisibility.set(View.GONE)
                //刷新
                upDateVideoListState()
            }
            SHOW_RECORD_FILE->{
                mShowCode = SHOW_NONE
                mShowRecordFile.set(false)
                mRecordListVisibility.set(View.GONE)
            }
            SHOW_REMOTE_FILE->{
                mShowCode = SHOW_RECORD_FILE
                mShowRemoteFile.set(false)
                mShowRecordFile.set(true)
                mRecordListVisibility.set(View.VISIBLE)
                mRemoteListVisibility.set(View.GONE)
                upDateVideoListState()
            }
            SHOW_PICTURE_FILE->{
                mShowCode = SHOW_RECORD_FILE
                mShowPictureFile.set(false)
                mShowRecordFile.set(true)
                mRecordListVisibility.set(View.VISIBLE)
                mPictureListVisibility.set(View.GONE)
                //刷新
                upDateVideoListState()
            }

        }
    }

    val mPlayListTitleBtnRemoteFile = Action {
        if(isFastClick())return@Action
        CtrlAction.setPlayReview(mContext)
        //play view
        ModelMgr.getReplayCtrlModelInstance(mContext).initUi()
        ModelMgr.getPlayViewModelInstance(mContext).change2AP()
        when(mShowCode){
            SHOW_NONE->{
                mShowCode = SHOW_REMOTE_FILE
                mShowRecordFile.set(false)
                mShowPictureFile.set(false)
                mShowRemoteFile.set(true)
                mRecordListVisibility.set(View.GONE)
                mPictureListVisibility.set(View.GONE)
                mRemoteListVisibility.set(View.VISIBLE)
                upDateRemoteListState()
            }
            SHOW_RECORD_FILE->{
                mShowCode = SHOW_REMOTE_FILE
                mShowRecordFile.set(false)
                mShowRemoteFile.set(true)
                mRecordListVisibility.set(View.GONE)
                mRemoteListVisibility.set(View.VISIBLE)
                upDateRemoteListState()
            }
            SHOW_REMOTE_FILE->{
                mShowCode = SHOW_NONE
                mShowRemoteFile.set(false)
                mRemoteListVisibility.set(View.GONE)
            }
            SHOW_PICTURE_FILE->{
                mShowCode = SHOW_REMOTE_FILE
                mShowPictureFile.set(false)
                mShowRemoteFile.set(true)
                mPictureListVisibility.set(View.GONE)
                mRemoteListVisibility.set(View.VISIBLE)
                upDateRemoteListState()
            }
        }
    }


    val mPlayListTitleBtnPictureFile = Action {
        Log.i("123","on picture file click")
        if (isFastClick())return@Action
        when(mShowCode){
            SHOW_NONE->{
                mShowCode = SHOW_PICTURE_FILE
                mShowRecordFile.set(false)
                mShowRemoteFile.set(false)
                mShowPictureFile.set(true)
                mRecordListVisibility.set(View.GONE)
                mRemoteListVisibility.set(View.GONE)
                mPictureListVisibility.set(View.VISIBLE)
                //刷新
                updatePictureListState()
            }
            SHOW_RECORD_FILE->{
                mShowCode = SHOW_PICTURE_FILE
                mShowRecordFile.set(false)
                mShowPictureFile.set(true)
                mRecordListVisibility.set(View.GONE)
                mPictureListVisibility.set(View.VISIBLE)
                //刷新
                updatePictureListState()
            }
            SHOW_REMOTE_FILE->{
                mShowCode = SHOW_PICTURE_FILE
                mShowRemoteFile.set(false)
                mShowPictureFile.set(true)
                mRemoteListVisibility.set(View.GONE)
                mPictureListVisibility.set(View.VISIBLE)
                updatePictureListState()
            }

            SHOW_PICTURE_FILE->{
                mShowCode = SHOW_NONE
                mShowPictureFile.set(false)
                mPictureListVisibility.set(View.INVISIBLE)
            }
        }

    }



    val mShowRecordFile              = ObservableField<Boolean>(false)
    val mShowRemoteFile              = ObservableField<Boolean>(false)
    val mShowPictureFile             = ObservableField<Boolean>(false)
    val mRecordListVisibility        = ObservableField<Int>(View.GONE)
    val mRemoteListVisibility        = ObservableField<Int>(View.GONE)
    val mPictureListVisibility       = ObservableField<Int>(View.GONE)
    val mUpdatePictureList           = ObservableField<Boolean>(false)
    val mUpdatePictureCmd            = ObservableField<Boolean>(false)
    val mUpdateVideoList             = ObservableField<Boolean>(false)
    val mUpdateRemoteList            = ObservableField<Boolean>(false)
    val mCmdBtnVisibility            = ObservableField<Boolean>(false)
    val mCmdBtnText                  = ObservableField<String>(mContext.getString(R.string.share_share))


    val onCmdClick            = Action {
        if(mUriList.size==0) {
            Toast.makeText(mContext,mContext.getString(R.string.share_no_file_error),Toast.LENGTH_SHORT).show()
            mUriList.clear()
            return@Action
        }

        //todo
        if (mCmd == CMD_SHARE) {
            var shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND_MULTIPLE
            shareIntent.type = "image/*"
            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, mUriList)
            mContext.startActivity(Intent.createChooser(shareIntent, mContext.getString(R.string.share_share)))
            mUriList.clear()
            updatePictureCmdState(false, mCmd)
        }else if(mCmd == CMD_DEL){
            Log.i("123","mUriList=$mUriList")
            var size = mUriList.size
            AlertDialog.Builder(mContext,R.style.alertDialog)
                    .setTitle(mContext.getString(R.string.item_del))
                    .setMessage(
                            "删除这$size 张图片？"
                    )
                    .setIcon(R.drawable.ic_warning_white)
                    .setPositiveButton(mContext.getString(R.string.ok)
                    ) { _, _ ->
                        try {
                            for(uri in mUriList){
                                FileUtil.deleteFile(URI(uri.toString()))
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                        updatePictureListState()
                        mUriList.clear()
                        updatePictureCmdState(false, mCmd)
                    }
                    .setNegativeButton(mContext.getString(R.string.cancel)){_,_->
                        mUriList.clear()
                        updatePictureCmdState(false, mCmd)
                    }
                    .create()
                    .show()
        }
    }

    val onCmdCancelClick           = Action {
        updatePictureCmdState(false,mCmd)
        mUriList.clear()
    }

    fun initPictureList(rv:RecyclerView, width:Int){
        Log.i("123","model  initPictureList")
        var picAdapter = MyPictureAdapter(mContext,{v, pos, b, isChecked ->
            var path = b?.path
            if (isChecked)mUriList.add(Uri.fromFile(File(path)))
            else mUriList.remove(Uri.fromFile(File(path)))
            Log.i("123","urilist = $mUriList")
        },{ v, list, pos ->
            var picPaths = ArrayList<String>()
            list?.forEach {
                picPaths.add(it.path)
            }
            var intent = Intent(mContext,BigImagesActivity::class.java)
            intent.putExtra("position",pos).putStringArrayListExtra("arrayList",picPaths)
//                mContext.startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(activity!!,v,"myImage").toBundle())
            activity!!.startActivityForResult(intent,0,ActivityOptions.makeSceneTransitionAnimation(activity!!,v,"myImage").toBundle())

        },{ v, pos, b ->
            Log.i("123","on item long click pos=$pos     path=${b.path}")
            mFunPop = PopWindowView.generate(mContext,{
                mLayoutId = R.layout.view_list_fun
                mViewModel = ModelMgr.getListItemModelInstance(mContext)
                build()
            })
            ModelMgr.getListItemModelInstance(mContext).mPop  = mFunPop
            ModelMgr.getListItemModelInstance(mContext).mBean = b
            mFunPop?.showAsDropDown(v)
        })
        picAdapter.setWidth(width)
        rv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rv.adapter = picAdapter
        rv.itemAnimator = null
//        updatePictureList(rv)
    }

    fun updatePictureList(v:RecyclerView){

        Observable.create(ObservableOnSubscribe<ArrayList<String>> {it->
            it.onNext(FileUtil.getPictureDirFile())
        })
                .map {it->
                    var arr :ArrayList<PictureBean> = ArrayList()
                    for(s in it){
                        arr.add(PictureBean(s))
                    }
                    arr
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({it->
                    (v.adapter as MyPictureAdapter) .setData(it)
                    mUpdatePictureList.set(false)
                },{e->e.printStackTrace()})
    }

    fun updatePictureCmd(v:RecyclerView, bCmdMode:Boolean){
        if (v.adapter is MyPictureAdapter) {
            (v.adapter as MyPictureAdapter).setCmdMode(bCmdMode)
        }
    }


    fun updatePictureListState(){
        Log.i("123","update picture list state")
        mUpdatePictureList.set(true)
    }

    fun updatePictureCmdState(b:Boolean,cmd:Int){
        mCmd = cmd
        when (cmd){
            CMD_SHARE-> mCmdBtnText.set(mContext.getString(R.string.share_share))
            CMD_DEL->   mCmdBtnText.set(mContext.getString(R.string.share_del))
            else ->     mCmdBtnText.set(mContext.getString(R.string.share_share))
        }
        mCmdBtnVisibility.set(b)
        mUpdatePictureCmd.set(b)
    }


    fun initVideoList(v:RecyclerView){
        var vidAdapter = MyVideoAdapter(mContext,{b, pos ->
            if (isFastClick())return@MyVideoAdapter
            CtrlAction.setPlayPlayback(mContext)
            ThreadUtil.cachedThreadStart({
                ModelMgr.getMainCtrlModelInstance(mContext).stopRecording()
                ModelMgr.getPlayViewModelInstance(mContext).stopView()
                Thread.sleep(500)
                ModelMgr.getPlayViewModelInstance(mContext).initLocalPlay()
                ModelMgr.getPlayViewModelInstance(mContext).setUrl(b.path)
                ModelMgr.getPlayViewModelInstance(mContext).setVideoPlayCurIndex(pos)
                ModelMgr.getPlayViewModelInstance(mContext).playView()
            })
        },{v, b, _ ->
            mFunPop = PopWindowView.generate(mContext,{
                mLayoutId = R.layout.view_list_fun
                mViewModel = ModelMgr.getListItemModelInstance(mContext)
                build()
            })
            ModelMgr.getListItemModelInstance(mContext).mPop  = mFunPop
            ModelMgr.getListItemModelInstance(mContext).mBean = b
            mFunPop?.showAsDropDown(v)
        })

        v.layoutManager = LinearLayoutManager(mContext)
        v.adapter = vidAdapter
        v.itemAnimator = null
//        updateVideoList(v)

    }

    fun updateVideoList(v:RecyclerView){
        Observable.create(ObservableOnSubscribe<ArrayList<String>> {e->
            e.onNext(FileUtil.getVideoDirFile())
        })
                .map {
                    var arr:ArrayList<VideoBean> = ArrayList()
                    for (s in it){
                        var str = s.split("/")
                        var nameArr = str[str.lastIndex].split(".")
                        if (nameArr[1]=="hw"){
                            arr.add(VideoBean(s,Utils.getVideoName(nameArr[0])))
                        }
                    }
                    arr
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({it->
                    Log.i("123","${v.adapter}")
                    (v.adapter as MyVideoAdapter).setData(it)
                    ModelMgr.getPlayViewModelInstance(mContext).setVideoSource(it as ArrayList<BaseBean>)
                    mUpdateVideoList.set(false)
                },{e->e.printStackTrace()})
    }

    fun upDateVideoListState(){
        Log.i("123","update picture list state")
        mUpdateVideoList.set(true)
    }

    fun initRemoteList(v:RecyclerView){
        var remoteAdapter = MyRemoteAdapter(mContext,{b, pos ->
            if (isFastClick())return@MyRemoteAdapter
            CtrlAction.setPlayPlayback(mContext)
            //todo 点击播放
//            Log.i("123","on click b=$b  pos=$pos")
            ThreadUtil.cachedThreadStart {
                Log.i("123","cachedThreadStart")
                ModelMgr.getMainCtrlModelInstance(mContext).stopRecording()
                ModelMgr.getPlayViewModelInstance(mContext).stopView()
                Thread.sleep(500)
                ModelMgr.getPlayViewModelInstance(mContext).initRemotePlay(b.beg,b.end)
                ModelMgr.getPlayViewModelInstance(mContext).setRemotePlayCurIndex(pos) }



        },{v, b, pos ->
            //todo 长按菜单
//            Log.i("123","on long click b=$b pos=$pos")
        })
        v.layoutManager = LinearLayoutManager(mContext)
        v.adapter = remoteAdapter
        v.itemAnimator = null
//        upDateRemoteListState()//不需要刷新
    }


    fun updataRemoteList(v:RecyclerView){
        //远程获取
        (v.adapter as MyRemoteAdapter).setData(mRemoteList)
    }

    fun upDateRemoteListState(){
        var now = Utils.getNow()
        var beg = Utils.fromTime(now,-7,Calendar.DAY_OF_MONTH)
        Log.i("123","now = $now    beg=$beg")
        ModelMgr.getApPlayerInstance().searchRemoteFile(beg,now,0,200)
    }

    fun onUpDateRemoteListState(f:ArrayList<RemoteBean>){
        mRemoteList = f
        mUpdateRemoteList.set(true)
    }


    fun isFastClick():Boolean{
        val curClickTime = System.currentTimeMillis()
        var flag = true
        if (curClickTime - mLastClickTime >= MIN_CLICK_DELAY_TIME){
            flag = false
        }
        mLastClickTime = curClickTime
        return flag
    }
}