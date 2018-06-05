package com.inz.adapter

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.LinearLayout

import com.bumptech.glide.Glide
import com.inz.bean.PictureBean
import com.inz.inzpro.R
import com.inz.utils.ScaleImageUtils

import java.io.File

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

class MyPictureAdapter : RecyclerView.Adapter<MyPictureAdapter.ViewHolder> {
    internal var mContext: Context
    internal var mList: MutableList<PictureBean>? = null

    internal var mListener: OnItemClickListener? = null
    internal var mCmdMode = false
    internal var mViewWidth: Int = 0

    private val widthHeight: Pair<Int, Int>
        get() {
            val width = mViewWidth / 2
            val height = width * 9 / 16
            return Pair(width, height)
        }

    constructor(c: Context, l: OnItemClickListener) {
        mContext = c
        mListener = l
        mCmdMode = false
    }

    constructor(c:Context
                ,onItemCmd:(v: View, pos: Int, b: PictureBean, isChecked: Boolean)->Unit
                ,onItemClick:(v: View, list: List<PictureBean>?, pos: Int)->Unit
                ,onItemLongClick:(v: View, pos: Int, b: PictureBean)->Unit){
        mContext = c
        mCmdMode = false
        mListener = object :OnItemClickListener{
            override fun onItemCmdCheck(v: View, pos: Int, b: PictureBean, isChecked: Boolean) {
                onItemCmd(v,pos,b,isChecked)
            }

            override fun onItemClick(v: View, list: List<PictureBean>?, pos: Int) {
                onItemClick(v,list,pos)
            }

            override fun onItemLongClick(v: View, pos: Int, b: PictureBean) {
                onItemLongClick(v,pos,b)
            }
        }
    }


    constructor(c: Context, list: MutableList<PictureBean>, l: OnItemClickListener) {
        mContext = c
        mList = list
        mListener = l
        mCmdMode = false
    }

    fun setWidth(width: Int) {
        mViewWidth = width
    }


    fun setData(l: MutableList<PictureBean>) {
        mList = l
        for (b in mList!!) {
            Log.i("123", "path=" + b.path)
            val p = widthHeight
            b.width = p.first
            b.height = p.second
        }
        Log.i("123", " we set data ")
        //        notifyDataSetChanged();
        notifyItemRangeChanged(0, mList!!.size)
    }

    fun setCmdMode(isCmdMode: Boolean) {
        if (mCmdMode == isCmdMode) return
        mCmdMode = isCmdMode
        //        notifyDataSetChanged();
        notifyItemRangeChanged(0, mList!!.size)
    }


    fun addData(b: PictureBean) {
        val p = widthHeight
        b.width = p.first
        b.height = p.second
        mList!!.add(b)
        notifyItemChanged(mList!!.size - 1)
    }

    fun removeData(b: PictureBean) {
        try {
            val i = mList!!.indexOf(b)
            mList!!.remove(b)
            notifyItemChanged(i)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun removePos(pos: Int) {
        if (pos < 0 || pos > mList!!.size - 1) return
        mList!!.removeAt(pos)
        notifyItemChanged(pos)
    }


    fun clearData() {
        mList!!.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_picture, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val b = mList!![position]
        init(holder, b, position)
    }

    override fun getItemCount(): Int {
        return if (mList == null) 0 else mList!!.size
    }

    interface OnItemClickListener {
        fun onItemCmdCheck(v: View, pos: Int, b: PictureBean, isChecked: Boolean)
        fun onItemClick(v: View, list: List<PictureBean>?, pos: Int)
        fun onItemLongClick(v: View, pos: Int, b: PictureBean)
    }


    private fun init(h: ViewHolder, b: PictureBean, pos: Int) {
        //TODO get pic
        Log.i("123", "init   pos=$pos")
        val params = h.itemView.layoutParams
        params.width = b.width
        params.height = b.height
        h.itemView.layoutParams = params
        h.ck.visibility = if (mCmdMode) View.VISIBLE else View.GONE
        h.ck.isChecked = false
        h.ck.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            Log.i("123", "isChecked  isChecked=$isChecked  pos=$pos")
            if (mListener == null) return@OnCheckedChangeListener
            mListener!!.onItemCmdCheck(buttonView, pos, b, isChecked)
        })

        Glide.with(mContext)
                .load(File(b.path))
                .override(b.width, b.height)
                .into(h.iv)
        h.ll.setOnClickListener(View.OnClickListener { v ->
            if (mListener == null) return@OnClickListener
            if (mCmdMode) return@OnClickListener
            mListener!!.onItemClick(v, mList, pos)
        })
        h.ll.setOnLongClickListener(View.OnLongClickListener { v ->
            if (mListener == null) return@OnLongClickListener false
            if (mCmdMode) return@OnLongClickListener false
            mListener!!.onItemLongClick(v, pos, b)
            true
        })
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var iv: ImageView
        internal var ll: LinearLayout
        internal var ck: CheckBox

        init {
            iv = itemView.findViewById(R.id.item_picture_iv)
            ll = itemView.findViewById(R.id.item_picture_ll)
            ck = itemView.findViewById(R.id.item_picture_ck)
        }
    }

}
