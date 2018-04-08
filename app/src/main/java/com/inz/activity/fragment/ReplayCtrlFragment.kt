package com.inz.activity.fragment

import android.os.Build
import android.support.annotation.RequiresApi
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.model.main.ReplayCtrlModel

class ReplayCtrlFragment :BaseFragment(){

    override fun getLayout(): Int  = R.layout.layout_replay_ctrl



    @RequiresApi(Build.VERSION_CODES.M)
    override fun getViewmodel(): BaseViewModel = ReplayCtrlModel(context)


}