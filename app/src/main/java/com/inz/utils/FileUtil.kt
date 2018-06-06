package com.inz.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.support.v4.os.EnvironmentCompat
import android.util.Log
import com.coremedia.iso.boxes.Container
import com.googlecode.mp4parser.FileDataSourceImpl
import com.googlecode.mp4parser.authoring.Movie
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder
import com.googlecode.mp4parser.authoring.tracks.h264.H264TrackImpl
import com.inz.action.Config
import com.inz.bean.StorageBean
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.Array
import java.lang.reflect.Method
import java.net.URI
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object FileUtil {
    val A_GB:Double = 1024*1024*1024.0
    val A_MB:Double = 1024*1024.0
    val A_KB:Double = 1024.0


    var FILE_SD_DIR:String?=null
    var FILE_TF_DIR:String?=null
    var FILE_PICTURE_DIR =   "/Picture/"
    var FILE_VIDEO_DIR   = "/Video/"
    var FILE_PICTURE_PATH = "/sdcard/InzPro/Picture/"
    var FILE_VIDEO_PATH = "/sdcard/InzPro/Video/"


    fun initFileDir(c:Context){
        var storageManager = c.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val getVolumeList:Method = storageManager.javaClass.getMethod("getVolumeList")
        val storageValumeClazz =  Class.forName("android.os.storage.StorageVolume")
        val getPath:Method = storageValumeClazz.getMethod("getPath")
        var isRemovable:Method = storageValumeClazz.getMethod("isRemovable")
        var getState:Method? = null
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            try {
                getState = storageValumeClazz.getMethod("getState")
            }catch (e:NoSuchMethodException){
                e.printStackTrace()
            }
        }

        val invokeVolumeList = getVolumeList.invoke(storageManager)
        val length = Array.getLength(invokeVolumeList)
        var dirs = ArrayList<StorageBean>()
        for (i in 0 until length){
            var storageValume = Array.get(invokeVolumeList,i)
            var path:String = getPath.invoke(storageValume) as String
            var removeable:Boolean  = isRemovable.invoke(storageValume) as Boolean
            var state:String = if (getState!=null){
                getState.invoke(storageValume) as String
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                    Environment.getExternalStorageState(File(path))
                }else{
                    if (removeable){
                        EnvironmentCompat.getStorageState(File(path))
                    }else{
                        Environment.MEDIA_MOUNTED
                    }
                }
            }

            dirs.add(StorageBean(path,removeable,state))
        }
        var mydir:String?=null
        for (b in dirs){
            if(b.removeable){
                FILE_TF_DIR = b.path
            }else{
                FILE_SD_DIR = b.path
            }
        }


        if (FILE_TF_DIR!=null){
            FILE_PICTURE_PATH = FILE_TF_DIR + "/Android/data/"+ c.packageName + FILE_PICTURE_DIR
            FILE_VIDEO_PATH   = FILE_TF_DIR + "/Android/data/"+c.packageName + FILE_VIDEO_DIR
        }else if(FILE_SD_DIR!=null){
            FILE_PICTURE_PATH = FILE_SD_DIR + "/Android/data/"+c.packageName + FILE_PICTURE_DIR
            FILE_VIDEO_PATH   = FILE_SD_DIR + "/Android/data/"+c.packageName + FILE_VIDEO_DIR
        }
        if (Config.IS_DEBUG){
             FILE_PICTURE_PATH = "/sdcard/InzPro/Picture/"
             FILE_VIDEO_PATH = "/sdcard/InzPro/Video/"
        }



        if (Build.VERSION.SDK_INT>=19){
            getFilesDirs(c)
        }

        getPictureDir()
        getVideoDir()
    }




    fun deleteLastFileIfSpaceLimit(dirPath:String,totSize:Long,limitSize:Long):Boolean{
        var d:File = File(dirPath)
        Log.i("123","file total size=${d.totalSpace}   fmt=${fmtSpace(d.totalSpace)}  ")

        var fs = d.listFiles()
        var allSize = 0L
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var lastest = 0L
        var lastestFile :File ?= null
        for (f in fs){
            allSize += f.length()
            Log.i("123","name=${f.name}  time=${format.format(Date(f.lastModified()))}")
            if (f.lastModified()<=lastest  || lastest==0L){
                lastest = f.lastModified()
                lastestFile = f
            }
        }
        Log.i("123","file all size=${fmtSpace(totSize)}    allSize=${fmtSpace(allSize)}   limitSize=${fmtSpace(limitSize)}")

        return if (totSize - allSize < limitSize){
            try {
                Log.e("123","~~~~~~~~~~~~~~we delete file ${lastestFile?.name}")
                deleteFile(lastestFile!!)
            }catch (e:Exception){e.printStackTrace() }
            true
        }else{
            false
        }

    }

    fun getPictureDir():File{
        Log.i("123","path=         $FILE_PICTURE_PATH")
        var dir = FILE_PICTURE_PATH
        var f = File(dir)
        if (!f.exists()){
            f.mkdirs()
        }
        return f
    }

    fun getVideoDir():File{
        var dir = FILE_VIDEO_PATH
        var f = File(dir)
        if (!f.exists()){
            f.mkdirs()
        }
        return f
    }

    fun getFilesDirs(c:Context){
        val files = c.getExternalFilesDir(null)
        Log.i("123","fils~~~~~~~~= $files")
    }


    fun createNewVideoDirPathName(type:Int):String {
        return when(type){
            0-> FILE_VIDEO_PATH + getFileName() + ".hw"
            1-> FILE_VIDEO_PATH + getFileName() + ".h264"
            else->FILE_VIDEO_PATH + getFileName() + ".mp4"
        }
    }


    fun getCharacterAndNumber():String = SimpleDateFormat("yyyyMMddHHmmss").format(Date(System.currentTimeMillis()))

    fun getFileName():String = getCharacterAndNumber()

    fun deleteFile(f:File){
        if (f?.exists() && f?.isFile){
            f.delete()
        }
    }

    fun deleteFile(uri:URI){
        var f:File = File(uri)
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


    fun fmtSpace(space:Long):String{
        if (space<0) return "0"
        var gb = space / A_GB
        var mb = (space % A_GB) / A_MB
        var kb = (space % A_MB) / A_KB
        var gbValue:Double = (space / A_GB).toDouble()
        return if (gbValue>=1){
            String.format("%.2fGB",gbValue)
        }else{
            var mbValue:Double = (space / A_MB).toDouble()
            if (mbValue >= 1){
                String.format("%.2fMB",mbValue)
            }else {
                var kbValue:Double = (space / A_KB).toDouble()
                String.format("%.2fKB",kbValue)
            }
        }
    }

    fun fmtSpeed(speed:Long):String{
        if (speed<=0)return "0kbps"
        var gb = speed / A_GB
        var mb = (speed % A_GB) / A_MB
        var kb = (speed % A_MB) / A_KB

        var gbValue:Double = (speed / A_GB).toDouble()
        return if(gbValue>=1){
            String.format("%.2fGbps",gbValue)
        }else{
            var mbValue:Double = (speed / A_MB).toDouble()
            if (mbValue>=1){
                String.format("%.2fMbps",mbValue)
            }else{
                var kbValue:Double = (speed / A_KB).toDouble()
                String.format("%.0fKbps",kbValue)
            }
        }
    }


    class SortByString():Comparator<String>{
        override fun compare(o1: String?, o2: String?): Int {
            return o2!!.compareTo(o1!!)
        }
    }


}