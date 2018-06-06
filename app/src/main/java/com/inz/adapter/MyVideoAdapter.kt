package com.inz.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import com.inz.bean.VideoBean
import com.inz.inzpro.R

class MyVideoAdapter : RecyclerView.Adapter<MyVideoAdapter.ViewHolder> {
    interface OnItemClickListener {
        fun onItemClickListener(b: VideoBean, pos: Int)
        fun onItemLongClickListener(v: View, b: VideoBean, pos: Int)
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv: TextView       = itemView.findViewById(R.id.item_video_tv)
        var iv: ImageView      = itemView.findViewById(R.id.item_video_iv)
        var ll: RelativeLayout = itemView.findViewById(R.id.item_video_ll)
    }
    var mContext: Context
    var mList: MutableList<VideoBean>? = null
    var mListener: OnItemClickListener? = null
    var mSelectPos = -1

    constructor(c: Context, l: OnItemClickListener) {
        mContext = c
        mListener = l
    }

    constructor(c:Context,onItemClick:(b: VideoBean, pos: Int)->Unit,onItemLongClick:(v: View, b: VideoBean, pos: Int)->Unit){
        mContext = c
        mListener = object :OnItemClickListener{
            override fun onItemClickListener(b: VideoBean, pos: Int)              = onItemClick(b,pos)
            override fun onItemLongClickListener(v: View, b: VideoBean, pos: Int) = onItemLongClick(v,b,pos)
        }
    }


    constructor(c: Context, l: MutableList<VideoBean>, listener: OnItemClickListener) {
        mContext = c
        mList = l
        mListener = listener
    }

    fun setData(l: MutableList<VideoBean>) {
        mList = l
        notifyDataSetChanged()
    }

    fun addData(b: VideoBean) {
        mList!!.add(b)
        notifyItemChanged(mList!!.size - 1)
    }

    fun removeData(b: VideoBean) {
        try {
            val i = mList!!.indexOf(b)
            mList!!.remove(b)
            notifyItemRemoved(i)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun clearData() {
        mList!!.clear()
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return ViewHolder(v)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val b = mList!![position]
        init(holder, b, position)
    }

    override fun getItemCount(): Int  = mList?.size?:0

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun init(holder: ViewHolder, bean: VideoBean, pos: Int) {
        //get bitmap
        holder.iv.setImageDrawable(mContext.getDrawable(R.mipmap.ic_launcher))
        holder.tv.text = bean.name
        if (mSelectPos == pos) {
            //TODO 加边框
//            Log.i("123","加边框")
            holder.ll.setBackgroundResource(R.drawable.item_video_border)
        } else {
            //todo 没边框
//            Log.i("123","没边框")
            holder.ll.setBackgroundColor(mContext.getColor(R.color.main_bk_light))
        }
        holder.ll.isLongClickable = true
        holder.ll.setOnClickListener(View.OnClickListener {
            mSelectPos = pos
            notifyDataSetChanged()
            mListener?.onItemClickListener(bean, pos)

        })
        holder.ll.setOnLongClickListener(View.OnLongClickListener { v ->
            if (mListener == null) return@OnLongClickListener false
            mListener!!.onItemLongClickListener(v, bean, pos)
            true
        })

    }
}
