package com.airatlovesmusic.kompressor

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.airatlovesmusic.kompressor.steps.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * Created by Airat Khalilov on 08/05/2020.
 */

class ImageCompressor {

    /***
     * @param inputStream [InputStream] - any type of inputStreams from which lib can read needed data
     * @return [OutputStream] - any type of outputStreams to which lib can save compressed data
     */
    fun compress (
        inputStream: InputStream,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        quality: Int = 80,
        steps: CompressionStep = defaultSteps(inputStream)
    ): InputStream {
        val baos = ByteArrayOutputStream()
        val result = steps.invoke(BitmapFactory.decodeStream(inputStream))
        result.compress(format, quality, baos)
        return ByteArrayInputStream(baos.toByteArray())
    }

    private fun defaultSteps(inputStream: InputStream): CompressionStep =
        rotationCompression(inputStream) + resolutionCompression(612, 816)

}