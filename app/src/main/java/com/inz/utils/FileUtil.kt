package com.inz.utils

import com.coremedia.iso.boxes.Container
import com.googlecode.mp4parser.FileDataSourceImpl
import com.googlecode.mp4parser.authoring.Movie
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder
import com.googlecode.mp4parser.authoring.tracks.h264.H264TrackImpl
import java.io.File
import java.io.FileOutputStream
import java.nio.channels.FileChannel
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

    fun createNewVideoDirPathName(type:Int):String {
        return when(type){
            0-> FILE_VIDEO_DIR + getFileName() + ".hw"
            1-> FILE_VIDEO_DIR + getFileName() + ".h264"
            else->FILE_VIDEO_DIR + getFileName() + ".mp4"
        }
    }


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

    fun h264File2mp4File(h264Path:String,mp4Path:String){
//        var h264Track: H264TrackImpl = H264TrackImpl(FileDataSourceImpl(h264Path))
        var h264Track: H264TrackImpl = H264TrackImpl(FileDataSourceImpl(h264Path),"eng",25,1)
        var movie:Movie = Movie()
        movie.addTrack(h264Track)
        var mp4file:Container = DefaultMp4Builder().build(movie)
        var fc:FileChannel = FileOutputStream(File(mp4Path)).channel
        mp4file.writeContainer(fc)
        fc.close()
    }


    fun hwFile2H264File(hwFilePath:String,h264FilePath:String){

    }



    class SortByString():Comparator<String>{
        override fun compare(o1: String?, o2: String?): Int {
            return o2!!.compareTo(o1!!)
        }
    }


}