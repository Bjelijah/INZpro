package com.inz.activity.fragment

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.model.ModelMgr

class PlayListFragment :BaseFragment() {
    override fun getLayout(): Int = R.layout.fragment_play_list

    @RequiresApi(Build.VERSION_CODES.M)
    override fun getViewmodel(): BaseViewModel = ModelMgr.getPlayListModelInstance(context)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var v = super.onCreateView(inflater, container, savedInstanceState)
        var rv:RecyclerView = v.findViewById(R.id.video_list_rv)
        
        return v
    }

}