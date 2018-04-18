package com.inz.activity


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.inz.inzpro.R
import com.inz.inzpro.BaseViewModel
import com.inz.model.ModelMgr

class MainActivity:BaseActivity() {
    override fun getLayout(): Int  = R.layout.activity_main


    override fun getViewmodel(): BaseViewModel  = ModelMgr.getMainViewModelInstance(this)


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.i("123","onActivityResult   req=$requestCode      rescode=$resultCode")

        when(resultCode){
            Activity.RESULT_OK->{
                Log.e("123","updata picture")
                ModelMgr.getPlayListModelInstance(this).upDatePictureListState()
            }
            Activity.RESULT_CANCELED->{}
            else->{}
        }
    }



}