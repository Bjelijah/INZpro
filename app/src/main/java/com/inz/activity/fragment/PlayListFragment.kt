package com.inz.activity.fragment

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.model.ModelMgr
import com.inz.model.main.PlayListModel

class PlayListFragment :BaseFragment() {
    override fun getLayout(): Int = R.layout.fragment_play_list

    @RequiresApi(Build.VERSION_CODES.M)
    override fun getViewmodel(): BaseViewModel = ModelMgr.getPlayListModelInstance(context)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.e("456","oncreateView")
        var v = super.onCreateView(inflater, container, savedInstanceState)
        var rv:RecyclerView = v.findViewById(R.id.video_list_rv)
        var rp:RecyclerView = v.findViewById(R.id.video_list_rp)
        var rr:RecyclerView = v.findViewById(R.id.video_list_rr)

        v.viewTreeObserver.addOnGlobalLayoutListener(object :ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                var m = (getViewmodel() as PlayListModel)
                m.initVideoList(rv)
                m.initRemoteList(rr)
                m.initPictureList(rp,v.width)
                v.viewTreeObserver.removeGlobalOnLayoutListener(this)
            }
        })
        ModelMgr.getPlayListModelInstance(context).activity = activity
        return v
    }

}