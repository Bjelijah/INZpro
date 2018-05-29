package com.inz.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import android.util.Log

object NetUtil {

    val NETWORK_NONE = 0
    val NETWORK_WIFI = 1
    val NETWORK_2G   = 2
    val NETWORK_3G   = 3
    val NETWORK_4G   = 4
    val NETWORK_MOBILE = 5

    fun getOperatorName(c:Context):String = (c.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).networkOperatorName

    fun getNetWorkState(c:Context):Int{
        val connManager = c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                ?: return NETWORK_NONE
        val activeNetInfo = connManager.activeNetworkInfo
        if (activeNetInfo==null || !activeNetInfo.isAvailable){
            return NETWORK_NONE
        }
        val wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        var state = wifiInfo?.state
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING){
            return NETWORK_WIFI
        }
        var telephonyManager = c.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var networkType = telephonyManager.networkType
        Log.i("123","")
        return when(networkType){
            TelephonyManager.NETWORK_TYPE_GPRS-> NETWORK_2G
            TelephonyManager.NETWORK_TYPE_CDMA-> NETWORK_2G
            TelephonyManager.NETWORK_TYPE_EDGE-> NETWORK_2G
            TelephonyManager.NETWORK_TYPE_1xRTT-> NETWORK_2G
            TelephonyManager.NETWORK_TYPE_IDEN-> NETWORK_2G
            TelephonyManager.NETWORK_TYPE_EVDO_A-> NETWORK_3G
            TelephonyManager.NETWORK_TYPE_UMTS-> NETWORK_3G
            TelephonyManager.NETWORK_TYPE_EVDO_0-> NETWORK_3G
            TelephonyManager.NETWORK_TYPE_HSDPA-> NETWORK_3G
            TelephonyManager.NETWORK_TYPE_HSUPA-> NETWORK_3G
            TelephonyManager.NETWORK_TYPE_HSPA-> NETWORK_3G
            TelephonyManager.NETWORK_TYPE_EVDO_B-> NETWORK_3G
            TelephonyManager.NETWORK_TYPE_EHRPD-> NETWORK_3G
            TelephonyManager.NETWORK_TYPE_HSPAP-> NETWORK_3G
            TelephonyManager.NETWORK_TYPE_LTE-> NETWORK_4G
            else-> NETWORK_MOBILE
        }
    }

    fun isNetConnected(c:Context):Boolean{
        val connMgr = c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connMgr?.activeNetworkInfo
        return info?.isConnected?:false
    }

    fun isWifiConnected(c:Context):Boolean{
        val connMgr = c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connMgr?.activeNetworkInfo
        if (info!=null){
            var type = info.type
            if (type == ConnectivityManager.TYPE_WIFI || type == ConnectivityManager.TYPE_ETHERNET){
                return info.isConnected
            }
        }
        return false
    }

}