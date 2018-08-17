package com.inz.model

import android.content.Context
import com.inz.model.download.DownloadMgr
import com.inz.model.main.*
import com.inz.model.net.PtzMgr
import com.inz.model.player.ApPlayer
import com.inz.model.player.LocalPlayer

/**
 * viewModel管理类
 */
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
    var mMainCtrlModel:MainCtrlModel        ?=null
    var mSettingModel:SettingModel          ?=null
    var mPtzCtrlModel:PtzCtrlModel          ?=null

    fun init(c:Context){
        mContext = c
    }

    fun getMainViewModelInstance(c: Context):MainViewModel{
        if (mMainViewModel==null) {
            mMainViewModel = MainViewModel(c)
        }else{
            mMainViewModel?.setContext(c)
        }
        return mMainViewModel!!
    }

    fun getPlayViewModelInstance(c: Context):PlayViewModel{
        if (mPlayViewModel==null) {
            mPlayViewModel = PlayViewModel(c)
        }else{
            mPlayViewModel?.setContext(c)
        }
        return mPlayViewModel!!
    }

    fun getReplayCtrlModelInstance(c:Context):ReplayCtrlModel {
        if(mReplayCtrlModel==null) {
            mReplayCtrlModel = ReplayCtrlModel(c)
        }else{
            mReplayCtrlModel?.setContext(c)
        }
        return mReplayCtrlModel!!
    }
    fun getSoundVolModelInstance(c: Context):SoundVolModel {
        if (mSoundVolModel==null){
            mSoundVolModel = SoundVolModel(c)
        }else{
            mSoundVolModel?.setContext(c)
        }
        return mSoundVolModel!!
    }
    fun getPlayListModelInstance(c:Context):PlayListModel{
        if (mPlayListModel==null){
            mPlayListModel = PlayListModel(c)
        }else{
            mPlayListModel?.setContext(c)
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
        }else{
            mListItemModel?.setContext(c)
        }
        return mListItemModel!!
    }
    fun getMainCtrlModelInstance(c:Context):MainCtrlModel{
        if (mMainCtrlModel==null){
            mMainCtrlModel = MainCtrlModel(c)
        }else{
            mMainViewModel?.setContext(c)
        }
        return mMainCtrlModel!!
    }
    fun getSettingModelInstance(c:Context):SettingModel{
        if(mSettingModel==null){
            mSettingModel = SettingModel(c)
        }else{
            mSettingModel?.setContext(c)
        }
        return mSettingModel!!
    }
    fun getPtzCtrlModelInstance(c:Context):PtzCtrlModel{
        if(mPtzCtrlModel==null){
            mPtzCtrlModel=PtzCtrlModel(c)
        }else{
            mPtzCtrlModel?.setCOntext(c)
        }
        return mPtzCtrlModel!!
    }
}