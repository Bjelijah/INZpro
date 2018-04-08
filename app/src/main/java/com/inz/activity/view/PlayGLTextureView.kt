package com.inz.activity.view

import android.content.Context
import android.util.AttributeSet

import com.howellsdk.api.player.GLESRendererImpl
import com.howellsdk.api.player.GLESTextureView
import com.howellsdk.api.player.ZoomableTextureView

/**
 * Created by Administrator on 2017/6/23.
 */

class PlayGLTextureView(private val mContext: Context, attrs: AttributeSet) : ZoomableTextureView(mContext, attrs) {
    internal var mRenderer: GLESRendererImpl

    init {
        mRenderer = GLESRendererImpl(mContext, this, null)
        setRenderer(mRenderer)
        setRenderMode(GLESTextureView.RENDERMODE_WHEN_DIRTY)
    }
}
