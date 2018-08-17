package com.inz.model.main

import android.content.Context
import android.databinding.ObservableField
import android.util.Log
import com.inz.inzpro.BaseViewModel
import com.inz.model.ModelMgr
import com.inz.utils.SpConifgUtil
import io.reactivex.functions.Action

/**
 * @deprecated
 */
class SettingModel(private var mContext:Context):BaseViewModel {
    override fun onCreate() {
        Log.i("123","on setting model create")

        //read




        var name = SpConifgUtil.getWIFIName(mContext)
        var pwd = SpConifgUtil.getWIFIPwd(mContext)
        if (name.equals("") || pwd.equals("")){

        }
    }

    override fun onDestory() {
        Log.i("123","on setting model destory")
    }

    val mWifiName   = ObservableField<String>("")
    val mWifiPwd    = ObservableField<String>("")

    val onClickOk  = Action{
        Log.i("123","on click ok")

        //set to
        //if ok
        SpConifgUtil.setWIFIConfig(mContext,mWifiName.get()!!,mWifiPwd.get()!!)

    }

    val onClickCancel = Action {
        Log.i("123","on click cancel")
        ModelMgr.getMainCtrlModelInstance(mContext).dismissDialog()
    }

    val onClickLast = Action{
        var name = SpConifgUtil.getWIFIName(mContext)
        var pwd = SpConifgUtil.getWIFIPwd(mContext)
        mWifiName.set(name)
        mWifiPwd.set(pwd)
    }

    fun setContext(c:Context){
        mContext = c
    }


}