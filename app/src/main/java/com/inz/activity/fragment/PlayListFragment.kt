package com.inz.activity.fragment

import android.os.Build
import android.support.annotation.RequiresApi
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.model.ModelMgr

class PlayListFragment :BaseFragment() {
    override fun getLayout(): Int = R.layout.layout_play_list

    @RequiresApi(Build.VERSION_CODES.M)
    override fun getViewmodel(): BaseViewModel = ModelMgr.getPlayListModelInstance(context)
}