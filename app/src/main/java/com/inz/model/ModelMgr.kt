package com.inz.model

import android.content.Context
import com.inz.model.download.DownloadMgr
import com.inz.model.main.*
import com.inz.model.player.ApPlayer
import com.inz.model.player.LocalPlayer

object ModelMgr {
    var mContext:Context                    ?=null
    var mMainViewModel:MainViewModel        ?=null
    var mPlayViewModel:PlayViewModel        ?=null
    var mReplayCtrlModel:ReplayCtrlModel    ?=null
    var mSoundVolModel:SoundVolModel        ?=null
    var mPlayListModel:PlayListModel        ?=null
    var mApPlayer:ApPlayer                  ?=null
    var mLocalPlayer:LocalPlayer            ?=null
    var mDownloadMgr:DownloadMgr            ?=null
    var mListItemModel:ListItemModel        ?=null
    fun init(c:Context){
        mContext = c
    }

    fun getMainViewModelInstance(c: Context):MainViewModel{
        if (mMainViewModel==null)
            mMainViewModel = MainViewModel(c)
        return mMainViewModel!!
    }

    fun getPlayViewModelInstance(c: Context):PlayViewModel{
        if (mPlayViewModel==null)
            mPlayViewModel = PlayViewModel(c)
        return mPlayViewModel!!
    }

    fun getReplayCtrlModelInstance(c:Context):ReplayCtrlModel {
        if(mReplayCtrlModel==null)
            mReplayCtrlModel = ReplayCtrlModel(c)
        return mReplayCtrlModel!!
    }
    fun getSoundVolModelInstance(c: Context):SoundVolModel {
        if (mSoundVolModel==null){
            mSoundVolModel = SoundVolModel(c)
        }
        return mSoundVolModel!!
    }
    fun getPlayListModelInstance(c:Context):PlayListModel{
        if (mPlayListModel==null){
            mPlayListModel = PlayListModel(c)
        }
        return mPlayListModel!!
    }

    fun getApPlayerInstance():ApPlayer{
        if (mApPlayer==null){
            mApPlayer = ApPlayer()
        }
        return mApPlayer!!
    }
    fun getDownloadMgrInstance():DownloadMgr{
        if (mDownloadMgr==null){
            mDownloadMgr = DownloadMgr()
        }
        return mDownloadMgr!!
    }
    fun getLoaclPlayerInstance():LocalPlayer{
        if (mLocalPlayer==null){
            mLocalPlayer = LocalPlayer()
        }
        return mLocalPlayer!!
    }
    fun getListItemModelInstance(c:Context):ListItemModel{
        if (mListItemModel==null){
            mListItemModel = ListItemModel(c)
        }
        return mListItemModel!!
    }

}