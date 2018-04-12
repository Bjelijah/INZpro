package com.inz.utils

import android.content.Context
import com.inz.inzpro.R

object MessageHelp {
    fun msgCatchPic(c: Context) = c.getString(R.string.catchPicTo) + FileUtil.FILE_PICTURE_DIR
    fun msgCatchError(c:Context) = c.getString(R.string.catchPicError)
}