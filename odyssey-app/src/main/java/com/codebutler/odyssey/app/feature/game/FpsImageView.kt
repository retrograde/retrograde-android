package com.codebutler.odyssey.app.feature.game

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView
import com.codebutler.odyssey.app.feature.common.FpsCalculator

class FpsImageView(context: Context?, attrs: AttributeSet?) : ImageView(context, attrs) {
    private val fpsCalculator = FpsCalculator()

    private var callback: ((Long) -> Unit)? = null

    fun setFpsCallback(callback: (Long) -> Unit) {
        this.callback = callback
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        fpsCalculator.update()

        callback?.invoke(fpsCalculator.fps())
    }
}