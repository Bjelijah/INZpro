package com.inz.activity.fragment

import android.os.Build
import android.support.annotation.RequiresApi
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.model.ModelMgr

/**
 * 播放界面fragment
 * @see com.inz.model.main.MainViewModel
 */
class PlayFragment :BaseFragment(){


    override fun getLayout(): Int = R.layout.fragment_play_view

    @RequiresApi(Build.VERSION_CODES.M)
    override fun getViewmodel(): BaseViewModel = ModelMgr.getPlayViewModelInstance(context)


}