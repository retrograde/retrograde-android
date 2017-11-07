package com.codebutler.odyssey.app.feature.game

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView

class FpsImageView(context: Context?, attrs: AttributeSet?) : ImageView(context, attrs) {
    private var lastUpdate: Long = System.currentTimeMillis()
    private var callback: ((Long) -> Unit)? = null
    private var lastFpsWrite: Int = 0
    private var totalFps: Long = 0
    private val fpsHistory: LongArray = LongArray(100)

    fun setFpsCallback(callback: (Long) -> Unit) {
        this.callback = callback
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val currentUpdate = System.currentTimeMillis()
        val deltaTime = currentUpdate - lastUpdate
        val fps = 1000 / deltaTime

        totalFps -= fpsHistory[lastFpsWrite]
        totalFps += fps

        fpsHistory[lastFpsWrite++] = fps
        lastFpsWrite %= 100
        lastUpdate = currentUpdate

        callback?.invoke(totalFps / 100)
    }
}