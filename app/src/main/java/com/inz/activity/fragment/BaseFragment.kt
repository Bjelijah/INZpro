package com.inz.activity.fragment

import android.app.Fragment
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.BinderHelper

/**
 * 基于的fragment基类MVVM，实现dataBind
 */
abstract class BaseFragment:Fragment() {
    private lateinit var mBinder: ViewDataBinding
    private lateinit var mViewModel: BaseViewModel


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinder = DataBindingUtil.inflate(inflater!!,getLayout(),container,false)
        mViewModel = getViewmodel()
        BinderHelper.defaultBinder.bind(mBinder,mViewModel)
        mViewModel.onCreate()
        return mBinder.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewModel.onDestory()
        BinderHelper.defaultBinder.bind(mBinder,null)
        mBinder.executePendingBindings()
    }

    @LayoutRes
    abstract fun getLayout():Int
    abstract fun getViewmodel(): BaseViewModel
}