package com.inz.model

import android.content.Context
import com.inz.model.main.MainViewModel
import com.inz.model.main.PlayViewModel
import com.inz.model.main.ReplayCtrlModel
import com.inz.model.main.SoundVolModel

object ModelMgr {
    var mContext:Context                    ?=null
    var mMainViewModel:MainViewModel        ?=null
    var mPlayViewModel:PlayViewModel        ?=null
    var mReplayCtrlModel:ReplayCtrlModel    ?=null
    var mSoundVolModel:SoundVolModel        ?=null

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

}