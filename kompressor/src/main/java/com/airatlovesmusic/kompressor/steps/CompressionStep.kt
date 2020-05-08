package com.airatlovesmusic.kompressor.steps

import android.graphics.Bitmap

 /** Typealias for all compression steps.
   In order to use your custom steps you should override this typealias
   with function which takes Bitmap and returns Bitmap
   e.g.
   val id: CompressionStep = { bm -> bm }
  */
typealias CompressionStep = (Bitmap) -> Bitmap

 /**
    Operator overriding allows us to use more readable syntax,
    e.g customStep + customStep = customStep(customStep(bitmap))
 */
operator fun CompressionStep.plus(
     compression: CompressionStep
 ): CompressionStep = {
    compression(invoke(it))
}