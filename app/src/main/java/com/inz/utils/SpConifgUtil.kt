package com.inz.utils

import android.content.Context
import android.content.SharedPreferences
import android.view.ContextThemeWrapper

object SpConifgUtil {
    val SP_WIFI = "WIFI_SET"

    fun setWIFIConfig(c:Context,name:String,pwd:String){
        var sp = c.getSharedPreferences(SP_WIFI,Context.MODE_PRIVATE)
        var editor = sp.edit()
        editor.putString("wifi_name",name)
        editor.putString("wifi_pwd",pwd)
        editor.commit()
    }
    fun getWIFIName(c:Context):String = c.getSharedPreferences(SP_WIFI,Context.MODE_PRIVATE).getString("wifi_name","")

    fun getWIFIPwd(c:Context):String = c.getSharedPreferences(SP_WIFI,Context.MODE_PRIVATE).getString("wifi_pwd","")

    fun setPassword(c:Context,pwd:String){
        var sp = c.getSharedPreferences(SP_WIFI,Context.MODE_PRIVATE)
        var editor = sp.edit()
        editor.putString("wifi_pwd",pwd)
        editor.commit()
    }



}