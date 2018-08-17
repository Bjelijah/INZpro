package com.inz.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.util.Log
import com.inz.action.Config

/**
 * 网络状态接收器
 */
class NetworkConnectChangedReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("123","action=${intent?.action}")


        if (WifiManager.WIFI_STATE_CHANGED_ACTION == intent?.action){
            var wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,0)
            Log.i("123","wifiState=$wifiState")
            when(wifiState){
                WifiManager.WIFI_STATE_DISABLED->{
                    Log.i("123","wifi state disable")
                }
                WifiManager.WIFI_STATE_DISABLING->{
                    Log.i("123","wifi state disabling")
                }
                WifiManager.WIFI_STATE_ENABLED->{
                    Log.i("123","wifi state enable")
                    Config.init(context!!)
                }
                WifiManager.WIFI_STATE_ENABLING->{
                    Log.i("123","wifi state enabling")
                }
                WifiManager.WIFI_STATE_UNKNOWN->{
                    Log.i("123","wifi state unknown")
                }
            }

        }else if(ConnectivityManager.CONNECTIVITY_ACTION==intent?.action){
            Log.i("123","get CONNECTIVITY_ACTION")
            Config.init(context!!)
        }
    }
}