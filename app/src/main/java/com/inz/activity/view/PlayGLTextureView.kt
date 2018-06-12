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
    private var mRenderer: GLESRendererImpl = GLESRendererImpl(mContext, this, null)

    init {
        setRenderer(mRenderer)
        setRenderMode(GLESTextureView.RENDERMODE_WHEN_DIRTY)
    }
}
