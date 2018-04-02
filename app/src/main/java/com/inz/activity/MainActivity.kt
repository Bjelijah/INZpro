package com.inz.activity

import com.inz.inzpro.R
import com.inz.inzpro.BaseViewModel
import com.inz.model.main.MainViewModel

class MainActivity:BaseActivity() {
    override fun getLayout(): Int  = R.layout.activity_main


    override fun getViewmodel(): BaseViewModel  = MainViewModel(this)

}