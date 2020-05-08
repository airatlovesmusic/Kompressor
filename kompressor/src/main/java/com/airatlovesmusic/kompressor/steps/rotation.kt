package com.airatlovesmusic.kompressor.steps

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.io.InputStream

/**
 * Created by Airat Khalilov on 08/05/2020.
 */

/**
 * Step which rotates bitmap to right orientation
 */
fun rotationCompression(inputStream: InputStream): CompressionStep = { bm ->
    val orientation = ExifInterface(inputStream).getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
    val matrix = Matrix()
    when (orientation) {
        6 -> matrix.postRotate(90f)
        3 -> matrix.postRotate(180f)
        8 -> matrix.postRotate(270f)
    }
    Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, matrix, true)
}