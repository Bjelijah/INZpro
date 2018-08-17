package com.inz.activity.fragment

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.model.ModelMgr

/**
 * 右下侧ptz方向按钮fragment
 * @see com.inz.model.main.PtzCtrlModel
 */
class PtzFragment:BaseFragment() {
    override fun getLayout(): Int = R.layout.fragment_ptz
    @RequiresApi(Build.VERSION_CODES.M)
    override fun getViewmodel(): BaseViewModel = ModelMgr.getPtzCtrlModelInstance(context)


}