package com.inz.utils

import java.text.SimpleDateFormat

object Utils {
    fun getVideoName(s:String):String{
        var f1 = SimpleDateFormat("yyyyMMddHHmmss")
        var f2 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return f2.format(f1.parse(s))
    }
}