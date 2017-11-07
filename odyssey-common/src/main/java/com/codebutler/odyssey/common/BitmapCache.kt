package com.codebutler.odyssey.common

import android.graphics.Bitmap

class BitmapCache {

    private var bitmap: Bitmap? = null

    fun getBitmap(width: Int, height: Int, videoBitmapConfig: Bitmap.Config): Bitmap {
        var bitmap = bitmap
        if (bitmap == null || bitmap.width != width || bitmap.height != height || bitmap.config != videoBitmapConfig) {
            bitmap = Bitmap.createBitmap(width, height, videoBitmapConfig)
            this@BitmapCache.bitmap = bitmap
        }
        return bitmap!!
    }
}