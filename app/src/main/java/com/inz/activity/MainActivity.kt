package com.inz.activity

import android.os.Bundle
import android.view.View
import com.inz.inzpro.R
import com.inz.inzpro.BaseViewModel
import com.inz.model.ModelMgr

class MainActivity:BaseActivity() {
    override fun getLayout(): Int  = R.layout.activity_main


    override fun getViewmodel(): BaseViewModel  = ModelMgr.getMainViewModelInstance(this)



}