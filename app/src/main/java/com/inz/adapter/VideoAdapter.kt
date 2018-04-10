package com.inz.adapter

import android.content.Context
import android.databinding.ViewDataBinding
import com.inz.bean.VideoBean
import com.inz.inzpro.BinderHelper
import com.inz.inzpro.R
import com.inz.model.main.VideoModel

class VideoAdapter(mContext: Context): BaseBindingAdapter<VideoBean>(mContext) {
    override fun getLayoutResId(viewType: Int): Int = R.layout.item_video


    override fun onBindItem(binding: ViewDataBinding?, item: VideoBean?) {

        BinderHelper.defaultBinder.bind(binding!!,VideoModel())
        binding!!.executePendingBindings()
    }


}