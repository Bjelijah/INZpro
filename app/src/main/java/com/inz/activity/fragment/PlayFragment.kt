package com.inz.activity.fragment

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.*
import com.inz.action.CtrlAction
import com.inz.activity.view.PlayGLTextureView
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.model.ModelMgr

class PlayFragment :BaseFragment(){


    override fun getLayout(): Int = R.layout.layout_play_view

    @RequiresApi(Build.VERSION_CODES.M)
    override fun getViewmodel(): BaseViewModel = ModelMgr.getPlayViewModelInstance(context)


}