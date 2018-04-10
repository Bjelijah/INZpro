package com.inz.activity.fragment

import android.os.Build
import android.support.annotation.RequiresApi
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.model.ModelMgr

class ReplayCtrlFragment :BaseFragment(){

    override fun getLayout(): Int  = R.layout.fragment_replay_ctrl

    @RequiresApi(Build.VERSION_CODES.M)
    override fun getViewmodel(): BaseViewModel = ModelMgr.getReplayCtrlModelInstance(context)


}