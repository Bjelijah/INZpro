package com.inz.activity.fragment

import android.graphics.Bitmap
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
import android.view.ViewTreeObserver
import com.inz.adapter.MyPictureAdapter
import com.inz.adapter.MyVideoAdapter
import com.inz.adapter.VideoAdapter
import com.inz.bean.PictureBean
import com.inz.bean.VideoBean
import com.inz.inzpro.BaseViewModel
import com.inz.inzpro.R
import com.inz.model.ModelMgr
import com.inz.utils.FileUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableSource
import java.util.function.Function

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
                //share or del
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
                //share or del

            }

        })
        rp.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        rp.adapter = picAdapter

        v.viewTreeObserver.addOnGlobalLayoutListener(object :ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                funPictureInit(v, rp.adapter as MyPictureAdapter)
                v.viewTreeObserver.removeGlobalOnLayoutListener(this)
            }
        })
        return v
    }

    private fun funPictureInit(v:View,adapter:MyPictureAdapter){
        var width = v.width
        Log.i("123","width=$width")
        adapter.setWidth(v.width)
        updatePictureData(adapter)
    }

    private fun updatePictureData(adapter: MyPictureAdapter){
        var lst = FileUtil.getPictureDirFile()
        var arr :ArrayList<PictureBean> = ArrayList()
        for (s in lst){
            arr.add(PictureBean(s))
        }
        adapter.setData(arr)
    }

}