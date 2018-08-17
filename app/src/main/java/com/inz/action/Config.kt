package com.inz.action

import android.content.Context
import android.util.Log
import com.inz.utils.NetUtil

/**
 * 运行参数
 */
object Config {
    /**
     * 连接cam使用的协议
     */
    val ECAM = 0x00
    val AP   = 0x01
    val TURN = 0x02

    val SHOW_REMOTE_CONTROL   = true //是否显示ptz按钮
    val SHOW_PASSWORD_CONTROL = true  //是否显示设置密码按钮
//

    val playType        = AP
    var CAM_IP              = "192.168.18.156"  //cam ip 会根据网段选择 1.100 或8.100
    var CAM_GATEWAY         = "192.168.1.36"    //网关 ip
    val CAM_SLOT            = 0                 // 默认slot
    val CAM_Crypto          = -1   //0:h264  1:h265  2:h264crypto  3:h265crypto    -1:使用获取到的
    val CAM_IS_SUB          = false
    val DOWN_WH_STREAM      = true //true down  hw  //false down h264 covert to mp4  下载的格式

    val FILE_DIR_PICTURE_SIZE = 1024*1024*1024*2L  // picture   2G
    val FILE_DIR_VIDEO_SIZE   = 1024*1024*1024*8L  //video  8G
    val FILE_DIR_LIMITE       = 1024*1024*100L     // 100M  小于100m则开始删除文件


    val IS_DEBUG = false


    val HW_ALARM_HARD          = 1
    val HW_ALARM_MOTION        = 2
    val HW_ALARM_VIDEO_LOST    = 3
    val HW_ALARM_START_REC     = 4
    val HW_ALARM_STOP_REC      = 5
    val HW_ALARM_MASK          = 6
    val HW_ALARM_IN            = 7
    val HW_ALARM_HEARTBEAT     = 8
    val HW_ALARM_MOTIONEX      = 10
    val HW_ALARM_SLOT_NODEFINE = 11
    val HW_ALARM_SLOT_LOST     = 12


    /**
     * 根据网关选择ip
     * @param c 上下文
     */
    fun init(c:Context){
        var localIp = NetUtil.getIpAddress(c)
        Log.i("123","localIp=$localIp")
        var s = localIp.split(".")
        if (s.size==4) {
            when (s[2]) {
                "1" -> {
                    CAM_IP = "192.168.1.100"
                }
                "8" -> {
                    CAM_IP = "192.168.8.100"
                }
            }
        }
    }
}