package com.codebutler.odyssey.app.feature.game

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.codebutler.odyssey.app.feature.common.FpsCalculator
import timber.log.Timber

class GameGLSurfaceView(context: Context?, attrs: AttributeSet?) : GLSurfaceView(context, attrs) {
    private val fpsCalculator: FpsCalculator = FpsCalculator()
    private val renderer: GLRenderer2d

    private var surfaceHolder: SurfaceHolder? = null
    private var lastBitmap: Bitmap? = null

    init {
        val holder = holder
        holder.addCallback(this)

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2)

        // Set the Renderer for drawing on the GLSurfaceView
        renderer = GLRenderer2d()
        setRenderer(renderer)

        // Render the view only when there is a change in the drawing data
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    private var callback: ((Long) -> Unit)? = null

    fun setFpsCallback(callback: (Long) -> Unit) {
        this.callback = callback
    }

    fun update(bitmap: Bitmap) {
        lastBitmap = bitmap
    }
}