package com.inz.activity.fragment

import android.os.Build
import android.support.annotation.RequiresApi
import android.view.Display
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.model.ModelMgr

/**
 * 主页面下方功能控制按钮 fragment
 * @see com.inz.model.main.MainCtrlModel
 */
class MainCtrlFragment:BaseFragment() {
    override fun getLayout(): Int = R.layout.fragment_main_ctrl

    @RequiresApi(Build.VERSION_CODES.M)
    override fun getViewmodel(): BaseViewModel  = ModelMgr.getMainCtrlModelInstance(context)
}