package com.inz.bean

data class RemoteBean(var name:String,var beg:String,var end:String):BaseBean("$beg&$end") {
}