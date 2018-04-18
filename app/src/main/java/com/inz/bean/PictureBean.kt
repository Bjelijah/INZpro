package com.inz.bean

data class PictureBean(override val path:String) :BaseBean(path){
    var width = 0
    var height = 0
}