package com.inz.activity.fragment

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inz.adapter.MyPictureAdapter
import com.inz.adapter.MyVideoAdapter
import com.inz.adapter.VideoAdapter
import com.inz.bean.PictureBean
import com.inz.bean.VideoBean
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.model.ModelMgr

class PlayListFragment :BaseFragment() {
    override fun getLayout(): Int = R.layout.fragment_play_list

    @RequiresApi(Build.VERSION_CODES.M)
    override fun getViewmodel(): BaseViewModel = ModelMgr.getPlayListModelInstance(context)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var v = super.onCreateView(inflater, container, savedInstanceState)
        var rv:RecyclerView = v.findViewById(R.id.video_list_rv)
        var rp:RecyclerView = v.findViewById(R.id.video_list_rp)
        var l = ArrayList<VideoBean>()
        l.add(VideoBean("id1","aaa"))
        l.add(VideoBean("id2","bbb"))
        l.add(VideoBean("id3","ccc"))
        var videoAdapter = MyVideoAdapter(context,l,object :MyVideoAdapter.OnItemClickListener{
            override fun onItemClickListener(pos: Int) {
                Log.i("123","onItemClickListener pos= $pos")
            }

            override fun onItemLongClickListener(pos: Int) {
                Log.i("123","onItemLongClickListener pos= $pos")
            }

        })
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = videoAdapter

        var p = ArrayList<PictureBean>()
        p.add(PictureBean("id1"))
        p.add(PictureBean("id2"))
        p.add(PictureBean("id3"))
        p.add(PictureBean("id4"))
        var picAdapter = MyPictureAdapter(context,p,object :MyPictureAdapter.OnItemClickListener{
            override fun onItemClick(pos: Int) {
                Log.i("123","onItemClickListener pos= $pos")
            }

            override fun onItemLongClick(pos: Int) {
                Log.i("123","onItemLongClickListener pos= $pos")
            }

        })
        rp.layoutManager = StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
        rp.adapter = picAdapter

        return v
    }

}