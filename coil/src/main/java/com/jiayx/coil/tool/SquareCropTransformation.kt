package com.jiayx.coil.tool

import android.graphics.Bitmap
import coil.size.Size
import coil.transform.Transformation
import kotlin.math.max

/**
 * 应用裁剪平方变换的[变换]。
 */
class SquareCropTransformation : Transformation {

    override val cacheKey: String = SquareCropTransformation::class.java.name

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val largerSize = max(
            size.width.pxOrElse { input.width/2 },
            size.height.pxOrElse { input.height/2 }
        )
        return Util.centerCrop(input, largerSize, largerSize)
    }
}