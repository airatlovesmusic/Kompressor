package com.airatlovesmusic.kompressor.steps

import android.graphics.Bitmap

/**
 * Created by Airat Khalilov on 08/05/2020.
 */

/**
 * Step which creates new bitmap which fits given height and width
 */
fun resolutionCompression(outHeight: Int, outWidth: Int): CompressionStep = { bm ->
    val (inWidth, inHeight) = bm.width to bm.height
    var inSampleSize = 1
    if (inHeight > outHeight || inWidth > outWidth) {
        val halfHeight: Int = inHeight / 2
        val halfWidth: Int = inWidth / 2
        while (halfHeight / inSampleSize >= outHeight && halfWidth / inSampleSize >= outWidth) {
            inSampleSize *= 2
        }
    }
    if (inSampleSize > 0) {
        Bitmap.createBitmap(bm, 0, 0, bm.width / inSampleSize, bm.height / inSampleSize)
    } else bm
}
