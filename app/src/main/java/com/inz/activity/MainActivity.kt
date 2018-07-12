package com.inz.activity


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.WindowManager
import android.widget.LinearLayout
import com.inz.inzpro.R
import com.inz.inzpro.BaseViewModel
import com.inz.model.ModelMgr

class MainActivity:BaseActivity() {

    val REQUEST_EXTERNAL_STORAGE = 1
    var PERMISSIONS_STORAGE =  arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun getLayout(): Int  = R.layout.activity_main


    override fun getViewmodel(): BaseViewModel  = ModelMgr.getMainViewModelInstance(this)


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.i("123","onActivityResult   req=$requestCode      rescode=$resultCode")

        when(resultCode){
            Activity.RESULT_OK->{
                Log.e("123","updata picture")
                ModelMgr.getPlayListModelInstance(this).updatePictureListState()
            }
            Activity.RESULT_CANCELED->{}
            else->{}
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onStart() {
        super.onStart()
        verifyStoragePermissions()
    }


    fun verifyStoragePermissions(){
        var permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE)
        }

    }

}