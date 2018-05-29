package com.inz.action

object Config {
    val ECAM = 0x00
    val AP   = 0x01
    val TURN = 0x02



    val playType        = AP
//    val CAM_IP              = "192.168.3.7"
//    val CAM_IP              = "192.168.18.104"
    val CAM_IP              = "192.168.1.100"
    val CAM_SLOT            = 0
    val CAM_Crypto          = 3   //0:h264  1:h265  2:h264crypto  3:h265crypto
    val CAM_IS_SUB          = false
    val DOWN_WH_STREAM      = true //true down  hw  //false down h264 covert to mp4


    val FILE_DIR_PICTURE_SIZE = 1024*1024*1024*2L // picture   2G
//    val FILE_DIR_PICTURE_SIZE = 1024*1024*101L // picture   2G
    val FILE_DIR_VIDEO_SIZE   = 1024*1024*1024*8L  //video  8G
//    val FILE_DIR_VIDEO_SIZE   = 1024*1024*101L   //video  8G
    val FILE_DIR_LIMITE= 1024*1024*100L      // 100M


    val IS_DEBUG = false

}