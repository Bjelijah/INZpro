package com.inz.activity.view

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.BinderHelper

abstract class BaseView() {
    private lateinit var mBinder: ViewDataBinding
    private lateinit var mViewModel: BaseViewModel


    fun bindView(context: Context):View{
        mBinder = DataBindingUtil.inflate(LayoutInflater.from(context),getLayout(),null,false)
        mViewModel = getViewmodel()
        BinderHelper.defaultBinder.bind(mBinder,mViewModel)
        return mBinder.root
    }

    fun unBindView(){
        mViewModel.onDestory()
        BinderHelper.defaultBinder.bind(mBinder,null)
        mBinder.executePendingBindings()
    }

    @LayoutRes
    abstract fun getLayout():Int
    abstract fun getViewmodel(): BaseViewModel
}