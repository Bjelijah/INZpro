package com.inz.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.inz.bean.RemoteBean
import com.inz.inzpro.R

/**
 * 远程录像adapter
 */
class MyRemoteAdapter :RecyclerView.Adapter<MyRemoteAdapter.ViewHolder>{
    interface OnItemClickListener{
        fun onItemClickListener(b: RemoteBean, pos: Int)
        fun onItemLongClickListener(v: View, b: RemoteBean, pos: Int)
    }
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tv:TextView       = v.findViewById(R.id.item_remote_tv)
        val ll:RelativeLayout = v.findViewById(R.id.item_remote_ll)
    }
    var mContext:Context
    var mList: MutableList<RemoteBean>? = null
    var mListener: OnItemClickListener? = null
    var mSelectPos = -1

    constructor(c:Context,l:MutableList<RemoteBean>,o:OnItemClickListener){
        mContext = c
        mList = l
        mListener = o
    }

    constructor(c:Context,o:OnItemClickListener){
        mContext = c
        mListener = o
    }

    constructor(c:Context, onItemClick: (b:RemoteBean,pos:Int) -> Unit,onItemLongClick:(v: View, b: RemoteBean, pos: Int)->Unit){
        mContext = c
        mListener = object :OnItemClickListener{
            override fun onItemClickListener(b: RemoteBean, pos: Int)              = onItemClick(b,pos)
            override fun onItemLongClickListener(v: View, b: RemoteBean, pos: Int) = onItemLongClick(v,b,pos)
        }
    }

    fun setData(l: MutableList<RemoteBean>?){
        mList = l
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_remote,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = mList?.size?:0



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val b = mList!![position]
        init(holder,b,position)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun init(holder:ViewHolder, b:RemoteBean, pos:Int){
        holder.tv.text = b.name
        if (mSelectPos == pos){
            holder.ll.setBackgroundResource(R.drawable.item_video_border)
        }else{
            holder.ll.setBackgroundColor(mContext.getColor(R.color.main_bk_light))
        }
        holder.ll.isLongClickable = true
        holder.ll.setOnClickListener(View.OnClickListener {
            mSelectPos = pos
            notifyDataSetChanged()
            mListener?.onItemClickListener(b,pos)
        })
        holder.ll.setOnLongClickListener(View.OnLongClickListener { v->
            mListener?.onItemLongClickListener(v,b,pos)
            true
        })
    }
}