package com.codebutler.odyssey.app.feature.game

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.codebutler.odyssey.app.feature.common.FpsCalculator
import timber.log.Timber

class GameSurfaceView(context: Context?, attrs: AttributeSet?) : SurfaceView(context, attrs),
        SurfaceHolder.Callback, Runnable {
    private val paint: Paint = Paint()
    private val fpsCalculator: FpsCalculator = FpsCalculator()

    private var thread: Thread? = null
    private var surfaceHolder: SurfaceHolder? = null
    private var lastBitmap: Bitmap? = null
    private var isRunning: Boolean = false

    init {
        val holder = holder
        holder.addCallback(this)
        thread = Thread(this)
    }

    private var callback: ((Long) -> Unit)? = null

    fun setFpsCallback(callback: (Long) -> Unit) {
        this.callback = callback
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        if (isRunning) {
            var retry = true
            while (retry) {
                try {
                    thread?.join()
                    retry = false
                } catch (e: InterruptedException) {
                    // try again shutting down the thread
                }
            }
        }

        isRunning = false
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        surfaceHolder = getHolder()
        if (!isRunning) {
            isRunning = true
            thread?.start()
        }
    }

    fun update(bitmap: Bitmap) {
        lastBitmap = bitmap
    }

    @SuppressLint("NewApi")
    override fun run() {
        Timber.d("Run it")
        while(isRunning) {
            surfaceHolder?.let {
                if (!it.surface.isValid) return@let

                val bitmap = lastBitmap ?: return@let
                val canvas = it.lockHardwareCanvas()

                synchronized(it)
                {
                    canvas.drawBitmap(bitmap, 0f, 0f, paint)
                }

                it.unlockCanvasAndPost(canvas)

                fpsCalculator.update()
                callback?.invoke(fpsCalculator.fps())
            }
        }
    }
}
