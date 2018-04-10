package com.inz.model.main

import android.content.Context
import android.databinding.ObservableField
import android.util.Log
import android.view.View
import com.inz.inzpro.BaseViewModel
import io.reactivex.functions.Action

class PlayListModel(mContext: Context):BaseViewModel {
    val SHOW_NONE           = 0x00
    val SHOW_RECORD_FILE    = 0x01
    val SHOW_PICTURE_FILE   = 0x02

    var mShowCode       = SHOW_NONE

    override fun onDestory() {
    }

    override fun setFullScreen(b: Boolean) {
    }

    val mPlayListTitleBtnRecordFile = Action {
        Log.i("123","on record file click")
        when(mShowCode){
            SHOW_NONE->{
                mShowCode = SHOW_RECORD_FILE
                mShowRecordFile.set(true)
                mShowPictureFile.set(false)
                mRecordListVisibility.set(View.VISIBLE)
                mPictureListVisibility.set(View.GONE)

            }
            SHOW_RECORD_FILE->{
                mShowCode = SHOW_NONE
                mShowRecordFile.set(false)
                mRecordListVisibility.set(View.GONE)
            }
            SHOW_PICTURE_FILE->{
                mShowCode = SHOW_RECORD_FILE
                mShowPictureFile.set(false)
                mShowRecordFile.set(true)
                mRecordListVisibility.set(View.VISIBLE)
                mPictureListVisibility.set(View.GONE)
            }
        }
    }

    val mPlayListTitleBtnPictureFile = Action {
        Log.i("123","on picture file click")
        when(mShowCode){
            SHOW_NONE->{
                mShowCode = SHOW_PICTURE_FILE
                mShowRecordFile.set(false)
                mShowPictureFile.set(true)
                mRecordListVisibility.set(View.GONE)
                mPictureListVisibility.set(View.VISIBLE)
            }
            SHOW_RECORD_FILE->{
                mShowCode = SHOW_PICTURE_FILE
                mShowRecordFile.set(false)
                mShowPictureFile.set(true)
                mRecordListVisibility.set(View.GONE)
                mPictureListVisibility.set(View.VISIBLE)
            }
            SHOW_PICTURE_FILE->{
                mShowCode = SHOW_NONE
                mShowPictureFile.set(false)
                mPictureListVisibility.set(View.GONE)
            }
        }

    }



    val mShowRecordFile              = ObservableField<Boolean>(false)
    val mShowPictureFile             = ObservableField<Boolean>(false)
    val mRecordListVisibility        = ObservableField<Int>(View.GONE)
    val mPictureListVisibility       = ObservableField<Int>(View.GONE)

}