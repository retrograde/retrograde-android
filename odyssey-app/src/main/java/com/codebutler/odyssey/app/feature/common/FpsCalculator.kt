package com.codebutler.odyssey.app.feature.common

class FpsCalculator {
    companion object {
        private var size: Int = 100
    }

    private var lastUpdate: Long = System.currentTimeMillis()
    private var lastFpsWrite: Int = 0
    private var totalFps: Long = 0
    private val fpsHistory: LongArray = LongArray(size)

    fun update() {
        val currentUpdate = System.currentTimeMillis()
        val deltaTime = currentUpdate - lastUpdate + 1 // Add 1 to make sure delta time is non-zero.
        val fps = 1000 / deltaTime

        totalFps -= fpsHistory[lastFpsWrite]
        totalFps += fps

        fpsHistory[lastFpsWrite++] = fps
        lastFpsWrite %= 100
        lastUpdate = currentUpdate
    }

    fun fps(): Long = totalFps / 100
}