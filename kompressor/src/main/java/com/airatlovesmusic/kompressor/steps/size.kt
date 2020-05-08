package com.airatlovesmusic.kompressor.steps

import android.graphics.Bitmap

/**
 * Created by Airat Khalilov on 08/05/2020.
 */

/**
 * Step which compresses bitmap with file size limits
 */
fun sizeCompression(maxSize: Long): CompressionStep = { bm ->
    if (bm.byteCount > maxSize) {
        val ratio = bm.byteCount.div(maxSize)
        Bitmap.createBitmap(bm, 0, 0, bm.width.div(ratio).toInt(), bm.height.div(ratio).toInt())
    } else bm
}