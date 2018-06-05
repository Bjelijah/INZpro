package com.inz.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun getVideoName(s:String):String{
        var f1 = SimpleDateFormat("yyyyMMddHHmmss")
        var f2 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return f2.format(f1.parse(s))
    }

    fun getTimeFromMsec(msec:Int):String{
        var sdf = SimpleDateFormat("HH:mm:ss")
        var data = Date(msec.toLong())
        var s = sdf.format(data)
        Log.i("123","msec=$msec      s=$s")
        return s
    }

    fun formatMsec(mss:Long):String{
        var hours = (mss % (1000*60*60*24))/(1000*60*60)
        var min = (mss % (1000*60*60))/(1000*60)
        var sec = (mss % (1000*60))/1000
        var s= String.format("%02d:%02d:%02d",hours,min,sec)
        return s
    }

    fun getNow():String{
        var d = Date()
        var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return sdf.format(d)
    }

    fun fromTime(time:String,offset:Int,field:Int):String{
        var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var cal = Calendar.getInstance()
        cal.time = sdf.parse(time)
        cal.add(field,offset)
        return sdf.format(cal.time)
    }

}