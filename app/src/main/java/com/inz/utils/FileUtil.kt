package com.inz.utils

import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object FileUtil {
    val FILE_PICTURE_DIR = "/sdcard/InzPro/Picture/"
    val FILE_VIDEO_DIR   = "/sdcard/InzPro/Video/"

    fun getPictureDir():File{
        var dir = FILE_PICTURE_DIR
        var f = File(dir)
        if (!f.exists()){
            f.mkdirs()
        }
        return f
    }

    fun getVideoDir():File{
        var dir = FILE_VIDEO_DIR
        var f = File(dir)
        if (!f.exists()){
            f.mkdirs()
        }
        return f
    }

    fun createNewVideoDirPathName():String = FILE_VIDEO_DIR + getFileName() + ".mp4"

    fun getCharacterAndNumber():String = SimpleDateFormat("yyyyMMddHHmmss").format(Date(System.currentTimeMillis()))

    fun getFileName():String = getCharacterAndNumber()

    fun deleteFile(f:File){
        if (f.exists() && f.isFile){
            f.delete()
        }
    }

    fun isExists(path:String):Boolean = File(path).exists()

    fun getPictureDirFile():ArrayList<String>{
        var arr:ArrayList<String> = ArrayList()
        var dir = getPictureDir()
        var files = dir.listFiles()
        for (f in files){
            if (f.isFile) {
                arr.add(f.path)
            }
        }
        Collections.sort(arr,SortByString())
        return arr
    }

    fun getVideoDirFile():ArrayList<String>{
        var arr:ArrayList<String> = ArrayList()
        var dir = getVideoDir()
        var files = dir.listFiles()
        for (f in files){
            if (f.isFile){
                arr.add(f.path)
            }
        }
        Collections.sort(arr,SortByString())
        return arr
    }


    class SortByString():Comparator<String>{
        override fun compare(o1: String?, o2: String?): Int {
            return o2!!.compareTo(o1!!)
        }
    }


}