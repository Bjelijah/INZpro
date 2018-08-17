package com.inz.activity


import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import com.howell.jni.JniUtil
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.BinderHelper
import com.inz.model.ModelMgr

/**
 * activity 基类实现dataBind
 */
abstract class BaseActivity : AppCompatActivity() {
    private lateinit var mBinder:ViewDataBinding
    private lateinit var mViewModel:BaseViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        JniUtil.logEnable(true)
        super.onCreate(savedInstanceState)
        ModelMgr.init(this)
        mBinder = DataBindingUtil.setContentView(this,getLayout())
        mViewModel = getViewmodel()
        BinderHelper.defaultBinder.bind(mBinder,mViewModel)
        mViewModel.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.onDestory()
        BinderHelper.defaultBinder.bind(mBinder,null)
        mBinder.executePendingBindings()
    }


    @LayoutRes
    abstract fun getLayout():Int

    abstract fun getViewmodel(): BaseViewModel

}